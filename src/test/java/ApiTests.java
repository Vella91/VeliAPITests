import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;


public class ApiTests {

    public String loginToken;
    public Integer myUserId;
    public Integer postId;
    public Integer userId;
    public Integer commentId;

    //before method to get Access token
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
    public void loginTest400() throws JsonProcessingException {
        LoginPOJO login = new LoginPOJO();

        //set the login credentials by using the loginPOJO class setters
        login.setUsernameOrEmail("test91test");
        login.setPassword("test91");

        ObjectMapper objectMapper = new ObjectMapper();
        String convertedJson2 = objectMapper.writeValueAsString(login);

        given()
                .header("Content-Type", "application/json")
                .body(convertedJson2)
                .when()
                .post("/users/login")
                .then()
                .statusCode(400)
                .log()
                .all();
    }

        //Tests for Get Posts
    @Test
    public void getPosts200() {
        Response response = given()
                .when()
                .get("/posts?take=5&skip=0");
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


    //Tests for Get User
    @Test
    public void getUser200() {
        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .when()
                .get("/users/" + myUserId)
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

    //Tests for PATCH like post
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

    @Test
    public void likePost400() {
        ActionsPOJO likePost = new ActionsPOJO();
        likePost.setAction("likePost");

        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .body(likePost)
                .when()
                .patch("/posts/1000")
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


    //Tests for PATCH Follow User
    @Test
    public void followUser200() {

        getPosts200();
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

    @Test
    public void unfollowUser200() {

        getPosts200();
        ActionsPOJO unfollowUser = new ActionsPOJO();
        unfollowUser.setAction("unfollowUser");

        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .body(unfollowUser)
                .when()
                .patch("/users/" + userId)
                .then()
                .statusCode(200)
                .body("user.id", equalTo(userId))
                .log()
                .all();
    }

    @Test
    public void followUser401() {

        getPosts200();
        ActionsPOJO followUser = new ActionsPOJO();
        followUser.setAction("followUser");

        given()
                .header("Content-Type", "application/json")
                .body(followUser)
                .when()
                .patch("/users/" + userId)
                .then()
                .statusCode(401);
    }

    @Test
    public void getUserPosts200() {
        Response response = (Response) given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .queryParam("take", 5)
                .queryParam("skip", 0)
                .when()
                .get("/users/" + myUserId + "/posts");
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
                .get("/users/" + myUserId + "/posts");
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
                .get("/users" + myUserId + "posts");
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
    public void putPostStatus200() {
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

    @Test
    public void putPostStatus401() {
        ActionsPOJO postStatus = new ActionsPOJO();
        postStatus.setPostStatus("public");
        Response response = (Response) given()
                .header("Content-Type", "application/json")
                .body(postStatus)
                .when()
                .put("/posts/" + postId);
        response
                .then()
                .statusCode(401);
    }

    @Test
    public void putPostStatus400() {
        ActionsPOJO postStatus = new ActionsPOJO();
        postStatus.setPostStatus("public");
        Response response = (Response) given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .body(postStatus)
                .when()
                .put("/posts/1000");
        response
                .then()
                .statusCode(400);
    }

    @Test
    public void commentPost201() {
        ActionsPOJO commentPost = new ActionsPOJO();
        commentPost.setContent("My Newest Comment!");

        Response response = (Response)  given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .body(commentPost)
                .when()
                .post("/posts/4626/comment");
        response
                .then()
                .body("content", equalTo("My Newest Comment!"))
                .log()
                .all()
                .statusCode(201);

        String comment = response.getBody().asString();
        commentId = JsonPath.parse(comment).read("$.id");
    }

    @Test
    public void commentPost401() {
        ActionsPOJO commentPost = new ActionsPOJO();
        commentPost.setContent("My New Comment!");

        given()
                .header("Content-Type", "application/json")
                .body(commentPost)
                .when()
                .post("/posts/4626/comment")
                .then()
                .log()
                .all()
                .statusCode(401);
    }

    @AfterTest
    public void DeleteCommentPost200(){
        given()
                .header("Authorization", "Bearer " + loginToken)
                .header("Content-Type", "application/json")
                .when()
                .delete("posts/4626/comments/" + commentId)
                .then()
                .log()
                .all()
                .statusCode(200);
    }
}