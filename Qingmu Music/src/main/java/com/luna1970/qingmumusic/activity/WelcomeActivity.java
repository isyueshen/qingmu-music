package com.luna1970.qingmumusic.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.luna1970.qingmumusic.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_acitvity);

        ImageView imageView = (ImageView) findViewById(R.id.welcomeImageView);
        imageView.setImageBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.raw.welcome)));
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
        new Handler().postDelayed(new Runnable() {

            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.start_activity_alpha_in, R.anim.start_activity_scale_out);
                finish();
            }
        }, 2000);
    }
}
