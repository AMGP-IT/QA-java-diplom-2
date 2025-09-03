package models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class IngredientModel {
    private String _id;
    private String name;
    private String type;
    private String proteins;
    private String fat;
    private String carbohydrates;
    private String calories;
    private String price;
    private String image;
    private String image_mobile;
    private String image_large;
    private String __v;
}
