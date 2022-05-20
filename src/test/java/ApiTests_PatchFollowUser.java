import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiTests_PatchFollowUser {

        static String loginToken;
        static int userId;

        @Test
    public void followUser() {

        ActionsPOJO followUser = new ActionsPOJO();
        followUser.setAction("followUser");

        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .body(followUser)
                .when()
                .patch("/users/" + userId)
                .then()
                /*   .statusCode(200)*/
                .body("user.id", equalTo(userId))
                .log()
                .all();
    }
}
