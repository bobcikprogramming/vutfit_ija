/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbobci03;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import xbobci03.sharedClasses.Constants;

/**
 * Jedná se o hlavní třídu, jenž zavolá třídu pro zobrazení GUI.
 * @author Pavel Bobčík (xbobci03)
 */
public class Main extends Application {

    /**
    * Metoda slouží k inicializaci a spuštění grafického prostředí aplikace.To načte ze souboru FXMLDocument.fxml a MyGUISetting.css pro dodatečného grafického nastavení.
    * @param stage grafické okno
    */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(Constants.pathFxml));
        root.getStylesheets().add(getClass().getResource(Constants.pathCss).toExternalForm());
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Simulátor skladiště");
        stage.show();
    }

    
    public static void main(String[] args) {
        launch(args);
    }

}