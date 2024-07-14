package com.magnoliacms.domain.note;

import com.magnoliacms.AppConfig;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@ApplicationScoped
public class CleanupScheduler {

    private static final Logger LOG = Logger.getLogger(CleanupScheduler.class);

    private final AppConfig appConfig;
    private final NoteRepository noteRepository;
    private final NoteVersionRepository noteVersionRepository;

    @Scheduled(cron = "{app.cleanup-scheduler.cron}")
    @Transactional
    void cleanUpNotes() {
        Instant expirationDate = Instant.now().minus(appConfig.cleanupScheduler().expirationDays(), ChronoUnit.DAYS);
        LOG.info("Running cleanup scheduler for all notes older than " + expirationDate.toString());
        noteRepository.selectAllWhereCreatedAtIsBefore(expirationDate)
                .forEach(note -> {
                    noteVersionRepository.deleteAllByNoteId(note.getId());
                    noteRepository.delete(note);
                });
    }
}
