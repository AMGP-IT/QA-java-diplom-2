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
import static steps.UserSteps.createUser;
import static steps.UserSteps.deleteUser;

@DisplayName("Тесты на создание пользователя")
public class CreateUserTest extends BaseTest {
    private UserModel userModel;

    @Before
    public void setUp(){
        userModel = new UserModel(EMAIL, PASSWORD, FIRST_NAME);
    }

    @Test
    @DisplayName("Позитивный тест на создание пользователя")
    @Description("Тест проверяет успешное создание нового пользователя с корректными данными. " +
            "Ожидаемые результаты: " +
            "* Код ответа HTTP 200 " +
            "* Флаг success равен true " +
            "* Пользователь успешно создается в системе")
    public void testCreateUserSuccess(){
        createUser(userModel)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Негативный тест на создание пользователя без Email")
    @Description("Тест проверяет валидацию при попытке создания пользователя без email. " +
            "Ожидаемые результаты: " +
            "* Код ответа HTTP 403 " +
            "* Флаг success равен false " +
            "* Сообщение об ошибке содержит информацию о необходимости заполнения email")
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
    @DisplayName("Негативный тест на создание пользователя без Password")
    @Description("Тест проверяет валидацию при попытке создания пользователя без пароля. " +
            "Ожидаемые результаты: " +
            "* Код ответа HTTP 403 " +
            "* Флаг success равен false " +
            "* Сообщение об ошибке содержит информацию о необходимости заполнения пароля")
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
    @DisplayName("Негативный тест на создание пользователя без Name")
    @Description("Тест проверяет валидацию при попытке создания пользователя без имени. " +
            "Ожидаемые результаты: " +
            "* Код ответа HTTP 403 " +
            "* Флаг success равен false " +
            "* Сообщение об ошибке содержит информацию о необходимости заполнения имени")
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
    @DisplayName("Негативный тест на создание пользователя, который уже зарегистрирован")
    @Description("Тест проверяет обработку попытки создания уже существующего пользователя. " +
            "Сценарий: " +
            "* Первый запрос создает нового пользователя " +
            "* Второй запрос пытается создать того же пользователя повторно " +
            "Ожидаемые результаты: " +
            "* Код ответа HTTP 403 " +
            "* Флаг success равен false " +
            "* Сообщение об ошибке указывает на существование пользователя")
    public void testCreateTwoIdenticalUserFailure(){
        createUser(userModel);
        //при повторном создании индентичного юзера поля с токенами обнуляются, поэтому сохраняем токен в переменной, чтобы его удалить в @After
        String accessToken = userModel.getAccessToken();

        createUser(userModel)
                .then()
                .log().all()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
        userModel.setAccessToken(accessToken);
    }


    @After
    public void cleanUp(){
        if (userModel != null){
            deleteUser(userModel)
                    .then()
                    .statusCode(HTTP_ACCEPTED);
        }
    }
}
