import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest {
    User user;

    @Before
    public void setUp() {
        Faker faker = new Faker();
        user = new User(faker.internet().emailAddress(), faker.internet().password(6, 20), faker.name().firstName());
    }

    @Test
    public void createUserTest() {
        UserApi.createUser(user)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    public void createIdenticalUserTest() {
        UserApi.createUser(user);
        UserApi.createUser(user)
                .then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    public void createUserWithoutEmailTest() {
        UserApi.createUser(new User("", user.getPassword(), user.getName()))
                .then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithoutPasswordTest() {
        UserApi.createUser(new User(user.getEmail(), "", user.getName()))
                .then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithoutNameTest() {
        UserApi.createUser(new User(user.getEmail(), user.getPassword(), ""))
                .then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithoutAllFieldsTest() {
        UserApi.createUser(new User("", "", ""))
                .then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void deleteUser() {
        String accessToken = UserApi.loginUser(user)
                .then()
                .extract()
                .body()
                .path("accessToken");

        if (accessToken != null) {
            UserApi.deleteUser(accessToken)
                    .then()
                    .assertThat()
                    .statusCode(202)
                    .and()
                    .body("success", equalTo(true));
        }
    }
}
