package data;

import io.restassured.RestAssured;
import org.junit.Before;

import static data.DataTest.BASE_URI;

public class BaseTest {
    @Before
    public void startUp(){
        RestAssured.baseURI = BASE_URI;
    }
}
