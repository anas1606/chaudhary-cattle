package com.chaudhary.chaudharycattle.controllers.Farm;

import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import com.chaudhary.chaudharycattle.model.DashboardTableView;
import com.chaudhary.chaudharycattle.model.farm.MedicalTableView;
import com.chaudhary.chaudharycattle.service.farm.FoodPurchasedService;
import com.chaudhary.chaudharycattle.service.farm.FoodUsageService;
import com.chaudhary.chaudharycattle.service.farm.MedicalService;
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
public class MadicineController implements Initializable {

    @FXML
    private Button save, saveNewSupplier, addSupplier;
    @FXML
    private TextField supplierAdd, contactAdd, description, supplier, amount;
    @FXML
    private ComboBox<String> payment;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableColumn<MedicalTableView, String> discCol, amountCol , supplierCol, dateCol, paymentModeCol;
    @FXML
    private TableView<MedicalTableView> table = new TableView<>();
    @FXML
    private TableView<DashboardTableView> recTable = new TableView<>();
    @FXML
    private TableColumn<DashboardTableView, String>  amountColRec, supplierColRec;
    private Label pagination;
    @Autowired
    private FoodPurchasedService foodPurchasedService;
    @Autowired
    private FoodUsageService foodUsageService;
    @Autowired
    private MedicalService medicalService;
    private static List<String> supplierList;
    private static int pageNo = 0;
    private static final int maxSize = 10;
    private static int from = 1;
    private static int to = maxSize;
    private static long totalRecord = 0;
    public void loadMilk(){
        CommanUtils.loadPage(FxmlPaths.FARM_MILK);
    }
    public void loadFoodUsage(){
        CommanUtils.loadPage(FxmlPaths.FARM_FOOD_USAGE);
    }
    public void loadFoodPurchase(){
        CommanUtils.loadPage(FxmlPaths.FARM_FOOD_PURCHASE);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread dataTable = new Thread(this::renderDataTable);
        dataTable.start();
        Thread recDataTable = new Thread(this::renderRecDataTable);
        recDataTable.start();
        supplierList = foodUsageService.getBuyerList().stream().map(Supplier::getName).collect(Collectors.toList());
        TextFields.bindAutoCompletion(supplier,supplierList);
        ObservableList<String> options = FXCollections.observableArrayList("CASH","PAYLATER");
        payment.setItems(options);
        datePicker.setValue(LocalDate.now());
        Platform.runLater(()-> description.requestFocus());
        amount.focusedProperty().addListener((ov, oldv, newV) -> {
            if(!medicalService.validSupplier(supplier.getText())){
                amount.clear();
                supplier.clear();
                supplier.requestFocus();
            }
        });
    }
    public void addNewSupplierPrompt (){
        if(addSupplier.getText().equalsIgnoreCase("Add New Supplier")) {
            setAddNewSupplierVisible(true);
            save.setVisible(false);
            supplierAdd.requestFocus();
            addSupplier.setText("Close New Supplier");
        }else{
            setAddNewSupplierVisible(false);
            save.setVisible(true);
            description.requestFocus();
        }
    }
    public void submit_key(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER))
            submit();
    }
    public void submit () {
        double _amount =  parseAmount(amount.getText());
        if(validate(_amount,description.getText(),payment.getValue())){
            if(medicalService.submit(description.getText(),supplier.getText(),_amount,datePicker.getValue(),payment.getValue())){
                CommanUtils.informationAlert("Information", "Medical Recorded Inserted");
            }else {
                CommanUtils.warningAlert("Warning", "Check The Filed Something Wrong");
            }
        }else {
            CommanUtils.warningAlert("Warning", "Please Fill All The Fields");
            amount.requestFocus();
        }
    }
    public void addNewSupplier (){
        if(validateAddNewSupplier() && foodPurchasedService.addNewBuyer(supplierAdd.getText(),contactAdd.getText())){
            CommanUtils.informationAlert("Information", "New Supplier Inserted");
            supplierList = foodUsageService.getBuyerList().stream().map(Supplier::getName).collect(Collectors.toList());
            TextFields.bindAutoCompletion(supplier,supplierList);
            setAddNewSupplierVisible(false);
            save.setVisible(true);
            description.requestFocus();
        }else {
            CommanUtils.warningAlert("Warning", "Buyer Already Exist");
            supplierAdd.clear();
            contactAdd.clear();
            supplierAdd.requestFocus();
        }
    }
    private void renderDataTable (){
        discCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        supplierCol.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        paymentModeCol.setCellValueFactory(new PropertyValueFactory<>("paymentMode"));
        //Creating a table view
        final ObservableList<MedicalTableView> data = FXCollections.observableArrayList(
                medicalService.getTableData(pageNo, maxSize)
        );
        totalRecord = medicalService.getTableDataCount();
        table.setItems(data);
        pagination.setText(""+(from)+" - "+(Math.min(to,totalRecord))+" / "+totalRecord);
    }
    private void renderRecDataTable (){
        supplierColRec.setCellValueFactory(new PropertyValueFactory<>("name"));
        amountColRec.setCellValueFactory(new PropertyValueFactory<>("amount"));
        //Creating a table view
        final ObservableList<DashboardTableView> data = FXCollections.observableArrayList(
                medicalService.getRecTableData()
        );
        recTable.setItems(data);
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
    private void setAddNewSupplierVisible (boolean val){
        supplierAdd.setVisible(val);
        contactAdd.setVisible(val);
        saveNewSupplier.setVisible(val);
        addSupplier.setText("Add New Supplier");
    }
    private boolean validateAddNewSupplier (){
        return (!supplierAdd.getText().equalsIgnoreCase("") && supplierAdd.getText() != null && !contactAdd.getText().equalsIgnoreCase("") && contactAdd.getText() != null);
    }
    private boolean validate (double amount, String description, String payment){
        return amount > 0.0 && !description.equalsIgnoreCase("") && payment != null;
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
}
