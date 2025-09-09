package order;

import data.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.IngredientModel;
import models.OrderModel;
import models.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static data.DataTest.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static steps.OrderSteps.*;
import static steps.UserSteps.*;

@DisplayName("Тесты на создание заказа")
public class CreateOrderTest extends BaseTest {
    private OrderModel orderModel;
    private UserModel userModel;

    @Before
    public void setUp(){ //запрашиваю список всех ингредиентов и сохраняю первые два id ингредиентов в список для создания заказа
        List<IngredientModel> ingredients = getDataIngredients();
        ArrayList<String> idIngredients = new ArrayList<>();

        for (int i = 0; i < 2 && i < ingredients.size(); i++) {
            idIngredients.add(ingredients.get(i).getId());
        }
        orderModel = new OrderModel(idIngredients);

        userModel = new UserModel(EMAIL, PASSWORD, FIRST_NAME);
        createUser(userModel);
    }

    @Test
    @DisplayName("Позитивный тест на создание заказа незарегистрированным пользователем")
    @Description("Тест проверяет возможность создания заказа незарегистрированным пользователем. " +
            "Ожидаемые результаты: " +
            "* Код ответа HTTP 200 " +
            "* Флаг success равен true " +
            "* Заказ успешно создается в системе")
    public void testCreateOrderUnregisteredUserSuccess(){
        createOrderUnregisteredUser(orderModel)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Позитивный тест на создание заказа зарегистрированным пользователем")
    @Description("Тест проверяет возможность создания заказа зарегистрированным пользователем. " +
            "Ожидаемые результаты: " +
            "* Код ответа HTTP 200 " +
            "* Флаг success равен true " +
            "* Заказ успешно создается в системе " +
            "* Данные пользователя корректно привязываются к заказу")
    public void testCreateOrderRegisteredUserSuccess(){
        createOrderRegisteredUser(orderModel, userModel)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Негативный тест на создание заказа с несуществующими id ингредиентов")
    @Description("Тест проверяет обработку попытки создания заказа с несуществующими id ингредиентов. " +
            "Ожидаемые результаты: " +
            "* Код ответа HTTP 500 " +
            "* Система корректно обрабатывает ошибку при поиске несуществующих ингредиентов")
    public void testCreateOrderIncorrectIdIngredientFailure(){
        orderModel = new OrderModel(List.of(new String[]{"eqw123", "dsf231"}));
        createOrderRegisteredUser(orderModel, userModel)
                .then()
                .log().all()
                .statusCode(HTTP_INTERNAL_ERROR);
    }

    @Test
    @DisplayName("Негативный тест на создание заказа без id ингредиентов")
    @Description("Тест проверяет валидацию при попытке создания заказа без указания id ингредиентов. " +
            "Ожидаемые результаты: " +
            "* Код ответа HTTP 400 " +
            "* Флаг success равен false " +
            "* Сообщение об ошибке указывает на необходимость предоставления id ингредиентов")
    public void testCreateOrderWithoutIngredientFailure(){
        orderModel = new OrderModel(null);
        createOrderRegisteredUser(orderModel, userModel)
                .then()
                .log().all()
                .statusCode(HTTP_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @After
    public void cleanUp(){
        if (userModel != null){
            deleteUser(userModel);
        }
    }
}
