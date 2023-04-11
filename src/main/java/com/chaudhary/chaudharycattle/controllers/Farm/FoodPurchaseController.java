package com.chaudhary.chaudharycattle.controllers.Farm;

import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.model.farm.FoodPurchaseTableView;
import com.chaudhary.chaudharycattle.service.farm.FoodPurchasedService;
import com.chaudhary.chaudharycattle.service.farm.FoodUsageService;
import com.chaudhary.chaudharycattle.utils.CommanUtils;
import com.chaudhary.chaudharycattle.utils.FxmlPaths;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private TextField food,unit,rate,rated,qty,qtyd,amount,foodAdd,unitAdd,buyerAdd,contactAdd,buyer;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button addFood, addBuyer, saveNewFood, saveNewBuyer, save;
    @FXML
    private Label pagination;
    @FXML
    private TableView<FoodPurchaseTableView> table = new TableView<>();
    @FXML
    private TableColumn<FoodPurchaseTableView, String> foodCol,supplierCol,rateCol,qtyCol,amountCol,dateCol;
    @Autowired
    private FoodUsageService foodUsageService;
    @Autowired
    private FoodPurchasedService foodPurchasedService;
    private static List<String> foodList;
    private static List<String> buyerList;
    private static int pageNo = 0;
    private static final int maxSize = 10;
    private static int from = 1;
    private static int to = maxSize;
    private static long totalRecord = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        foodList = foodUsageService.getFoodList().stream().map(Food::getName).collect(Collectors.toList());
        TextFields.bindAutoCompletion(food,foodList);
        buyerList = foodUsageService.getBuyerList().stream().map(Supplier::getName).collect(Collectors.toList());
        TextFields.bindAutoCompletion(buyer,buyerList);

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
                amount.clear();
                buyer.clear();
                food.clear();
                food.requestFocus();
            }else{
                this.unit.setText(unit);
                qty.clear();
                qtyd.clear();
                rate.clear();
                rated.clear();
                amount.clear();
                buyer.clear();
                buyer.requestFocus();
            }
        });
        renderDataTable();
    }
    public void submit_key(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER))
            submit();
    }
    public void submit (){
        double _rate = Double.parseDouble(parse(rate.getText())+"."+parse(rated.getText()));
        double _qty = Double.parseDouble(parse(qty.getText())+"."+parse(qtyd.getText()));
        double _amount =  parseAmount(amount.getText());
        if(validate(_rate, _qty, _amount)){
            StringBuilder str = new StringBuilder();
            str.append("Date : ").append(datePicker.getValue());
            str.append("\nFood : ").append(food.getText());
            str.append("\nSupplier : ").append(buyer.getText());
            str.append("\nRate : ").append(_rate);
            str.append("\nQty : ").append(_qty);
            str.append("\nAmount : ").append(_amount);
            if( (CommanUtils.confirmationAlert("Purchase Slip ", str.toString())).equalsIgnoreCase("OK") ) {
                if (foodPurchasedService.submit(food.getText(), buyer.getText(), _rate, _qty, _amount, datePicker.getValue())) {
                    CommanUtils.informationAlert("Information", "Food Stock & Supplier Leader Updated");
                    renderDataTable();
                    clearFields();
                }else{
                    CommanUtils.warningAlert("Warning", "Check The Filed Something Wrong");
                }
            }
        } else
            CommanUtils.warningAlert("Warning", "Check The Filed Something Wrong");

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
        if(totalRecord > (long) (pageNo + 1) *maxSize) {
            pageNo++;
            from = to + 1;
            to = to + maxSize;
            renderDataTable();
        }
    }
    public void prevPage (){
        if(pageNo > 0) {
            pageNo--;
            from = from - maxSize;
            to = to - maxSize;
            renderDataTable();
        }
    }
    public void addNewFood (){
        if(validateAddNewFood() && foodPurchasedService.addNewFood(foodAdd.getText(),unitAdd.getText())){
            CommanUtils.informationAlert("Information", "New Food Inserted");
            foodList = foodUsageService.getFoodList().stream().map(Food::getName).collect(Collectors.toList());
            TextFields.bindAutoCompletion(food,foodList);
            setAddNewProductVisible(false);
            save.setVisible(true);
            food.requestFocus();
        }else {
            CommanUtils.warningAlert("Warning", "Food Already Exist");
            foodAdd.clear();
            unitAdd.clear();
            foodAdd.requestFocus();
        }
    }
    public void addNewBuyer (){
        if(validateAddNewBuyer() && foodPurchasedService.addNewBuyer(buyerAdd.getText(),contactAdd.getText())){
            CommanUtils.informationAlert("Information", "New Supplier Inserted");
            buyerList = foodUsageService.getBuyerList().stream().map(Supplier::getName).collect(Collectors.toList());
            TextFields.bindAutoCompletion(buyer,buyerList);
            setAddNewBuyerVisible(false);
            save.setVisible(true);
            food.requestFocus();
        }else {
            CommanUtils.warningAlert("Warning", "Buyer Already Exist");
            buyerAdd.clear();
            contactAdd.clear();
            buyerAdd.requestFocus();
        }
    }
    public void addNewFoodPrompt (){
        if(addFood.getText().equalsIgnoreCase("Add New Food")) {
            setAddNewProductVisible(true);
            setAddNewBuyerVisible(false);
            save.setVisible(false);
            foodAdd.requestFocus();
            addFood.setText("Close Add Food");
        }else{
            setAddNewProductVisible(false);
            save.setVisible(true);
            food.requestFocus();
        }
    }
    public void addNewBuyerPrompt (){
        if(addBuyer.getText().equalsIgnoreCase("Add New Supplier")) {
            setAddNewBuyerVisible(true);
            setAddNewProductVisible(false);
            save.setVisible(false);
            buyerAdd.requestFocus();
            addBuyer.setText("Close New Supplier");
        }else{
            setAddNewBuyerVisible(false);
            save.setVisible(true);
            food.requestFocus();
        }
    }
    private void renderDataTable (){
        foodCol.setCellValueFactory(new PropertyValueFactory<>("food"));
        supplierCol.setCellValueFactory(new PropertyValueFactory<>("buyer"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        //Creating a table view
        final ObservableList<FoodPurchaseTableView> data = FXCollections.observableArrayList(
                foodPurchasedService.getTableData(pageNo, maxSize)
        );
        totalRecord = foodPurchasedService.getTableDataCount();
        table.setItems(data);
        pagination.setText(""+(from)+" - "+(Math.min(to,totalRecord))+" / "+totalRecord);
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
        addFood.setText("Add New Food");
    }
    private void setAddNewBuyerVisible (boolean val){
        buyerAdd.setVisible(val);
        contactAdd.setVisible(val);
        saveNewBuyer.setVisible(val);
        addBuyer.setText("Add New Supplier");
    }
    private boolean validateAddNewFood (){
        return (!foodAdd.getText().equalsIgnoreCase("") && foodAdd.getText() != null && !unitAdd.getText().equalsIgnoreCase("") && unitAdd.getText() != null);
    }
    private boolean validateAddNewBuyer (){
        return (!buyerAdd.getText().equalsIgnoreCase("") && buyerAdd.getText() != null && !contactAdd.getText().equalsIgnoreCase("") && contactAdd.getText() != null);
    }
    private int parse (String str){
        try {
            return (str.equals("")) ? 0 : Integer.parseInt(str);
        }catch (Exception exception){
            CommanUtils.warningAlert("Warning","Something Wrong Please Check The Fields");
        }
        return 0;
    }
    private double parseAmount (String str){
        try {
            return (str.equals("")) ? 0.0 : Double.parseDouble(str);
        }catch (Exception exception){
            CommanUtils.warningAlert("Warning","Something Wrong Please Check The Fields");
        }
        return 0.0;
    }
    private boolean validate(Double rate, Double qty, Double amount) {
        return rate > 0.0 && qty > 0.0 && amount > 0.0;
    }
    private void clearFields(){
        food.clear();
        food.requestFocus();
        unit.clear();
        buyer.clear();
        rate.clear();
        rated.clear();
        qty.clear();
        qtyd.clear();
        amount.clear();
        datePicker.setValue(LocalDate.now());
    }
}
