import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ApiTests_PatchLikePost {

    static String loginToken;
    static int postId;

        @Test
                public void likePost() {
            ActionsPOJO likePost = new ActionsPOJO();
            likePost.setAction("likePost");

            given()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + loginToken)
                    .body(likePost)
                    .when()
                    .patch("/posts/" + postId)
                    .then()
                    .statusCode(200)
                    .log()
                    .all();
        }


}
