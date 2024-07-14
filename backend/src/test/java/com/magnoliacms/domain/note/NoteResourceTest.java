package com.magnoliacms.domain.note;

import com.magnoliacms.domain.note.dto.CreateNewNoteRequest;
import com.magnoliacms.domain.note.dto.CreateNewNoteVersionRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class NoteResourceTest {

    private static final String USER_EMAIL = "test@example.com";
    private static final String USER_PASSWORD = "password";

    @Inject
    NoteRepository noteRepository;

    @Inject
    NoteVersionRepository noteVersionRepository;

    @BeforeEach
    public void setup() {
        noteRepository.deleteAll();
        noteVersionRepository.deleteAll();
    }

    @Test
    @TestSecurity(user = "test@example.com", roles = "User")
    public void testCreateNote() {
        given()
                .contentType("application/json")
                .body(new CreateNewNoteRequest("test-note", "test-content"))
                .when()
                .post("/api/notes")
                .then()
                .assertThat()
                .statusCode(201)
                .and()
                .body("id", Matchers.notNullValue());
    }

    @Test
    @TestSecurity(user = "test@example.com", roles = "User")
    public void testGetAllNotes() {
        createNote();

        given()
                .when()
                .get("/api/notes")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("content[0]", Matchers.notNullValue());
    }

    @Test
    @TestSecurity(user = "test@example.com", roles = "User")
    public void testGetNoteById() {
        NoteEntity noteEntity = createNote();

        given()
                .when()
                .get("/api/notes/" + noteEntity.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("id", Matchers.equalTo(noteEntity.getId()));
    }

    @Test
    @TestSecurity(user = "test@example.com", roles = "User")
    public void deleteNote() {
        NoteEntity noteEntity = createNote();

        given()
                .when()
                .delete("/api/notes/" + noteEntity.getId())
                .then()
                .assertThat()
                .statusCode(204)
                .and();

        assertTrue(noteRepository.findById(noteEntity.getId()).isEmpty());
    }

    @Test
    @TestSecurity(user = "test@example.com", roles = "User")
    public void createNewNoteVersion() {
        NoteEntity noteEntity = createNote();

        given()
                .contentType("application/json")
                .body(new CreateNewNoteVersionRequest("new test content"))
                .when()
                .post("/api/notes/" + noteEntity.getId() + "/versions")
                .then()
                .assertThat()
                .statusCode(201)
                .and()
                .body("id", Matchers.notNullValue());

        assertEquals(2, noteVersionRepository.findAll().size());
    }

    @Test
    @TestSecurity(user = "test@example.com", roles = "User")
    public void getAllNoteVersions() {
        NoteEntity noteEntity = createNote();

        given()
                .when()
                .get("/api/notes/" + noteEntity.getId() + "/versions")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("content[0]", Matchers.notNullValue());
    }

    @Test
    @TestSecurity(user = "test@example.com", roles = "User")
    public void restoreVersion() {
        NoteEntity noteEntity = createNote();
        var initVersionId = noteVersionRepository.findAll().get(0).getId();
        var noteVersion = NoteVersionEntity.createNew("test-content-2", 2L);
        noteEntity.addVersion(noteVersion);
        noteEntity.setCurrentVersionId(noteVersion.getId());
        noteEntity.setLatestVersionNo(2L);
        noteEntity = noteRepository.save(noteEntity);

        given()
                .when()
                .post("/api/notes/" + noteEntity.getId() + "/versions/" + initVersionId + "/restore")
                .then()
                .assertThat()
                .statusCode(200);

        assertEquals(1, noteVersionRepository.findAll().size());
        assertNotEquals(noteVersion.getId(), noteRepository.findById(noteEntity.getId()).get().getCurrentVersionId());
        assertEquals(1L, noteRepository.findById(noteEntity.getId()).get().getLatestVersionNo());
    }

    private NoteEntity createNote() {
        var noteEntity = NoteEntity.createNew("test-note");
        noteEntity.setLatestVersionNo(1L);
        var noteVersion = NoteVersionEntity.createNew("test-content", 1L);
        noteEntity.addVersion(noteVersion);
        noteEntity.setCurrentVersionId(noteVersion.getId());
        return noteRepository.save(noteEntity);
    }
}
