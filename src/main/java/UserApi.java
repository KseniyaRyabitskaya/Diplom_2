import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserApi {

    @Step("Send POST request to /api/auth/register")
    public static Response createUser(User user) {
        return given()
                .spec(RestAssuredUtils.getRequestSpecification())
                .and()
                .body(user)
                .when()
                .post(Endpoints.REGISTER_ENDPOINT);
    }

    @Step("Send POST request to /api/auth/login")
    public static Response loginUser(User userLogin) {
        return given()
                .spec(RestAssuredUtils.getRequestSpecification())
                .and()
                .body(userLogin)
                .when()
                .post(Endpoints.LOGIN_ENDPOINT);
    }

    @Step("Send DELETE request to /api/auth/user")
    public static Response deleteUser(String token) {
        return given()
                .spec(RestAssuredUtils.getRequestSpecification())
                .header("Authorization", token)
                .and()
                .delete(Endpoints.USER_ENDPOINT);
    }

    @Step("Send PATCH request to /api/auth/user")
    public static Response changeUserInformationWithAuthorization(String token, User user) {
        return given()
                .spec(RestAssuredUtils.getRequestSpecification())
                .header("Authorization", token)
                .and()
                .body(user)
                .patch(Endpoints.USER_ENDPOINT);
    }

    @Step("Send PATCH request to /api/auth/user")
    public static Response changeUserInformationWithoutAuthorization(User user) {
        return given()
                .spec(RestAssuredUtils.getRequestSpecification())
                .and()
                .body(user)
                .patch(Endpoints.USER_ENDPOINT);
    }
}
