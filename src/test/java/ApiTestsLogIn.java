import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ApiTestsLogIn {

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
