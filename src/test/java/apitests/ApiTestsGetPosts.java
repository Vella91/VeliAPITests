package apitests;

import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiTestsGetPosts extends LoginSetUp {

    static int postId;
    static int userId;

    @Test
    public void getPosts200() {
        Response response = (Response) given()
           .queryParam("take", 5)
           .queryParam("skip", 0)
           .when()
           .get("/posts");
        response
           .then()
           .statusCode(200);

        String posts = response.getBody().asString();
        postId = JsonPath.parse(posts).read("$.[0].id");
        userId = JsonPath.parse(posts).read("$.[0].user.id");


    }

    @Test
    public void getPosts400() {
        Response response = given()
                .header("Content-Type", "application/json")
                .queryParam("skip", 0)
                .when()
                .get("/posts");
        response
                .then()
                .statusCode(400)
                .body("message", equalTo("Take query param cannot be 0"));

    }
}
