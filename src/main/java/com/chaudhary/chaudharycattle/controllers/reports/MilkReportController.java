package com.chaudhary.chaudharycattle.controllers.reports;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.model.farm.MilkRecordModel;
import com.chaudhary.chaudharycattle.model.farm.MilkTableView;
import com.chaudhary.chaudharycattle.service.reports.MilkReportService;
import com.chaudhary.chaudharycattle.utils.CommanUtils;
import com.chaudhary.chaudharycattle.utils.FxmlPaths;
import com.opencsv.CSVWriter;
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

import java.io.FileWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class MilkReportController implements Initializable {

    @FXML
    private TableView<MilkTableView> table = new TableView<>();
    @FXML
    private TableColumn<MilkTableView, String> shiftCol,dateCol,literCol,fatCol,rateCol,amountCol,codeCol;
    @FXML
    private ComboBox<String> cb;
    @FXML
    private DatePicker fromDate, toDate;
    @FXML
    private Label pagination;
    @FXML
    private TextField code, morning, evening, fat, rate, amount;
    @FXML
    private Button export;
    @Autowired
    private MilkReportService milkReportService;
    private static int pageNo = 0;
    private static final int maxSize = 10;
    private static int from = 1;
    private static int to = maxSize;
    private static long totalRecord = 0;
    private static final String PATH = "Reports/Milk Report/";
    private static final List<String> codes = new ArrayList<>(Arrays.asList("0599","0868"));
    public void loadRepayment() {
        CommanUtils.loadPage(FxmlPaths.SUPLLIER_REPAYMENT);
    }
    public void loadUsageReport () {
        CommanUtils.loadPage(FxmlPaths.USAGE_REPORT);
    }
    public void filter_key(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER))
            filter();
    }
    public void filter () {
        export.setVisible(false);
        getFilteredMilkReport();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(()-> code.requestFocus());
        TextFields.bindAutoCompletion(code, codes);
        fromDate.setValue(LocalDate.now().withDayOfMonth(1));
        toDate.setValue(fromDate.getValue().plusMonths(1).minusDays(1));
        ObservableList<String> options = FXCollections.observableArrayList("MORNING","EVENING");
        cb.setItems(options);
    }
    private void getFilteredMilkReport (){
        shiftCol.setCellValueFactory(new PropertyValueFactory<>("shift"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        literCol.setCellValueFactory(new PropertyValueFactory<>("liters"));
        fatCol.setCellValueFactory(new PropertyValueFactory<>("fat"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        //Creating a table view
        final ObservableList<MilkTableView> data = FXCollections.observableArrayList(
                milkReportService.getFilterMilkReport(fromDate.getValue(), toDate.getValue(), code.getText(), cb.getValue() ,pageNo, maxSize)
        );
        if(data.size()>0)
            export.setVisible(true);
        totalRecord = milkReportService.getTableDataCount(fromDate.getValue(),toDate.getValue(),code.getText(), cb.getValue());
        table.setItems(data);
        pagination.setText(""+(from)+" - "+(Math.min(to,totalRecord))+" / "+totalRecord);
        getTotalCounts();
    }
    private void getTotalCounts () {
        if(cb.getValue() == null) {
            morning.setText(milkReportService.totalLitersOfMilkByShift(fromDate.getValue(), toDate.getValue(), code.getText(), Shift.MORNING).toString());
            evening.setText(milkReportService.totalLitersOfMilkByShift(fromDate.getValue(), toDate.getValue(), code.getText(), Shift.EVENING).toString());
        } else if (cb.getValue().equalsIgnoreCase("MORNING")){
            morning.setText(milkReportService.totalLitersOfMilkByShift(fromDate.getValue(), toDate.getValue(), code.getText(), Shift.MORNING).toString());
            evening.setText("0.0");
        } else {
            morning.setText("0.0");
            evening.setText(milkReportService.totalLitersOfMilkByShift(fromDate.getValue(), toDate.getValue(), code.getText(), Shift.EVENING).toString());
        }
        MilkRecordModel model = milkReportService.fatRateAndAmountOfReport(fromDate.getValue(), toDate.getValue(), code.getText(), cb.getValue());
        fat.setText(model.getFat().toString());
        rate.setText(model.getRate().toString());
        amount.setText(model.getAmount().toString());
    }
    public void nextPage (){
        if(totalRecord > (long) (pageNo + 1) *maxSize) {
            pageNo++;
            from = to + 1;
            to = to + maxSize;
            getFilteredMilkReport();
        }
    }
    public void prevPage (){
        if(pageNo > 0) {
            pageNo--;
            from = from - maxSize;
            to = to - maxSize;
            getFilteredMilkReport();
        }
    }

    public void exportCSV () throws Exception{
        String file_name = "MilkReport_FROM_"+fromDate.getValue()+"_TO_"+toDate.getValue()+".csv";
        CSVWriter writer = new CSVWriter(new FileWriter(PATH+file_name));
        List<MilkTableView> list = milkReportService.getFilterMilkReport(fromDate.getValue(), toDate.getValue(), code.getText(), cb.getValue() ,pageNo, Integer.MAX_VALUE);
        writer.writeNext(new String[]{"", ""});
//        Insert The Record
        writer.writeNext(new String[]{"Total Liters", String.valueOf((Double.parseDouble(morning.getText()) + Double.parseDouble(evening.getText()) ))});
        writer.writeNext(new String[]{"Morning", morning.getText()});
        writer.writeNext(new String[]{"Evening", evening.getText()});
        writer.writeNext(new String[]{"Fat", fat.getText()});
        writer.writeNext(new String[]{"Rate", rate.getText()});
        writer.writeNext(new String[]{"Amount", amount.getText()});
        writer.writeNext(new String[]{"", ""});
        String[] headers = {"Code","Shift","Liters","Fat","Rate","Amount","Date"};
        writer.writeNext(headers);
        for (MilkTableView obj : list){
            String[] line = {obj.getCode(), obj.getShift(), obj.getLiters().toString(), obj.getFat().toString(), obj.getRate().toString(), obj.getAmount().toString(), obj.getDate().toString()};
            writer.writeNext(line);
        }
        writer.flush();

        CommanUtils.informationAlert("Information","Milk Report is Exported to CSV.\nPath: /Reports/Milk Report/");
    }
}
