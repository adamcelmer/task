package com.magnoliacms.domain.note.dto;

import com.magnoliacms.domain.note.NoteVersionEntity;

public record NoteVersionDto(String id, String content, Long versionNo) {

    public static NoteVersionDto fromEntity(NoteVersionEntity entity) {
        return new NoteVersionDto(entity.getId(), entity.getContent(), entity.getVersionNo());
    }
}
