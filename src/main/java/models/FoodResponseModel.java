package models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class FoodResponseModel {
    private boolean success;
    private List<IngredientModel> data;
}
