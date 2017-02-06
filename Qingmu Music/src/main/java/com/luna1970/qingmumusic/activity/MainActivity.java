package com.luna1970.qingmumusic.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.lapism.searchview.SearchView;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.fragment.MainFragment;
import com.luna1970.qingmumusic.fragment.MainFragmentViewPagerFragment;
import com.luna1970.qingmumusic.fragment.MainTopSongListFragment;
import com.luna1970.qingmumusic.fragment.PlayControlFragment;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private FloatingActionButton floatingActionButton;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setToolbar();
        setDrawerLayout();
        setNavigation();
        setFab();
        setSearchView();
        setFragment();
    }

    private void setFragment() {
        Fragment fragment = new PlayControlFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.play_control_container, fragment);
        fragmentTransaction.commit();
        Log.i(TAG, "setFragment: ");
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
                floatingActionButton.show();
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
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
            @Override
            public void onMenuClick() {
                if (drawerLayout != null) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
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
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }
}
