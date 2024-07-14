package com.magnoliacms.security;

import com.magnoliacms.domain.user.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class AuthResourceIT {

    private static final String USER_EMAIL = "test-user@example.com";
    private static final String USER_PASSWORD = "test-password";

    @Inject
    UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    void registerUser() {
        given()
                .multiPart("email", USER_EMAIL)
                .multiPart("password", USER_PASSWORD)
                .when().post("/auth/register")
                .then()
                .statusCode(201);
    }

    @Test
    void returnConflictWhenRegisteringExistingUser() {
        given()
                .multiPart("email", USER_EMAIL)
                .multiPart("password", USER_PASSWORD)
                .when().post("/auth/register")
                .then()
                .statusCode(201);
        given()
                .multiPart("email", USER_EMAIL)
                .multiPart("password", USER_PASSWORD)
                .when().post("/auth/register")
                .then()
                .statusCode(409)
                .and()
                .body("errors[0]", equalTo("User already exists"));
    }

    @Test
    void getAuthToken() {
        // Register user
        given()
                .multiPart("email", USER_EMAIL)
                .multiPart("password", USER_PASSWORD)
                .when().post("/auth/register")
                .then()
                .statusCode(201);

        // Get token
        given()
                .multiPart("email", USER_EMAIL)
                .multiPart("password", USER_PASSWORD)
                .when().post("/auth/login")
                .then()
                .statusCode(200)
                .and()
                .body("token", notNullValue());
    }
}
