import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;


public class ApiTests {

    public String loginToken;
    public Integer myUserId;
    public Integer postId;
    public Integer userId;

    @BeforeMethod
    public void loginTest() throws JsonProcessingException {
        LoginPOJO login = new LoginPOJO();

        //set the login credentials by using the loginPOJO class setters
        login.setUsernameOrEmail("test91");
        login.setPassword("test91");

        RestAssured.baseURI = "http://training.skillo-bg.com:3100";

        //convert pojo object to json using Jackson library!
        ObjectMapper objectMapper = new ObjectMapper();
        String convertedJson = objectMapper.writeValueAsString(login);
        System.out.println("Converted JSON for Login is " + convertedJson);

        Response response = given()
                .header("Content-Type", "application/json")
                .body(convertedJson)
                .when()
                .post("/users/login");

        response
                .then()
                .statusCode(201);

        //convert the response body json into a string
        String loginResponseBody = response.getBody().asString();
        loginToken = JsonPath.parse(loginResponseBody).read("$.token");
        System.out.println("Extracted token is: " + loginToken);

        myUserId = JsonPath.parse(loginResponseBody).read("$.user.id");
    }

    @Test
    public void getPosts() {
        Response response = given()
                .when()
                .get("/posts?take=5&skip=0");

        String posts = response.getBody().asString();
        postId = JsonPath.parse(posts).read("$.[0].id");
        userId = JsonPath.parse(posts).read("$.[0].user.id");
    }

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

    @Test
    public void followUser() {

        getPosts();
        ActionsPOJO followUser = new ActionsPOJO();
        followUser.setAction("followUser");

        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .body(followUser)
                .when()
                .patch("/users/" + userId)
                .then()
                .statusCode(200)
                .body("user.id", equalTo(userId))
                .log()
                .all();
    }
}