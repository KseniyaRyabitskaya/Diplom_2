import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateUserInformationTest {

    User firstUser;
    User secondUser;

    @Before
    public void setUp() {
        firstUser = new User("alexxxbublikovvv@mail.ru", "Hfggg65JJhg", "Alex");
        secondUser = new User("borisivanovvv67@yandex.ru", "hdfjvHH34yyyGG", "Boris");
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

        firstUser.setEmail("alexxxbublikovvvff@mail.ru");

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

        firstUser.setName("Alexxx67");

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

        firstUser.setEmail("alexxxbublikovvvff@mail.ru");
        firstUser.setName("Alexxx67");

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
                                "alexxxbublikovvvff@mail.ru",
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
                                "Alexxx67"
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
                                "alexxxbublikovvvff@mail.ru",
                                firstUser.getPassword(),
                                "Alexxx67"
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
