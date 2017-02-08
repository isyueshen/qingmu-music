package com.luna1970.qingmumusic.entity;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by Yue on 2/9/2017.
 *
 */

public class ViewQuerySuggestion implements SearchSuggestion{

    private String content;
    private boolean isHistory = false;

    public ViewQuerySuggestion(String suggestion) {
        this.content = suggestion.toLowerCase();
    }

    public ViewQuerySuggestion(Parcel source) {
        this.content = source.readString();
        this.isHistory = source.readInt() != 0;
    }

    public void setIsHistory(boolean isHistory) {
        this.isHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.isHistory;
    }

    @Override
    public String getBody() {
        return content;
    }

    public static final Creator<ViewQuerySuggestion> CREATOR = new Creator<ViewQuerySuggestion>() {
        @Override
        public ViewQuerySuggestion createFromParcel(Parcel in) {
            return new ViewQuerySuggestion(in);
        }

        @Override
        public ViewQuerySuggestion[] newArray(int size) {
            return new ViewQuerySuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeInt(isHistory ? 1 : 0);
    }
}
