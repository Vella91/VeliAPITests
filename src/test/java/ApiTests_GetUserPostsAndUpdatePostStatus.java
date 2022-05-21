import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiTests_GetUserPostsAndUpdatePostStatus {

    static String loginToken;
    static Integer myUserId;
    public Integer postId;


    /*@BeforeMethod
    public void loginTest() throws JsonProcessingException {
        LoginPOJO login = new LoginPOJO();

        //set the login credentials by using the loginPOJO class setters
        login.setUsernameOrEmail("test91");
        login.setPassword("test91");

        //convert pojo object to json using Jackson library!
        ObjectMapper objectMapper = new ObjectMapper();
        String convertedJson = objectMapper.writeValueAsString(login);
        System.out.println("Converted JSON for Login is " + convertedJson);

        RestAssured.baseURI = "http://training.skillo-bg.com:3100";
        Response response = given()
                .header("Content-Type", "application/json")
                .body(convertedJson)
                .when()
                .post("/users/login");


        //convert the response body json into a string
        String loginResponseBody = response.getBody().asString();
        loginToken = JsonPath.parse(loginResponseBody).read("$.token");
        System.out.println("Extracted token is: " + loginToken);

        myUserId = JsonPath.parse(loginResponseBody).read("$.user.id");
    }
*/

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