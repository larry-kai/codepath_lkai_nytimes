package com.codepath.larry_kai.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.larry_kai.nytimessearch.R;
import com.codepath.larry_kai.nytimessearch.fragments.DatePickerFragment;
import com.codepath.larry_kai.nytimessearch.models.SearchSetting;

public class SettingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private SearchSetting setting = new SearchSetting();
    private TextView tvBeginDate;

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        setting.setBeginDate(year, monthOfYear, dayOfMonth);
        tvBeginDate.setText(setting.getBeginDate(SearchSetting.FORMAT_MONTH_DAY_YEAR));
    }

    public void showDatePickerDialog(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tvBeginDate = (TextView) findViewById(R.id.tvBeginDate);

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

    public void onClickBeginDate(View view) {
        showDatePickerDialog(view);
    }
}
