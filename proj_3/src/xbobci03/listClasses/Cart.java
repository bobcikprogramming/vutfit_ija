package xbobci03.listClasses;

import java.util.ArrayList;

/**
 * Pomocná třída sloužící pro ukládání objektů do arraylistu.
 * Obsahuje informace o vozíku, jakož jsou počátek a konec, obsah, cesta a další.
 * @author Pavel Bobčík (xbobci03)
 */
public class Cart {
    int space = 5;
    ArrayList<RequestList> list;
    ArrayList<RequestList> contains;
    ArrayList<CellAStar> path;
    int begX, begY, finX, finY, number;
    boolean pathVis;
    
    /**
     * Konstruktor třídy Cart.
     * Dojde k deklaraci uvedených hodnot.
     * Při inicializace třídy je počáteční X souřadnice nastavena na 23 a Y na 9 (výstupní pozice z parkoviště).
     */
    public Cart(){
        this.begX = 23;
        this.begY = 9;
        this.list = new ArrayList<>();
        this.contains = new ArrayList<>();
        this.path = new ArrayList<>();
        this.pathVis = false;
    }

    /**
     * Metoda sloužící jako getter volného místa ve vozíku.
     * @return space velikost volného místa
     */
    public int getSpace() {
        return space;
    }

    /**
     * Metoda sloužící jako setter volného místa ve vozíku.
     * @param space nová velikost volného místa
     */
    public void setSpace(int space) {
        this.space = space;
    }

    /**
     * Metoda sloužící jako getter pole zboží, jenž má vozík vyzvednout.
     * @return list pole zboží k vyzvednutí
     */
    public ArrayList<RequestList> getList() {
        return list;
    }

    /**
     * Metoda sloužící jako setter pole zboží, jenž má vozík vyzvednout.
     * @param item přidání zboží do pole k vyzvednutí
     */
    public void setList(RequestList item) {
        this.list.add(item);
    }

    /**
     * Metoda sloužící jako getter počáteční X souřadnice.
     * @return begX počáteční X souřadnice
     */
    public int getBegX() {
        return begX;
    }

    /**
     * Metoda sloužící jako setter počáteční X souřadnice.
     * @param begX počáteční X souřadnice
     */
    public void setBegX(int begX) {
        this.begX = begX;
    }

    /**
     * Metoda sloužící jako getter počáteční Y souřadnice.
     * @return begY počáteční Y souřadnice
     */
    public int getBegY() {
        return begY;
    }

    /**
     * Metoda sloužící jako setter počáteční Y souřadnice.
     * @param begY počáteční Y souřadnice
     */
    public void setBegY(int begY) {
        this.begY = begY;
    }

    /**
     * Metoda sloužící jako getter konečné X souřadnice.
     * @return finX konečná X souřadnice
     */
    public int getFinX() {
        return finX;
    }

    /**
     * Metoda sloužící jako setter konečné X souřadnice.
     * @param finX konečná X souřadnice
     */
    public void setFinX(int finX) {
        this.finX = finX;
    }

    /**
     * Metoda sloužící jako getter konečné Y souřadnice.
     * @return finY konečná Y souřadnice
     */
    public int getFinY() {
        return finY;
    }

    /**
     * Metoda sloužící jako setter konečné Y souřadnice.
     * @param finY konečná Y souřadnice
     */
    public void setFinY(int finY) {
        this.finY = finY;
    }

    /**
     * Metoda sloužící jako getter pole zboží, jenž vozík obsahuje.
     * @return contains pole zboží ve vozíku
     */
    public ArrayList<RequestList> getContains() {
        return contains;
    }

    /**
     * Metoda sloužící jako setter pole zboží, jenž vozík obsahuje.
     * @param item přidání zboží do pole s obsahem
     */
    public void setContains(RequestList item) {
        this.contains.add(item);
    }

    /**
     * Metoda sloužící k vylunování pole s obsahem vozíku.
     */
    public void deleteContains() {
        this.contains = new ArrayList<>();
    }
    
    /**
     * Metoda sloužící jako getter pole cesty.
     * @return path pole cesty vozíku
     */
    public ArrayList<CellAStar> getPath() {
        return path;
    }

    /**
     * Metoda sloužící jako setter pole cesty.
     * @param path pole cesty vozíku
     */
    public void setPath(ArrayList<CellAStar> path) {
        this.path = path;
    }

    /**
     * Metoda sloužící jako getter čísla vozíku.
     * @return number číslo vozíku
     */
    public int getNumber() {
        return number;
    }

    /**
     * Metoda sloužící jako setter čísla vozíku.
     * @param number číslo vozíku
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Metoda sloužící jako getter pravdivostní hodnoty, zdali je cesta vozíku zobrazena.
     * @return pathVis pravdivostní hodnota
     */
    public boolean isPathVis() {
        return pathVis;
    }

    /**
     * Metoda sloužící jako setter pravdivostní hodnoty, zdali je cesta vozíku zobrazena.
     * @param pathVis pravdivostní hodnota
     */
    public void setPathVis(boolean pathVis) {
        this.pathVis = pathVis;
    }

}
