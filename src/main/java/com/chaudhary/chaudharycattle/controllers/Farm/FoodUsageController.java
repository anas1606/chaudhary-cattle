package com.chaudhary.chaudharycattle.controllers.Farm;

import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.model.DashboardTableView;
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
import javafx.scene.input.MouseEvent;
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
    private TextField food,qty,unit,qtyd;
    @FXML
    private ComboBox<String> cb;
    @FXML
    private TableView<FoodUsageTableView> table = new TableView<>();
    @FXML
    private TableView<DashboardTableView> usageTable = new TableView<>();
    @FXML
    private TableColumn<FoodUsageTableView, String> foodCol,dateCol,qtyCol,shiftCol;
    @FXML
    private TableColumn<DashboardTableView, String> foodUsageCol, qtyUsageCol;
    @FXML
    private Label pagination;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button deleteBtn,saveBtn;
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
                deleteBtn.setVisible(false);
                saveBtn.setVisible(true);
                this.unit.setText(unit);
                qty.clear();
                qtyd.clear();
                qty.requestFocus();
            }
        });

        Thread rederTotalData = new Thread(this::renderTotalData);
        rederTotalData.start();
        renderDataTable();
        table.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                if (table.getSelectionModel().getSelectedItem() != null) {
                    deleteBtn.setVisible(true);
                    saveBtn.setVisible(false);
                }
            }
        });
    }
    public void delete (){
        FoodUsageTableView foodUsageTableView = table.getSelectionModel().getSelectedItem();
        StringBuilder str = new StringBuilder();
        str.append("Name : ").append(foodUsageTableView.getFood());
        str.append("\nUnit : ").append(foodUsageTableView.getUnit());
        str.append("\nQty : ").append(foodUsageTableView.getQty());
        if( (CommanUtils.confirmationAlert("Deleting Slip ", str.toString())).equalsIgnoreCase("OK") ) {
            foodUsageService.deleteRecord(foodUsageTableView);
        }
        renderDataTable();
        deleteBtn.setVisible(false);
        saveBtn.setVisible(true);
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
            str.append("\nUnit : ").append(unit.getText());
            str.append("\nQty : ").append(qty);
            str.append("\nShift : ").append(cb.getValue());
            if((CommanUtils.confirmationAlert("Food Usage Slip ", str.toString())).equalsIgnoreCase("OK") ) {
                if(foodUsageService.submit(food.getText(), qty, cb.getValue(), datePicker.getValue())) {
                    CommanUtils.informationAlert("Information", "Stock Updated & Data Inserted");
                    renderTotalData();
                    renderDataTable();
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
        foodUsageCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        qtyUsageCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        //Creating a table view
        final ObservableList<DashboardTableView> data = FXCollections.observableArrayList(
                foodUsageService.foodUsageRecord()
        );
        usageTable.setItems(data);
    }
    private void renderDataTable (){
        foodCol.setCellValueFactory(new PropertyValueFactory<>("food"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        shiftCol.setCellValueFactory(new PropertyValueFactory<>("shift"));
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
