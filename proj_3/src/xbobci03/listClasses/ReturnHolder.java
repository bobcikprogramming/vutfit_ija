package xbobci03.listClasses;

import java.util.ArrayList;

/**
 * Pomocná třída sloužící pro return metod.
 * Obsahuje informace o výstupním textu a pole požadovaného zboží.
 * @author Pavel Bobčík (xbobci03)
 */
public class ReturnHolder {
    String output = "";
    ArrayList<RequestList> items;
    
    /**
     * Konstruktor třídy ReturnHolder.
     * Dojde k deklaraci uvedených hodnot.
     * @param output výstupní text
     * @param items pole požadovaného zboží
     */
    public ReturnHolder(String output, ArrayList<RequestList> items){
        this.output = output;
        this.items = items;
    }

    /**
     * Metoda sloužící jako getter výstupního textu.
     * @return output výstupní text
     */
    public String getOutput() {
        return output;
    }

    /**
     * Metoda sloužící jako getter volného pole požadovaného zboží.
     * @return items pole požadovaného zboží
     */
    public ArrayList<RequestList> getItems() {
        return items;
    }
}
