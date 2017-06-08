package com.example.shoppingcart.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.shoppingcart.R;
import com.example.shoppingcart.adapter.SelectAdapter;
import com.example.shoppingcart.interfaces.IShopingCar;
import com.example.shoppingcart.model.GoodsItem;
import com.example.shoppingcart.util.DividerDecoration;
import com.example.shoppingcart.util.InterceptViewpager;
import com.example.shoppingcart.util.TabEntity;
import com.example.shoppingcart.util.ViewDirectionDetector;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity implements View.OnClickListener, IShopingCar {

    public ViewDirectionDetector.Direction mCurrentBottomSheetLayoutDirection;
    private FloatingActionButton imgCart;
    private ViewGroup anim_mask_layout;//必须是RelativeLayout
    private RecyclerView rvSelected;
    private TextView tvCount, tvCost, tvSubmit, tvDay;
    private BottomSheetLayout bottomSheetLayout;
    private View bottomSheet_ShoppingList, bottomSheet_Detail;
    private CommonTabLayout toptab;
    private InterceptViewpager viewpager;
    private SelectAdapter selectAdapter;//已购列表
    private NumberFormat nf;
    private Handler mHanlder;
    private SparseArray<GoodsItem> selectedList;//在购物车中的列表：key=item.id val=item
    private List<IShopingCar> fragments;
    private ImageView user;
    private TopRightMenu mTopRightMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        mHanlder = new Handler(getMainLooper());
        selectedList = new SparseArray<>();
        initView();
    }

    private void initView() {
        toptab = (CommonTabLayout) findViewById(R.id.toptab);
        user = (ImageView) findViewById(R.id.user);
        viewpager = (InterceptViewpager) findViewById(R.id.pager);
        tvCount = (TextView) findViewById(R.id.tvCount);
        tvCost = (TextView) findViewById(R.id.tvCost);
        tvDay = (TextView) findViewById(R.id.tvDay);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);


        imgCart = (FloatingActionButton) findViewById(R.id.imgCart);
        anim_mask_layout = (RelativeLayout) findViewById(R.id.containerLayout);
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);
        bottomSheetLayout.setPeekOnDismiss(true);
        bottomSheetLayout.setPeekSheetTranslation(getWindowManager().getDefaultDisplay().getHeight());

        //顶部tab
        ArrayList<CustomTabEntity> tabEntityList = new ArrayList<>();
        Collections.addAll(tabEntityList,
                new TabEntity("精神文化", R.mipmap.ic_tab2, R.mipmap.ic_tab2),
                new TabEntity("组织管理", R.mipmap.ic_tab1, R.mipmap.ic_tab1),
                new TabEntity("知识技能", R.mipmap.ic_tab3, R.mipmap.ic_tab3));
        toptab.setTabData(tabEntityList);
        //内容列表
        fragments = new ArrayList<>();
        Collections.addAll(fragments, new ShopingCarFrg4Culture());
        viewpager.setNoScroll(true);
        viewpager.setOffscreenPageLimit(fragments.size());
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return (Fragment) fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        toptab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewpager.setCurrentItem(position, false);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        user.setImageResource(getSharedPreferences("login", MODE_PRIVATE).getBoolean("login", false) ?
                R.drawable.ic_top_user_login :
                R.drawable.ic_top_user_logout);
    }

    public void playAnimation(int[] start_location, int drawable) {
        ImageView img = new ImageView(this);
        img.setImageResource(drawable);
        setAnim(img, start_location);
    }

    private Animation createAnim(int startX, int startY) {
        int[] des = new int[2];
        imgCart.getLocationInWindow(des);

        AnimationSet set = new AnimationSet(false);

        Animation translationX = new TranslateAnimation(0, des[0] - startX, 0, 0);
        translationX.setInterpolator(new LinearInterpolator());
        Animation translationY = new TranslateAnimation(0, 0, 0, des[1] - startY);
        translationY.setInterpolator(new AccelerateInterpolator());
        Animation alpha = new AlphaAnimation(1, 0.5f);
        set.addAnimation(translationX);
        set.addAnimation(translationY);
        set.addAnimation(alpha);
        set.setDuration(500);

        return set;
    }

    private void addViewToAnimLayout(final ViewGroup vg, final View view,
                                     int[] location) {

        int x = location[0];
        int y = location[1];
        int[] loc = new int[2];
        vg.getLocationInWindow(loc);
        view.setX(x);
        view.setY(y - loc[1]);
        vg.addView(view);
    }

    private void setAnim(final View v, int[] start_location) {

        addViewToAnimLayout(anim_mask_layout, v, start_location);
        Animation set = createAnim(start_location[0], start_location[1]);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                v.setVisibility(View.GONE);
                mHanlder.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        anim_mask_layout.removeView(v);
                    }
                }, 100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(set);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgCart:
                showBottomSheetForShoppingList();
                break;
            case R.id.clear:
                clearCart();
                break;
            case R.id.tvSubmit:
                startActivity(new Intent(this, OrderConfirmActivity.class));
                break;
            case R.id.user:
                //登录与切换
                if (getSharedPreferences("login", MODE_PRIVATE).getBoolean("login", false)) {
                    startActivity(new Intent(this, UserInfoActivity.class));
                } else {
                    startActivity(new Intent(this, UserLoginActivity.class));
                }
                break;
            default:
                break;
        }
    }

    //添加商品
    public void add(GoodsItem item, boolean refreshGoodList) {

        GoodsItem temp = selectedList.get(item.id);
        if (temp == null) {
            item.count = 1;
            selectedList.append(item.id, item);
        } else {
            temp.count++;
        }
        //刷新内容
        for (IShopingCar f : fragments) {
            f.add(item, refreshGoodList);

        }

    }

    //移除商品
    public void remove(GoodsItem item, boolean refreshGoodList) {

        GoodsItem temp = selectedList.get(item.id);
        if (temp != null) {
            if (temp.count < 2) {
                selectedList.remove(item.id);
            } else {
                item.count--;
            }
        }
        //刷新内容
        for (IShopingCar f : fragments) {
            f.remove(item, refreshGoodList);
        }
    }

    //刷新布局 总价、购买数量等
    public void update(boolean refreshGoodList) {
        int size = selectedList.size();
        int count = 0;
        double cost = 0;
        int day = 0;
        for (int i = 0; i < size; i++) {
            GoodsItem item = selectedList.valueAt(i);
            count += item.count;
            cost += item.count * item.price;
            day += item.day;
        }

        if (count < 1) {
            tvCount.setVisibility(View.GONE);
        } else {
            tvCount.setVisibility(View.VISIBLE);
        }

        tvCount.setText(String.valueOf(count));

        //触发购买条件（100元起送）
        if (cost > 99.99) {
            tvSubmit.setVisibility(View.VISIBLE);
        } else {
            tvSubmit.setVisibility(View.GONE);
        }

        tvCost.setText(nf.format(cost));
        tvDay.setText(day + "天");

        if (selectAdapter != null) {
            selectAdapter.notifyDataSetChanged();
        }

        if (bottomSheetLayout.isSheetShowing() && selectedList.size() < 1) {
            bottomSheetLayout.dismissSheet();
        }

        //刷新内容
        for (IShopingCar f : fragments) {
            f.update(refreshGoodList);

        }
    }

    //清空购物车
    public void clearCart() {
        selectedList.clear();
        //刷新内容
        for (IShopingCar f : fragments) {
            f.clearCart();
        }
        update(true);

    }

    public void onTypeClicked(int typeId) {
        //刷新内容
        for (IShopingCar f : fragments) {
            f.onTypeClicked(typeId);

        }
    }

    //根据商品id获取当前商品的采购数量
    public int getSelectedItemCountById(int id) {
        GoodsItem temp = selectedList.get(id);
        if (temp == null) {
            return 0;
        }
        return temp.count;
    }

    //根据类别Id获取属于当前类别的数量
    public int getSelectedGroupCountByTypeId(int typeId) {
        //刷新内容
        for (IShopingCar f : fragments) {
            return f.getSelectedGroupCountByTypeId(typeId);
        }
        return 0;
    }


    //底部购物列表
    private View createBottomSheetView_ShoppingList() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet, (ViewGroup) getWindow().getDecorView(), false);
        rvSelected = (RecyclerView) view.findViewById(R.id.selectRecyclerView);
        rvSelected.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration decor = new DividerDecoration(this);
        decor.setDividerOffset(8f);
        decor.setDivider(R.drawable.recycler_divider_gray);
        rvSelected.addItemDecoration(decor);
        LinearLayout clear = (LinearLayout) view.findViewById(R.id.clear);
        clear.setOnClickListener(this);
        selectAdapter = new SelectAdapter(this, selectedList);
        rvSelected.setAdapter(selectAdapter);
        return view;
    }

    private void showBottomSheetForShoppingList() {
        if (bottomSheet_ShoppingList == null) {
            bottomSheet_ShoppingList = createBottomSheetView_ShoppingList();
        }
        if (bottomSheetLayout.isSheetShowing()) {
            bottomSheetLayout.dismissSheet();
        } else {
            if (selectedList.size() != 0) {
                bottomSheetLayout.showWithSheetView(bottomSheet_ShoppingList);
            }
        }
    }

    //商品详情
    private View createBottomSheetView_Detail() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_detail, (ViewGroup) getWindow().getDecorView(), false);
        //关闭
        view.findViewById(R.id.user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetLayout.isSheetShowing()) {
                    bottomSheetLayout.dismissSheet();
                }
            }
        });
        //banner
        MZBannerView mMZBanner = (MZBannerView) view.findViewById(R.id.banner);
        List<Integer> ids = new ArrayList<>();
        Collections.addAll(ids, R.mipmap.detail_1, R.mipmap.detail_2, R.mipmap.detail_3);
        mMZBanner.setPages(ids, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        //add anim
        final ImageView iv_add = (ImageView) view.findViewById(R.id.iv_add);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] loc = new int[2];
                iv_add.getLocationInWindow(loc);
                playAnimation(loc, R.drawable.ic_detail_add);
            }
        });
        //share
        final ImageView share = (ImageView) view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTopRightMenu == null) {
                    mTopRightMenu = new TopRightMenu(ShoppingCartActivity.this);
                    List<MenuItem> menuItems = new ArrayList<>();
                    menuItems.add(new MenuItem(R.mipmap.ic_share_weixin_1, "微信"));
                    menuItems.add(new MenuItem(R.mipmap.ic_share_weixinf_1, "朋友圈"));
                    menuItems.add(new MenuItem(R.mipmap.ic_share_sina_1, "微博"));

                    mTopRightMenu
                            .setHeight(getDp(160))     //默认高度480
                            .setWidth(getDp(150))      //默认宽度wrap_content
                            .showIcon(true)     //显示菜单图标，默认为true
                            .dimBackground(true)        //背景变暗，默认为true
                            .needAnimationStyle(true)   //显示动画，默认为true
                            .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                            .addMenuList(menuItems)
                            .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                                @Override
                                public void onMenuItemClick(int position) {
                                    startActivity(new Intent(ShoppingCartActivity.this,DetailActivity.class));
                                }
                            });
                }
                mTopRightMenu.showAsDropDown(v, getDp(-80), 0);
            }
        });
        //viewPager
//        MZBannerView pager = (MZBannerView)view.findViewById(R.id.pager);
//        Collections.addAll(ids,R.mipmap.detail_1,R.mipmap.detail_2,R.mipmap.detail_3);
//        pager.setPages(ids, new MZHolderCreator<BannerViewHolder>() {
//            @Override
//            public BannerViewHolder createViewHolder() {
//                return new BannerViewHolder();
//            }
//        });
        InterceptViewpager pager = (InterceptViewpager) view.findViewById(R.id.pager);

        List<Integer> idss = new ArrayList<>();
        Collections.addAll(idss, R.mipmap.item_detail_tab1, R.mipmap.item_detail_tab2, R.mipmap.item_detail_tab3);
        pager.setAdapter(new MZBannerView.MZPagerAdapter<Integer>(idss, new MZHolderCreator() {
            @Override
            public MZViewHolder createViewHolder() {
                return new BannerViewHolder_();
            }
        }, false));
        final SlidingTabLayout tab = (SlidingTabLayout) view.findViewById(R.id.tab);
        tab.setViewPager(pager, new String[]{"课程介绍", "行程与费用", "讲师简介"});
        return view;
    }

    public void showBottomSheetForDetail(String title) {
        if (bottomSheet_Detail == null) {
            bottomSheet_Detail = createBottomSheetView_Detail();
        }
        if (bottomSheetLayout.isSheetShowing()) {
            bottomSheetLayout.dismissSheet();
        } else {
            ((TextView) bottomSheet_Detail.findViewById(R.id.title)).setText(title);
            ((ScrollView) bottomSheet_Detail.findViewById(R.id.sv)).fullScroll(ScrollView.FOCUS_UP);
            final InterceptViewpager interceptViewpager = (InterceptViewpager) bottomSheet_Detail.findViewById(R.id.pager);
            interceptViewpager.post(new Runnable() {
                @Override
                public void run() {

                    BitmapFactory.Options options = new BitmapFactory.Options();

                    options.inJustDecodeBounds = true;
                    Bitmap bitmap = BitmapFactory.decodeResource(ShoppingCartActivity.this.getResources(), R.mipmap.item_detail_tab1, options);
//
                    interceptViewpager.setLayoutParams(new LinearLayout.LayoutParams(-1, options.outHeight + getDp(300f)));
                    bottomSheetLayout.showWithSheetView(bottomSheet_Detail);
                }
            });


        }
    }

    public int getDp(float dividerOffset) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerOffset, getResources().getDisplayMetrics());
    }

    public static class BannerViewHolder_ implements MZViewHolder<Integer> {
        private SubsamplingScaleImageView iv;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item_, null);
            iv = (SubsamplingScaleImageView) view.findViewById(R.id.iv);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            iv.setImage(ImageSource.resource(data));
        }
    }

    public static class BannerViewHolder implements MZViewHolder<Integer> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            mImageView.setImageResource(data);
        }
    }
}
