package userTests;

import data.BaseTest;
import models.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static data.DataTest.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static steps.UserSteps.*;

public class LogInUserTest extends BaseTest {
    private UserModel userModel;

    @Before
    public void setUp(){
        userModel = new UserModel(EMAIL, PASSWORD, FIRST_NAME);
        createUser(userModel);
    }

    @Test
    public void logInUserSuccess(){
        logInUser(userModel)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void logInUserWithoutEmailFailure(){
       UserModel failUserModel = new UserModel(null, userModel.getPassword(), userModel.getName());
       logInUser(failUserModel)
               .then()
               .log().all()
               .statusCode(HTTP_UNAUTHORIZED)
               .body("success", equalTo(false))
               .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void logInUserWithoutPasswordFailure(){
        UserModel failUserModel = new UserModel(userModel.getEmail(), null, userModel.getName());
        logInUser(failUserModel)
                .then()
                .log().all()
                .statusCode(HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void logInUserIncorrectEmailFailure(){
        UserModel failUserModel = new UserModel("test"+userModel.getEmail(), userModel.getPassword(), userModel.getName());
        logInUser(failUserModel)
                .then()
                .log().all()
                .statusCode(HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void logInUserIncorrectPasswordFailure(){
        UserModel failUserModel = new UserModel(userModel.getEmail(), userModel.getPassword()+"test", userModel.getName());
        logInUser(failUserModel)
                .then()
                .log().all()
                .statusCode(HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void cleanUp(){
        if (userModel != null) {
            deleteUser(userModel)
                    .then().statusCode(HTTP_ACCEPTED);
        }
    }
}
