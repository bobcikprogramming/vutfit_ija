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
import xbobci03.listClasses.Cart;
import xbobci03.listClasses.CellAStar;
import xbobci03.listClasses.RequestList;

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
        for(int listIndex = 0; listIndex < 300; listIndex++){
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
            
            if(shelfIndex < 0 || shelfIndex > 299){
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
                
                if(x > (Constants.xMax - 1) || y > (Constants.yMax - 1) || line[2].isEmpty()){
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
     * Je-li na políčku vozík, zobrazí jeho trasu a vypíše jeho inforamce.
     * @param x souřadnice x
     * @param y souřadnice y
     */
    public void onCellClickListener(int x, int y){
        if(this.fileLoaded == true){
            int index = SharedMethods.getIndex(x, y);
            Shelf shelf = this.shelfList.get(index);
            if(this.shelfList.get(index).getIsShelf()){
                String outputText = "Regál na pozici ("+x+":"+y+") obsahuje: "+shelf.getGoods()+" ("+shelf.getAmount()+" ks).\n" + makeVSpace() + this.tvOut.getText();
                    String[] lines = outputText.split("\n");
                    
                    if(lines.length > 500){
                        outputText = SharedMethods.reduceLines(lines);
                    }
                
                this.tvOut.setText(outputText);
            }else{
                if(!shelf.isWall){
                    if(shelf.getCart().isEmpty()){
                        if(!mySetImage(Constants.imgWidth, Constants.imgHeight, "cone.png", shelf.getPosition())){
                            SharedMethods.errorMsg("Nepodařilo se přidat překážku.");
                        }

                        String outputText = "Cesta ("+x+":"+y+") byla uzavřena.\n" + makeVSpace() + this.tvOut.getText();
                        String[] lines = outputText.split("\n");

                        if(lines.length > 500){
                            outputText = SharedMethods.reduceLines(lines);
                        }

                        this.tvOut.setText(outputText);
                        shelf.setIsWall(true);
                    }else{
                        onCartClickListener(shelf);
                    }
                }else{
                    this.imgList.get(index).getImg().setImage(null);
                    
                    String outputText = "Cesta ("+x+":"+y+") byla otevřena.\n" + makeVSpace() + this.tvOut.getText();
                    String[] lines = outputText.split("\n");
                    
                    if(lines.length > 500){
                        outputText = SharedMethods.reduceLines(lines);
                    }
                    
                    this.tvOut.setText(outputText);
                    shelf.setIsWall(false);
                } 
            }
        }
    }
    
    /**
     * Metoda slouží k reagování na kliknutí na vozík.
     * Z odpovídajícího políčka vybere ze seznamu vozíků vozík na poslední pozici a zobrazí jeho trasu a vypíše jeho inforamce.
     * @param shelf políčko na mapě
     */
    private void onCartClickListener(Shelf shelf){
        ArrayList<Cart> cart = shelf.getCart();
        int last = cart.size() - 1;
        ArrayList<CellAStar> pathArr = cart.get(last).getPath();

        String tmpText = "Informace o vozíku:\n"
                + " +  číslo: " + cart.get(last).getNumber() + ",\n"
                + " +  směřuje do [" + cart.get(last).getFinX() + ":" + cart.get(last).getFinY() + "],";
        for(RequestList item : cart.get(last).getContains()){
            tmpText += "\n +  obsahuje zboží: " + item.getGoods() + " (" + item.getAmount() + " ks),";
        }
        tmpText = tmpText.replaceFirst(",$", ".\n");
        tmpText += makeVSpace() + this.tvOut.getText();
        String outputText = tmpText;

        String[] lines = outputText.split("\n");

        if(lines.length > 500){
            outputText = SharedMethods.reduceLines(lines);
        }

        this.tvOut.setText(outputText);

        for(Shelf cell : this.shelfList){
            if(cell.getCartNumber() != -1){
                cell.setCartNumber(-1);
                if(cell.getCart().isEmpty() && !cell.isWall){
                    this.imgList.get(cell.getPosition()).getImg().setImage(null);
                }
            }
        }
        int cartNum = cart.get(last).getNumber();
        try{
            for(CellAStar path : pathArr){
                int position = SharedMethods.getIndex(path.getX(), path.getY());
                if(this.shelfList.get(position).getCart().isEmpty() && !this.shelfList.get(position).isWall){
                    mySetImage(Constants.imgWidth, Constants.imgHeight, "cart_path.png", position);
                }
                this.shelfList.get(position).setCartNumber(cartNum);
            }
        }catch(Exception ex){
            System.err.println("Snaha o změnu zobrazení trasy při posunu vozíku.");
            for(Shelf cell : this.shelfList){
                if(cell.getCartNumber() != -1){
                    cell.setCartNumber(-1);
                    if(cell.getCart().isEmpty() && !cell.isWall){
                        this.imgList.get(cell.getPosition()).getImg().setImage(null);
                    }
                }
            }
        }
    }
    
    /**
     * Metoda slouží k přidání odřádování k textu, pokud se již na výstupu nějaký text vyskytuje.
     * @return string obsahující odřádkování či prázdnou hodnotu
     */
    private String makeVSpace(){
        if(!this.tvOut.getText().isEmpty()){
            return "\n";
        }
        return "";
    }
    
}
