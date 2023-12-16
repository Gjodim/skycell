import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources", glue = "com.example.steps")
public class CucumberTestRunnerTest {
}

@Test
public class AuthenticationStepsTest {

    private Response response;
    private final Properties config = new Properties();

    public AuthenticationSteps() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            config.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }

    @Given("the authentication URL is {string}")
    public void setAuthenticationURL(String url) {
        RestAssured.baseURI = url;
    }

    @And("the content type is {string}")
    public void setContentType(String contentType) {
        RestAssured.given().contentType(contentType);
    }

    @When("I authenticate with username {string} and password {string}")
    public void authenticate(String username, String password) {
        Map<String, String> credentials = getCredentials(username, password);

        response = given()
                .formParams(credentials)
                .when().post()
                .andReturn();
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @And("the response body contains an access token")
    public void verifyAccessTokenExists() {
        response.then().body("access_token", not(emptyOrNullString()));
    }

    private Map<String, String> getCredentials(String username, String password) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("client_id", "webapp");
        credentials.put("grant_type", "password");
        credentials.put("username", username);
        credentials.put("password", password);
        return credentials;
    }
}