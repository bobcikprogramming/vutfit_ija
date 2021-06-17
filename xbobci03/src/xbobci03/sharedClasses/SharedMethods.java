/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xbobci03.sharedClasses;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Třída slouží k uložení stejných metod využívaných více třídami.
 * @author Pavel Bobčík (xbobci03)
 */
public class SharedMethods {
    /**
     * Sdílená metoda pro výpis chybového hlášení a ukončení programu.
     * @param message obsah chybového hlášení
     */
    public static void exitCode(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Chybový stav");
        alert.setHeaderText("Vyskytla se chyba.");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            Platform.exit();
        }
    }
    
    /**
     * Sdílená metoda pro výpis chybového hlášení.
     * @param message obsah chybového hlášení
     */
    public static void errorMsg(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.setTitle("Chybový stav");
        alert.setHeaderText("Vyskytla se chyba.");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            //do stuff
        }
    }
    
    /**
     * Metoda pro výpočet indexu v 2D poli
     * @param x souřadnice x
     * @param y souřadnice y
     * @return index 2D pole
     */
    public static int getIndex(int x, int y){
        return x * 10 + y;
    }
    
    /**
     * Metoda pro výpočet souřadnic z indexu.
     * @param index index v 2D poli
     * @return arraylist obsahující souřadnice
     */
    public static ArrayList<Integer> getCoordinations(int index){
        ArrayList<Integer> coordinations = new ArrayList<>();
        int y = index % 10;
        int x = (index - y) / 10;
        coordinations.add(x);
        coordinations.add(y);
        return coordinations;
    }
}
