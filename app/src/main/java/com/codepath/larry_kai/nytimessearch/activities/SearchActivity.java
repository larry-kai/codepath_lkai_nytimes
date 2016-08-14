package com.codepath.larry_kai.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.codepath.larry_kai.nytimessearch.R;
import com.codepath.larry_kai.nytimessearch.adapters.ArticleArrayAdapter;
import com.codepath.larry_kai.nytimessearch.listeners.EndlessScrollListener;
import com.codepath.larry_kai.nytimessearch.models.Article;
import com.codepath.larry_kai.nytimessearch.models.SearchSetting;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    public static final String NYT_SEARCH_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

    GridView gvResults;

    String searchQuery;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    SearchSetting setting;

    private final int REQUEST_SETTING = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setting = new SearchSetting();
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }

    public void setupViews() {
        gvResults = (GridView) findViewById(R.id.gvResults);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        // hook up listener for grid click
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get(position);
                i.putExtra("article", article);
                startActivity(i);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadMoreArticles(page);
                return true; // XLKAI
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                searchView.clearFocus();
                int page = 0;

                AsyncHttpClient client = new AsyncHttpClient();
                client.get(NYT_SEARCH_URL, getSearchQueryParams(page), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        JSONArray articleJsonResults = null;
                        try {
                            articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                            adapter.clear();
                            adapter.addAll(Article.fromJSONArray(articleJsonResults));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    private void loadMoreArticles(int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NYT_SEARCH_URL, getSearchQueryParams(page), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), SettingActivity.class);
            startActivityForResult(i, REQUEST_SETTING);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_SETTING) {
            setting = Parcels.unwrap(data.getParcelableExtra("setting"));
        }
    }

    private RequestParams getSearchQueryParams(int page) {
        RequestParams params = new RequestParams();
        params.put("api-key", "ffc8f9a7e84e4cdbacc421df6994dd52");
        params.put("page", page);
        params.put("q", searchQuery);

        String beginDateFormatted = setting.getBeginDate(SearchSetting.FORMAT_YYYYMMDD);
        if (!TextUtils.isEmpty(beginDateFormatted)) {
            params.put("begin_date", beginDateFormatted);
        }

        String order = setting.getSortOrder();
        if (!TextUtils.isEmpty(order)) {
            params.put("sort", order);
        }

        String topic = setting.getTopic();
        if (!TextUtils.isEmpty(topic)) {
            params.put("fq", "news_desk:(\"" + topic + "\")");
        }

        Log.d("DEBUG", ">>>>>>" + params.toString());
        return params;
    }
}
