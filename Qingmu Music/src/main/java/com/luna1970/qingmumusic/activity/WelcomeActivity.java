package com.luna1970.qingmumusic.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.util.HttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_acitvity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP解决方案
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        final ImageView imageView = (ImageView) findViewById(R.id.welcome_iv);
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Intent intent = new Intent(WelcomeActivity.this, MusicListActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.image_switcher_translate_in_right2left, R.anim.image_switcher_translate_out_right2left);
////                finish();
//            }
//        }.start();
        String api = "http://guolin.tech/api/bing_pic";
        HttpUtils.sendHttpRequest(api, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WelcomeActivity.this).load("http://cn.bing.com/az/hprichbg/rb/Shimaenaga_ZH-CN14747993510_1920x1080.jpg").into(imageView);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String uri = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WelcomeActivity.this).load(uri).into(imageView);

                    }
                });
            }
        });
        new Handler().postDelayed(new Runnable() {

            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.start_activity_alpha_in, R.anim.start_activity_scale_out);
                finish();
            }
        }, 4000);
    }
}
