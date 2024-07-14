package com.magnoliacms.domain.note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

public interface NoteRepository extends JpaRepository<NoteEntity, String> {

    Optional<NoteEntity> findByTitle(String title);

    Page<NoteEntity> findAll(Pageable pageable);

    @Query("SELECT n FROM NoteEntity n WHERE n.createdAt < :createdAt")
    Stream<NoteEntity> selectAllWhereCreatedAtIsBefore(Instant createdAt);
}
