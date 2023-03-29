package com.chaudhary.chaudharycattle.controllers.Farm;

import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.model.farm.FoodUsageRecordModel;
import com.chaudhary.chaudharycattle.model.farm.FoodUsageTableView;
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
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class FoodUsageController implements Initializable {

    @FXML
    private TextField food,qty,unit,qtyd,chhar,gool,tail,bear;
    @FXML
    private ComboBox<String> cb;
    @FXML
    private TableView<FoodUsageTableView> table = new TableView<>();
    @FXML
    private TableColumn<FoodUsageTableView, String> foodCol,dateCol,qtyCol;
    @FXML
    private Label pagination;
    @FXML
    private DatePicker datePicker;
    @Autowired
    private FoodUsageService foodUsageService;
    private static List<String> foodList;
    private static int pageNo = 0;
    private static final int maxSize = 10;
    private static int from = 1;
    private static int to = maxSize;
    private static int totalRecord = 0;

    public void loadMilk(){
        CommanUtils.loadPage(FxmlPaths.FARM_MILK);
    }
    public void loadMadicine(){
        CommanUtils.loadPage(FxmlPaths.FARM_MADICINE);
    }
    public void loadFoodPurchase(){
        CommanUtils.loadPage(FxmlPaths.FARM_FOOD_PURCHASE);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> options = FXCollections.observableArrayList("MORNING","EVENING");
        cb.setItems(options);
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
                food.clear();
                food.requestFocus();
            }else{
                this.unit.setText(unit);
                qty.clear();
                qtyd.clear();
                qty.requestFocus();
            }
        });

        Thread rederTotalData = new Thread(this::renderTotalData);
        rederTotalData.start();
    }
    public void submit_key(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER))
            submit();
    }
    public void submit (){
        double qty = Double.parseDouble(parse(this.qty.getText()) + "." + parse(this.qtyd.getText()));
        if(validate(qty)) {
            StringBuilder str = new StringBuilder();
            str.append("Name : ").append(food.getText());
            str.append("\nUnit : ").append("KG");
            str.append("\nQty : ").append(qty);
            if((CommanUtils.confirmationAlert("Food Usage Slip ", str.toString())).equalsIgnoreCase("OK") ) {
                if(foodUsageService.submit(food.getText(), qty, cb.getValue())) {
                    CommanUtils.informationAlert("Information", "Stock Updated & Data Inserted");
                    renderTotalData();
                    food.clear();
                    food.requestFocus();
                    unit.clear();
                    this.qty.clear();
                    qtyd.clear();
                }else {
                    CommanUtils.warningAlert("Warning", "Insufficient Stock");
                }
            }
        }else {
            CommanUtils.warningAlert("Warning", "Please Fill All The Fields");
        }
    }
    public void isNumber(){
        CommanUtils.numberFormate(qty);
    }
    public void isNumberd(){
        CommanUtils.numberFormate(qtyd);
    }
    public void nextPage (){
        if(totalRecord > (pageNo+1)*maxSize) {
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
    private  void renderTotalData (){
        FoodUsageRecordModel model = foodUsageService.foodUsageRecord();
        chhar.setText(model.getChhar().toString());
        gool.setText(model.getGool().toString());
        tail.setText(model.getTail().toString());
        bear.setText(model.getBear().toString());
        renderDataTable();
    }
    private void renderDataTable (){
        foodCol.setCellValueFactory(new PropertyValueFactory<>("food"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        //Creating a table view
        final ObservableList<FoodUsageTableView> data = FXCollections.observableArrayList(
                foodUsageService.getDataTable(pageNo, maxSize)
        );
        totalRecord = foodUsageService.getTableDataCount();
        table.setItems(data);
        pagination.setText(""+(from)+" - "+(Math.min(to,totalRecord))+" / "+totalRecord);
    }
    private boolean validate(Double qty) {
        return foodList.contains(food.getText()) && qty > 0.0 && cb.getValue() != null;
    }
    private int parse (String str){
        return (str.equals(""))?0:Integer.parseInt(str);
    }
}
