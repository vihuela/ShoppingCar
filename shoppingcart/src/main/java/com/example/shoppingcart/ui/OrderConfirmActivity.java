package com.example.shoppingcart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shoppingcart.R;
import com.example.shoppingcart.util.ViewUtils;
import com.flipboard.bottomsheet.BottomSheetLayout;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.List;

import tyrantgit.explosionfield.ExplosionField;

public class OrderConfirmActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomSheetLayout bottomSheetLayout;
    private View bottomSheet;
    private TextView tvTip;
    private ExplosionField mExplosionField;
    private View mBottomSheetView;
    private Handler handler= new Handler(Looper.myLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        tvTip = (TextView) findViewById(R.id.tip);
        JumpingBeans.with(tvTip)
                .makeTextJump(tvTip.getText().toString().indexOf("邀请"), tvTip.getText().toString().length())
                .setIsWave(true)
                .setLoopDuration(1000)  // ms
                .build();
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);
        bottomSheetLayout.setPeekOnDismiss(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.share:
                showBottomSheet();
                break;
            case R.id.iv_tip:
                break;
        }

    }

    private void showBottomSheet() {
        if (bottomSheet == null) {
            bottomSheet = createBottomSheetView();
        }
        if (bottomSheetLayout.isSheetShowing()) {
            bottomSheetLayout.dismissSheet();
        } else {
            reset(mBottomSheetView);
            bottomSheetLayout.showWithSheetView(bottomSheet);
        }
    }

    private View createBottomSheetView() {
        mBottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_share, (ViewGroup) getWindow().getDecorView(), false);
        mExplosionField = ExplosionField.attach2Window(this);
        List<View> views = ViewUtils.generateViews(mBottomSheetView, R.id.iv1, R.id.iv2, R.id.iv3);
        ViewUtils.apply(views, new ViewUtils.Action<View>() {
            @Override
            public void apply(@NonNull final View view, int index) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mExplosionField.explode(v);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivityForResult(new Intent(OrderConfirmActivity.this,DetailActivity.class),1);
                            }
                        }, 1000);

                    }
                });
            }
        });
        return mBottomSheetView;
    }
    private void reset(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                reset(parent.getChildAt(i));
            }
        } else {
            root.setScaleX(1);
            root.setScaleY(1);
            root.setAlpha(1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode == RESULT_OK){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showBottomSheet();
                    }
                },500);
            }
        }
    }
}
