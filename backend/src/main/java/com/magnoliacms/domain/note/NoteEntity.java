package com.magnoliacms.domain.note;

import com.magnoliacms.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "note")
@NoArgsConstructor
@AllArgsConstructor
public class NoteEntity extends AuditableEntity {

    @Serial
    private static final long serialVersionUID = 13252432256L;

    @Id
    private String id;

    @NotBlank
    @Length(max = 128)
    private String title;

    @OneToMany(mappedBy = "note", orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<NoteVersionEntity> versions = new ArrayList<>();

    @Column(name = "latest_version", nullable = false)
    private Long latestVersionNo;

    @Column(name = "current_version", nullable = false)
    private String currentVersionId;

    public void addVersion(NoteVersionEntity noteVersion) {
        noteVersion.setNote(this);
        versions.add(noteVersion);
        currentVersionId = noteVersion.getId();
    }

    public static NoteEntity createNew(String title) {
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setId(UUID.randomUUID().toString());
        noteEntity.setTitle(title);
        noteEntity.setLatestVersionNo(1L);
        return noteEntity;
    }
}
