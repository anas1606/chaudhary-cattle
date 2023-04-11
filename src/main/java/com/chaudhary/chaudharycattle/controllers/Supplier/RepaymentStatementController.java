package com.chaudhary.chaudharycattle.controllers.Supplier;

import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import com.chaudhary.chaudharycattle.model.supplier.RepaymentStatementTableView;
import com.chaudhary.chaudharycattle.service.farm.FoodUsageService;
import com.chaudhary.chaudharycattle.service.supplier.RepaymentStatementService;
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
public class RepaymentStatementController implements Initializable {

    @FXML
    private TableView<RepaymentStatementTableView> table = new TableView<>();
    @FXML
    private TableColumn<RepaymentStatementTableView, String> suppplierCol,amountCol,dateCol;
    @FXML
    private Label pagination;
    @FXML
    private DatePicker fromDate, toDate;
    @FXML
    private TextField supplier, amount;
    @Autowired
    private RepaymentStatementService repaymentStatementService;
    @Autowired
    private FoodUsageService foodUsageService;
    private static int pageNo = 0;
    private static final int maxSize = 10;
    private static int from = 1;
    private static int to = maxSize;
    private static long totalRecord = 0;
    public void loadRepayment() {
        CommanUtils.loadPage(FxmlPaths.SUPLLIER_REPAYMENT);
    }
    public void loadPurchaseStatement () {
        CommanUtils.loadPage(FxmlPaths.SUPLLIER_PURCHASE_RECORD);
    }
    public void filter_key(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER))
            filter();
    }
    public void filter () {
        getRepaymentStatement();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(()-> supplier.requestFocus());
        fromDate.setValue(LocalDate.now().withDayOfMonth(1));
        toDate.setValue(fromDate.getValue().plusMonths(1).minusDays(1));
        List<String> supplierList = foodUsageService.getBuyerList().stream().map(Supplier::getName).collect(Collectors.toList());
        TextFields.bindAutoCompletion(supplier, supplierList);
    }
    private void getRepaymentStatement () {
        suppplierCol.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        //Creating a table view
        final ObservableList<RepaymentStatementTableView> data = FXCollections.observableArrayList(
                repaymentStatementService.getRepaymentStatement(fromDate.getValue(), toDate.getValue(), supplier.getText(), pageNo, maxSize)
        );
        totalRecord = repaymentStatementService.getTableDataCount(fromDate.getValue(),toDate.getValue());
        amount.setText(String.valueOf(repaymentStatementService.getRepaymentStatementTotal(fromDate.getValue(), toDate.getValue(), supplier.getText())));
        table.setItems(data);
        pagination.setText(""+(from)+" - "+(Math.min(to,totalRecord))+" / "+totalRecord);
    }
    public void nextPage (){
        if(totalRecord > (long) (pageNo + 1) *maxSize) {
            pageNo++;
            from = to + 1;
            to = to + maxSize;
            getRepaymentStatement();
        }
    }
    public void prevPage (){
        if(pageNo > 0) {
            pageNo--;
            from = from - maxSize;
            to = to - maxSize;
            getRepaymentStatement();
        }
    }
}
