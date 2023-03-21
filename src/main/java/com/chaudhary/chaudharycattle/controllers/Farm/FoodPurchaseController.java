package com.chaudhary.chaudharycattle.controllers.Farm;

import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.service.farm.FoodPurchasedService;
import com.chaudhary.chaudharycattle.service.farm.FoodUsageService;
import com.chaudhary.chaudharycattle.utils.CommanUtils;
import com.chaudhary.chaudharycattle.utils.FxmlPaths;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Controller
public class FoodPurchaseController implements Initializable {
    @FXML
    private TextField food,unit,rate,rated,qty,qtyd,amount,foodAdd,unitAdd;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button addFood, saveNewFood, save;
    @Autowired
    private FoodUsageService foodUsageService;
    @Autowired
    private FoodPurchasedService foodPurchasedService;
    private static List<String> foodList;
    private static int pageNo = 0;
    private static final int maxSize = 10;
    private static int from = 1;
    private static int to = maxSize;
    private static int totalRecord = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        foodList = foodUsageService.getFoodList().stream().map(Food::getName).collect(Collectors.toList());
        TextFields.bindAutoCompletion(food,foodList);
        datePicker.setValue(LocalDate.now());
        Platform.runLater(()-> food.requestFocus());

        unit.focusedProperty().addListener((ov, oldv, newV) -> {
            String unit = foodUsageService.getFoodUnit(food.getText());
            if(unit.equalsIgnoreCase("")){
                this.unit.clear();
                qty.clear();
                qtyd.clear();
                rate.clear();
                rated.clear();
                food.clear();
                food.requestFocus();
            }else{
                this.unit.setText(unit);
                qty.clear();
                qtyd.clear();
                rate.clear();
                rated.clear();
                rate.requestFocus();
            }
        });
    }
    public void loadMilk(){
        CommanUtils.loadPage(FxmlPaths.FARM_MILK);
    }
    public void loadFoodUsage(){
        CommanUtils.loadPage(FxmlPaths.FARM_FOOD_USAGE);
    }
    public void loadMadicine(){
        CommanUtils.loadPage(FxmlPaths.FARM_MADICINE);
    }
    public void nextPage (){
        if(totalRecord > (pageNo+1)*maxSize) {
            pageNo++;
            from = to + 1;
            to = to + maxSize;
//            renderDataTable();
        }
    }
    public void prevPage (){
        if(pageNo > 0) {
            pageNo--;
            from = from - maxSize;
            to = to - maxSize;
//            renderDataTable();
        }
    }
    public void addNewFood (){
        if(validateAddNewFood() && foodPurchasedService.addNewFood(foodAdd.getText(),unitAdd.getText())){
            CommanUtils.informationAlert("Information", "New Food Inserted");
            foodList = foodUsageService.getFoodList().stream().map(Food::getName).collect(Collectors.toList());
            TextFields.bindAutoCompletion(food,foodList);
        }else {
            CommanUtils.warningAlert("Warning", "Food Already Exist");
        }
        foodAdd.clear();
        unitAdd.clear();
        foodAdd.requestFocus();
    }
    public void addNewFoodPrompt (){
        if(addFood.getText().equalsIgnoreCase("Add New Food")) {
            setAddNewProductVisible(true);
            save.setVisible(false);
            foodAdd.requestFocus();
            addFood.setText("Close Add Food");
        }else{
            setAddNewProductVisible(false);
            save.setVisible(true);
            food.requestFocus();
            addFood.setText("Add New Food");
        }

    }
    public void isNumberAmt(){
        CommanUtils.numberFormate(amount);
    }
    public void isNumberRate(){
        CommanUtils.numberFormate(rate);
    }
    public void isNumberRated(){
        CommanUtils.numberFormate(rated);
    }
    public void isNumberQty(){
        CommanUtils.numberFormate(qty);
    }
    public void isNumberQtyd(){
        CommanUtils.numberFormate(qtyd);
    }

    private void setAddNewProductVisible (boolean val){
        foodAdd.setVisible(val);
        unitAdd.setVisible(val);
        saveNewFood.setVisible(val);
    }
    private boolean validateAddNewFood (){
        return (!foodAdd.getText().equalsIgnoreCase("") && foodAdd.getText() != null && !unitAdd.getText().equalsIgnoreCase("") && unitAdd.getText() != null);
    }
}
