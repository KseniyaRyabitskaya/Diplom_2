import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateUserInformationTest {

    User firstUser;
    User secondUser;
    Faker faker;

    @Before
    public void setUp() {
        faker = new Faker();
        firstUser = new User(faker.internet().emailAddress(), faker.internet().password(6, 20), faker.name().firstName());
        secondUser = new User(faker.internet().emailAddress(), faker.internet().password(6, 20), faker.name().firstName());
        UserApi.createUser(firstUser);
        UserApi.createUser(secondUser);
    }

    @Test
    public void changeEmailForAuthorisedUserTest() {
        String accessTokenFirstUser = UserApi.loginUser(firstUser)
                .then()
                .extract()
                .body()
                .path("accessToken");

        firstUser.setEmail(faker.internet().emailAddress());

        UserApi.changeUserInformationWithAuthorization(accessTokenFirstUser, firstUser)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("user.email", equalTo(firstUser.getEmail()));
    }

    @Test
    public void changeNameForAuthorisedUserTest() {
        String accessTokenFirstUser = UserApi.loginUser(firstUser)
                .then()
                .extract()
                .body()
                .path("accessToken");

        firstUser.setName(faker.name().firstName());

        UserApi.changeUserInformationWithAuthorization(accessTokenFirstUser, firstUser)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("user.name", equalTo(firstUser.getName()));
    }

    @Test
    public void changeEmailAndNameForAuthorisedUserTest() {
        String accessTokenFirstUser = UserApi.loginUser(firstUser)
                .then()
                .extract()
                .body()
                .path("accessToken");

        firstUser.setEmail(faker.internet().emailAddress());
        firstUser.setName(faker.name().firstName());

        UserApi.changeUserInformationWithAuthorization(accessTokenFirstUser, firstUser)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("user.email", equalTo(firstUser.getEmail()))
                .and()
                .body("user.name", equalTo(firstUser.getName()));
    }

    @Test
    public void changeEmailToExistEmailForAuthorisedUserTest() {
        String accessTokenFirstUser = UserApi.loginUser(firstUser)
                .then()
                .extract()
                .body()
                .path("accessToken");

        UserApi.changeUserInformationWithAuthorization(
                        accessTokenFirstUser,
                        new User(
                                secondUser.getEmail(),
                                firstUser.getPassword(),
                                firstUser.getName()
                        )
                )
                .then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("User with such email already exists"));
    }

    @Test
    public void changeEmailForUnauthorisedUserTest() {
        UserApi.changeUserInformationWithoutAuthorization(
                        new User(
                                faker.internet().emailAddress(),
                                firstUser.getPassword(),
                                firstUser.getName()
                        )
                )
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    public void changeNameForUnauthorisedUserTest() {
        UserApi.changeUserInformationWithoutAuthorization(
                        new User(
                                firstUser.getEmail(),
                                firstUser.getPassword(),
                                faker.name().firstName()
                        )
                )
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    public void changeEmailAndNameForUnauthorisedUserTest() {
        UserApi.changeUserInformationWithoutAuthorization(
                        new User(
                                faker.internet().emailAddress(),
                                firstUser.getPassword(),
                                faker.name().firstName()
                        )
                )
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    public void changeEmailToExistEmailForUnauthorisedUserTest() {
        UserApi.changeUserInformationWithoutAuthorization(
                        new User(
                                secondUser.getEmail(),
                                firstUser.getPassword(),
                                firstUser.getName()
                        )
                )
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void deleteUser() {
        String accessTokenFirstUser = UserApi.loginUser(firstUser)
                .then()
                .extract()
                .body()
                .path("accessToken");

        String accessTokenSecondUser = UserApi.loginUser(secondUser)
                .then()
                .extract()
                .body()
                .path("accessToken");

        UserApi.deleteUser(accessTokenFirstUser)
                .then()
                .assertThat()
                .statusCode(202)
                .and()
                .body("success", equalTo(true));

        UserApi.deleteUser(accessTokenSecondUser)
                .then()
                .assertThat()
                .statusCode(202)
                .and()
                .body("success", equalTo(true));
    }
}
