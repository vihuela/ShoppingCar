package com.example.shoppingcart.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.example.shoppingcart.R;
import com.example.shoppingcart.adapter.GoodsAdapter;
import com.example.shoppingcart.adapter.TypeAdapter;
import com.example.shoppingcart.interfaces.IShopingCar;
import com.example.shoppingcart.model.GoodsItem;
import com.example.shoppingcart.util.DividerDecoration;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * @author ricky.yao on 2017/6/1.
 */

public class ShopingCarFrg4Organization extends Fragment implements IShopingCar {
    private RecyclerView rvType;
    private StickyListHeadersListView listView;
    private GoodsAdapter myAdapter;
    private TypeAdapter typeAdapter;
    private ArrayList<GoodsItem> dataList, typeList;


    private SparseIntArray groupSelect;//左侧列表：key=typeId val=count

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View item = inflater.inflate(R.layout.layout_mian_shopping_cart, container, false);
        dataList = GoodsItem.getGoodsList(getClass().getSimpleName());
        typeList = GoodsItem.getTypeList(getClass().getSimpleName());


        groupSelect = new SparseIntArray();
        return item;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() != null) {
            listView = (StickyListHeadersListView) getView().findViewById(R.id.itemListView);
            listView.setDivider(null);
            rvType = (RecyclerView) getView().findViewById(R.id.typeRecyclerView);
            rvType.setLayoutManager(new LinearLayoutManager((ShoppingCartActivity) getActivity()));
            typeAdapter = new TypeAdapter((ShoppingCartActivity) getActivity(), typeList);
            rvType.setAdapter(typeAdapter);
            DividerDecoration decor = new DividerDecoration((ShoppingCartActivity) getActivity());
            decor.setDividerOffset(5f);
            rvType.addItemDecoration(decor);

            myAdapter = new GoodsAdapter(dataList, (ShoppingCartActivity) getActivity());
            listView.setAdapter(myAdapter);

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    GoodsItem item = dataList.get(firstVisibleItem);
                    if (typeAdapter.selectTypeId != item.typeId) {
                        typeAdapter.selectTypeId = item.typeId;
                        typeAdapter.notifyDataSetChanged();
                        rvType.smoothScrollToPosition(getSelectedGroupPosition(item.typeId));
                    }
                }
            });
        }

    }
    //根据类别id获取分类的Position 用于滚动左侧的类别列表
    public int getSelectedGroupPosition(int typeId) {
        for (int i = 0; i < typeList.size(); i++) {
            if (typeId == typeList.get(i).typeId) {
                return i;
            }
        }
        return 0;
    }

    public void onTypeClicked(int typeId) {
        listView.setSelection(getSelectedPosition(typeId));
    }

    private int getSelectedPosition(int typeId) {
        int position = 0;
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).typeId == typeId) {
                position = i;
                break;
            }
        }
        return position;
    }
    public void update(boolean refreshGoodList) {
        if (myAdapter != null && refreshGoodList) {
            myAdapter.notifyDataSetChanged();
        }if (typeAdapter != null) {
            typeAdapter.notifyDataSetChanged();
        }
    }
    //添加商品
    public void add(GoodsItem item, boolean refreshGoodList) {

        int groupCount = groupSelect.get(item.typeId);
        if (groupCount == 0) {
            groupSelect.append(item.typeId, 1);
        } else {
            groupSelect.append(item.typeId, ++groupCount);
        }

        ((ShoppingCartActivity)getActivity()).update(refreshGoodList);
    }
    //移除商品
    public void remove(GoodsItem item, boolean refreshGoodList) {

        int groupCount = groupSelect.get(item.typeId);
        if (groupCount == 1) {
            groupSelect.delete(item.typeId);
        } else if (groupCount > 1) {
            groupSelect.append(item.typeId, --groupCount);
        }

        ((ShoppingCartActivity)getActivity()).update(refreshGoodList);
    }
    //清空购物车
    public void clearCart() {
        groupSelect.clear();
    }

    //根据类别Id获取属于当前类别的数量
    public int getSelectedGroupCountByTypeId(int typeId) {
        return groupSelect.get(typeId);
    }
}
