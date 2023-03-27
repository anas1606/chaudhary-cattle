package com.chaudhary.chaudharycattle.controllers.Farm;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.model.farm.MilkRecordModel;
import com.chaudhary.chaudharycattle.model.farm.MilkTableView;
import com.chaudhary.chaudharycattle.service.farm.MilkService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class MilkController implements Initializable {

    @FXML
    private TextField lt, ltd, ft, ftd, rt, rtd, at, atd, morning, evening, fat, rate, amount,code;
    @FXML
    private TableView<MilkTableView> table = new TableView<>();
    @FXML
    private ComboBox<String> cb;
    @FXML
    private TableColumn<MilkTableView, String> shift,date,liters,fats,rates,amounts;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label pagination,recordLabel;
    @Autowired
    private MilkService milkService;
    private static int pageNo = 0;
    private static final int maxSize = 10;
    private static int from = 1;
    private static int to = maxSize;
    private static int totalRecord = 0;
    private static final List<String> codes = new ArrayList<>(Arrays.asList("0599","0868"));
    public void submit_key(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER))
            submit();
    }
    public void submit() {
        double _lt = Double.parseDouble(parse(lt.getText())+"."+parse(ltd.getText()));
        double _fat = Double.parseDouble(parse(ft.getText())+"."+parse(ftd.getText()));
        double _rate = Double.parseDouble(parse(rt.getText())+"."+parse(rtd.getText()));
        double _amount = Double.parseDouble(parse(at.getText())+"."+parse(atd.getText()));
        if(validate(_lt,_fat,_rate,_amount)) {
            StringBuilder str = new StringBuilder();
            str.append("Date : ").append(datePicker.getValue());
            str.append("\nShift : ").append(cb.getValue());
            str.append("\nLt : ").append(_lt);
            str.append("\nFat : ").append(_fat);
            str.append("\nRate : ").append(_rate);
            str.append("\nAmount : ").append(_amount);

            if( (CommanUtils.confirmationAlert("Dairy Slip ", str.toString())).equalsIgnoreCase("OK") ) {
                milkService.saveData(cb.getValue(), datePicker.getValue(), _lt, _fat, _rate, _amount, code.getText());
                CommanUtils.informationAlert("Information", "Dairy Slip Saved.");
                renderTotalData(code.getText());
                clearAllFields();
            }
        }else {
            CommanUtils.warningAlert("Warning", "Please Fill All The Fields");
            lt.requestFocus();
        }
    }
    public void loadFoodUsage(){
        CommanUtils.loadPage(FxmlPaths.FARM_FOOD_USAGE);
    }
    public void loadMadicine(){
        CommanUtils.loadPage(FxmlPaths.FARM_MADICINE);
    }
    public void loadFoodPurchase(){
        CommanUtils.loadPage(FxmlPaths.FARM_FOOD_PURCHASE);
    }

    public void isNumberlt(){
        CommanUtils.numberFormate(lt);
    }
    public void isNumberltd(){
        CommanUtils.numberFormate(ltd);
    }
    public void isNumberft(){
        CommanUtils.numberFormate(ft);
    }
    public void isNumberftd(){
        CommanUtils.numberFormate(ftd);
    }
    public void isNumberrt(){
        CommanUtils.numberFormate(rt);
    }
    public void isNumberrtd(){
        CommanUtils.numberFormate(rtd);
    }
    public void isNumberat(){
        CommanUtils.numberFormate(at);
    }
    public void isNumberatd(){
        CommanUtils.numberFormate(atd);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<String> options = FXCollections.observableArrayList("MORNING","EVENING");
        cb.setItems(options);
        datePicker.setValue(LocalDate.now());
        TextFields.bindAutoCompletion(code, codes);
        Platform.runLater(()-> code.requestFocus());

        Thread totalData = new Thread(()->{renderTotalData("0");});
        totalData.start();
        Thread dataTable = new Thread(this::renderDataTable);
        dataTable.start();
        lt.focusedProperty().addListener((ov, oldv, newV) -> {
            if(codes.contains(code.getText())){
                renderTotalData(code.getText());
                recordLabel.setText(code.getText());
            }else{
                recordLabel.setText("ALL");
                renderTotalData("0");
                code.clear();
                code.requestFocus();
            }
        });
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
    private boolean validate(Double lt, Double fat, Double rate, Double amount) {
        return lt > 0.0 && fat > 0.0 && rate > 0.0 && amount > 0.0 && cb.getValue() != null && datePicker.getValue() != null;
    }
    private  void renderTotalData (String code){
        morning.setText(milkService.totalLitersOfMilkByShift(Shift.MORNING, code).toString());
        evening.setText(milkService.totalLitersOfMilkByShift(Shift.EVENING, code).toString());
        MilkRecordModel model = milkService.milkRecord(code);
        fat.setText(model.getFat().toString());
        rate.setText(model.getRate().toString());
        amount.setText(model.getAmount().toString());
    }
    private void renderDataTable (){
        shift.setCellValueFactory(new PropertyValueFactory<>("shift"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        liters.setCellValueFactory(new PropertyValueFactory<>("liters"));
        fats.setCellValueFactory(new PropertyValueFactory<>("fat"));
        rates.setCellValueFactory(new PropertyValueFactory<>("rate"));
        amounts.setCellValueFactory(new PropertyValueFactory<>("amount"));
        //Creating a table view
        final ObservableList<MilkTableView> data = FXCollections.observableArrayList(
                milkService.getTableData(pageNo, maxSize)
        );
        totalRecord = milkService.getTableDataCount();
        table.setItems(data);
        pagination.setText(""+(from)+" - "+(Math.min(to,totalRecord))+" / "+totalRecord);
    }
    private int parse (String str){
        return (str.equals(""))?0:Integer.parseInt(str);
    }
    private void clearAllFields() {
        lt.clear();
        ltd.clear();
        rt.clear();
        rtd.clear();
        ft.clear();
        ftd.clear();
        at.clear();
        atd.clear();
        code.requestFocus();
    }
}
