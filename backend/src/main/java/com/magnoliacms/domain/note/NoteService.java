package com.magnoliacms.domain.note;

import com.magnoliacms.domain.note.dto.CreateNewNoteRequest;
import com.magnoliacms.domain.note.dto.CreateNewNoteVersionRequest;
import com.magnoliacms.exception.NotFoundException;
import com.magnoliacms.exception.NoteAlreadyExistsException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@RequiredArgsConstructor
@ApplicationScoped
public class NoteService {

    private static final Logger LOG = Logger.getLogger(NoteService.class);

    private final NoteRepository noteRepository;
    private final NoteVersionRepository noteVersionRepository;

    public Uni<NoteEntity> createNote(CreateNewNoteRequest createNewNoteRequest) {
        Optional<NoteEntity> optionalExistingNote = noteRepository.findByTitle(createNewNoteRequest.title());
        if (optionalExistingNote.isPresent()) {
            throw new NoteAlreadyExistsException(createNewNoteRequest.title());
        }
        NoteEntity newNote = NoteEntity.createNew(createNewNoteRequest.title());
        NoteVersionEntity newNoteVersion = NoteVersionEntity.createNew(createNewNoteRequest.content(), 1L);
        newNote.addVersion(newNoteVersion);
        return Uni.createFrom().item(noteRepository.save(newNote));
    }

    public Page<NoteEntity> getNotes(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return noteRepository.findAll(pageRequest);
    }

    public void deleteNote(String noteId) {
        noteRepository.deleteById(noteId);
    }

    public Optional<NoteEntity> getNoteById(String id) {
        return noteRepository.findById(id);
    }

    public Optional<NoteVersionEntity> getNoteVersionById(String id) {
        return noteVersionRepository.findById(id);
    }

    public Uni<NoteVersionEntity> createNoteVersion(String noteId, CreateNewNoteVersionRequest createNewNoteVersionRequest) {
        return Uni.createFrom().item(() -> {
            NoteEntity noteEntity = noteRepository.findById(noteId)
                    .orElseThrow(() -> NotFoundException.noteNotFound(noteId));
            noteEntity.setLatestVersionNo(noteEntity.getLatestVersionNo() + 1);
            Long versionNumber = noteEntity.getLatestVersionNo();
            NoteVersionEntity newNoteVersion = NoteVersionEntity.createNew(createNewNoteVersionRequest.content(), versionNumber);
            noteEntity.addVersion(newNoteVersion);
            noteEntity.setCurrentVersionId(newNoteVersion.getId());
            noteRepository.save(noteEntity);
            return noteVersionRepository.findById(newNoteVersion.getId()).get();
        });
    }

    public Page<NoteVersionEntity> getNoteVersions(String noteId, int pageNumber, int pageSize) {
        noteRepository.findById(noteId).orElseThrow(() -> NotFoundException.noteNotFound(noteId));
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return noteVersionRepository.findAllByNoteIdOrderByVersionNoDesc(noteId, pageRequest);
    }

    public Uni<NoteEntity> restoreVersion(String noteId, String versionId) {
        return Uni.createFrom().item(() -> {
            NoteEntity noteEntity = noteRepository.findById(noteId)
                    .orElseThrow(() -> NotFoundException.noteNotFound(noteId));
            NoteVersionEntity noteVersionEntity = noteVersionRepository.findByNoteIdAndId(noteId, versionId)
                    .orElseThrow(() -> NotFoundException.noteVersionNotFoundForNoteId(noteId, versionId));
            if (noteEntity.getCurrentVersionId().equals(noteVersionEntity.getId())) {
                return noteEntity;
            }
            noteEntity.setCurrentVersionId(noteVersionEntity.getId());
            noteEntity.setLatestVersionNo(noteVersionEntity.getVersionNo());
            noteVersionRepository.deleteAllByNoteIdAndVersionNoGreaterThan(noteId, noteVersionEntity.getVersionNo());
            return noteRepository.save(noteEntity);
        });
    }
}
