package com.codepath.larry_kai.nytimessearch.models;

import java.io.Serializable;

/**
 * Created by larry_kai on 8/14/16.
 */
public class SearchSetting implements Serializable {

    // XLKAI
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd"; //"YYYYMMDD";
    public static final String FORMAT_MONTH_DAY_YEAR = "MM/dd/yyyy";

    public static final String SORT_ORDER_DEFAULT = "Default";
    public static final String SORT_ORDER_NEWEST = "Newest";
    public static final String SORT_ORDER_OLDEST = "Oldest";

    public static final String NEWS_DESK_DEFAULT = "Default";
    public static final String NEWS_DESK_ARTS = "Arts";
    public static final String NEWS_DESK_FASHION_AND_STYLE = "Fashion & Style";
    public static final String NEWS_DESK_SPORTS = "Sports";

    Long beginDateTimestamp = null;
    String sortOrder = SORT_ORDER_DEFAULT;
    String topic = NEWS_DESK_DEFAULT;

    public void clear() {
        beginDateTimestamp = null;
        sortOrder = SORT_ORDER_DEFAULT;
        topic = NEWS_DESK_DEFAULT;
    }

    public String getBeginDate(String format) {
        CharSequence str = "";
        if (beginDateTimestamp != null) {
            str = android.text.format.DateFormat.format(format, beginDateTimestamp);
        }
        return str.toString();
    }

    public void setBeginDateTimestamp(Long timestamp) {
        if (timestamp > 0) {
            beginDateTimestamp = timestamp;
        } else {
            beginDateTimestamp = null;
        }
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String newSortOrder) {
        switch (newSortOrder) {
            case SORT_ORDER_NEWEST:
                sortOrder = SORT_ORDER_NEWEST;
                break;
            case SORT_ORDER_OLDEST:
                sortOrder = SORT_ORDER_OLDEST;
                break;
            default:
                sortOrder = SORT_ORDER_DEFAULT;
        }
    }

    public String getTopic() {
        return topic;
    }

    public void setTopics(String newTopic) {
        switch (newTopic) {
            case NEWS_DESK_ARTS:
                topic = NEWS_DESK_ARTS;
                break;
            case NEWS_DESK_FASHION_AND_STYLE:
                topic = NEWS_DESK_FASHION_AND_STYLE;
                break;
            case NEWS_DESK_SPORTS:
                topic = NEWS_DESK_SPORTS;
                break;
            default:
                topic = NEWS_DESK_DEFAULT;
        }
    }
}