package com.luna1970.qingmumusic.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.luna1970.qingmumusic.Gson.QuerySuggestion;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.entity.ViewQuerySuggestion;
import com.luna1970.qingmumusic.fragment.PlayControlFragment;
import com.luna1970.qingmumusic.util.DataCentral;
import com.luna1970.qingmumusic.util.GlobalConst;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.luna1970.qingmumusic.application.MusicApplication.playState;

/**
 * Created by Yue on 1/28/2017.
 *
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    protected FloatingSearchView floatingSearchView;
    protected DrawerLayout drawerLayout;

    protected boolean playBarState;
    protected Fragment fragment;
    private long lastTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void setMaterialDesign() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP解决方案

            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    protected void setFloatingSearchView() {
        if (floatingSearchView != null) {
            if (drawerLayout != null) {
                floatingSearchView.attachNavigationDrawerToMenuButton(drawerLayout);
            }
            floatingSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
                @Override
                public void onActionMenuItemSelected(MenuItem item) {
                    Log.d(TAG, "onActionMenuItemSelected() called with: item = [" + item + "]");
                }
            });
            floatingSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
                @Override
                public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {
                    // 暂时不知有何用
                }
            });
            floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
                @Override
                public void onSearchTextChanged(String oldQuery, String newQuery) {
                    requestQuerySuggestion(newQuery);
                }

            });
            floatingSearchView.setDismissOnOutsideClick(true);
            floatingSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
                @Override
                public void onFocus() {
                    requestQuerySuggestion(floatingSearchView.getQuery());
                }

                @Override
                public void onFocusCleared() {
                    floatingSearchView.hideProgress();
                    floatingSearchView.clearSuggestions();
                }
            });
        }
    }
    private void requestQuerySuggestion(String newQuery) {
        if (TextUtils.isEmpty(newQuery)) {
            floatingSearchView.clearSuggestions();
            return;
        }
        // 防止用户长按delete键时持续查询
        long time = System.currentTimeMillis();
//                    Logger.d(time - lastTime);
        if (time - lastTime < 100) {
            lastTime = time;
            return;
        }
        lastTime = time;
        // 显示查询状态
        floatingSearchView.showProgress();
        DataCentral.getInstance().querySuggestion(newQuery, new DataCentral.ResponseQuerySuggestionListener() {
            @Override
            public void onResponse(final QuerySuggestion querySuggestion) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<ViewQuerySuggestion> suggestions = new ArrayList<>();
                        if (querySuggestion != null && querySuggestion.suggestionList != null) {
                            for (int i = 0; i < querySuggestion.suggestionList.size(); i++) {
                                if (i > 5) {
                                    break;
                                }
                                String content = querySuggestion.suggestionList.get(i);
                                if (TextUtils.isEmpty(content)) {
                                    suggestions.add(new ViewQuerySuggestion("没有查询结果..."));
                                } else {
                                    suggestions.add(new ViewQuerySuggestion(content));
                                }
//                                            Logger.d(querySuggestion.suggestionList.get(i));
                            }
                        } else {
                            suggestions.add(new ViewQuerySuggestion("查询失败..."));
                            Logger.d(suggestions);
                        }
                        // 显示结果
                        floatingSearchView.swapSuggestions(suggestions);
                        floatingSearchView.hideProgress();
                    }
                });
            }
        });
    }

    protected void setFragment() {
        if (playState.getListSize() > 0) {
            Logger.d(playState.getListSize());
            fragment = new PlayControlFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.play_control_container, fragment, GlobalConst.PLAY_CONTROL_BAR_FRAGMENT_TAG);
            fragmentTransaction.commit();
            playBarState = true;
        }
    }

    protected void removeFragment() {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            playBarState = false;
        }
    }
}
