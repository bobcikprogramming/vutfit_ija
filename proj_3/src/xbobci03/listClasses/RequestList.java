package xbobci03.listClasses;

/**
 * Pomocná třída sloužící pro ukládání objektů do arraylistu.
 * Obsahuje informace o obsahu polí obsahu vozíku a požadavků na vyzvednutí.
 * @author Pavel Bobčík (xbobci03)
 * @author Tomáš Daněk (xdanek23)
 */
public class RequestList {
    String goods;
    int amount, x, y;
    
    /**
     * Konstruktor třídy RequestList.
     * Dojde k deklaraci uvedených hodnot.
     * @param goods název zboží
     * @param amount počet kusů zboží
     * @param x hodnota souřadnice x
     * @param y hodnota souřadnice y
     */
    public RequestList(String goods, int amount, int x, int y){
        this.goods = goods;
        this.amount = amount;
        this.x = x;
        this.y = y;
    }

    /**
     * Metoda sloužící jako getter názvu zboží.
     * @return goods název zboží
     */
    public String getGoods() {
        return goods;
    }

    /**
     * Metoda sloužící jako getter počtu kusů.
     * @return amount počet kusů
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Metoda sloužící jako getter X souřadnice polohy zboží.
     * @return x X souřadnice polohy zboží
     */
    public int getX() {
        return x;
    }

    /**
     * Metoda sloužící jako getter Y souřadnice polohy zboží.
     * @return y Y souřadnice polohy zboží
     */
    public int getY() {
        return y;
    }

    /**
     * Metoda sloužící jako setter názvu zboží.
     * @param goods název zboží
     */
    public void setGoods(String goods) {
        this.goods = goods;
    }

    /**
     * Metoda sloužící jako setter počtu kusů.
     * @param amount počet kusů
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Metoda sloužící jako setter X souřadnice polohy zboží.
     * @param x X souřadnice polohy zboží
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Metoda sloužící jako setter Y souřadnice polohy zboží.
     * @param y Y souřadnice polohy zboží
     */
    public void setY(int y) {
        this.y = y;
    }
     
}
