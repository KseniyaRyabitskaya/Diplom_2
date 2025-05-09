import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrdersCertainUserTest {
    User user;

    @Before
    public void createUser() {
        user = new User("alexxxbublikovvv@mail.ru", "Hfggg65JJhg", "Alex");
        UserApi.createUser(user);
    }

    @Test
    public void getOrdersOfCertainUserWithAuthorizationTest() {
        String accessToken = UserApi.loginUser(user)
                .then()
                .extract()
                .body()
                .path("accessToken");

        OrderApi.createOrderWithAuthorization(
                new Order(new String[]{IngredientsHash.FLUORESCENT_BUN, IngredientsHash.SAUCE_SPICY_X, IngredientsHash.BEEF_METEORITE}),
                accessToken
        );

        OrderApi.createOrderWithAuthorization(
                new Order(new String[]{IngredientsHash.FLUORESCENT_BUN, IngredientsHash.SAUCE_SPICY_X, IngredientsHash.BEEF_METEORITE}),
                accessToken
        );

        OrderApi.getListOrdersOfCertainUserWithAuthorization(accessToken)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("orders", notNullValue())
                .and()
                .body("orders.size()", equalTo(2));
    }

    @Test
    public void getOrdersOfCertainUserWithoutAuthorizationTest() {
        OrderApi.getListOrdersOfCertainUserWithoutAuthorization()
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
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
