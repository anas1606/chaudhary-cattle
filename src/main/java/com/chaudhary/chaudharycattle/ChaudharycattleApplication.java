package com.chaudhary.chaudharycattle;

import com.chaudhary.chaudharycattle.utils.FxmlPaths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.Objects;


@SpringBootApplication
public class ChaudharycattleApplication extends Application{

	private  static String TITTLE = "Chaudhary Cattle Farm";
	private static ConfigurableApplicationContext applicationContext;
	private Parent parent;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void init() throws Exception {
		applicationContext = new SpringApplicationBuilder(ChaudharycattleApplication.class).run();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FxmlPaths.LOGIN));
		fxmlLoader.setControllerFactory(applicationContext::getBean);
		parent = fxmlLoader.load();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/fxmls/images/chaudhary_logo.png")));
		primaryStage.getIcons().add(icon);
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		primaryStage.setScene(new Scene(parent, 800, 600));
		primaryStage.setTitle(TITTLE);
		primaryStage.show();
	}

	public void switchToScene (String fxmlPath, Button btn, int width, int height){
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
		fxmlLoader.setControllerFactory(applicationContext::getBean);
		Parent parent = null;
		try {
			parent = fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Stage stage = (Stage) btn.getScene().getWindow();
		stage.close();
		Scene scene = new Scene(parent, width, height);
		stage.setResizable(false);
		stage.centerOnScreen();
		stage.setScene(scene);
		stage.show();
	}

	public void loadPage (BorderPane bp,String fxmlPath){
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
		fxmlLoader.setControllerFactory(applicationContext::getBean);
		Parent parent = null;
		try {
			parent = fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		bp.setCenter(parent);
	}
	@Override
	public void stop() throws Exception {
		applicationContext.close();
	}
}
