package com.chaudhary.chaudharycattle.controllers;

import com.chaudhary.chaudharycattle.service.LoginService;
import com.chaudhary.chaudharycattle.utils.CommanUtils;
import com.chaudhary.chaudharycattle.utils.FxmlPaths;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class LoginController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button signin;

    @Autowired
    private LoginService loginService;

    public void signIn_Key (KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER))
            signIn();
    }
    public void signIn (){
        if(loginService.login(username.getText(), password.getText())){
            CommanUtils.switchToScene(FxmlPaths.LANDING, signin,1340 , 700);
        }else {
            CommanUtils.warningAlert("Invalid UserName or Password.","");
            username.clear();
            password.clear();
            username.requestFocus();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginService.init();
    }
}
