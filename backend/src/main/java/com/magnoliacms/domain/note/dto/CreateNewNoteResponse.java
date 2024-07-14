package com.magnoliacms.domain.note.dto;

import com.magnoliacms.domain.note.NoteEntity;

public record CreateNewNoteResponse(String id, String title) {

    public static CreateNewNoteResponse createNewNoteResponse(NoteEntity noteEntity) {
        return new CreateNewNoteResponse(noteEntity.getId(), noteEntity.getTitle());
    }
}
