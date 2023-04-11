package com.chaudhary.chaudharycattle.controllers.Supplier;

import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import com.chaudhary.chaudharycattle.model.supplier.RepaymentStatementTableView;
import com.chaudhary.chaudharycattle.service.farm.FoodUsageService;
import com.chaudhary.chaudharycattle.service.supplier.SupplierRepaymentService;
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
public class SupplierRepaymentController implements Initializable {
    @FXML
    private TableView<RepaymentStatementTableView> table = new TableView<>();
    @FXML
    private TableColumn<RepaymentStatementTableView, String> suppplierCol,amountCol,dateCol;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField supplier, amount, amountToPay, rePayAmount;
    @FXML
    private Label pagination;
    @Autowired
    private SupplierRepaymentService supplierRepaymentService;
    @Autowired
    private FoodUsageService foodUsageService;
    private static int pageNo = 0;
    private static final int maxSize = 10;
    private static int from = 1;
    private static int to = maxSize;
    private static long totalRecord = 0;
    public void loadPurchaseStatement() {
        CommanUtils.loadPage(FxmlPaths.SUPLLIER_PURCHASE_RECORD);
    }
    public void loadRepaymentStatement () {
        CommanUtils.loadPage(FxmlPaths.SUPLLIER_REPAYMENT_STATEMENT);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(()-> supplier.requestFocus());
        datePicker.setValue(LocalDate.now());
        getPresentMonthSupplierRepayment();
        List<String> supplierList = foodUsageService.getBuyerList().stream().map(Supplier::getName).collect(Collectors.toList());
        TextFields.bindAutoCompletion(supplier, supplierList);

        amountToPay.focusedProperty().addListener((ov, oldv, newV) -> {
            double amount = supplierRepaymentService.getSupplieroldBalance(supplier.getText());
            if(amount != -1){
                amountToPay.setText(String.valueOf(amount));
                rePayAmount.requestFocus();
            }else {
                amountToPay.clear();
                supplier.clear();
                supplier.requestFocus();
            }

        });
    }
    public void submit_key(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER))
            submit();
    }
    public void submit () {
        double _amount =  parseAmount(rePayAmount.getText());
        double _oldAmount =  parseAmount(amountToPay.getText());
        if(validate(_amount)){
            StringBuilder str = new StringBuilder();
            str.append("Date : ").append(datePicker.getValue());
            str.append("\nSupplier : ").append(supplier.getText());
            str.append("\nOld Balance : ").append(_oldAmount);
            str.append("\nRepay Amount : ").append(_amount);
            str.append("\n__________________\nRemaining Amount : ").append( _oldAmount - _amount );
            if( (CommanUtils.confirmationAlert("RePayment Slip ", str.toString())).equalsIgnoreCase("OK") ){
                supplierRepaymentService.submit(supplier.getText(),datePicker.getValue(),_amount);
                CommanUtils.informationAlert("Information","RePayment Is Added To Supplier Ledger");
                getPresentMonthSupplierRepayment();
                clearFields();
            }
        }else {
            CommanUtils.warningAlert("Warning", "Check The Filed Something Wrong");
        }
    }
    public void getPresentMonthSupplierRepayment () {
        suppplierCol.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        //Creating a table view
        final ObservableList<RepaymentStatementTableView> data = FXCollections.observableArrayList(
                supplierRepaymentService.getSupplierRepayments(pageNo, maxSize)
        );
        totalRecord = supplierRepaymentService.getTableDataCount();
        amount.setText(String.valueOf(supplierRepaymentService.getRepaymentTotal()));
        table.setItems(data);
        pagination.setText(""+(from)+" - "+(Math.min(to,totalRecord))+" / "+totalRecord);
    }
    public void nextPage (){
        if(totalRecord > (long) (pageNo + 1) *maxSize) {
            pageNo++;
            from = to + 1;
            to = to + maxSize;
        }
    }
    public void prevPage (){
        if(pageNo > 0) {
            pageNo--;
            from = from - maxSize;
            to = to - maxSize;
        }
    }
    private double parseAmount (String str){
        try {
            return (str.equals("")) ? 0.0 : Double.parseDouble(str);
        }catch (Exception exception){
            CommanUtils.warningAlert("Warning","Something Wrong Please Check The Fields");
        }
        return 0.0;
    }
    private boolean validate (double amount){
        return amount > 0.0 && datePicker.getValue() != null;
    }
    private void clearFields (){
        supplier.clear();
        amountToPay.clear();
        rePayAmount.clear();
        datePicker.setValue(LocalDate.now());
    }
}
