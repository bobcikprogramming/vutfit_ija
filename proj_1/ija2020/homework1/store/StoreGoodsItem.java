package ija.ija2020.homework1.store;

import ija.ija2020.homework1.goods.Goods;
import ija.ija2020.homework1.goods.GoodsItem;
import java.time.LocalDate;

/**
 *
 * @author xbobci03
 */

public class StoreGoodsItem implements GoodsItem {

    LocalDate date;
    Goods good;
    
    public StoreGoodsItem(Goods good, LocalDate date) {
        this.good = good;
        this.date = date;
    }

    @Override
    public Goods goods() {
        return good;
    }

    @Override
    public boolean sell() {
        return this.goods().remove(this);
    }
}
