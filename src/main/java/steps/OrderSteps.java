package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.FoodResponseModel;
import models.IngredientModel;
import models.OrderModel;
import models.UserModel;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    private static final String PATH_CREATE_ORDER = "/api/orders";
    private static final String PATH_GET_DATA_INGREDIENTS = "/api/ingredients";

    @Step("Отправить запрос на создание заказа")
    public static Response createOrder(OrderModel order){
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(PATH_CREATE_ORDER)
                .then()
                .extract().response();
    }

    @Step("Отправить запрос на создание заказа зарегистрированным пользователем")
    public static Response createOrderRegisteredUser(OrderModel order, UserModel user){
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", user.getAccessToken())
                .body(order)
                .when()
                .post(PATH_CREATE_ORDER)
                .then()
                .extract().response();
    }

    @Step("Отправить запрос на получение списка ингредиентов")
    public static List<IngredientModel> getDataIngredients(){
        FoodResponseModel foodResponse = given()
                .contentType(ContentType.JSON)
                .when()
                .get(PATH_GET_DATA_INGREDIENTS)
                .then()
                .statusCode(200)
                .extract()
                .body().as(FoodResponseModel.class);

        return foodResponse.getData();
    }
}