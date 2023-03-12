package com.chaudhary.chaudharycattle.controllers.Farm;

import com.chaudhary.chaudharycattle.model.FarmMilkTableView;
import com.chaudhary.chaudharycattle.utils.CommanUtils;
import com.chaudhary.chaudharycattle.utils.FxmlPaths;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Controller;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class Milk implements Initializable {

    @FXML
    private TextField lt, ltd, ft, ftd, rt, rtd, at, atd, morning, evening, fat, rate, amount;
    @FXML
    TableView<FarmMilkTableView> table = new TableView<>();
    @FXML
    private ComboBox<String> cb;
    @FXML
    TableColumn<FarmMilkTableView, String> id,shift,date,liters,fats,rates,amounts,action;
    public void loadFoodUsage(){
        new CommanUtils();
        CommanUtils.loadPage(FxmlPaths.FARM_FOOD_USAGE);
    }
    public void loadMadicine(){
        new CommanUtils();
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
        morning.setText("189.45");
        evening.setText("179.89");
        fat.setText("6.3");
        rate.setText("50.45");
        amount.setText("14987.56");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        shift.setCellValueFactory(new PropertyValueFactory<>("shift"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        liters.setCellValueFactory(new PropertyValueFactory<>("liters"));
        fats.setCellValueFactory(new PropertyValueFactory<>("fat"));
        rates.setCellValueFactory(new PropertyValueFactory<>("rate"));
        amounts.setCellValueFactory(new PropertyValueFactory<>("amount"));
        //Creating a table view
        final ObservableList<FarmMilkTableView> data = FXCollections.observableArrayList(
                new FarmMilkTableView("1","Morning","10.03.2023","1200","6.4","50.65","14987.45","Action"),
                new FarmMilkTableView("1","Morning","10.03.2023","1200","6.4","50.65","14987.45","Action"),
                new FarmMilkTableView("1","Morning","10.03.2023","1200","6.4","50.65","14987.45","Action")
        );
        table.setItems(data);
        Platform.runLater(()-> lt.requestFocus());
    }
}
