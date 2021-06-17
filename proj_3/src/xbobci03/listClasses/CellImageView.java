package xbobci03.listClasses;

import javafx.scene.image.ImageView;

/**
 * Pomocná třída sloužící pro ukládání objektů do arraylistu.
 * Obsahuje seznam tlačítek na ploše skladiště a jejich pozici.
 * @author Pavel Bobčík (xbobci03)
 * @author Tomáš Daněk (xdanek23)
 */
public class CellImageView implements Comparable<CellImageView>{
    ImageView img = null;
    int position;
    
    /**
     * Konstruktor třídy CellImageView.
     * Dojde k deklaraci uvedených hodnot. 
     * @param img odkaz na objekt ImageView
     * @param position pozice na ploše skladiště
     */
    public CellImageView(ImageView img, int position){
        this.img = img;
        this.position = position;
        
    }

    /**
     * Metoda sloužící jako getter ImageView.
     * @return objekt ImageView
     */
    public ImageView getImg() {
        return img;
    }

    public int getPosition() {
        return position;
    }
    
    
    /**
     * Metoda sloužící k určení klíče pro řazení pole.
     * @param second hodnota se kterou porovnáváme
     * @return hodnotu, zdali je zkoumaný prvek menší, větší či roven
     */
    @Override
    public int compareTo(CellImageView second) {
        return this.position - second.position;
    }
    
    
    
}
