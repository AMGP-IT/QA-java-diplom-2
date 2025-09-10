package models;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientModel {
    @SerializedName("_id")
    private String id;
    private String name;
    private String type;
    private String proteins;
    private String fat;
    private String carbohydrates;
    private String calories;
    private String price;
    private String image;

    @SerializedName("image_mobile")
    private String imageMobile;

    @SerializedName("image_large")
    private String imageLarge;

    @SerializedName("__v")
    private String version;
}