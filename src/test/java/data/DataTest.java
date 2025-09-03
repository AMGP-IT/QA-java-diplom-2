package data;

import com.github.javafaker.Faker;

public class DataTest {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site/";

    static Faker faker = new Faker();
    public static final String EMAIL = faker.name().lastName() + faker.regexify("[1-9]{4}") + "@mail.ru";
    public static final String PASSWORD = faker.regexify("[0-9]{8}");
    public static final String FIRST_NAME = faker.name().firstName();
}
