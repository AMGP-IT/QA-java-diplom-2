package user;

import data.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static data.DataTest.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static steps.UserSteps.*;

@DisplayName("Тесты на авторизацию пользователя")
public class LogInUserTest extends BaseTest {
    private UserModel userModel;

    @Before
    public void setUp(){
        userModel = new UserModel(EMAIL, PASSWORD, FIRST_NAME);
        createUser(userModel);
    }

    @Test
    @DisplayName("Позитивный тест на авторизацию пользователя")
    @Description("Тест проверяет успешную авторизацию существующего пользователя с корректными учетными данными. " +
            "Ожидаемые результаты: " +
            "* Код ответа HTTP 200 " +
            "* Флаг success равен true " +
            "* Пользователь успешно авторизуется в системе")
    public void logInUserSuccess(){
        logInUser(userModel)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Негативный тест на авторизацию пользователя без Email")
    @Description("Тест проверяет обработку попытки авторизации без указания email. " +
            "Ожидаемые результаты: " +
            "* Код ответа HTTP 401 " +
            "* Флаг success равен false " +
            "* Сообщение об ошибке указывает на неверные учетные данные")
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
    @DisplayName("Негативный тест на авторизацию пользователя без Password")
    @Description("Тест проверяет обработку попытки авторизации без указания пароля. " +
            "Ожидаемые результаты: " +
            "* Код ответа HTTP 401 " +
            "* Флаг success равен false " +
            "* Сообщение об ошибке указывает на неверные учетные данные")
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
    @DisplayName("Негативный тест на авторизацию пользователя с неверным Email")
    @Description("Тест проверяет обработку попытки авторизации с некорректным email. " +
            "Ожидаемые результаты: " +
            "* Код ответа HTTP 401 " +
            "* Флаг success равен false " +
            "* Сообщение об ошибке указывает на неверные учетные данные")
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
    @DisplayName("Негативный тест на авторизацию пользователя с неверным Password")
    @Description("Тест проверяет обработку попытки авторизации с некорректным паролем. " +
            "Ожидаемые результаты: " +
            "* Код ответа HTTP 401 " +
            "* Флаг success равен false " +
            "* Сообщение об ошибке указывает на неверные учетные данные")
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
