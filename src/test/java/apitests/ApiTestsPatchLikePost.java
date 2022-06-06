package apitests;

import apitests.ActionsPOJO;
import apitests.LoginSetUp;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ApiTestsPatchLikePost extends LoginSetUp {

    static int postId;

        @Test
                public void likePost200() {
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

        //test with wrong action wording in JSON to test for bad request
    @Test
    public void likePost400() {
        ActionsPOJO likePost = new ActionsPOJO();
        likePost.setAction("likePosts");

        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .body(likePost)
                .when()
                .patch("/posts/" + postId)
                .then()
                .statusCode(400)
                .log()
                .all();
    }

    //test for Unauthorized - no Bearer token is passed
    @Test
    public void likePost401() {
        ActionsPOJO likePost = new ActionsPOJO();
        likePost.setAction("likePosts");

        given()
                .header("Content-Type", "application/json")
                .body(likePost)
                .when()
                .patch("/posts/" + postId)
                .then()
                .statusCode(401)
                .log()
                .all();
    }
}
