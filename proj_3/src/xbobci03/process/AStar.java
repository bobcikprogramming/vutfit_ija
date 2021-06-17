package xbobci03.process;

import java.util.ArrayList;
import java.util.Collections;
import xbobci03.listClasses.CellAStar;
import xbobci03.sharedClasses.Constants;
import xbobci03.sharedClasses.SharedMethods;

/**
 * Algoritmus A* pro hledání nejkratší cesty.
 * @author Pavel Bobčík (xbobci03)
 */
public class AStar {
    ArrayList<CellAStar> openCells;
    ArrayList<CellAStar> closeCells;
    ArrayList<CellAStar> path;
    CellAStar actual;
    Storage storage;
    int xBeg, yBeg, xFin, yFin, width, height;
    
    /**
     * Konstruktor třídy AStar.
     * Dojde k deklaraci uvedených hodnot a výpočtu hodnoty h.
     * @param storage odkaz na objekt storage 
     * @param xBeg počáteční hodnota x
     * @param yBeg počáteční hodnota y
     * @param xFin koncová hodnota x
     * @param yFin koncová hodnota y
     */
    public AStar(Storage storage, int xBeg, int yBeg, int xFin, int yFin) {
        this.xBeg = xBeg;
        this.yBeg = yBeg;
        this.openCells = new ArrayList<>();
        this.closeCells = new ArrayList<>();
        this.path = new ArrayList<>();
        this.storage = storage;
        double h = Math.sqrt(this.xBeg*this.xBeg + this.yBeg*this.yBeg);
        h = Math.round(h * 100.0) / 100.0;
        this.actual = new CellAStar(null, this.xBeg, this.yBeg, 1, h);
        this.xFin = xFin;
        this.yFin = yFin;
        this.width = Constants.xMax;
        this.height = Constants.yMax;
    }
    
    /**
     * Metoda slouží k nalezení souřadnic, jenž se nachází vedle finálního políčka.
     * Jelikož chceme přistupovat před regál, nikoliv do regálu, tak se musí zjistit,
     * z jakých souřadnic lze k regálu přistupovat.
     * @return pravdivostní hodnotu, zda-li byly souřadnice nalezeny
     */
    private boolean findFinCoordinations(){
        int left = this.xFin - 1;
        int right = this.xFin + 1;
        int up = this.yFin - 1;
        int down = this.yFin + 1;
        
        if(left >= 0 && !this.storage.getShelfList().get(SharedMethods.getIndex(left, yFin)).isWall){
            this.xFin = left;
            return true;
        }else if(right >= 0 && !this.storage.getShelfList().get(SharedMethods.getIndex(right, yFin)).isWall){
            this.xFin = right;
            return true;
        }else if(up >= 0 && !this.storage.getShelfList().get(SharedMethods.getIndex(xFin, up)).isWall){
            this.yFin = up;
            return true;
        }else if(down >= 0 && !this.storage.getShelfList().get(SharedMethods.getIndex(xFin, down)).isWall){
            this.yFin = down;
            return true;
        }else{
            return false;
        }   
    }
    
    /**
     * Metoda slouží k nalezení cesty.
     * Zkontroluje, že lze začít cestu a přistoupit k regálu. Uloží aktuální uzel do zásobníku uzavřených uzlů.
     * Dokud nenarazila na koncový uzel, nebo dokud má uzly k dispozici, tak prochází cyklem. První uzel ze zásobníku otevřených uzlů
     * nastaví jako aktuální, expanduje jej a přesune na zásobník uzavřených uzlů.
     * Po skončení cyklu dojde k zpětnému projítí cesty a postupnému ukládání aktuálního uzlu na první pozici v arraylistu.
     * @return arraylist cesty
     */
    public ArrayList<CellAStar> findPath(){
        if((this.xFin != Constants.xIssue && this.yFin != Constants.yIssue) || (this.xFin != Constants.xParking && this.yFin != Constants.yParking)){
            if(!findFinCoordinations()){
                return null;
            }
        }
        if(this.storage.getShelfList().get(SharedMethods.getIndex(xBeg, yBeg)).isWall){
            return null;
        }
        this.closeCells.add(actual);
        expandActualCell();
        while(this.actual.getX() != this.xFin || this.actual.getY() != this.yFin){
            if(this.openCells.isEmpty()){
                return null;
            }
            
            this.actual = this.openCells.get(0);
            this.openCells.remove(0);
            expandActualCell();
            this.closeCells.add(this.actual);
        }
        
        this.path.add(this.actual);
        while(this.actual.getX() != this.xBeg || this.actual.getY() != this.yBeg){
            this.actual = this.actual.getPrev();
            this.path.add(0, this.actual);
        }
        return this.path;
    }
    
    /**
     * Metoda slouží k rozšíření aktuálního uzlu.
     * Metoda zkontroluje okolní uzly (nepodporuje pohyb po úhlopříčce) a pokud se jedná o uzel, 
     * jenž nebyl zatím prozkoumán a je cestou, tak se přidá tento uzel do zásobníku otevřených uzlů.
     * Na konci je zásobník otevřených uzlů seřazen vzestupně.
     */
    private void expandActualCell(){
        int xPrev = this.actual.getX();
        int yPrev = this.actual.getY();
        double gParent = this.actual.getG();
        
        for(int y = -1; y < 2; y++){
            for(int x = -1; x < 2; x++){
                if((y == 0 && x == 0) || (x != 0 && y != 0) || myContains(this.openCells, xPrev + x, yPrev + y) || myContains(this.closeCells, xPrev + x, yPrev + y)){
                    continue;
                }
                double h = Math.sqrt(x*x + y*y);
                h = Math.round(h * 100.0) / 100.0;
                CellAStar cas = new CellAStar(this.actual, xPrev + x, yPrev + y, gParent + 1, h);
                if((xPrev + x >= 0 && xPrev + x < this.width) && (yPrev + y >= 0 && yPrev + y < this.height)
                        && !(this.storage.getShelfList().get(SharedMethods.getIndex(xPrev + x, yPrev + y)).isWall)){
                    this.openCells.add(cas);
                }
            }
        }
        Collections.sort(this.openCells);
    }
    
    /**
     * Metoda sloužící k zjištění, zda-li arraylist obsahuje uzel na pozici x, y.
     * @param searchIn prozkoumávaný arraylist
     * @param x souřadnice x
     * @param y souřadnice y
     * @return pravdivostní hodnotu, zda-li se uzel v arraylistu vyskytuje
     */
    private boolean myContains(ArrayList<CellAStar> searchIn, int x, int y){
        boolean found = false;
        for(CellAStar cas : searchIn){
            if(cas.getX() == x && cas.getY() == y){
                found = true;
                break;
            }
        }
        return found;
    }
}
