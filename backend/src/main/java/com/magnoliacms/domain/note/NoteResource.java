package com.magnoliacms.domain.note;

import com.magnoliacms.AppConfig;
import com.magnoliacms.domain.note.dto.*;
import com.magnoliacms.exception.NotFoundException;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;
import org.springframework.data.domain.Page;

import java.net.URI;
import java.util.concurrent.Executor;

@RequiredArgsConstructor
@Path("/api/notes")
public class NoteResource {

    private static final Logger LOG = Logger.getLogger(NoteResource.class);

    private final AppConfig appConfig;
    private final Validator validator;
    private final NoteService noteService;

    @Named("dbExecutor")
    private Executor dbExecutor;

    @RolesAllowed({"User"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @POST
    public Uni<Response> createNote(CreateNewNoteRequest createNewNoteRequest) {
        validator.validate(createNewNoteRequest);
        return noteService.createNote(createNewNoteRequest)
                .emitOn(dbExecutor)
                .onItem()
                .transform(noteEntity -> Response.created(createLocationHeaderForNewNote(noteEntity.getId()))
                        .entity(CreateNewNoteResponse.createNewNoteResponse(noteEntity))
                        .build());
    }

    @RolesAllowed({"User"})
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Page<NoteDto> getNotes(@QueryParam("page") @DefaultValue("0") @Min(0) int page,
                                  @QueryParam("limit") @DefaultValue("20") @Min(1) @Max(50) int limit) {
        return noteService.getNotes(page, limit)
                .map(this::NoteEntityToDto);
    }

    @RolesAllowed({"User"})
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}")
    public NoteDto getNoteById(@PathParam("id") String id) {
        return noteService.getNoteById(id)
                .map(this::NoteEntityToDto)
                .orElseThrow(() -> NotFoundException.noteNotFound(id));
    }

    @RolesAllowed({"User"})
    @DELETE
    @Path("/{noteId}")
    public void deleteNote(@PathParam("noteId") String noteId) {
        noteService.deleteNote(noteId);
    }

    @RolesAllowed({"User"})
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{noteId}/versions")
    @Transactional
    public Uni<Response> createNewNoteVersion(@PathParam("noteId") String noteId,
                                              CreateNewNoteVersionRequest createNewNoteVersionRequest) {
        validator.validate(createNewNoteVersionRequest);
        return noteService.createNoteVersion(noteId, createNewNoteVersionRequest)
                .map(noteVersionEntity -> Response
                        .created(createLocationHeaderForNewNoteVersion(noteId, noteVersionEntity.getId()))
                        .entity(NoteVersionDto.fromEntity(noteVersionEntity))
                        .build());
    }

    @RolesAllowed({"User"})
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{noteId}/versions")
    public Page<NoteVersionDto> getNoteVersions(
            @PathParam("noteId") String noteId,
            @QueryParam("page") @DefaultValue("0") @Min(0) int page,
            @QueryParam("limit") @DefaultValue("20") @Min(1) @Max(50) int limit) {
        return noteService.getNoteVersions(noteId, page, limit)
                .map(NoteVersionDto::fromEntity);
    }

    @RolesAllowed({"User"})
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{noteId}/versions/{versionId}/restore")
    @Transactional
    public Uni<NoteDto> restoreVersion(
            @PathParam("noteId") String noteId,
            @PathParam("versionId") String versionId) {
        return noteService.restoreVersion(noteId, versionId)
                .map(this::NoteEntityToDto);
    }

    private URI createLocationHeaderForNewNote(String noteId) {
        return URI.create(appConfig.baseUrl() + "/api/notes/" + noteId);
    }

    private URI createLocationHeaderForNewNoteVersion(String noteId, String noteVersionId) {
        return URI.create(appConfig.baseUrl() + "/api/notes/" + noteId + "/version/" + noteVersionId);
    }

    private NoteDto NoteEntityToDto(NoteEntity noteEntity) {
        NoteVersionEntity noteVersionEntity = noteService.getNoteVersionById(noteEntity.getCurrentVersionId())
                .orElseThrow(() -> NotFoundException.noteVersionNotFound(noteEntity.getCurrentVersionId()));
        NoteVersionDto noteVersionDto = NoteVersionDto.fromEntity(noteVersionEntity);
        return new NoteDto(noteEntity.getId(), noteEntity.getTitle(), noteVersionDto);
    }
}
