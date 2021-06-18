/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xbobci03.process;

import xbobci03.listClasses.ReturnHolder;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import xbobci03.listClasses.Cart;
import xbobci03.listClasses.CellAStar;
import xbobci03.listClasses.CellImageView;
import xbobci03.listClasses.RequestList;
import xbobci03.listClasses.Shelf;
import xbobci03.sharedClasses.Constants;
import xbobci03.sharedClasses.SharedMethods;

/**
 * Třída sloužící pro obsluhu vozíků.
 * @author Pavel Bobčík (xbobci03)
 */
public class Carts {
    TextArea tvOut;
    Storage storage;
    ExecutorService cart;
    ExecutorService spawner;
    ReturnHolder rHolder;
    ArrayList<CellImageView> imgList;
    int cartNumber = 0;
    boolean reset;
    double speed;
    
    /**
     * Konstruktor třídy Storage.
     * Dojde k deklaraci proměnných.
     * @param tvOut odkaz na objekt výstupní konzole
     * @param storage odkaz na třídu Storage
     * @param rHolder pomocná třída obsahující výstupní text a list se zbožím k vyzvednutí
     * @param imgList arraylist načtených ImageView plochy skladiště
     * @param speed nastavení rychlosti
     */
    public Carts(TextArea tvOut, Storage storage, ReturnHolder rHolder, ArrayList<CellImageView> imgList, double speed){
        this.tvOut = tvOut;
        this.storage = storage;
        this.rHolder = rHolder;
        this.imgList = imgList;
        this.cart = SharedMethods.initThreadExecutor(5);
        this.spawner = SharedMethods.initThreadExecutor(1);
        this.reset = false;
        this.speed = speed;
    }

    /**
     * Metoda slouží jako setter pro rychlost.
     * @param speed rychlost provádění úkonů
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    /**
     * Metoda slouží k ukončení běžících vláken.
     * Je volána z InputController při stisknutí tlačítka Obnovit či Nahrát sklad.
     */
    public void threadShutDownNow(){
        this.reset = true;
        this.cart.shutdownNow();
        this.spawner.shutdown();
    }
    
    /**
     * Metoda slouží k vyslání a pohybu vozíků.
     * Metoda postupně zpracovává úkony pohybu. Jako první v cyklu zpracuje pohyb vozíku po mapě, dokud má požadavky na naložení zboží.
     * Poté zpracuje požadavek k pohybu k výdeji, obsahuje-li vozík nějaké zboží. Je-li vozík prázdný, vykoná se pohyb na parkoviště.
     * @param requestArr pole s požadovaným zbožím
     * @param customerCounter číslo zákazníka
     */
    public void sendCart(ArrayList<RequestList> requestArr, int customerCounter){
        spawner.submit(new Runnable(){
            public void run(){
                ArrayList<Cart> cartContains = makeQueue(requestArr);
                int sizeCC = cartContains.size();
                for(int i = 0; i < sizeCC; i++){
                    try{
                        Thread.sleep((long) ((int) Constants.spawnTime / speed));  
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                    cart.submit(new Runnable(){
                        public void run(){
                            Cart cartCon = cartContains.remove(0);
                            int prevPos = -1;
                            int actualPos = -1;
                            boolean updatePath = false;

                            while(!cartCon.getList().isEmpty()){
                                ReturnValue returnV = goForGoods(cartCon, actualPos, prevPos, updatePath);
                                updatePath = returnV.isUpdatePath();
                                actualPos = returnV.getActualPos();
                                prevPos = returnV.getPrevPos();
                            }
                            if(!cartCon.getContains().isEmpty()){
                                ReturnValue returnV = goToIssue(cartCon, actualPos, prevPos, updatePath, customerCounter);
                                updatePath = returnV.isUpdatePath();
                                actualPos = returnV.getActualPos();
                                prevPos = returnV.getPrevPos();
                            }
                            goToParkingLot(cartCon, actualPos, prevPos, updatePath);
                        }
                    });
                }
            }
        });
    }
    
    /**
     * Metoda zprostředující pohyb vozíku po mapě při nakládání zboží.
     * @param cartCon pomocná třída vozíku, nad kterým se vykonává pohyb
     * @param actualPos aktuální pozice vozíku
     * @param prevPos předchozí pozice vozíku
     * @param updatePath pravdivostní hodnota, zda-li aktualizovat zobrazenou cestu
     * @return returnV pomocnou třídu obsahující aktuální hodnoty pro návrat
     */
    private ReturnValue goForGoods(Cart cartCon, int actualPos, int prevPos, boolean updatePath){
        RequestList item = cartCon.getList().remove(0);
        int xBeg = Constants.xParking;
        int yBeg = Constants.yParking;
        
        if(actualPos > -1){
            ArrayList<Integer> coor = SharedMethods.getCoordinations(actualPos);
            xBeg = coor.get(0);
            yBeg = coor.get(1);
        }
        cartCon.setFinX(item.getX());
        cartCon.setFinY(item.getY());
        
        ReturnValue returnV = go(xBeg, yBeg, item.getX(), item.getY(), actualPos, prevPos, item, cartCon, false, false, updatePath);
        returnV.setUpdatePath(false);
        prevPos = returnV.getPrevPos();
        
        if(returnV.getWhileOper() == -1){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    printOutMsg("Omlouváme se, zboží '" + item.getGoods() + "' je dočasně nedostupné.\n");
                }
            });
            returnV.setUpdatePath(true);
            if(prevPos <= -1){
                try{  
                    Thread.sleep((long) ((int) Constants.spawnTime / speed));
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
            return returnV;
        }else if(returnV.getWhileOper() == -2){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    printOutMsg("Omlouváme se, zboží '" + item.getGoods() + "' je dočasně nedostupné kvůli uzavření skladiště.\n");
                }
            });
        }
        
        try{
            Thread.sleep((long) ((int) Constants.inOutTime / speed));  
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        
        if(returnV.getWhileOper() == 0){
            cartCon.setContains(item);
        }
        
        return returnV;
    }
    
    /**
     * Metoda zprostředující pohyb vozíku k výdeji.
     * @param cartCon pomocná třída vozíku, nad kterým se vykonává pohyb
     * @param actualPos aktuální pozice vozíku
     * @param prevPos předchozí pozice vozíku
     * @param updatePath pravdivostní hodnota, zda-li aktualizovat zobrazenou cestu
     * @param customerCounter číslo zákazníka
     * @return returnV pomocnou třídu obsahující aktuální hodnoty pro návrat
     */
    private ReturnValue goToIssue(Cart cartCon, int actualPos, int prevPos, boolean updatePath, int customerCounter){
        ArrayList<Integer> coor = SharedMethods.getCoordinations(actualPos);
        cartCon.setFinX(Constants.xIssue);
        cartCon.setFinY(Constants.yIssue);
        
        ReturnValue returnV = go(coor.get(0), coor.get(1), Constants.xIssue, Constants.yIssue, actualPos, prevPos, null, cartCon,true, false, updatePath);  
        returnV.setUpdatePath(false);
        actualPos = returnV.getActualPos();
        prevPos = returnV.getPrevPos();
        
        while(returnV.getWhileOper() == -3){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    printOutMsg("Omlouváme se, ale výdej je dočasně uzavřen.\n");
                }
            });
            updatePath = true;
            
            try{  
                Thread.sleep((long) ((int) Constants.waitTime / speed));  
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
            
            coor = SharedMethods.getCoordinations(actualPos);
            returnV = go(coor.get(0), coor.get(1), Constants.xIssue, Constants.yIssue, actualPos, prevPos, null, cartCon, true, false, updatePath); 
            updatePath = false;
            actualPos = returnV.getActualPos();
            prevPos = returnV.getPrevPos();
        }

        try{  
            Thread.sleep((long) ((int) Constants.inOutTime / speed));  
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        
        String outputText = "Výdej zboží pro zákazníka č." + customerCounter + ":";
        for(RequestList goods : cartCon.getContains()){
            outputText += "\n +  " + goods.getGoods() + " (" + goods.getAmount() + " ks),";
        }
        outputText = outputText.replaceFirst(",$", ".\n");
        final String outText = outputText;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                printOutMsg(outText);
            }
        });
        
        cartCon.deleteContains();
        
        return returnV;
    }
    
    
    /**
     * Metoda zprostředující pohyb vozíku na parkoviště.
     * @param cartCon pomocná třída vozíku, nad kterým se vykonává pohyb
     * @param actualPos aktuální pozice vozíku
     * @param prevPos předchozí pozice vozíku
     * @param updatePath pravdivostní hodnota, zda-li aktualizovat zobrazenou cestu
     */
    private void goToParkingLot(Cart cartCon, int actualPos, int prevPos, boolean updatePath){
        ArrayList<Integer> coor = SharedMethods.getCoordinations(actualPos);
        cartCon.setFinX(Constants.xParking);
        cartCon.setFinY(Constants.yParking);
        
        ReturnValue returnV = go(coor.get(0), coor.get(1), Constants.xParking, Constants.yParking, actualPos, prevPos, null, cartCon, false, true, updatePath);
        returnV.setUpdatePath(false);
        actualPos = returnV.getActualPos();
        prevPos = returnV.getPrevPos();
        
        while(returnV.getWhileOper() == -2){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    printOutMsg("Omlouváme se, ale skladiště je dočasně uzavřeno.\n");
                }
            });
            updatePath = true;
            
            try{  
                Thread.sleep((long) ((int) Constants.waitTime / speed));  
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
            
            coor = SharedMethods.getCoordinations(actualPos);
            returnV = go(coor.get(0), coor.get(1), Constants.xParking, Constants.yParking, actualPos, prevPos, null, cartCon, false, true, updatePath);  
            updatePath = false;
            actualPos = returnV.getActualPos();
            prevPos = returnV.getPrevPos();
        }

        try{  
            Thread.sleep((long) ((int) Constants.stepTime / speed));  
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        } 
        
        Shelf cell = storage.getShelfList().get(actualPos);
        cell.getCart().remove(cartCon);
        ImageView img = imgList.get(actualPos).getImg();
        if(cell.getCart().isEmpty()){
            if(cell.getCartNumber() != -1){
                if(cell.getCartNumber() == cartCon.getNumber()){
                    updateImgInUIThread(false, "", img);
                    cell.setCartNumber(-1);
                }else{
                    updateImgInUIThread(true, "cart_path.png", img);
                }
            }else{
                updateImgInUIThread(false, "", img);
            }
        }else{
            updateImgInUIThread(true, "cart.png", img);
        }
    }
    
    /**
     * Metoda pro získání celkového množství kusů zboží k vydání.
     * @param requestQueue pole obsahující požadované zboží
     * @return celkové množství
     */
    private int getTotalAmount(ArrayList<RequestList> requestQueue){
        int total = 0;
        for(RequestList item : requestQueue){
            total += item.getAmount();
        }
        return total;
    }
    
    /**
     * Metoda sloužící pro vytvoření fronty vozíků k vykonání příkazu vydej.
     * Rozdělní požadované zboží do jednotlivých vozíků po pěti kusech.
     * @param requestArr pole obsahující požadované zboží
     * @return cartContains pole obsahující frontu vozíků
     */
    private ArrayList<Cart> makeQueue(ArrayList<RequestList> requestArr){
        int total = getTotalAmount(requestArr);
        ArrayList<Cart> cartContains = new ArrayList<>();
        while(total > 0){
            Cart cart = new Cart();
            for(RequestList item : requestArr){
                if(item.getAmount() <= 0){
                    continue;
                }else if(item.getAmount() < cart.getSpace()){
                    RequestList cartContents = new RequestList(item.getGoods(), item.getAmount(), item.getX(), item.getY());
                    cart.setList(cartContents);
                    cart.setSpace(cart.getSpace() - item.getAmount());
                    cart.setFinX(item.getX());
                    cart.setFinY(item.getY());
                    total -= item.getAmount();
                    item.setAmount(0);
                    continue;
                }else{
                    RequestList cartContents = new RequestList(item.getGoods(), cart.getSpace(), item.getX(), item.getY());
                    cart.setList(cartContents);
                    cart.setFinX(item.getX());
                    cart.setFinY(item.getY());
                    item.setAmount(item.getAmount() - cart.getSpace());
                    total -= cart.getSpace();
                    cart.setSpace(0);
                    break;
                }
            }
            cart.setNumber(cartNumber);
            if(cart.getSpace() < 5){
                cartContains.add(cart);
            }
            this.cartNumber ++;
            if(cartNumber == -1){
                cartNumber = 0;
            }
        }
        return cartContains;
    }
    
    /**
     * Metoda jenž se stará o vykreslení pohybu vozíku na mapě.
     * @param xBeg počáteční X souřadnice
     * @param yBeg počáteční Y souřadnice
     * @param xFin konečná X souřadnice
     * @param yFin konečná Y souřadnice
     * @param actualPos aktuální pozice vozíku
     * @param prevPos předchozí pozice vozíku
     * @param item informace o zboží k vyzvednutí
     * @param cartCon informace o vozíku
     * @param issue pravdivostní hodnota, zda-li míříme k výdeji
     * @param parking pravdivostní hodnota, zda-li míříme na parkoviště
     * @param updatePath pravdivostní hodnota, zda-li aktualizovat zobrazení cesty
     * @return returnV pomocnou třídu obsahující aktuální hodnoty pro návrat
     */
    private ReturnValue go(int xBeg, int yBeg, int xFin, int yFin, int actualPos, int prevPos, RequestList item, Cart cartCon, boolean issue, boolean parking, boolean updatePath){
        AStar aStar = new AStar(this.storage, xBeg, yBeg, xFin, yFin);
        ArrayList<CellAStar> pathArr = aStar.findPath();
        
        if(pathArr == null){
            return getWhileOp(actualPos, prevPos, item, issue, parking);
        }else{
            while(!pathArr.isEmpty()){
                cartCon.setPath(pathArr);
                CellAStar path = pathArr.remove(0);
                int prevPrevPos = prevPos;
                prevPos = actualPos;
                actualPos = SharedMethods.getIndex(path.getX(), path.getY());
                
                if(storage.getShelfList().get(actualPos).isWall){
                    ReturnValue ifWall = updatePathIfWall(cartCon, prevPos, prevPrevPos, xFin, yFin, item, issue, parking);
                    if(ifWall.getWhileOper() < 0){
                        return ifWall;
                    }
                    
                    pathArr = ifWall.getPathArr();
                    actualPos = ifWall.getActualPos();

                    int cartNum = removePathVis(cartCon);
                    setPathVis(cartCon, cartNum, pathArr);
                    
                    continue;
                }else if(updatePath){
                    int cartNum = removePathVis(cartCon);
                    setPathVis(cartCon, cartNum, pathArr);
                    updatePath = false;
                }
                
                if(prevPos > -1){
                    try{
                        Thread.sleep((long) ((int) Constants.stepTime / speed));  
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                    
                    if(storage.getShelfList().get(actualPos).isWall){
                        ReturnValue ifWall = updatePathIfWall(cartCon, prevPos, prevPrevPos, xFin, yFin, item, issue, parking);
                        if(ifWall.getWhileOper() < 0){
                            return ifWall;
                        }

                        pathArr = ifWall.getPathArr();
                        actualPos = ifWall.getActualPos();

                        int cartNum = removePathVis(cartCon);
                        setPathVis(cartCon, cartNum, pathArr);

                        continue;
                    }
                    
                    Shelf cell = storage.getShelfList().get(prevPos);
                    cell.getCart().remove(cartCon);
                    ImageView img = imgList.get(prevPos).getImg();
                    if(cell.getCart().isEmpty()){
                        if(cell.getCartNumber() != -1){
                            if(cell.getCartNumber() == cartCon.getNumber()){
                                updateImgInUIThread(false, "", img);
                                cell.setCartNumber(-1);
                            }else{
                                updateImgInUIThread(true, "cart_path.png", img);
                            }
                        }else{
                            updateImgInUIThread(false, "", img);
                        }
                    }else{
                        updateImgInUIThread(true, "cart.png", img);
                    }
                }
                Shelf cell = storage.getShelfList().get(actualPos);
                cell.setCart(cartCon);
                if(cell.getCartNumber() == cartCon.getNumber()){
                    cell.setCartNumber(-1);
                }
                ImageView img = imgList.get(actualPos).getImg();
                updateImgInUIThread(true, "cart.png", img);
            }
            
            if(prevPos > -1 && storage.getShelfList().get(actualPos).getCartNumber() == cartCon.getNumber()){
                storage.getShelfList().get(actualPos).setCartNumber(-1);
            }
        }
        ReturnValue returnV = new ReturnValue(actualPos, prevPos, 0, false, null);
        return returnV;
    }
    
    /**
     * Metoda sloužící k odstranění zobrazené cesty vozíku.
     * @param cartCon informace o vozíku
     * @return vrací číslo vozíku
     */
    private int removePathVis(Cart cartCon){
        int cartNum = -1;
        for(Shelf pathVis : this.storage.getShelfList()){
            if(pathVis.getCartNumber() == cartCon.getNumber()){
                cartNum = pathVis.getCartNumber();
                pathVis.setCartNumber(-1);
                if(pathVis.getCart().isEmpty() && !pathVis.isWall){
                    ImageView img = this.imgList.get(pathVis.getPosition()).getImg();
                    updateImgInUIThread(false, "", img);
                }
            }
        }
        
        return cartNum;
    }
    
    /**
     * Metoda sloužící k zobrazení cesty vozíku.
     * @param cartCon inforamce o vozíku
     * @param cartNum číslo vozíku
     * @param pathArr cesta vozíku
     */
    private void setPathVis(Cart cartCon, int cartNum, ArrayList<CellAStar> pathArr){
        if(cartCon.getNumber() == cartNum){
            for(CellAStar pathVis : pathArr){
                int position = SharedMethods.getIndex(pathVis.getX(), pathVis.getY());
                if(storage.getShelfList().get(position).getCart().isEmpty() && !storage.getShelfList().get(position).isWall){
                    ImageView img = this.imgList.get(position).getImg();
                    updateImgInUIThread(true, "cart_path.png", img);
                }
                storage.getShelfList().get(position).setCartNumber(cartCon.getNumber());
            }
        }
    }
    
    /**
     * Metoda sloužící k aktualizování hodnot, pokud narazí na překážku
     * @param cartCon informace o vozíku
     * @param prevPos předchozí pozice
     * @param prevPrevPos pozice o dvě políčka zpět
     * @param xFin koncová pozice X
     * @param yFin koncová pozice Y
     * @param item informace o zboží k naložení
     * @param issue pravdivostní hodnota, zda-li míříme k výdeji
     * @param parking pravdivostní hodnota, zda-li míříme na parkoviště
     * @return returnV pomocnou třídu obsahující aktuální hodnoty pro návrat 
     */
    private ReturnValue updatePathIfWall(Cart cartCon, int prevPos, int prevPrevPos, int xFin, int yFin, RequestList item, boolean issue, boolean parking){
        ArrayList<Integer> coor = SharedMethods.getCoordinations(prevPos);
        if(item != null){
            xFin = item.getX();
            yFin = item.getY();
        }

        AStar newPath = new AStar(storage, coor.get(0), coor.get(1), xFin, yFin);
        ArrayList<CellAStar> pathArr = newPath.findPath();
        int actualPos = prevPos;

        if(pathArr == null){
            return getWhileOp(prevPos, prevPrevPos, item, issue, parking);
        }
        
        ReturnValue returnV = new ReturnValue(actualPos, prevPos, 0, false, pathArr);
        return returnV;
    }
    
    /**
     * Pomocná metoda pro aktualizování ImageView v jádře UI.
     * @param setImg pravdivostní hodnota, zda-li budeme nastavovat obrázek
     * @param imgName název obrázku
     * @param image odkaz na objekt ImageView
     */
    private void updateImgInUIThread(boolean setImg, String imgName, ImageView image){
        final ImageView img = image;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(!reset){
                    if(setImg){
                        img.setImage(new Image(Constants.pathImg+imgName, Constants.imgWidth, Constants.imgHeight, false, true));
                    }else{
                        img.setImage(null);
                    }
                }
            }
        });
    }
    
    /**
     * Metoda pro získání operační hodnoty, podle které budeme řídit while (continue/break).
     * Metoda na základě pozice vozíku a uzavření cest rozhodne o tom, jak se zachová while cyklus při pohybu vozíku.
     * @param actualPos aktuální pozice vozíku
     * @param prevPos předchozí pozice vozíku
     * @param item informace o zboží k vyzvednutí
     * @param issue pravdivostní hodnota, zda-li míříme k výdeji
     * @param parking pravdivostní hodnota, zda-li míříme na parkoviště
     * @return returnV pomocnou třídu obsahující aktuální hodnoty pro návrat 
     */
    private ReturnValue getWhileOp(int actualPos, int prevPos, RequestList item, boolean issue, boolean parking){
        int position = SharedMethods.getIndex(Constants.xParking, Constants.yParking);
        if((prevPos == -1 && this.storage.shelfList.get(position).isWall) || parking){
            ReturnValue returnV = new ReturnValue(actualPos, prevPos, -2, false, null);
            return returnV;
        }else if(issue){
            ReturnValue returnV = new ReturnValue(actualPos, prevPos, -3, false, null);
            return returnV;
        }
        
        int shelfPosition = SharedMethods.getIndex(item.getX(), item.getY());
        if(this.storage.getShelfList().get(shelfPosition).getAmount() <= 0){
            ImageView img = imgList.get(shelfPosition).getImg();
            updateImgInUIThread(true, "pallet_box.png", img);
        }
        this.storage.getShelfList().get(shelfPosition).returnAmount(item.getAmount());
        ReturnValue returnV = new ReturnValue(actualPos, prevPos, -1, false, null);
        return returnV;
    }
    
    /**
     * Pomocná metoda k vypsání výstupu do výstupní konzole.
     * @param msg textový řetězec k vypsání
     */
    private void printOutMsg(String msg){
        String outputText = msg + makeVSpace("");
        String[] lines = outputText.split("\n");
        if(lines.length > 500){
                outputText = SharedMethods.reduceLines(lines);
            }
        if(!this.reset){
            this.tvOut.setText(outputText + this.tvOut.getText());
        }
    }
    
    /**
     * Metoda slouží k přidání odřádování k textu, pokud se již na výstupu nějaký text vyskytuje.
     * @param output proměnné obsahuje připravený výstupní text nebo je prázdná
     * @return string obsahující odřádkování či prázdnou hodnotu
     */
    private String makeVSpace(String output){
        if(!this.tvOut.getText().isEmpty() || !output.isEmpty()){
            return "\n";
        }
        return "";
    }
}

/**
 * Pomocná třída sloužící k uchování a vrácení více hodnot.
 * @author Pavel Bobčík (xbobci03)
 */
class ReturnValue{
    int actualPos;
    int prevPos;
    int whileOper;
    boolean updatePath;
    ArrayList<CellAStar> pathArr;

    /**
     * Konstruktor třídy ReturnValue.
     * Dojde k deklaraci proměnných.
     * @param actualPos aktuální pozice
     * @param prevPos předchozí pozice
     * @param whileOper operační hodnota pro while cyklus
     * @param updatePath pravdivostní hodnota, zda-li aktualizovat zobrazenou cestu
     * @param pathArr pole cesty
     */
    public ReturnValue(int actualPos, int prevPos, int whileOper, boolean updatePath, ArrayList<CellAStar> pathArr) {
        this.actualPos = actualPos;
        this.prevPos = prevPos;
        this.whileOper = whileOper;
        this.updatePath = updatePath;
        this.pathArr = pathArr;
    }

    /**
     * Metoda sloužící jako getter aktuální pozice vozíku.
     * @return actualPos aktuální pozici vozíku
     */
    public int getActualPos() {
        return actualPos;
    }

    /**
     * Metoda sloužící jako getter předchozí pozice vozíku.
     * @return prevPos předchozí pozici vozíku
     */
    public int getPrevPos() {
        return prevPos;
    }

    /**
     * Metoda sloužící jako getter operační hodnoty pro while cyklus.
     * @return whileOper operační hodnotu pro while cyklus
     */
    public int getWhileOper() {
        return whileOper;
    }

    /**
     * Metoda sloužící jako getter pravdivostní hodnoty aktualizace zobrazené cesty.
     * @return updatePath pravdivostní hodnotu aktualizace zobrazené cesty
     */
    public boolean isUpdatePath() {
        return updatePath;
    }

    /**
     * Metoda sloužící jako setter pravdivostní hodnoty aktualizace zobrazené cesty.
     * @param updatePath pravdivostní hodnota aktualizace zobrazené cesty
     */
    public void setUpdatePath(boolean updatePath) {
        this.updatePath = updatePath;
    }

    /**
     * Metoda sloužící jako getter pole cesty.
     * @return pathArr pole cesty
     */
    public ArrayList<CellAStar> getPathArr() {
        return pathArr;
    }

    /**
     * Metoda sloužící jako setter pole cesty.
     * @param pathArr pole cesty
     */
    public void setPathArr(ArrayList<CellAStar> pathArr) {
        this.pathArr = pathArr;
    }
    
    

}
