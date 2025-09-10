package models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserModel {
    private String email;
    private String password;
    private String name;
    private String accessToken;
    private String refreshToken;

    public UserModel(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
