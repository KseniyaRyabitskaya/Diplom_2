import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApi {
    @Step("Send POST request to /api/orders")
    public static Response createOrderWithAuthorization(Order order, String token) {
        return given()
                .spec(RestAssuredUtils.getRequestSpecification())
                .header("Authorization", token)
                .and()
                .body(order)
                .when()
                .post("/api/orders");
    }

    @Step("Send POST request to /api/orders")
    public static Response createOrderWithoutAuthorization(Order order) {
        return given()
                .spec(RestAssuredUtils.getRequestSpecification())
                .and()
                .body(order)
                .when()
                .post("/api/orders");
    }

    @Step("Send GET request to /api/orders")
    public static Response getListOrdersOfCertainUserWithAuthorization(String token) {
        return given()
                .spec(RestAssuredUtils.getRequestSpecification())
                .header("Authorization", token)
                .when()
                .get("/api/orders");
    }

    @Step("Send GET request to /api/orders")
    public static Response getListOrdersOfCertainUserWithoutAuthorization() {
        return given()
                .spec(RestAssuredUtils.getRequestSpecification())
                .when()
                .get("/api/orders");
    }
}
