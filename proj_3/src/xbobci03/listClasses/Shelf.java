package xbobci03.listClasses;

import java.util.ArrayList;

/**
 * Pomocná třída sloužící pro ukládání objektů do arraylistu.
 * Slouží k uchování informací o jednotlivých políčkách.
 * Zda-li se jedná o regál či překážku. Pokud se jedná o regál, tak si navíc udržeju jeho obsah.
 * @author Pavel Bobčík (xbobci03)
 * @author Tomáš Daněk (xdanek23)
 */
public class Shelf{
    public boolean isShelf;
    public int position;
    public String goods;
    public int amount;
    public boolean isWall;
    int cartNumber;
    ArrayList<Cart> cart;
    
    /**
     * Konstruktor třídy Shelf.
     * Dojde k deklaraci uvedených hodnot.  
     * @param isShelf pravdivostní hodnota, zda-li se jedná o regál
     * @param position pozice na ploše skladiště
     * @param goods název zboží
     * @param amount počet zboží
     * @param isWall pravdivostní hodnota, zda-li se jedná o překážku
     */
    public Shelf(boolean isShelf, int position, String goods, int amount, boolean isWall){
        this.isShelf = isShelf;
        this.position = position;
        this.goods = goods;
        this.amount = amount;
        this.isWall = isWall;
        this.cartNumber = -1;
        this.cart = new ArrayList<>();
    }

    /**
     * Metoda sloužící jako getter pravdivostní hodnoty isShelf.
     * @return pravdivostní hodnotu isShelf
     */
    public boolean getIsShelf() {
        return isShelf;
    }

    /**
     * Metoda sloužící jako setter pravdivostní hodnoty isShelf.
     * @param isShelf pravdivostní hodnota
     */
    public void setIsShelf(boolean isShelf) {
        this.isShelf = isShelf;
    }
    
    /**
     * Metoda sloužící jako getter hodnoty pozice umístění na ploše.
     * @return pozice na ploše skladiště
     */
    public int getPosition() {
        return position;
    }

    /**
     * Metoda sloužící jako setter hodnoty pozice umístění na ploše.
     * @param position celočíselná hodnota pozice
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Metoda sloužící jako getter pro získání názvu zboží.
     * @return název zboží
     */
    public String getGoods() {
        return goods;
    }

    /**
     * Metoda sloužící jako setter pro nastavení názvu zboží.
     * @param goods název k nastavení
     */
    public void setGoods(String goods) {
        this.goods = goods;
    }

    /**
     * Metoda sloužící jako getter pro získání počtu zboží.
     * @return počet zboží
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Metoda sloužící jako setter pro nastavení počtu zboží.
     * @param amount počet k nastavení
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public void returnAmount(int amount) {
        this.amount = this.amount + amount;
    }

    /**
     * Metoda sloužící jako getter hodnoty určující, zda-li se jedná o překážku či cestu.
     * @return pravdivostní hodnota
     */
    public boolean getIsWall() {
        return isWall;
    }

    /**
     * Metoda sloužící jako setter hodnoty určující, zda-li se jedná o překážku či cestu.
     * @param isWall pravdivostní hodnota
     */
    public void setIsWall(boolean isWall) {
        this.isWall = isWall;
    }
    
    /**
     * Metoda sloužící jako getter pole s vozíky na daném políčku.
     * @return cart pole vozíků
     */
    public ArrayList<Cart> getCart() {
        return cart;
    }

    /**
     * Metoda sloužící jako setter pole s vozíky na daném políčku.
     * @param cart přidá vozík do pole vozíků
     */
    public void setCart(Cart cart) {
        this.cart.add(cart);
    }

    /**
     * Metoda sloužící jako getter čísla vozíku.
     * @return cartNumber číslo vozíku
     */
    public int getCartNumber() {
        return cartNumber;
    }

    /**
     * Metoda sloužící jako setter čísla vozíku.
     * @param cartNumber číslo vozíku
     */
    public void setCartNumber(int cartNumber) {
        this.cartNumber = cartNumber;
    }
}
