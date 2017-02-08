package com.luna1970.qingmumusic.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Process;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bumptech.glide.Glide;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.luna1970.qingmumusic.Gson.QuerySuggestion;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.dao.SongDao;
import com.luna1970.qingmumusic.entity.ViewQuerySuggestion;
import com.luna1970.qingmumusic.fragment.MainFragment;
import com.luna1970.qingmumusic.fragment.MainFragmentViewPagerFragment;
import com.luna1970.qingmumusic.fragment.MainTopSongListFragment;
import com.luna1970.qingmumusic.fragment.PlayControlFragment;
import com.luna1970.qingmumusic.util.DataCentral;
import com.luna1970.qingmumusic.util.GlideCacheUtil;
import com.luna1970.qingmumusic.util.GlobalConst;
import com.luna1970.qingmumusic.util.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.luna1970.qingmumusic.application.MusicApplication.playState;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    public static final int REQUEST_PERMISSION_VOICE = 1;
    private DrawerLayout drawerLayout;
    private FloatingActionButton floatingActionButton;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private boolean playBarState = false;
    private Fragment fragment;
    private SearchView searchView;
    private SearchAdapter adapter;
    private List<SearchItem> searchItemList;
    private Handler handler;
    private FloatingSearchView floatingSearchView;
    private long lastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setToolbar();
        setDrawerLayout();
        setNavigation();
        setFab();
//        setSearchView();
        setFloatingSearchView();
        playState.updatePlayList(SongDao.getSongList());
        setFragment();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                searchItemList.add(new SearchItem("dfdsaf"));
                adapter.notifyItemInserted(0);
            }
        };
    }

    private void setFragment() {
        if (playState.getListSize() > 0) {
            Logger.d(playState.getListSize());
            fragment = new PlayControlFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.play_control_container, fragment, GlobalConst.PLAY_CONTROL_BAR_FRAGMENT_TAG);
            fragmentTransaction.commit();
            Log.i(TAG, "setFragment: " + playState.getListSize());
            playBarState = true;
        }
    }

    private void removeFragment() {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            playBarState = false;
        }
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
            actionBar.setTitle("text");
        }
    }

    private void setViews() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        MainFragmentViewPagerFragment mainFragmentViewPagerFragment = new MainFragmentViewPagerFragment(getSupportFragmentManager());
        mainFragmentViewPagerFragment.addFragment(new MainFragment(), "榜单");
        mainFragmentViewPagerFragment.addFragment(new MainTopSongListFragment(), "电台");
        viewPager.setAdapter(mainFragmentViewPagerFragment);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
//        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                appBarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setSearchView() {
        searchView = (SearchView) findViewById(R.id.search_view);
        if (searchView != null) {
            // hint
            searchView.setHint(R.string.appWigetSeracheViewHint);
            // drawer layout btn
            searchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
                @Override
                public void onMenuClick() {
                    if (drawerLayout != null) {
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                }
            });
            searchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public boolean onClose() {
//                    setFragment();
                    return false;
                }

                @Override
                public boolean onOpen() {
//                    removeFragment();
                    return false;
                }
            });
            // search
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.showSuggestions();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(final String newText) {
                    searchItemList.clear();
                    adapter.notifyDataSetChanged();
                    handler.sendEmptyMessage(1);
//                    MainActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            searchItemList.add(new SearchItem(newText));
//                            adapter.notifyItemInserted(0);
//                        }
//                    });
                    if (TextUtils.isEmpty(newText)) {
                        return false;
                    }
                    DataCentral.getInstance().querySuggestion(newText, new DataCentral.ResponseQuerySuggestionListener() {
                        @Override
                        public void onResponse(final QuerySuggestion querySuggestion) {

//                            MainActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (querySuggestion != null && querySuggestion.suggestionList != null) {
//                                        for (int i = 0; i < querySuggestion.suggestionList.size(); i++) {
//                                            if (i > 5) {
//                                                break;
//                                            }
//                                            searchItemList.add(new SearchItem(querySuggestion.suggestionList.get(i)));
//                                            adapter.notifyItemInserted(i);
//                                            Logger.d(querySuggestion.suggestionList.get(i));
//                                        }
//                                    } else {
//                                        searchItemList.add(new SearchItem("没有结果"));
//                                        adapter.notifyItemInserted(0);
//                                    }
//
//                                }
//                            });
                        }
                    });
                    return false;
                }
            });
            searchView.setVoiceText("语音识别: 点击按钮开始识别!");
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
            searchItemList = new ArrayList<>();
            adapter = new SearchAdapter(MainActivity.this, searchItemList);
            adapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ToastUtils.show(position + "");
                }
            });
            searchView.setAdapter(adapter);
            SearchHistoryTable searchHistoryTable = new SearchHistoryTable(this);
            searchHistoryTable.setHistorySize(5);
            searchHistoryTable.clearDatabase();
        }
    }


    private void setFloatingSearchView() {
        floatingSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        if (floatingSearchView != null) {
            floatingSearchView.attachNavigationDrawerToMenuButton(drawerLayout);
//            floatingSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
//                @Override
//                public void onMenuOpened() {
//                    drawerLayout.openDrawer(GravityCompat.START);
//                }
//
//                @Override
//                public void onMenuClosed() {
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                }
//            });
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
            floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
                @Override
                public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                    Log.d(TAG, "onSuggestionClicked() called with: searchSuggestion = [" + searchSuggestion + "]");
                }

                @Override
                public void onSearchAction(String currentQuery) {
                    Log.d(TAG, "onSearchAction() called with: currentQuery = [" + currentQuery + "]");
                }
            });
            floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
                @Override
                public void onSearchTextChanged(String oldQuery, String newQuery) {
                    if (TextUtils.isEmpty(newQuery)) {
                        floatingSearchView.clearSuggestions();
                        return;
                    }
                    // 防止用户长按delete键时持续查询
                    long time = System.currentTimeMillis();
                    Logger.d(time - lastTime);
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
                            MainActivity.this.runOnUiThread(new Runnable() {
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
            });
            floatingSearchView.setDismissOnOutsideClick(true);
            floatingSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
                @Override
                public void onFocus() {
                    floatingSearchView.showProgress();
                }

                @Override
                public void onFocusCleared() {
                    floatingSearchView.hideProgress();
                    floatingSearchView.clearQuery();
                    floatingSearchView.clearSuggestions();
                }
            });
        }
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
