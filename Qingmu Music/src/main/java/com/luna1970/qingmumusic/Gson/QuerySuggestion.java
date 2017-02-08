package com.luna1970.qingmumusic.Gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yue on 2/9/2017.
 *
 */

public class QuerySuggestion {

    public String query;

    @SerializedName("suggestion_list")
    public List<String> suggestionList;

}
