import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class ApiTestsGetUser extends LoginSetUp {

    @Test
    public void getUser200() {

     /*   RestAssured.baseURI = "http://training.skillo-bg.com:3100";*/
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
                .get("/users/" + loggedUserId)
                .then()
                .statusCode(401);
    }
}