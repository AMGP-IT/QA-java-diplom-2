package userTests;

import data.BaseTest;
import models.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static data.DataTest.*;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static steps.UserSteps.createUser;
import static steps.UserSteps.deleteUser;

public class CreateUserTest extends BaseTest {
    private UserModel userModel;

    @Before
    public void setUp(){
        userModel = new UserModel(EMAIL, PASSWORD, FIRST_NAME);
    }

    @Test
    public void testCreateUserSuccess(){
        createUser(userModel)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void testCreateUserWithoutEmailFailure(){
        userModel.setEmail(null);

        createUser(userModel)
                .then()
                .log().all()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
        userModel = null;
    }

    @Test
    public void testCreateUserWithoutPasswordFailure(){
        userModel.setPassword(null);

        createUser(userModel)
                .then()
                .log().all()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
        userModel = null;
    }

    @Test
    public void testCreateUserWithoutNameFailure(){
        userModel.setName(null);

        createUser(userModel)
                .then()
                .log().all()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
        userModel = null;
    }

    @Test
    public void testCreateTwoIdenticalUserFailure(){
        createUser(userModel);
        createUser(userModel)
                .then()
                .log().all()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }


    @After
    public void cleanUp(){
        if (userModel != null){
            //deleteUser(userModel);
        }
    }
}
