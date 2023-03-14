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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Controller
public class MilkController implements Initializable {

    @FXML
    private TextField lt, ltd, ft, ftd, rt, rtd, at, atd, morning, evening, fat, rate, amount;
    @FXML
    TableView<MilkTableView> table = new TableView<>();
    @FXML
    private ComboBox<String> cb;
    @FXML
    TableColumn<MilkTableView, String> id,shift,date,liters,fats,rates,amounts;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button save;

    @Autowired
    private MilkService milkService;

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
                milkService.saveData(cb.getValue(), datePicker.getValue(), _lt, _fat, _rate, _amount);
                CommanUtils.informationAlert("Information", "Dairy Slip Saved.");
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
        new CommanUtils();
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
        morning.setText(milkService.totalLitersOfMilkByShift(Shift.MORNING).toString());
        evening.setText(milkService.totalLitersOfMilkByShift(Shift.EVENING).toString());
        MilkRecordModel model = milkService.milkRecord();
        fat.setText(model.getFat().toString());
        rate.setText(model.getRate().toString());
        amount.setText(model.getAmount().toString());
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        shift.setCellValueFactory(new PropertyValueFactory<>("shift"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        liters.setCellValueFactory(new PropertyValueFactory<>("liters"));
        fats.setCellValueFactory(new PropertyValueFactory<>("fat"));
        rates.setCellValueFactory(new PropertyValueFactory<>("rate"));
        amounts.setCellValueFactory(new PropertyValueFactory<>("amount"));
        //Creating a table view
        final ObservableList<MilkTableView> data = FXCollections.observableArrayList(
                milkService.getTableData()
        );
        table.setItems(data);
        datePicker.setValue(LocalDate.now());
        Platform.runLater(()-> lt.requestFocus());
    }

    private boolean validate(Double lt, Double fat, Double rate, Double amount) {
        return lt > 0.0 && fat > 0.0 && rate > 0.0 && amount > 0.0 && cb.getValue() != null && datePicker.getValue() != null;
    }

    private int parse (String str){
        return (str.equals(""))?0:Integer.parseInt(str);
    }
}
