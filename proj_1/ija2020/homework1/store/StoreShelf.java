package ija.ija2020.homework1.store;

import ija.ija2020.homework1.goods.Goods;
import ija.ija2020.homework1.goods.GoodsItem;
import ija.ija2020.homework1.goods.GoodsShelf;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author xbobci03
 */

public class StoreShelf implements GoodsShelf {

    HashMap<String, ArrayList<StoreGoodsItem>> shelfItem = new HashMap<>();
    
    @Override
    public void put(GoodsItem goodsItem) {
        if(shelfItem.containsKey(goodsItem.goods().getName())){
            shelfItem.get(goodsItem.goods().getName()).add((StoreGoodsItem) goodsItem);
        }else{
          ArrayList<StoreGoodsItem> newShelf = new ArrayList<>();
          newShelf.add((StoreGoodsItem) goodsItem);
          shelfItem.put(goodsItem.goods().getName(), newShelf);
        }
    }

    @Override
    public boolean containsGoods(Goods goods) {
        if(shelfItem.get(goods.getName()) == null || shelfItem.get(goods.getName()).isEmpty()){
            return false;
        }else{
            return true;
        }
        
    }

    @Override
    public GoodsItem removeAny(Goods goods) {
        if(!containsGoods(goods)){
            return null;
        }
        int tmpIndex = shelfItem.get(goods.getName()).size()-1;
        StoreGoodsItem tmpItem = shelfItem.get(goods.getName()).get(tmpIndex);
        shelfItem.get(goods.getName()).remove(tmpIndex);
        return tmpItem;
    }

    @Override
    public int size(Goods goods) {
        if(shelfItem.get(goods.getName()) == null){
            return 0;
        }
        return shelfItem.get(goods.getName()).size();
    }
    
}
