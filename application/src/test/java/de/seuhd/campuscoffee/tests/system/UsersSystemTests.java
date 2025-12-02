package de.seuhd.campuscoffee.systest; // WICHTIG: Der korrekte package-Name!

import de.seuhd.campuscoffee.TestUtils;
import de.seuhd.campuscoffee.api.dtos.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class UsersSystemTests extends AbstractSysTest {

    @Test
    void shouldCreateUserAndThenRetrieveItById() {
        // ARRANGE (Vorbereitung)
        // Wir nehmen uns einen Test-User aus den TestFixtures
        UserDto userToCreate = TestUtils.USER_FIXTURES.get(0);

        // ACT (Ausführung) - User erstellen
        UserDto createdUser = given()
                .contentType("application/json")
                .body(userToCreate)
                .when()
                .post("/api/users")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(UserDto.class);

        // ASSERT (Überprüfung)
        assertThat(createdUser.id()).isNotNull();
        assertThat(createdUser.loginName()).isEqualTo(userToCreate.loginName());

        // Zusätzlicher Test: Können wir ihn per ID wieder abrufen?
        UserDto retrievedUser = TestUtils.retrieveUserById(createdUser.id());
        assertThat(retrievedUser).isEqualTo(createdUser);
    }

    @Test
    void shouldUpdateUserFirstName() {
        // ARRANGE
        UserDto createdUser = TestUtils.createUser(TestUtils.USER_FIXTURES.get(1));
        UserDto userToUpdate = createdUser.toBuilder().firstName("Maxine").build();

        // ACT
        UserDto updatedUser = given()
                .contentType("application/json")
                .body(userToUpdate)
                .when()
                .put("/api/users/" + createdUser.id())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(UserDto.class);

        // ASSERT
        assertThat(updatedUser.firstName()).isEqualTo("Maxine");
        assertThat(updatedUser.lastName()).isEqualTo(createdUser.lastName()); // Nachname sollte gleich bleiben
    }
}