package com.magnoliacms.domain.note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteVersionRepository extends JpaRepository<NoteVersionEntity, String> {

    Page<NoteVersionEntity> findAllByNoteIdOrderByVersionNoDesc(String noteId, PageRequest pageRequest);

    Optional<NoteVersionEntity> findByNoteIdAndId(String noteId, String versionId);

    void deleteAllByNoteIdAndVersionNoGreaterThan(String noteId, Long versionNo);

    void deleteAllByNoteId(String noteId);
}
