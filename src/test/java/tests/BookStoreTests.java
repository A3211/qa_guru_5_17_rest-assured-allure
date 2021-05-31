package tests;

import models.AuthorizationResponse;
import org.junit.jupiter.api.Test;

import static filters.CustomLogFilter.customLogFilter;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class BookStoreTests {
    @Test
    void noLogsTest() {
        given()
                .get("https://demoqa.com/BookStore/v1/Books")
                .then()
                .body("books", hasSize(greaterThan(0)));
    }

    @Test
    void withAllureLogsTest() {
        given()
                .filter(customLogFilter().withCustomTemplates())
                .log().all()
                .get("https://demoqa.com/BookStore/v1/Books")
                .then()
                .log().all()
                .body("books", hasSize(greaterThan(0)));
    }

    @Test
    void withModelTest() {
        AuthorizationResponse response =
                given()
                        .filter(customLogFilter().withCustomTemplates())
                        .contentType(JSON)
                        .body("{ \"userName\": \"alex\", \"password\": \"asdsad#frew_DFS2\" }")
                        .when()
                        .log().uri()
                        .log().body()
                        .post("https://demoqa.com/Account/v1/GenerateToken")
                        .then()
                        .log().body()
                        .extract().as(AuthorizationResponse.class);

        assertThat(response.getStatus()).isEqualTo("Success");
        assertThat(response.getResult()).isEqualTo("User authorized successfully.");
    }
}
