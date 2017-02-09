package com.luna1970.qingmumusic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.luna1970.qingmumusic.Gson.QueryResult;
import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.adapter.SearchResultListAdapter;
import com.luna1970.qingmumusic.util.DataCentral;
import com.luna1970.qingmumusic.util.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends BaseActivity {

    public static final String QUERY_STRING_EXTRA = "QUERY_STRING_EXTRA";
    private String querySting;
    private RecyclerView recyclerView;
    private List<Song> songList;
    private SearchResultListAdapter searchResultListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        initView();
        childSetFloatingSearchView();
        initData();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        songList = new ArrayList<>();
        searchResultListAdapter = new SearchResultListAdapter(songList);
        searchResultListAdapter.setItemOnClickListener(new SearchResultListAdapter.ItemOnClickListener() {
            @Override
            public void onClick(int position) {
                ToastUtils.makeText(position + "");
            }
        });
        searchResultListAdapter.setMenuOnClickListener(new SearchResultListAdapter.MenuOnClickListener() {
            @Override
            public void onClick(int position) {
                ToastUtils.makeText(position + "");
            }
        });
        recyclerView.setAdapter(searchResultListAdapter);
    }

    private void childSetFloatingSearchView() {
        floatingSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        super.setFloatingSearchView();
        floatingSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                finish();
            }
        });
        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                requestQueryResult(searchSuggestion.getBody());
                floatingSearchView.setSearchText(searchSuggestion.getBody());
            }

            @Override
            public void onSearchAction(String currentQuery) {
                requestQueryResult(currentQuery);
                floatingSearchView.setSearchText(currentQuery);
            }
        });
    }

    private void initData() {
        querySting = getIntent().getStringExtra(QUERY_STRING_EXTRA);
        floatingSearchView.setSearchText(querySting);
        requestQueryResult(querySting);
    }

    protected void requestQueryResult(String query) {
        DataCentral.getInstance().queryResult(query, new DataCentral.ResponseQueryResultListener() {
            @Override
            public void onResponse(final QueryResult queryResult) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       if (queryResult != null) {
                           songList.clear();
                           Logger.d(queryResult);
                           if (queryResult.songList != null && !queryResult.songList.isEmpty()) {
                               songList.addAll(queryResult.songList);
                               searchResultListAdapter.notifyDataSetChanged();
                           }
                       } else {
                           ToastUtils.makeText("failed");
                       }
                   }
               });
            }
        });
    }

    /**
     * start this activity
     * @param context 当前context
     * @param queryString 启动参数(查询字符串)
     */
    public static void startAction(Context context, String queryString) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra(QUERY_STRING_EXTRA, queryString);
        context.startActivity(intent);
    }
}