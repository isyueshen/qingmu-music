package com.luna1970.qingmumusic.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bumptech.glide.Glide;
import com.lapism.searchview.SearchView;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.dao.SongDao;
import com.luna1970.qingmumusic.fragment.MainFragment;
import com.luna1970.qingmumusic.fragment.ViewPagerFragmentAdapter;
import com.luna1970.qingmumusic.fragment.PlayListFragment;
import com.luna1970.qingmumusic.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static com.luna1970.qingmumusic.application.MusicApplication.playState;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    public static final int REQUEST_PERMISSION_VOICE = 1;
    private FloatingActionButton floatingActionButton;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private SearchView searchView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMaterialDesign();


        setContentView(R.layout.activity_main);
        setViews();
        setToolbar();
        setDrawerLayout();
        setNavigation();
        setFab();
        floatingSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        setFloatingSearchView();
        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                SearchResultActivity.startAction(MainActivity.this, searchSuggestion.getBody());
            }

            @Override
            public void onSearchAction(String currentQuery) {
                SearchResultActivity.startAction(MainActivity.this, currentQuery);
            }
        });
        playState.setPlayList(SongDao.getSongList());
        setFragment();
    }


    private void setFab() {
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();
    }

    private void setDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                floatingActionButton.hide();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                floatingActionButton.show();
            }
        };
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }

    private void setNavigation() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exit:
                        showAlertDialog();
                        break;
                    case R.id.setting:
                        Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
                        startActivity(intent);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }

        });
//        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                GlideCacheUtil.getInstance().clearImageMemoryCache();
//                return super.onScroll(e1, e2, distanceX, distanceY);
//            }
//        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("退出")
                .setMessage("确定要退出吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Process.killProcess(Process.myPid());
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    private void setViews() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new MainFragment());
        fragmentList.add(new PlayListFragment());
        List<String> titleList = new ArrayList<>();
        titleList.add("榜单");
        titleList.add("电台");
        ViewPagerFragmentAdapter viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(viewPagerFragmentAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
//        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                appBarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                viewPager.setCurrentItem(tab.getPosition());
                Log.i(TAG, "onTabSelected: " + viewPager.getChildCount() + viewPager.getCurrentItem());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i(TAG, "onTabUnselected: ");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i(TAG, "onTabReselected: ");
            }
        });
    }

    private void setSearchView() {
        searchView.setOnVoiceClickListener(new SearchView.OnVoiceClickListener() {
            @Override
            public void onVoiceClick() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED) {
                        searchView.setVoiceText("请授权该应用使用语音权限");
                        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_VOICE);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            // 请求语音权限
            case REQUEST_PERMISSION_VOICE:
                if (permissions[0].equals(Manifest.permission.RECORD_AUDIO) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.show("您已经授权, 请重新点击使用语音识别");
                    searchView.setVoiceText("语音识别: 点击按钮开始识别!");
                } else {
                    ToastUtils.show("您拒绝了麦克风使用权限! 相关功能将无法使用");
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.i(TAG, "onWindowFocusChanged: ");
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        if (!playBarState) {
            setFragment();
        }
        if (playBarState && playState.getListSize() == 0) {
            removeFragment();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        Glide.get(MainActivity.this).clearMemory();
        super.onStop();
    }
}
