package steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.UserModel;

import static io.restassured.RestAssured.given;

public class UserSteps {
    private static final String PATH_CREATE_USER = "/api/auth/register";
    private static final String PATH_DELETE_USER = "/api/auth/user";
    private static final String PATH_LOGIN_USER = "/api/auth/login";

    public static Response createUser(UserModel user){
        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(PATH_CREATE_USER)
                .then()
                .extract().response();

        user.setAccessToken(
                response
                .then()
                .extract().body().path("accessToken"));
        user.setRefreshToken(
                response
                .then()
                .extract().body().path("refreshToken"));

        return response;
    }

    public static Response deleteUser(UserModel user){
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .log().all()
                .when()
                .delete(PATH_DELETE_USER)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response logInUser(UserModel user){
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(PATH_LOGIN_USER)
                .then()
                .extract().response();
    }
}
