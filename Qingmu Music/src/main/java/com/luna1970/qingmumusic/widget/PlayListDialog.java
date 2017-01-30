package com.luna1970.qingmumusic.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.luna1970.qingmumusic.R;

/**
 * Created by Yue on 1/31/2017.
 *
 */

public class PlayListDialog extends Dialog {
    private Context mContext;

    public PlayListDialog(Context context) {
        super(context, R.style.MyDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_play_list_dialog);
    }

    @Override
    public void show() {
        super.show();
        // 设置宽度全屏，要设置在show的后面
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (layoutParams != null) {
            layoutParams.gravity= Gravity.BOTTOM;
            layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
            // 边距
            window.getDecorView().setPadding(0, 0, 0, 0);
            // 设置
            window.setAttributes(layoutParams);
        }
    }


}
