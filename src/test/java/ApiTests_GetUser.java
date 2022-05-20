import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ApiTests_GetUser {

    static String loginToken;
    static int myUserId;


    ApiTests_PostLogin login = new ApiTests_PostLogin();

    @Test
    public void getUser() {
        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .get("/users/" + myUserId)
                .then()
                .statusCode(200);
    }
}