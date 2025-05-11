import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest {
    User user;
    Faker faker;

    @Before
    public void createUser() {
        faker = new Faker();
        user = new User(faker.internet().emailAddress(), faker.internet().password(6, 20), faker.name().firstName());
        UserApi.createUser(user);
    }

    @Test
    public void loginWithCorrectEmailAndPasswordTest() {
        UserApi.loginUser(new User(user.getEmail(), user.getPassword(), ""))
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    public void loginWithIncorrectEmailTest() {
        UserApi.loginUser(new User(faker.internet().emailAddress(), user.getPassword(), ""))
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void loginWithIncorrectPasswordTest() {
        UserApi.loginUser(new User(user.getEmail(), faker.internet().password(6, 20), ""))
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void loginWithoutEmailTest() {
        UserApi.loginUser(new User("", user.getPassword(), ""))
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void loginWithoutPasswordTest() {
        UserApi.loginUser(new User(user.getEmail(), "", ""))
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void loginWithoutEmailAndPasswordTest() {
        UserApi.loginUser(new User("", "", ""))
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void deleteUser() {
        String accessToken = UserApi.loginUser(user)
                .then()
                .extract()
                .body()
                .path("accessToken");

        UserApi.deleteUser(accessToken)
                .then()
                .assertThat()
                .statusCode(202)
                .and()
                .body("success", equalTo(true));
    }
}
