package orderTests;

import data.BaseTest;
import models.IngredientModel;
import models.OrderModel;
import models.UserModel;
import org.junit.Before;
import org.junit.Test;
import userTests.LogInUserTest;

import java.util.ArrayList;
import java.util.List;

import static data.DataTest.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static steps.OrderSteps.*;
import static steps.UserSteps.*;

public class CreateOrder extends BaseTest {
    private OrderModel orderModel;
    private UserModel userModel;

    @Before
    public void setUp(){
        List<IngredientModel> ingredients = getDataIngredients();
        ArrayList<String> idIngredients = new ArrayList<>();

        for (int i = 0; i < 2 && i < ingredients.size(); i++) {
            idIngredients.add(ingredients.get(i).get_id());
        }

        orderModel = new OrderModel(idIngredients);
    }

    @Test
    public void createOrderUnregisteredUserSuccess(){
        createOrder(orderModel)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void createOrderRegisteredUserSuccess(){
        userModel = new UserModel(EMAIL, PASSWORD, FIRST_NAME);
        createUser(userModel);
        logInUser(userModel);

        createOrder(orderModel)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
        UserModel asd = new UserModel(null, null, null);
        asd.setAccessToken(userModel.getAccessToken());
        deleteUser(asd);
    }

    @Test
    public void createOrderIncorrectIdIngredientFailure(){
        orderModel = new OrderModel(List.of(new String[]{"eqw123", "dsf231"}));
        createOrder(orderModel)
                .then()
                .log().all()
                .statusCode(HTTP_INTERNAL_ERROR);
    }

    @Test
    public void createOrderWithoutIngredientFailure(){
        orderModel = new OrderModel(null);
        createOrder(orderModel)
                .then()
                .log().all()
                .statusCode(HTTP_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));;
    }
}
