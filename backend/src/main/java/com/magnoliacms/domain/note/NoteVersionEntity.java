package com.magnoliacms.domain.note;

import com.magnoliacms.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "note_version")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteVersionEntity extends AuditableEntity {

    @Serial
    private static final long serialVersionUID = 63434252L;

    @Id
    private String id;

    @Min(1)
    @Column(name = "version_no", nullable = false)
    private Long versionNo;

    @Length(min = 1, max = 5000)
    private String content;

    @ManyToOne
    @JoinColumn(name="note_id", nullable=false)
    private NoteEntity note;

    public static NoteVersionEntity createNew(String content, Long versionNo) {
        NoteVersionEntity newNoteVersion = new NoteVersionEntity();
        newNoteVersion.setId(UUID.randomUUID().toString());
        newNoteVersion.setContent(content);
        newNoteVersion.setVersionNo(versionNo);
        return newNoteVersion;
    }
}
