package com.example.shoppingcart.model;

import com.example.shoppingcart.R;
import com.example.shoppingcart.ui.ShopingCarFrg4Culture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GoodsItem {
    private static ArrayList<GoodsItem> goodsList;
    private static ArrayList<GoodsItem> typeList;
    public int id;
    public int typeId;
    public int rating;
    public String name;
    public String typeName;
    public double price;
    public int count;
    public int img;//图片
    public int day;//天数
    public String des;//描述
    public int person;//人数

    public GoodsItem() {
    }

    public GoodsItem(int id, double price, String name, int typeId, String typeName) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.typeId = typeId;
        this.typeName = typeName;
        rating = new Random().nextInt(5) + 1;
    }

    public GoodsItem(int id, double price, String name, int typeId, String typeName, int img, int day, String des, int person) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.typeId = typeId;
        this.typeName = typeName;
        this.img = img;
        this.day = day;
        this.des = des;
        this.person = person;
        rating = new Random().nextInt(5) + 1;
    }


    public GoodsItem(String typeName, String name, double price, int img, int day, String des, int person) {
        this.typeName = typeName;
        this.price = price;
        this.name = name;
        this.img = img;
        this.day = day;
        this.des = des;
        this.person = person;
    }

    private static void initData(String tag) {
        goodsList = new ArrayList<>();
        typeList = new ArrayList<>();
        GoodsItem item = null;
        //精神文化数据mock
        if (tag.equalsIgnoreCase(ShopingCarFrg4Culture.class.getSimpleName())) {

            for (int i = 1; i < 6; i++) {
                for (int j = 1; j < 5; j++) {
                    GoodsItem goodsItem = getCultureData().get(i).get(j);
                    item = new GoodsItem(100 * i + j, goodsItem.price, goodsItem.name, i, goodsItem.typeName, goodsItem.img, goodsItem.day, goodsItem.des, goodsItem.person);
                    goodsList.add(item);
                }
                typeList.add(item);
            }
        } else {
            //组织管理数据mock
            for (int i = 15; i < 25; i++) {
                for (int j = 1; j < 10; j++) {
                    item = new GoodsItem(100 * i + j, Math.random() * 100, "商品" + (100 * i + j), i, "种类" + i);
                    goodsList.add(item);
                }
                typeList.add(item);
            }
        }

    }

    public static ArrayList<GoodsItem> getGoodsList(String tag) {
        initData(tag);
        return goodsList;
    }

    public static ArrayList<GoodsItem> getTypeList(String tag) {
        initData(tag);
        return typeList;
    }

    private static HashMap<Integer, List<GoodsItem>> getCultureData() {
        //id id对于List
        HashMap<Integer, List<GoodsItem>> map = new HashMap<>();
        //忠孝意识
        List<GoodsItem> l1 = new ArrayList<>();
        Collections.addAll(l1,
                new GoodsItem(),
                new GoodsItem("忠孝意识", "学习丰田“十大意识”", 5000f, R.mipmap.ic_item_tab1_type4_1, 10, "丰田培训经理讲座开讲，解读丰田企业管理意识", 5),
                new GoodsItem("忠孝意识", "学习丰田“八个步骤”", 2500f, R.mipmap.ic_item_tab1_type4_2, 5, "丰田培训经理讲座开讲，深入学习企业管理文化精髓", 3),
                new GoodsItem("忠孝意识", "丰田销售顾问成长篇讲座", 10000f, R.mipmap.ic_item_tab1_type4_3, 13, "了解企业文化对员工工作意识的重要性", 6),
                new GoodsItem("忠孝意识", "参观丰田公司内部环境", 25000f, R.mipmap.ic_item_tab1_type4_4, 10, "感受丰田公司工作氛围，丰田培训经理带领游览", 10));
        //价值与使命
        List<GoodsItem> l2 = new ArrayList<>();
        Collections.addAll(l2,
                new GoodsItem(),
                new GoodsItem("价值与使命", "了解丰田的由来与历史", 5000f, R.mipmap.ic_item_tab2_type4_1, 10, "由丰田高层管理者主讲，讲解丰田发展史", 5),
                new GoodsItem("价值与使命", "了解丰田企业的价值", 2500f, R.mipmap.ic_item_tab2_type4_2, 5, "由丰田高层管理者主讲，深度了解丰田企业的价值", 3),
                new GoodsItem("价值与使命", "了解丰田企业的使命", 10000f, R.mipmap.ic_item_tab2_type4_3, 13, "由丰田高层管理者主讲，深度了解丰田企业多年来的使命", 6),
                new GoodsItem("价值与使命", "学习丰田企业文化", 25000f, R.mipmap.ic_item_tab2_type4_4, 10, "了解丰田各部门建立的目标，听人力总监讲座", 10));
        //企业传承
        List<GoodsItem> l3 = new ArrayList<>();
        Collections.addAll(l3,
                new GoodsItem(),
                new GoodsItem("企业传承", "参观丰田集团发祥地", 5000f, R.mipmap.ic_item_tab3_type4_1, 10, "参观丰田集团的发祥地，深入了解丰田传承理念", 5),
                new GoodsItem("企业传承", "参观丰田博物馆", 2500f, R.mipmap.ic_item_tab3_type4_2, 5, "见证丰田历年来的产品创新与理念传承", 3),
                new GoodsItem("企业传承", "参观丰田生产线", 10000f, R.mipmap.ic_item_tab3_type4_3, 13, "领略丰田原汁原味的生产方式，感受企业理念传承的经久不衰", 6),
                new GoodsItem("企业传承", "深度了解丰田传承理念", 25000f, R.mipmap.ic_item_tab3_type4_4, 10, "听丰田产品经理讲座，了解丰田产品创新的过程与秉承的理念", 10));
        //自觉自悟
        List<GoodsItem> l4 = new ArrayList<>();
        Collections.addAll(l4,
                new GoodsItem(),
                new GoodsItem("自觉自悟", "丰田体系学习", 5000f, R.mipmap.ic_item_tab4_type4_1, 10, "参观学习丰田汽车生产线，听山本一夫讲座", 5),
                new GoodsItem("自觉自悟", "把丰田模式运用在你的组织", 2500f, R.mipmap.ic_item_tab4_type4_2, 5, "参观学习三菱汽车生产线，听山本一夫讲座", 3),
                new GoodsItem("自觉自悟", "三菱重工的企业文化", 10000f, R.mipmap.ic_item_tab4_type4_3, 13, "参观学习三菱汽车生产线，听山本一夫讲座", 6),
                new GoodsItem("自觉自悟", "三菱组织架构学习", 25000f, R.mipmap.ic_item_tab4_type4_4, 10, "参观学习三菱汽车生产线，听山本一夫讲座", 10));
        //提升企业形象
        List<GoodsItem> l5 = new ArrayList<>();
        Collections.addAll(l5,
                new GoodsItem(),
                new GoodsItem("提升企业形象", "了解丰田企业形象设计的概念", 5000f, R.mipmap.ic_item_tab5_type4_1, 10, "听丰田市场总监《企业形象设计》讲座", 5),
                new GoodsItem("提升企业形象", "学习了解丰田员工培训课程", 2500f, R.mipmap.ic_item_tab5_type4_2, 5, "参观学习风团员工形象塑造课程，听人力资源总监讲座", 3),
                new GoodsItem("提升企业形象", "参观丰田的产品生产线", 10000f, R.mipmap.ic_item_tab5_type4_3, 13, "了解产品生产设计的过程，听丰田市场总监讲座", 6),
                new GoodsItem("提升企业形象", "了解丰田企业形象的维护与推广", 25000f, R.mipmap.ic_item_tab5_type4_4, 10, "学习了解丰田企业形象的精准定位与推广模式", 10));
        map.put(1, l1);
        map.put(2, l2);
        map.put(3, l3);
        map.put(4, l4);
        map.put(5, l5);
        return map;
    }

}
