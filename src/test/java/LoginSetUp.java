import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;

public class LoginSetUp {
    //creating a utility method to pass login token to other test suites
    protected static String loginToken;
    protected static Integer loggedUserId;
    private static Response response;

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI = "http://training.skillo-bg.com:3100";
        LoginSetUp.setLoginToken();
        LoginSetUp.getLoggedUserId();
    }


    private static void setLoginToken(){
        LoginPOJO login = new LoginPOJO();

        login.setUsernameOrEmail("test91");
        login.setPassword("test91");

        RestAssured.baseURI = "http://training.skillo-bg.com:3100";

         response = given()
                .header("Content-Type", "application/json")
                .body(login)
                .when()
                .post("/users/login");

        String loginResponseBody = response.getBody().asString();
        loginToken = JsonPath.parse(loginResponseBody).read("$.token");
        System.out.println("This is the login token: " + loginToken);
    }

    private static void getLoggedUserId(){

        String loginResponseBody = response.getBody().asString();
        loginToken = JsonPath.parse(loginResponseBody).read("$.token");
        System.out.println("Extracted token is: " + loginToken);

        loggedUserId = JsonPath.parse(loginResponseBody).read("$.user.id");
    }


}
