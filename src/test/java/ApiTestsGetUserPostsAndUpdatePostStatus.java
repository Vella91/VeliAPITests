import com.jayway.jsonpath.JsonPath;
import groovy.util.logging.Log;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiTestsGetUserPostsAndUpdatePostStatus extends LoginSetUp {

    public Integer postId;

    @Test
    public void getUserPosts200() {
        Response response = (Response) given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .queryParam("take", 5)
                .queryParam("skip", 0)
                .when()
                .get("/users/" + loggedUserId + "/posts");
        response
                .then()
                .statusCode(200);

        String posts = response.getBody().asString();
        postId = JsonPath.parse(posts).read("$.[0].id");
    }

    @Test
    public void getUserPosts401() {
        Response response = (Response) given()
                .header("Content-Type", "application/json")
                .queryParam("take", 5)
                .queryParam("skip", 0)
                .when()
                .get("/users/" + loggedUserId + "/posts");
        response
                .then()
                .statusCode(401);
    }

    @Test
    public void getUserPosts404() {
        Response response = (Response) given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .queryParam("take", 5)
                .queryParam("skip", 0)
                .when()
                .get("/users" + loggedUserId + "posts");
        response
                .then()
                .statusCode(404);
    }

    @Test
    public void getUserPosts400() {
        Response response = (Response) given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .queryParam("take", 5)
                .queryParam("skip", 0)
                .when()
                .get("/users/1000/posts");
        response
                .then()
                .statusCode(400);
    }

    @Test
    public void putPostStatus() {
        ActionsPOJO postStatus = new ActionsPOJO();
        postStatus.setPostStatus("public");
        Response response = (Response) given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .body(postStatus)
                .when()
                .put("/posts/" + postId);
        response
                .then()
                .statusCode(200)
                .body("coverUrl", equalTo("https://i.imgur.com/D6cm1pa.png"));
    }
}