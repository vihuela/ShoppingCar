package com.example.shoppingcart.interfaces;

import com.example.shoppingcart.model.GoodsItem;


public interface IShopingCar {
    void add(GoodsItem item, boolean refreshGoodList);

    void remove(GoodsItem item, boolean refreshGoodList);

    void update(boolean refreshGoodList);

    void clearCart();

    void onTypeClicked(int typeId);

    int getSelectedGroupCountByTypeId(int typeId);
}
