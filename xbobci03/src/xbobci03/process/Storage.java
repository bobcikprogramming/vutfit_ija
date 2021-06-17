/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xbobci03.process;

import xbobci03.sharedClasses.SharedMethods;
import xbobci03.sharedClasses.Constants;
import xbobci03.listClasses.CellImageView;
import xbobci03.listClasses.Shelf;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Hlavní třída pro ovládání skladiště.
 * Probíhá otevření souboru, jeho následné načtení a syntaktická kontrola. Dále uložení hodnot do arraylistu a vyobrazení regálů. Rovněž přidání či odebrání překážky a výpis obsahu regálu.
 * @author Pavel Bobčík (xbobci03)
 * @author Tomáš Daněk (xdanek23)
 */
public class Storage {
    GridPane gridPane;
    TextArea tvOut;
    ArrayList<CellImageView> imgList = new ArrayList<>();
    ArrayList<Shelf> shelfList;
    public String fileName = "";
    boolean fileLoaded = false;
    
    /**
     * Konstruktor třídy Storage.
     * Dojde k deklaraci uvedených hodnot.
     * @param gridPane odkaz na objekt mřížky plochy skladiště
     * @param imgList arraylist načtených ImageView plochy skladiště
     * @param tvOut odkaz na objekt výstupní konzole
     */
    public Storage(GridPane gridPane, ArrayList<CellImageView> imgList, TextArea tvOut){
        this.gridPane = gridPane;
        this.imgList = imgList;
        this.tvOut = tvOut;
    }

    /**
     * Metoda sloužící jako getter jména souboru.
     * @return jméno souboru
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Metoda sloužící jako getter arraylistu regálů.
     * @return arraylist regálů
     */
    public ArrayList<Shelf> getShelfList() {
        return shelfList;
    }
    
    /**
     * Metoda sloužící pro otevření souboru se skladištěm.
     * Dojde k otevření okna pro výběr souboru se skladištěm.
     * @return pravdivostní hodnota, zda-li byl otevřen soubor
     */
    public boolean openFile(){
        FileChooser fil_chooser = new FileChooser();
        fil_chooser.setTitle("Otevřít skladiště");
        fil_chooser.setInitialDirectory(new File(Constants.pathMap)); // Změnit u antu na data
        Stage stage = new Stage();
        File file = fil_chooser.showOpenDialog(stage);
        if(file == null){
            fileLoaded = false;
            return false;
        }else{
            this.fileName = String.valueOf(file);
            return true;
        }
    }
    
    /**
     * Metoda slouží k načtení dat ze souboru a jejich uložení do arraylistu.
     * Jako první dojde k naplnění arraylistu shelfList hodnotou reprezentující cestu.
     * Dále se ze souboru budou načítat jednotlivé řádky, jenž jsou kontrolovány na syntaktickou správnost.
     * Z načteného řádku se zjistí souřadnice regálu a dojde k jeho vložení (resp. přepsání) do arraylistu.
     * @return pravdivostní hodnota, zba byl načten soubor
     */
    public boolean getStorageArray(){
        shelfList = new ArrayList<>();
        for(int listIndex = 0; listIndex < 250; listIndex++){
            Shelf road = new Shelf(false, listIndex, "", 0, false);
            this.shelfList.add(road);
        } 
        
        Scanner scanner = null;
        try {
            File file = new File(fileName);
            scanner = new Scanner(new FileInputStream(file));
        } catch(FileNotFoundException ex){
            SharedMethods.exitCode("Neplatný souboru '"+fileName+"'.");
        }
        
        while(scanner.hasNextLine() ){
            String[] splitted = scanner.nextLine().split(";");
            if(checkSyntax(splitted)){
                SharedMethods.errorMsg("Špatný formát souboru '"+fileName+"'.");
                this.fileName = "";
                this.fileLoaded = false;
                return false;
            }
            
            int x = Integer.parseInt(splitted[0]);
            int y = Integer.parseInt(splitted[1]);
            int amount = Integer.parseInt(splitted[3]);
            int shelfIndex = SharedMethods.getIndex(x, y);
            
            if(shelfIndex < 0 || shelfIndex > 249){
                SharedMethods.exitCode("Snaha o zapsání mimo rozsah skladiště.");                
            }

            Shelf shelf = new Shelf(true, shelfIndex, splitted[2], amount, true);
            this.shelfList.set(shelfIndex, shelf);
        }
        scanner.close();
        return true;
    }
    
    /**
     * Metoda slouží ke kontrole syntaxe.
     * @param line načtený řádek ke kontrole
     * @return pravdivostní hodnota, zda-li je řádek syntakticky správně
     */
    private boolean checkSyntax(String[] line){
        boolean failed = false;
        
        if(line.length != 4){
            failed = true;
        }else{
            try{
                int x = Integer.parseInt(line[0]);
                int y = Integer.parseInt(line[1]);
                int amount = Integer.parseInt(line[3]);
                
                if(x > 24 || y > 9 || line[2].isEmpty()){
                    failed = true;
                }

                if(amount < 0){
                    failed = true;
                }
            } catch(NumberFormatException ex){
                failed = true;
            }
            
        }
        return failed;
    }
    
    /**
     * Metodou sloužící k vykreslení skladiště.
     */
    public void loadStorage(){
        for(Shelf shelf : this.shelfList){
            if(shelf.getIsShelf()){
                if(shelf.getAmount() == 0){
                    if(!mySetImage(Constants.imgWidth, Constants.imgHeight, "pallet_empty.png", shelf.getPosition())){
                        SharedMethods.exitCode("Chyba při načítání obrázku.");
                    }
                }else{
                    if(!mySetImage(Constants.imgWidth, Constants.imgHeight, "pallet_box.png", shelf.getPosition())){
                        SharedMethods.exitCode("Chyba při načítání obrázku.");
                    }
                }
            }
        }
        this.fileLoaded = true;
    }
    
    /**
     * Metoda sloužící k vyčištění plochy skladiště.
     */
    public void nullImages(){
        for(CellImageView img : imgList){
            img.getImg().setImage(null);
        }
    }
    
    /**
     * Metoda slouží k nastavení obrázku do ImageView na uvedených souřadnicích.
     * Z obrženého parametru index vypočítáme souřadnici v 2D poli, na kterou následovně
     * načteme obdržený obrázek.
     * @param width šířka obrázku
     * @param height výška obrázku
     * @param imgName název obrázku k nahrání
     * @param index pozice ImageView v poli
     * @return pravdivostní hodnotu, zda-li se načtení provedlo
     */
    private boolean mySetImage(int width, int height, String imgName, int index){
        boolean success = false;
        ArrayList<Integer> coor = SharedMethods.getCoordinations(index);
        ImageView myImage = null;
        for (Node child: this.gridPane.getChildren()){
            if(GridPane.getColumnIndex(child) == coor.get(0) && GridPane.getRowIndex(child) == coor.get(1)){
                myImage = this.imgList.get(index).getImg();
                myImage.setImage(new Image(Constants.pathImg+imgName, width, height, false, true));
                success = true;
                break;
             }
        }
        return success;
    }
    
    /**
     * Metoda slouží k reagování na kliknutí na plochu skladiště.
     * Motoda zkontrolu, že je skladiště načteno, vypočítá index v 2D poli ze souřadnic x a y
     * a provede reakci. Jedná-li se o regál, vypíše obsah regálu, v opačeném případě přidá či oddělá
     * překážku z cesty a informaci o svém úkonu opět vypíše na výstupní konzoli.
     * @param x souřadnice x
     * @param y souřadnice y
     */
    public void onCellClickListener(int x, int y){
        if(this.fileLoaded == true){
            int index = SharedMethods.getIndex(x, y);
            Shelf shelf = this.shelfList.get(index);
            if(this.shelfList.get(index).getIsShelf()){
                this.tvOut.setText("Regál na pozici ("+x+":"+y+") obsahuje: "+shelf.getGoods()+" ("+shelf.getAmount()+" ks).\n" + this.tvOut.getText());
                //this.tvOut.appendText("Regál na pozici ("+x+":"+y+") obsahuje:\n  Druh:   "+shelf.getGoods()+"\n  Počet:  "+shelf.getAmount()+"\n");
            }else{
                if(!shelf.isWall){
                    if(!mySetImage(Constants.imgWidth, Constants.imgHeight, "cone.png", shelf.getPosition())){
                        JOptionPane.showMessageDialog(new JFrame(), "Nepodařilo se přidat překážku.", "Chybný stav.", JOptionPane.ERROR_MESSAGE);
                    }
                    this.tvOut.setText("Cesta ("+x+":"+y+") byla uzavřena.\n" + this.tvOut.getText());
                    //this.tvOut.appendText("Cesta ("+x+":"+y+") byla uzavřena.\n");
                    shelf.setIsWall(true);
                }else{
                    this.imgList.get(index).getImg().setImage(null);
                    this.tvOut.setText("Cesta ("+x+":"+y+") byla otevřena.\n" + this.tvOut.getText());
                    //this.tvOut.appendText("Cesta ("+x+":"+y+") byla otevřena.\n");
                    shelf.setIsWall(false);
                } 
            }
        }
    }
    
}
