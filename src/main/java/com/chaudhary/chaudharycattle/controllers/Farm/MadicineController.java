package com.chaudhary.chaudharycattle.controllers.Farm;

import com.chaudhary.chaudharycattle.entities.farm.Supplier;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
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
    @Autowired
    private FoodPurchasedService foodPurchasedService;
    @Autowired
    private FoodUsageService foodUsageService;
    @Autowired
    private MedicalService medicalService;
    private static List<String> supplierList;
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
