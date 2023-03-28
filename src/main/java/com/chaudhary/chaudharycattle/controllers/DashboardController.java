package com.chaudhary.chaudharycattle.controllers;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.model.DashboardTableView;
import com.chaudhary.chaudharycattle.service.DashboardService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class DashboardController implements Initializable {
    @FXML
    private TableView<DashboardTableView> foodTable = new TableView<>();
    @FXML
    private TableView<DashboardTableView> supplierTable = new TableView<>();
    @FXML
    private TableColumn<DashboardTableView, String> foodName,foodStock,supplierAmount,supplierName;
    @FXML
    private Label D0599, E0599, D0868, E0868;
    @Autowired
    private DashboardService dashboardService;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread stockThread = new Thread(this::renderStockTable);
        stockThread.start();
        Thread supplierThread = new Thread(this::renderSupplierTable);
        supplierThread.start();
        Thread milkRecordRender = new Thread(this::rederMilkRecord);
        milkRecordRender.start();
    }
    private void renderStockTable (){
        foodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        foodStock.setCellValueFactory(new PropertyValueFactory<>("amount"));
        //Creating a table view
        final ObservableList<DashboardTableView> data = FXCollections.observableArrayList(
                dashboardService.getStockTable()
        );
        foodTable.setItems(data);
    }
    private void renderSupplierTable (){
        supplierName.setCellValueFactory(new PropertyValueFactory<>("name"));
        supplierAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        //Creating a table view
        final ObservableList<DashboardTableView> data = FXCollections.observableArrayList(
                dashboardService.getSupplierTable()
        );
        supplierTable.setItems(data);
    }
    private void rederMilkRecord (){
        D0599.setText(dashboardService.getTotalMilkCountByCode("0599", Shift.MORNING).toString());
        E0599.setText(dashboardService.getTotalMilkCountByCode("0599", Shift.EVENING).toString());
        D0868.setText(dashboardService.getTotalMilkCountByCode("0868", Shift.MORNING).toString());
        E0868.setText(dashboardService.getTotalMilkCountByCode("0868", Shift.EVENING).toString());
    }
}
