package com.codepath.larry_kai.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.codepath.larry_kai.nytimessearch.R;
import com.codepath.larry_kai.nytimessearch.models.SearchSetting;

public class SettingActivity extends AppCompatActivity {

    private SearchSetting setting = new SearchSetting();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Spinner spOrder = (Spinner) findViewById(R.id.spOrder);
        spOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setting.setSortOrder(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Spinner spTopics = (Spinner) findViewById(R.id.spTopics);
        spTopics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setting.setTopics(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void onClickSave(View view) {
        Intent data = new Intent();
        data.putExtra("setting", setting);
        setResult(RESULT_OK, data);
        this.finish();
    }

    public void onClickCancel(View view) {
        this.finish();
    }
}
