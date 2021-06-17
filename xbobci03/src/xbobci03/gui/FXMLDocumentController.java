/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbobci03.gui;

import xbobci03.process.AStar;
import xbobci03.process.Storage;
import xbobci03.listClasses.CellImageView;
import xbobci03.sharedClasses.Constants;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import xbobci03.listClasses.CellAStar;

/**
 * Jedná se o třídu, jenž se stará o grafickou část aplikace.
 * Inicializuje jednotlivé objekty, reaguje na jejich interaktivní ovládání a volá další třídy, potřebné pro ovládání grafického rozhraní.
 * @author Pavel Bobčík (xbobci03)
 * @author Tomáš Daněk (xdanek23)
 */
public class FXMLDocumentController implements Initializable {
    Storage storage = null;
    ArrayList<CellImageView> imgList = new ArrayList<>();
    boolean loaded = false;;
    
    @FXML
    private GridPane gridPane;
    @FXML
    private TextArea tvIn, tvOut;
    @FXML
    private ImageView cartParking, imgMap;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillImgList();
        this.tvOut.setText(Constants.demo);
        imgMap.setImage(new Image(Constants.pathImg+"map_background.png", Constants.mapWidth, Constants.mapHeight, false, true));
        
        this.storage = new Storage(this.gridPane, this.imgList, this.tvOut);
        this.cartParking.setImage(new Image(Constants.pathImg+"cart01.png", Constants.imgWidth, Constants.imgHeight, false, true));
    }   
    
    /**
    * Metoda slouží k uložení hodnot ImageView, jenž reprezentují plochu skladiště, do arraylistu.
    * Jednotlivé hodnoty získá z mřížky. Nakonec jsou hodnoty seřazeny.
    */
    private void fillImgList(){
        List<Node> listOfChild = this.gridPane.getChildren();
        for(int i = 0; i < listOfChild.size()-1; i++){
            CellImageView img = new CellImageView((ImageView) listOfChild.get(i), i);
            this.imgList.add(img);
        }
        Collections.sort(this.imgList);
    }
    
    /**
     * Metoda slouží k vymazání textu z výstupní konzole.
     * @param event reakce na stisknutí tlačítka myši.
     */
    @FXML
    private void btnCleanTvOutClicked(MouseEvent event) {
        this.tvOut.clear();
    }
    
    /**
     * Metoda slouží k načtení skladiště ze souboru.
     * Provede vynulování plochy a otevření souboru. Pokud se soubor správně otevřel,
     * dojde k načtení dat a uložení do arraylistu. 
     * Pokud i tato operace proběhla v pořádku, dojde k načtení plochy skladiště
     * a nastavení hodnoty loaded na true.
     * @param event reakce na stisknutí tlačítka myši.
     */
    @FXML
    private void btnStorageMapOpenClicked(MouseEvent event) {
        this.storage.nullImages();
        if(this.storage.openFile()){
            if(this.storage.getStorageArray()){
                this.storage.loadStorage();
                this.loaded = true;
            }
        }
    }
    
    /**
     * Metoda slouží k obnovení skladiště do původního stavu. 
     * Vynuluje aktuální plochu a zkontroluje, zda-li bylo skladiště již načteno, pokud ne, 
     * otevře se okno pro načtení.
     * Je-li skladiště načteno, tak dojde k načtení hodnot ze souboru do arraylistu
     * a načtení skladiště na plochu.
     * Dále dojde k vyčištění obou konzolí.
     * @param event reakce na stisknutí tlačítka myši.
     */
    @FXML
    private void btnResetClicked(MouseEvent event) {
        boolean open = false;
        this.storage.nullImages();
        if(this.storage.getFileName().isEmpty()){
            open = this.storage.openFile();
        }else{
           open = true;
        }
        if(open){
            if(this.storage.getStorageArray()){
                this.storage.loadStorage();
                this.loaded = true;
            }
            this.tvOut.clear();
            this.tvIn.clear();
            this.tvOut.setText(Constants.demo);
        }
    }
    
    /**
     * Metoda slouží k zobrazení nápovědy.
     * @param event reakce na stisknutí tlačítka myši.
     */
    @FXML
    private void btnHelpClicked(MouseEvent event) {
        this.tvOut.clear();
        this.tvOut.setText(Constants.help);
    }

    /**
     * Metoda bude sloužit pro ovládání aplikace přes vstupní konzoli.
     * Prozatím nám slouží k výpisu cesty pomocí algoritmu A*.
     * @param event reakce na stisknutí tlačítka myši.
     */
    @FXML
    private void btnRunClicked(MouseEvent event) {
        if(this.loaded){
            AStar aStar = new AStar(this.storage, 1, 1);
            ArrayList<CellAStar> path = aStar.findPath();
            if(path == null){
                System.out.println("Nelze nalézt cestu");
            }else{
                for(CellAStar cas : path){
                    System.out.print("["+cas.x+", "+cas.y+"] ");
                }
                System.out.print("\n Cena: "+path.get(path.size()-1).f+"\n");
            }
        }
    }
    
    /**
     * Metody slouží pro reakci na kliknutí na dané políčko na ploše skladiště.
     * @param event reakce na stisknutí tlačítka myši.
     */
    @FXML
    private void img0x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(0, 0);
    }

    @FXML
    private void img0x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(0, 1);
    }

    @FXML
    private void img0x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(0, 2);
    }

    @FXML
    private void img0x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(0, 3);
    }

    @FXML
    private void img0x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(0, 4);
    }

    @FXML
    private void img0x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(0, 5);
    }

    @FXML
    private void img0x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(0, 6);
    }

    @FXML
    private void img0x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(0, 7);
    }

    @FXML
    private void img0x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(0, 8);
    }

    @FXML
    private void img0x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(0, 9);
    }

    @FXML
    private void img1x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(1, 0);
    }

    @FXML
    private void img1x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(1, 1);
    }

    @FXML
    private void img1x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(1, 2);
    }

    @FXML
    private void img1x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(1, 3);
    }

    @FXML
    private void img1x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(1, 4);
    }

    @FXML
    private void img1x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(1, 5);
    }

    @FXML
    private void img1x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(1, 6);
    }

    @FXML
    private void img1x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(1, 7);
    }

    @FXML
    private void img1x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(1, 8);
    }

    @FXML
    private void img1x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(1, 9);
    }

    @FXML
    private void img2x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(2, 0);
    }

    @FXML
    private void img2x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(2, 1);
    }

    @FXML
    private void img2x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(2, 2);
    }

    @FXML
    private void img2x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(2, 3);
    }

    @FXML
    private void img2x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(2, 4);
    }

    @FXML
    private void img2x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(2, 5);
    }

    @FXML
    private void img2x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(2, 6);
    }

    @FXML
    private void img2x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(2, 7);
    }

    @FXML
    private void img2x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(2, 8);
    }

    @FXML
    private void img2x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(2, 9);
    }

    @FXML
    private void img3x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(3, 0);
    }

    @FXML
    private void img3x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(3, 1);
    }

    @FXML
    private void img3x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(3, 2);
    }

    @FXML
    private void img3x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(3, 3);
    }

    @FXML
    private void img3x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(3, 4);
    }

    @FXML
    private void img3x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(3, 5);
    }

    @FXML
    private void img3x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(3, 6);
    }

    @FXML
    private void img3x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(3, 7);
    }

    @FXML
    private void img3x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(3, 8);
    }

    @FXML
    private void img3x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(3, 9);
    }

    @FXML
    private void img4x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(4, 0);
    }

    @FXML
    private void img4x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(4, 1);
    }

    @FXML
    private void img4x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(4, 2);
    }

    @FXML
    private void img4x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(4, 3);
    }

    @FXML
    private void img4x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(4, 4);
    }

    @FXML
    private void img4x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(4, 5);
    }

    @FXML
    private void img4x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(4, 6);
    }

    @FXML
    private void img4x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(4, 7);
    }

    @FXML
    private void img4x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(4, 8);
    }

    @FXML
    private void img4x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(4, 9);
    }

    @FXML
    private void img5x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(5, 0);
    }

    @FXML
    private void img5x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(5, 1);
    }

    @FXML
    private void img5x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(5, 2);
    }

    @FXML
    private void img5x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(5, 3);
    }

    @FXML
    private void img5x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(5, 4);
    }

    @FXML
    private void img5x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(5, 5);
    }

    @FXML
    private void img5x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(5, 6);
    }

    @FXML
    private void img5x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(5, 7);
    }

    @FXML
    private void img5x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(5, 8);
    }

    @FXML
    private void img5x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(5, 9);
    }

    @FXML
    private void img6x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(6, 0);
    }

    @FXML
    private void img6x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(6, 1);
    }

    @FXML
    private void img6x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(6, 2);
    }

    @FXML
    private void img6x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(6, 3);
    }

    @FXML
    private void img6x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(6, 4);
    }

    @FXML
    private void img6x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(6, 5);
    }

    @FXML
    private void img6x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(6, 6);
    }

    @FXML
    private void img6x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(6, 7);
    }

    @FXML
    private void img6x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(6, 8);
    }

    @FXML
    private void img6x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(6, 9);
    }

    @FXML
    private void img7x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(7, 0);
    }

    @FXML
    private void img7x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(7, 1);
    }

    @FXML
    private void img7x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(7, 2);
    }

    @FXML
    private void img7x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(7, 3);
    }

    @FXML
    private void img7x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(7, 4);
    }

    @FXML
    private void img7x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(7, 5);
    }

    @FXML
    private void img7x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(7, 6);
    }

    @FXML
    private void img7x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(7, 7);
    }

    @FXML
    private void img7x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(7, 8);
    }

    @FXML
    private void img7x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(7, 9);
    }

    @FXML
    private void img8x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(8, 0);
    }

    @FXML
    private void img8x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(8, 1);
    }

    @FXML
    private void img8x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(8, 2);
    }

    @FXML
    private void img8x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(8, 3);
    }

    @FXML
    private void img8x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(8, 4);
    }

    @FXML
    private void img8x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(8, 5);
    }

    @FXML
    private void img8x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(8, 6);
    }

    @FXML
    private void img8x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(8, 7);
    }

    @FXML
    private void img8x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(8, 8);
    }

    @FXML
    private void img8x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(8, 9);
    }

    @FXML
    private void img9x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(9, 0);
    }

    @FXML
    private void img9x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(9, 1);
    }

    @FXML
    private void img9x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(9, 2);
    }

    @FXML
    private void img9x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(9, 3);
    }

    @FXML
    private void img9x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(9, 4);
    }

    @FXML
    private void img9x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(9, 5);
    }

    @FXML
    private void img9x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(9, 6);
    }

    @FXML
    private void img9x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(9, 7);
    }

    @FXML
    private void img9x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(9, 8);
    }

    @FXML
    private void img9x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(9, 9);
    }

    @FXML
    private void img10x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(10, 0);
    }

    @FXML
    private void img10x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(10, 1);
    }

    @FXML
    private void img10x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(10, 2);
    }

    @FXML
    private void img10x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(10, 3);
    }

    @FXML
    private void img10x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(10, 4);
    }

    @FXML
    private void img10x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(10, 5);
    }

    @FXML
    private void img10x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(10, 6);
    }

    @FXML
    private void img10x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(10, 7);
    }

    @FXML
    private void img10x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(10, 8);
    }

    @FXML
    private void img10x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(10, 9);
    }

    @FXML
    private void img11x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(11, 0);
    }

    @FXML
    private void img11x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(11, 1);
    }

    @FXML
    private void img11x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(11, 2);
    }

    @FXML
    private void img11x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(11, 3);
    }

    @FXML
    private void img11x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(11, 4);
    }

    @FXML
    private void img11x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(11, 5);
    }

    @FXML
    private void img11x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(11, 6);
    }

    @FXML
    private void img11x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(11, 7);
    }

    @FXML
    private void img11x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(11, 8);
    }

    @FXML
    private void img11x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(11, 9);
    }

    @FXML
    private void img12x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(12, 0);
    }

    @FXML
    private void img12x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(12, 1);
    }

    @FXML
    private void img12x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(12, 2);
    }

    @FXML
    private void img12x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(12, 3);
    }

    @FXML
    private void img12x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(12, 4);
    }

    @FXML
    private void img12x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(12, 5);
    }

    @FXML
    private void img12x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(12, 6);
    }

    @FXML
    private void img12x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(12, 7);
    }

    @FXML
    private void img12x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(12, 8);
    }

    @FXML
    private void img12x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(12, 9);
    }

    @FXML
    private void img13x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(13, 0);
    }

    @FXML
    private void img13x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(13, 1);
    }

    @FXML
    private void img13x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(13, 2);
    }

    @FXML
    private void img13x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(13, 3);
    }

    @FXML
    private void img13x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(13, 4);
    }

    @FXML
    private void img13x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(13, 5);
    }

    @FXML
    private void img13x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(13, 6);
    }

    @FXML
    private void img13x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(13, 7);
    }

    @FXML
    private void img13x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(13, 8);
    }

    @FXML
    private void img13x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(13, 9);
    }

    @FXML
    private void img14x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(14, 0);
    }

    @FXML
    private void img14x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(14, 1);
    }

    @FXML
    private void img14x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(14, 2);
    }

    @FXML
    private void img14x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(14, 3);
    }

    @FXML
    private void img14x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(14, 4);
    }

    @FXML
    private void img14x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(14, 5);
    }

    @FXML
    private void img14x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(14, 6);
    }

    @FXML
    private void img14x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(14, 7);
    }

    @FXML
    private void img14x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(14, 8);
    }

    @FXML
    private void img14x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(14, 9);
    }

    @FXML
    private void img15x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(15, 0);
    }

    @FXML
    private void img15x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(15, 1);
    }

    @FXML
    private void img15x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(15, 2);
    }

    @FXML
    private void img15x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(15, 3);
    }

    @FXML
    private void img15x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(15, 4);
    }

    @FXML
    private void img15x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(15, 5);
    }

    @FXML
    private void img15x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(15, 6);
    }

    @FXML
    private void img15x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(15, 7);
    }

    @FXML
    private void img15x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(15, 8);
    }

    @FXML
    private void img15x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(15, 9);
    }

    @FXML
    private void img16x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(16, 0);
    }
    
    @FXML
    private void img16x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(16, 1);
    }

    @FXML
    private void img16x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(16, 2);
    }

    @FXML
    private void img16x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(16, 3);
    }

    @FXML
    private void img16x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(16, 4);
    }

    @FXML
    private void img16x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(16, 5);
    }

    @FXML
    private void img16x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(16, 6);
    }

    @FXML
    private void img16x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(16, 7);
    }

    @FXML
    private void img16x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(16, 8);
    }

    @FXML
    private void img16x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(16, 9);
    }

    @FXML
    private void img17x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(17, 0);
    }

    @FXML
    private void img17x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(17, 1);
    }

    @FXML
    private void img17x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(17, 2);
    }

    @FXML
    private void img17x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(17, 3);
    }

    @FXML
    private void img17x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(17, 4);
    }

    @FXML
    private void img17x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(17, 5);
    }

    @FXML
    private void img17x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(17, 6);
    }

    @FXML
    private void img17x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(17, 7);
    }

    @FXML
    private void img17x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(17, 8);
    }

    @FXML
    private void img17x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(17, 9);
    }

    @FXML
    private void img18x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(18, 0);
    }

    @FXML
    private void img18x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(18, 1);
    }

    @FXML
    private void img18x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(18, 2);
    }

    @FXML
    private void img18x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(18, 3);
    }

    @FXML
    private void img18x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(18, 4);
    }

    @FXML
    private void img18x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(18, 5);
    }

    @FXML
    private void img18x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(18, 6);
    }

    @FXML
    private void img18x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(18, 7);
    }

    @FXML
    private void img18x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(18, 8);
    }

    @FXML
    private void img18x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(18, 9);
    }

    @FXML
    private void img19x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(19, 0);
    }

    @FXML
    private void img19x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(19, 1);
    }

    @FXML
    private void img19x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(19, 2);
    }

    @FXML
    private void img19x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(19, 3);
    }

    @FXML
    private void img19x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(19, 4);
    }

    @FXML
    private void img19x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(19, 5);
    }

    @FXML
    private void img19x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(19, 6);
    }

    @FXML
    private void img19x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(19, 7);
    }

    @FXML
    private void img19x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(19, 8);
    }

    @FXML
    private void img19x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(19, 9);
    }

    @FXML
    private void img20x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(20, 0);
    }

    @FXML
    private void img20x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(20, 1);
    }

    @FXML
    private void img20x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(20, 2);
    }

    @FXML
    private void img20x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(20, 3);
    }

    @FXML
    private void img20x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(20, 4);
    }

    @FXML
    private void img20x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(20, 5);
    }

    @FXML
    private void img20x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(20, 6);
    }

    @FXML
    private void img20x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(20, 7);
    }

    @FXML
    private void img20x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(20, 8);
    }

    @FXML
    private void img20x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(20, 9);
    }

    @FXML
    private void img21x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(21, 0);
    }

    @FXML
    private void img21x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(21, 1);
    }

    @FXML
    private void img21x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(21, 2);
    }

    @FXML
    private void img21x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(21, 3);
    }

    @FXML
    private void img21x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(21, 4);
    }

    @FXML
    private void img21x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(21, 5);
    }

    @FXML
    private void img21x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(21, 6);
    }

    @FXML
    private void img21x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(21, 7);
    }

    @FXML
    private void img21x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(21, 8);
    }

    @FXML
    private void img21x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(21, 9);
    }

    @FXML
    private void img22x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(22, 0);
    }

    @FXML
    private void img22x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(22, 1);
    }

    @FXML
    private void img22x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(22, 2);
    }

    @FXML
    private void img22x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(22, 3);
    }

    @FXML
    private void img22x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(22, 4);
    }

    @FXML
    private void img22x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(22, 5);
    }

    @FXML
    private void img22x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(22, 6);
    }

    @FXML
    private void img22x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(22, 7);
    }

    @FXML
    private void img22x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(22, 8);
    }

    @FXML
    private void img22x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(22, 9);
    }

    @FXML
    private void img23x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(23, 0);
    }

    @FXML
    private void img23x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(23, 1);
    }

    @FXML
    private void img23x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(23, 2);
    }

    @FXML
    private void img23x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(23, 3);
    }

    @FXML
    private void img23x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(23, 4);
    }

    @FXML
    private void img23x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(23, 5);
    }

    @FXML
    private void img23x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(23, 6);
    }

    @FXML
    private void img23x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(23, 7);
    }

    @FXML
    private void img23x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(23, 8);
    }

    @FXML
    private void img23x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(23, 9);
    }

    @FXML
    private void img24x0Clicked(MouseEvent event) {
        this.storage.onCellClickListener(24, 0);
    }

    @FXML
    private void img24x1Clicked(MouseEvent event) {
        this.storage.onCellClickListener(24, 1);
    }

    @FXML
    private void img24x2Clicked(MouseEvent event) {
        this.storage.onCellClickListener(24, 2);
    }

    @FXML
    private void img24x3Clicked(MouseEvent event) {
        this.storage.onCellClickListener(24, 3);
    }

    @FXML
    private void img24x4Clicked(MouseEvent event) {
        this.storage.onCellClickListener(24, 4);
    }

    @FXML
    private void img24x5Clicked(MouseEvent event) {
        this.storage.onCellClickListener(24, 5);
    }

    @FXML
    private void img24x6Clicked(MouseEvent event) {
        this.storage.onCellClickListener(24, 6);
    }

    @FXML
    private void img24x7Clicked(MouseEvent event) {
        this.storage.onCellClickListener(24, 7);
    }

    @FXML
    private void img24x8Clicked(MouseEvent event) {
        this.storage.onCellClickListener(24, 8);
    }

    @FXML
    private void img24x9Clicked(MouseEvent event) {
        this.storage.onCellClickListener(24, 9);
    }

    
}
