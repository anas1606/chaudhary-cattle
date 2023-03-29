package com.chaudhary.chaudharycattle.controllers.Farm;

import com.chaudhary.chaudharycattle.entities.farm.Supplier;
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
public class MadicineController implements Initializable {

    @FXML
    private Button save, saveNewSupplier, addSupplier;
    @FXML
    private TextField supplierAdd, contactAdd, discription, supplier;
    @FXML
    private DatePicker datePicker;
    @Autowired
    private FoodPurchasedService foodPurchasedService;
    @Autowired
    private FoodUsageService foodUsageService;
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
        datePicker.setValue(LocalDate.now());
        Platform.runLater(()-> discription.requestFocus());
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
            discription.requestFocus();
        }
    }
    public void addNewSupplier (){
        if(validateAddNewSupplier() && foodPurchasedService.addNewBuyer(supplierAdd.getText(),contactAdd.getText())){
            CommanUtils.informationAlert("Information", "New Buyer Inserted");
            supplierList = foodUsageService.getBuyerList().stream().map(Supplier::getName).collect(Collectors.toList());
            TextFields.bindAutoCompletion(supplier,supplierList);
            setAddNewSupplierVisible(false);
            save.setVisible(true);
            discription.requestFocus();
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
}
