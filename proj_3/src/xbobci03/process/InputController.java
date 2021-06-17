package xbobci03.process;

import xbobci03.listClasses.ReturnHolder;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import xbobci03.listClasses.CellImageView;
import xbobci03.listClasses.RequestList;
import xbobci03.listClasses.Shelf;
import xbobci03.sharedClasses.Constants;
import xbobci03.sharedClasses.SharedMethods;

/**
 * Třída sloužící pro zpracováné požadavků a případnou inicializaci třídy carts.
 * Dojde k rozpoznání příkazů a vykonání odpovídajících reakcí. V případě, že se jedná o příkaz
 * vydej, dojde rovněž k nalezení požadovaného zboží, jenž je uloženo do pole a předáno třídě carts.
 * @author Pavel Bobčík (xbobci03)
 * @author Tomáš Daněk (xdanek23)
 */
public class InputController{
    TextArea tvIn, tvOut;
    Storage storage;
    boolean loaded;
    ExecutorService cashier;
    ExecutorService warehouseman;
    ArrayList<ArrayList<String>> commandQueue;
    ArrayList<ArrayList<RequestList>> requestQueue;
    ArrayList<CellImageView> imgList;
    Carts carts;
    int customerCounter;
    boolean reset;
    
    /**
     * Konstruktor třídy InputController.
     * Slouží k deklaraci hodnot.
     * @param tvIn odkaz na objekt TextArea reprezentující vstupní konzoli
     * @param tvOut odkaz na objekt TextArea reprezentující výstupní konzoli
     * @param storage odkaz na třídu Storage
     * @param loaded pravdivostní hodnota nesoucí informaci, zda-li byla načtena mapa
     * @param imgList pole obsahující buňky mapy
     * @param speed nastavení rychlosti
     */
    public InputController(TextArea tvIn, TextArea tvOut, Storage storage, boolean loaded, ArrayList<CellImageView> imgList, double speed){
        this.tvIn = tvIn;
        this.tvOut = tvOut;
        this.storage = storage;
        this.loaded = loaded;
        this.imgList = imgList;
        this.cashier = SharedMethods.initThreadExecutor(1);
        this.warehouseman = SharedMethods.initThreadExecutor(1);
        this.commandQueue = new ArrayList<>();
        this.requestQueue = new ArrayList<>();
        this.carts = new Carts(tvOut, storage, null, imgList, speed);
        this.customerCounter = 1;
        this.reset = false;
    }

    /**
     * Metoda slouží jako setter hodnoty loaded.
     * @param loaded pravdivostní hodnota
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
    
    /**
     * Metoda slouží jako setter pro předání hodnoty speed.
     * @param speed hodnota speed
     */
    public void setSpeed(double speed){
        this.carts.setSpeed(speed);
    }
    
    /**
     * Metoda slouží k ukončení běžících vláken.
     * Je volána z FXMLDocumentControlleru při stisknutí tlačítka Obnovit či Nahrát sklad.
     */
    public void threadShutDownNow(){
        this.reset = true;
        carts.threadShutDownNow();
        this.warehouseman.shutdownNow();
        this.cashier.shutdown();
    }
    
    /**
     * Metoda slouží k vykonání příkazu.
     * Vytvoří nové vlákno, a načte do pole příkazů obdržený vstup ze vstupní konzole.
     * Dále zavolá funkci parse, jenž se stará o rozpoznání a provedení jednotlivých příkazů.
     * Výsledek funkce parse vrací a vypíše na výstupní konzoli.
     */
    public void executeCommand(){
        Future<String> result = cashier.submit(new Callable<String>(){
            @Override
            public String call() throws Exception{
                loadCommandsArray();
                return parse();
            }
        });
        try{
            if(!this.reset){
                this.tvOut.setText(result.get());
            }
        }catch(InterruptedException | ExecutionException ex){
            System.err.println("Chyba calleble: "+ex);
        }
    }
    
    /**
     * Metoda slouží pro načtení příkazů ze vstupní konzole do pole.
     */
    private void loadCommandsArray(){
        ArrayList<String> commands = new ArrayList<>();
        String inputString = this.tvIn.getText();
        if(inputString.isEmpty()){
            this.tvOut.setText(makeOutput("Prosím zadejte příkaz. Jejich seznam lze zobrazit pomocí 'prikazy.'.\n", this.tvOut.getText()));
        }
        for(String input : this.tvIn.getText().split("\n")){
            if(input.isEmpty()){
                continue;
            }
            commands.add(input.toLowerCase().trim());
        }
        this.commandQueue.add(commands);
    }
    
    /**
     * Metoda slouží k rozpoznání příkazu a provedení odpovídající reakce.
     * @return outputText výstupní textový řetězec omezený na 500 řádků
     */
    private String parse(){
        String outputText = this.tvOut.getText();
        boolean incorrectCommand = false;
        ArrayList<String> inputCommand = this.commandQueue.remove(0);
        while(inputCommand.size() > 0){
            String tmpText = "";
            String input = inputCommand.remove(0);
            if(incorrectCommand){
                break;
            }
            switch(input){
                case "obsah.":
                    outputText = commandObsah(outputText);
                    break;
                case "vymaz.":
                    outputText = "";
                    break;
                case "prikazy.":
                    tmpText += Constants.commands;
                    outputText = makeOutput(tmpText, outputText);
                    break;
                case "pomoc.":
                    tmpText += Constants.help;
                    outputText = makeOutput(tmpText, outputText);
                    break;
                case "vydej:":
                    outputText = commandVydej(outputText, inputCommand);
                    break;
                default:
                    if (input.matches("najdi:[ ]?[a-zA-Z0-9]+[.]$")) {
                        outputText = commandNajdi(input, outputText);
                    }else{
                        tmpText += "Omlouváme se, ale nerozumíme vašemu požadavku '"+ input +"' .\n"
                                + "Prosíme uveďte správný požadavek. Nejste-li si jisti příkazem, použijte požadavek 'prikazy.'\n";
                        outputText = makeOutput(tmpText, outputText);
                        incorrectCommand = true;
                    }
                    break;
            }
            
            String[] lines = outputText.split("\n");
            if(lines.length > 500){
                outputText = SharedMethods.reduceLines(lines);
            }
        }
        return outputText;
    }
    
    /**
     * Metoda slouží k nalezení pozice odpovídajícího zboží.
     * @param name název hledaného zboží
     * @return pole obsahující hodnoty nalezeného zboží uložené v pomocné třídě Shelf
     */
    private ArrayList<Shelf> findGoods(String name){
        ArrayList<Shelf> goodsInShelf = new ArrayList<>();
        for(Shelf shelf : this.storage.shelfList){
            if(shelf.getIsShelf()){
                if(shelf.getGoods().toLowerCase().equals(name)){
                    goodsInShelf.add(shelf);
                }
            }
        }
        return goodsInShelf;
    }
    
    /**
     * Metoda sloužící pro zpracování seznamu požadavků pro výdej.
     * Zkontroluje správné udání názvu a počtu zboží. Dále jej nalezne a vytvoří seznam potvrzeného zboží k vydání.
     * Z regálů odečte počet zboží, jenž má být vydáno.
     * @param requestArr pole zboží k potvrzení k vydání
     * @return rHolder pomocná třída nesoucí výstupní textový řetězec a pole potvrzeného zboží
     */
    private ReturnHolder warehousemanCtrl(ArrayList<RequestList> requestArr){
        ArrayList<RequestList> confirmedArr = new ArrayList<>();
        String tmpText = "";
        for (RequestList item : requestArr) {
            ArrayList<Shelf> shelf = findGoods(item.getGoods());
            if(shelf.isEmpty()){
                String text = "Omlouváme se, ale uvedli jste neplatné zboží '"+item.getGoods()+"'.\n"
                        + "Pokud si nejste jisti, můžete zobrazit obsah skladu, pomocí 'obsah.', nebo zjistit dostupnost zboží pomocí 'najdi:název.'.\n";
                ReturnHolder rHolder = new ReturnHolder(text, null);
                return rHolder; 
            }
            if(item.getAmount() <= 0){
                String text = "Omlouváme se, ale neuvedli jste množství.\n"
                        + "Pokud si nejste jisti, můžete zobrazit obsah skladu, pomocí 'obsah.', nebo zjistit dostupnost zboží pomocí 'najdi:název'.\n";
                ReturnHolder rHolder = new ReturnHolder(text, null);
                return rHolder;
            }
        }
        
        for (RequestList item : requestArr) {
            ArrayList<Shelf> shelf = findGoods(item.getGoods());
            if(!shelf.isEmpty()){
                for(Shelf goodsInShelf : shelf){
                    if(goodsInShelf.getAmount() >= item.getAmount()){
                        ArrayList<Integer> coor = SharedMethods.getCoordinations(goodsInShelf.getPosition());
                        RequestList confirmedItem = new RequestList(item.getGoods(), item.getAmount(), coor.get(0), coor.get(1));
                        confirmedArr.add(confirmedItem);
                        goodsInShelf.setAmount(goodsInShelf.getAmount() - item.getAmount());
                        if(goodsInShelf.getAmount() <= 0){
                            imgList.get(goodsInShelf.getPosition()).getImg().setImage(new Image(Constants.pathImg+"pallet_empty.png", Constants.imgWidth, Constants.imgHeight, false, true));
                        }
                        item.setAmount(0);
                        break;
                    }else{
                        if(goodsInShelf.getAmount() > 0){
                            ArrayList<Integer> coor = SharedMethods.getCoordinations(goodsInShelf.getPosition());
                            RequestList confirmedItem = new RequestList(item.getGoods(), goodsInShelf.getAmount(), coor.get(0), coor.get(1));
                            confirmedArr.add(confirmedItem);
                            item.setAmount(item.getAmount() - goodsInShelf.getAmount());
                            goodsInShelf.setAmount(0);
                            imgList.get(goodsInShelf.getPosition()).getImg().setImage(new Image(Constants.pathImg+"pallet_empty.png", Constants.imgWidth, Constants.imgHeight, false, true));
                        }
                    }
                }
                if(item.getAmount() > 0){
                    tmpText += "Omlouváme se, ale nelze vydat "+ item.getAmount() +" ks zboží '"+ item.getGoods() +"', jelikož nám došlo.\n";
                }
            }
        }
        
        String text = "Výdej zboží zákazníka č."+ (this.customerCounter) +" byl zaslán do skladu.\n"
                + "Prosíme počkejte, než si naše vozíky načtou data a vyzvednou zboží.\n";
        ReturnHolder rHolder = new ReturnHolder(makeOutput(text, tmpText), confirmedArr);
        return rHolder;
    }
    
    /**
     * Metoda vykonávající příkaz obsah.
     * Metoda projde pole regálů a vypíše jejich obsah a pozici.
     * @param outputText již vypsaný či připravený výstupní text, ke kterému přidáme výpis metody
     * @return outputText upravený výstupní text
     */
    private String commandObsah(String outputText){
        String tmpText = "";
        if(this.loaded){
            for(Shelf shelf : this.storage.shelfList){
                if(shelf.isShelf){
                    ArrayList<Integer> coor = SharedMethods.getCoordinations(shelf.position);
                    tmpText += "Regál na pozici (" + coor.get(0) + ";" + coor.get(1) + ") obsahuje: " + shelf.goods + " (" + shelf.amount + " ks)" + "\n";
                }
            }
        }else{
            tmpText += "Skladiště nic neobsahuje, jelikož není načteno.\n"
                    + "Prosíme načtěte skladičtě, pomocí tlačítka 'Načíst sklad', před dalším provedením požadavku.\n";
        }
        return makeOutput(tmpText, outputText);
    }
    
    /**
     * Metoda vykonávající příkaz vydej.
     * Metoda zkontroluje syntaktickou správnost příkazu. Je-li příkaz syntakticky správně a mapa je načtena,
     * zavolá metodu třídy carts, jenž se stará o obsluhu vozíků.
     * @param outputText již vypsaný či připravený výstupní text, ke kterému přidáme výpis metody
     * @param inputCommand pole příkazů
     * @return outputText upravený výstupní text
     */
    private String commandVydej(String outputText, ArrayList<String> inputCommand){
        boolean lastFound = false;
        ArrayList<RequestList> requestArr = new ArrayList<>();
        while(inputCommand.size() > 0){
            String request = inputCommand.remove(0);
            if (request.matches("[a-zA-Z0-9]+,[ ]?[0-9]+([.]|,)")) {
                String amount = request.split(",")[1].trim();
                amount = amount.replace(".", "");
                amount = amount.replace(",", "");
                RequestList newRequest = new RequestList(request.split(",")[0], Integer.parseInt(amount), -1, -1);
                requestArr.add(newRequest);
                if(request.matches("[a-zA-Z0-9]+,[ ]?[0-9]+[.]")){
                    lastFound = true;
                    break;
                }
            }else{
                String text =  "Omlouváme se, ale nevyplnil jste správně formulář pro výdej.\n"
                    + "Prosíme vyplňte váš formulář na výdej správně. Nejste-li si jisti jak, použijte požadavek 'prikazy.'\n";
                return makeOutput(text, outputText);
            }
        }
        if(!lastFound){
            String text = "Omlouváme se, ale nevyplnil jste správně formulář pro výdej.\n"
                + "Prosíme vyplňte váš formulář na výdej správně. Nejste-li si jisti jak, použijte požadavek 'prikazy.'\n";
            return makeOutput(text, outputText);
        } else if(lastFound){
            if(this.loaded){
                String tmpText = "";
                ReturnHolder rHolder = warehousemanCtrl(requestArr);
                
                tmpText += rHolder.getOutput();
                outputText = makeOutput(tmpText, outputText);;
                if(rHolder.getItems() == null){
                    return outputText;
                }
                carts.sendCart(rHolder.getItems(), customerCounter);
                this.customerCounter ++;
            }else{
                String text = "Skladiště nic neobsahuje, jelikož není načteno.\n"
                        + "Prosíme načtěte skladičtě, pomocí tlačítka 'Načíst sklad', před dalším provedením požadavku.\n";
                return makeOutput(text, outputText);
            }  
        }
        return outputText;
    }
    
    /**
     * Metoda vykonávající příkaz najdi.
     * Metoda zkontroluje syntaktickou správnost příkazu. Je-li syntakticky správně, provede vyhledání zadaného zboží.
     * @param input celé znění příkazu na řádku
     * @param outputText již vypsaný či připravený výstupní text, ke kterému přidáme výpis metody
     * @return outputText upravený výstupní text
     */
    private String commandNajdi(String input, String outputText){
        String tmpText = "";
        if(this.loaded){
            String name = input.split(":")[1];
            name = name.replaceFirst(".$", "");
            name = name.toLowerCase();
            ArrayList<Shelf> shelf = findGoods(name);
            if(!shelf.isEmpty()){
                for(Shelf goodsInShelf : shelf){
                    ArrayList<Integer> coor = SharedMethods.getCoordinations(goodsInShelf.getPosition());
                    tmpText += "Zboží '"+ name +"' se nachází v regálu na pozici (" + coor.get(0) + ";" + coor.get(1) + ") v počtu " + goodsInShelf.amount + " ks.\n";
                }
            }
            else{
                tmpText += "Omlouváme se, ale hledané zboží '"+ name +"' není na skladu.\n";
            }
        }else{
            tmpText += "Skladiště nic neobsahuje, jelikož není načteno.\n"
                    + "Prosíme načtěte skladičtě, pomocí tlačítka 'Načíst sklad', před dalším provedením požadavku.\n";
        }
        
        return makeOutput(tmpText, outputText);
    }
    
    /**
     * Metoda pro vytvoření výstupního textu.
     * Obsahuje-li již výstupní text nějakou hodnotu, tak před něj přidá odřádkování.
     * @param tmpText text, jenž chceme přidat k výstupnímu textu
     * @param outputText již vypsaný či připravený výstupní text, ke kterému přidáme hodnotu tmpText
     * @return outputText upravený výstupní text
     */
    private String makeOutput(String tmpText, String outputText){
        if(!outputText.isEmpty()){
            tmpText += "\n";
        }
        
        tmpText += outputText;
        
        return tmpText;
    }
}
