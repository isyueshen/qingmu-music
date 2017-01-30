package com.luna1970.qingmumusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lapism.searchview.SearchView;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.fragment.MainFragment;
import com.luna1970.qingmumusic.fragment.MainFragmentViewPagerFragment;
import com.luna1970.qingmumusic.fragment.MainRecommendListFragment;

public class MainActivity extends BaseAcitivity {
    private static final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private FloatingActionButton floatingActionButton;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        Log.i(TAG, "onCreate: 1");
        setToolbar();
        Log.i(TAG, "onCreate: 2");
        setDrawerLayout();
        Log.i(TAG, "onCreate: 3");
        setFab();
        Log.i(TAG, "onCreate: 4");
        setSearchView();
        Log.i(TAG, "onCreate: 5");
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
        mainFragmentViewPagerFragment.addFragment(new MainRecommendListFragment(), "电台");
        viewPager.setAdapter(mainFragmentViewPagerFragment);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

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

}
