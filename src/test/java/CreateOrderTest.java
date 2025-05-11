import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {
    User user;

    @Before
    public void createUser() {
        Faker faker = new Faker();
        user = new User(faker.internet().emailAddress(), faker.internet().password(6, 20), faker.name().firstName());
        UserApi.createUser(user);
    }

    @Test
    public void createOrderWithIngredientsForAuthorizedUserTest() {
        String accessToken = UserApi.loginUser(user)
                .then()
                .extract()
                .body()
                .path("accessToken");

        OrderApi.createOrderWithAuthorization(
                        new Order(new String[]{IngredientsHash.FLUORESCENT_BUN, IngredientsHash.SAUCE_SPICY_X, IngredientsHash.BEEF_METEORITE}),
                        accessToken
                )
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    public void createOrderWithoutIngredientsForAuthorisedUserTest() {
        String accessToken = UserApi.loginUser(user)
                .then()
                .extract()
                .body()
                .path("accessToken");

        OrderApi.createOrderWithAuthorization(
                        new Order(new String[]{}),
                        accessToken
                )
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithIncorrectHashIngredientsForAuthorisedUserTest() {
        String accessToken = UserApi.loginUser(user)
                .then()
                .extract()
                .body()
                .path("accessToken");

        OrderApi.createOrderWithAuthorization(
                        new Order(new String[]{IngredientsHash.INCORRECT_INGREDIENT, IngredientsHash.INCORRECT_INGREDIENT}),
                        accessToken
                )
                .then()
                .assertThat()
                .statusCode(500);
    }

    @Test
    public void createOrderWithIngredientsForUnauthorisedUserTest() {
        OrderApi.createOrderWithoutAuthorization(
                        new Order(new String[]{IngredientsHash.FLUORESCENT_BUN, IngredientsHash.SAUCE_SPICY_X, IngredientsHash.BEEF_METEORITE})
                )
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    public void createOrderWithoutIngredientsForUnauthorisedUserTest() {
        OrderApi.createOrderWithoutAuthorization(
                        new Order(new String[]{})
                )
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithIncorrectHashIngredientsForUnauthorisedUserTest() {
        OrderApi.createOrderWithoutAuthorization(
                        new Order(new String[]{IngredientsHash.INCORRECT_INGREDIENT, IngredientsHash.INCORRECT_INGREDIENT})
                )
                .then()
                .assertThat()
                .statusCode(500);
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
