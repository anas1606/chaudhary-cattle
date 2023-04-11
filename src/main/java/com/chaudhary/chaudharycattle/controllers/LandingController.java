package com.chaudhary.chaudharycattle.controllers;

import com.chaudhary.chaudharycattle.utils.CommanUtils;
import com.chaudhary.chaudharycattle.utils.FxmlPaths;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class LandingController implements Initializable {

    @FXML
    private BorderPane bp;
    @FXML
    private Button logout, dashboard, farm, supplier;

    public void logOut (){
        CommanUtils.switchToScene(FxmlPaths.LOGIN, logout, 600, 400);
    }

    public void openDashboard(){
        CommanUtils.loadPage(FxmlPaths.DASHBOARD);
    }
    public void openFarm(){
        CommanUtils.loadPage(FxmlPaths.FARM_MILK);
    }
    public void openSupplier(){
        CommanUtils.loadPage(FxmlPaths.SUPLLIER_PURCHASE_RECORD);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CommanUtils.setLandingBorderPane(bp);
        openDashboard();
    }
}
