package ija.ija2020.homework1.store;

import ija.ija2020.homework1.goods.Goods;
import ija.ija2020.homework1.goods.GoodsItem;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author xbobci03
 */

public class StoreGoods implements Goods {

    ArrayList<StoreGoodsItem> items = new ArrayList<>();
    String name;
    
    
    public StoreGoods(String name){
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean addItem(GoodsItem goodsItem) {
        return items.add((StoreGoodsItem) goodsItem);
    }

    @Override
    public GoodsItem newItem(LocalDate localDate) {
        StoreGoodsItem item = new StoreGoodsItem(this, localDate); 
        items.add(item);
        return item;
    }

    @Override
    public boolean remove(GoodsItem goodsItem) {
        return items.remove((StoreGoodsItem) goodsItem);
    }

    @Override
    public boolean empty() {
        return items.isEmpty();
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StoreGoods other = (StoreGoods) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return name.equals(other.name);
        
    }

    
    
    
}
