import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ApiTests_GetUser {

    public String loginToken;
    public Integer myUserId;

//D: the API returns 401 when user is authorized
    @Test
    public void getUser200() {
        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .get("/users/2450")
                .then()
                .statusCode(200);
    }

    @Test
    public void getUser401(){
        given()
                .header("Content-Type", "application/json")
                .when()
                .get("/users/" + myUserId)
                .then()
                .statusCode(401);
    }
}