import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ApiTests_GetUsersPosts {

    static String loginToken;
    static Integer myUserId;
    public Integer privatePostId;

    @BeforeMethod
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
            privatePostId = JsonPath.parse(posts).read("$.[0].id");
    }
}