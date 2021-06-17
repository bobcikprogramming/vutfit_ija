package xbobci03.listClasses;

/**
 * Pomocná třída sloužící pro ukládání objektů do arraylistu.
 * Obsahuje uzly algoritmu A*.
 * @author Pavel Bobčík (xbobci03)
 */
public class CellAStar implements Comparable<CellAStar>{
    public CellAStar prev;
    public int x, y;
    public double g, h, f;
    
    /**
     * Konstruktor třídy CellAStar.
     * Dojde k deklaraci uvedených hodnot.
     * @param prev hodnota předka
     * @param x hodnota souřadnice x
     * @param y hodnota souřadnice y
     * @param g hodnota cesty do aktuálního uzlu
     * @param h hodnota heuristiky
     */
    public CellAStar(CellAStar prev, int x, int y, double g, double h){
        this.prev = prev;
        this.x = x;
        this.y = y;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }

    /**
     * Metoda sloužící jako getter x souřadnice.
     * @return x souřadnici
     */
    public int getX() {
        return x;
    }

    /**
     * Metoda sloužící jako getter y souřadnice.
     * @return y souřadnice
     */
    public int getY() {
        return y;
    }
    
    /**
     * Metoda sloužící jako getter hodnoty g.
     * @return g hodnotu
     */
    public double getG() {
        return g;
    }

    /**
     * Metoda sloužící jako getter předka.
     * @return hodnotu předka
     */
    public CellAStar getPrev() {
        return prev;
    }

    /**
     * Metoda sloužící jako getter hodnoty h.
     * @return h hodnotu
     */
    public double getH() {
        return h;
    }

    /**
     * Metoda sloužící jako getter hodnoty f.
     * @return f hodnotu
     */
    public double getF() {
        return f;
    }

    /**
     * Metoda sloužící k určení klíče pro řazení pole.
     * @return hodnotu, zda-li je zkoumaný prvek menší, větší či roven
     */
    @Override
    public int compareTo(CellAStar next) {
        return (int)(this.f - next.f);
    }
}
