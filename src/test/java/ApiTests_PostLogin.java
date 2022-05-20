import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ApiTests_PostLogin {

    static String loginToken;
    static int myUserId;

    @Test
    public void loginTest200() throws JsonProcessingException {
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
}
