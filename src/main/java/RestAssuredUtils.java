import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RestAssuredUtils {
    private static final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri("https://stellarburgers.nomoreparties.site/")
            .addHeader("Content-type", "application/json")
            .build();

    public static RequestSpecification getRequestSpecification() {
        return requestSpecification;
    }
}
