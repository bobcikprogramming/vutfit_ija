/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xbobci03.sharedClasses;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
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
            alert.hide();
        }
    }
    
    /**
     * Metoda pro výpočet indexu v 2D poli
     * @param x souřadnice x
     * @param y souřadnice y
     * @return index 2D pole
     */
    public static int getIndex(int x, int y){
        return x * 12 + y;
    }
    
    /**
     * Metoda pro výpočet souřadnic z indexu.
     * @param index index v 2D poli
     * @return arraylist obsahující souřadnice
     */
    public static ArrayList<Integer> getCoordinations(int index){
        ArrayList<Integer> coordinations = new ArrayList<>();
        int y = index % 12;
        int x = (index - y) / 12;
        coordinations.add(x);
        coordinations.add(y);
        return coordinations;
    }
    
    /**
     * Metoda slouží pro redukování počtu řádků na výstupní konzoli na uvedený počet (500).
     * @param lines pole textových řetěžců rozdělených po řádcích
     * @return String upravený textový řetězec obsahující pouze 500 řádků
     */
    public static String reduceLines(String[] lines){
        for (int i = lines.length - 1; i > 500; i--){
            lines[i] = "";
        }

        StringBuilder concatText = new StringBuilder("");
        for(int i = 0; i <= 500; i++){
            if(lines[i].equals("")){
                concatText.append("\n");
            }else{
                concatText.append(lines[i]).append("\n");
            }
        }

        return concatText.toString();
    }
    
    /**
     * Metoda sloužící k inicializování ExecutorService s odpovídajícím počtem vláken. 
     * @param maxSize maximální počet vláken
     * @return ExecutorService inicializovanou hodnotu
     */
    public static ExecutorService initThreadExecutor(int maxSize){
        ExecutorService executor = Executors.newScheduledThreadPool(maxSize,new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
        return executor;
    }
}
