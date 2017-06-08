package com.example.shoppingcart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.shoppingcart.R;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user:
                finish();
                break;
            case R.id.switch_user:
                getSharedPreferences("login", MODE_PRIVATE).edit().putBoolean("login", false).apply();
                startActivity(new Intent(this, UserLoginActivity.class));
                finish();
                break;
        }

    }
}
