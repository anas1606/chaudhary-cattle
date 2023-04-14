package com.chaudhary.chaudharycattle.controllers.reports;

import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.model.DashboardTableView;
import com.chaudhary.chaudharycattle.model.farm.FoodUsageTableView;
import com.chaudhary.chaudharycattle.service.farm.FoodUsageService;
import com.chaudhary.chaudharycattle.service.reports.UsageReportService;
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
public class UsageReportController implements Initializable {
    @FXML
    private TextField food;
    @FXML
    private ComboBox<String> cb;
    @FXML
    private DatePicker fromDate, toDate;
    @FXML
    private TableView<FoodUsageTableView> table = new TableView<>();
    @FXML
    private TableColumn<FoodUsageTableView, String> foodCol,dateCol,rateCol,qtyCol,amountCol,shiftCol;
    @FXML
    private TableView<DashboardTableView> totalTable = new TableView<>();
    @FXML
    private TableColumn<DashboardTableView, String> totalFoodCol,totalAmountCol;
    @FXML
    private Label pagination, amount;
    @Autowired
    private FoodUsageService foodUsageService;
    @Autowired
    private UsageReportService usageReportService;
    private static int pageNo = 0;
    private static final int maxSize = 10;
    private static int from = 1;
    private static int to = maxSize;
    private static long totalRecord = 0;
    public void loadRepayment() {
        CommanUtils.loadPage(FxmlPaths.SUPLLIER_REPAYMENT);
    }
    public void loadMilkReport () {
        CommanUtils.loadPage(FxmlPaths.MILK_REPORT);
    }
    public void filter_key(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER))
            filter();
    }
    public void filter () {
        getUsageFilterRecord();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fromDate.setValue(LocalDate.now().withDayOfMonth(1));
        toDate.setValue(fromDate.getValue().plusMonths(1).minusDays(1));
        List<String> foodList = foodUsageService.getFoodList().stream().map(Food::getName).collect(Collectors.toList());
        TextFields.bindAutoCompletion(food, foodList);
        ObservableList<String> options = FXCollections.observableArrayList("MORNING","EVENING");
        cb.setItems(options);
        Platform.runLater(()-> food.requestFocus());
    }
    private void getUsageFilterRecord () {
        foodCol.setCellValueFactory(new PropertyValueFactory<>("food"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        shiftCol.setCellValueFactory(new PropertyValueFactory<>("shift"));
        //Creating a table view
        final ObservableList<FoodUsageTableView> data = FXCollections.observableArrayList(
                usageReportService.getFilterUsageReport(fromDate.getValue(),toDate.getValue(),food.getText(),cb.getValue(),pageNo,maxSize)
        );
        totalRecord = usageReportService.getFilterUsageReportTotalCount(fromDate.getValue(),toDate.getValue(),food.getText(),cb.getValue());
        table.setItems(data);
        pagination.setText(""+(from)+" - "+(Math.min(to,totalRecord))+" / "+totalRecord);
        getFilterFoodAmounts();
    }
    private void getFilterFoodAmounts (){
        totalFoodCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        totalAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        //Creating a table view
        final ObservableList<DashboardTableView> data = FXCollections.observableArrayList(
                usageReportService.getFilterFoodAmounts(fromDate.getValue(),toDate.getValue(),food.getText(),cb.getValue())
        );
        totalTable.setItems(data);
        amount.setText(String.valueOf(usageReportService.getFilterFoodsTotalAmount(fromDate.getValue(),toDate.getValue(),food.getText(),cb.getValue())));
    }
    public void nextPage (){
        if(totalRecord > (long) (pageNo + 1) *maxSize) {
            pageNo++;
            from = to + 1;
            to = to + maxSize;
            getUsageFilterRecord();
        }
    }
    public void prevPage (){
        if(pageNo > 0) {
            pageNo--;
            from = from - maxSize;
            to = to - maxSize;
            getUsageFilterRecord();
        }
    }
}
