package com.example.grapes_pradip.vimalsagaradmin.activities.competition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.example.grapes_pradip.vimalsagaradmin.R;

public class SpecificDateWiseResultDeclareActivity extends AppCompatActivity {

    private EditText edt_percentage;
    private Button btn_submit;

    private NumberPicker numberPicker_s_day, numberPicker_s_month, numberPicker_s_year, numberPicker_e_day, numberPicker_e_month, numberPicker_e_year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_date_wise_result_declare);
        bindID();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edt_percentage.getText().toString())) {
                    edt_percentage.setError("Please enter attend competition.");
                    edt_percentage.requestFocus();
                } else {

                    Intent intent = new Intent(SpecificDateWiseResultDeclareActivity.this, WinnerListActivity.class);
                    intent.putExtra("start_date", String.valueOf(numberPicker_s_year.getValue())+"-"+String.valueOf(numberPicker_s_month.getValue())+"-"+numberPicker_s_day.getValue());
                    intent.putExtra("end_date", String.valueOf(numberPicker_e_year.getValue())+"-"+String.valueOf(numberPicker_e_month.getValue())+"-"+numberPicker_e_day.getValue());
                    intent.putExtra("percentage", edt_percentage.getText().toString());
                    startActivity(intent);
                }

            }
        });
    }

    private void bindID() {
//        edt_start_date = findViewById(R.id.edt_start_date);
//        edt_end_date = findViewById(R.id.edt_end_date);
        edt_percentage = findViewById(R.id.edt_percentage);
        btn_submit = findViewById(R.id.btn_submit);

        numberPicker_s_day = findViewById(R.id.numberPicker_s_day);
        numberPicker_s_month = findViewById(R.id.numberPicker_s_month);
        numberPicker_s_year = findViewById(R.id.numberPicker_s_year);

        numberPicker_e_day = findViewById(R.id.numberPicker_e_day);
        numberPicker_e_month = findViewById(R.id.numberPicker_e_month);
        numberPicker_e_year = findViewById(R.id.numberPicker_e_year);


        numberPicker_s_day.setMinValue(1);
        numberPicker_s_day.setMaxValue(31);
        numberPicker_s_day.setValue(20);

        numberPicker_s_month.setMinValue(1);
        numberPicker_s_month.setMaxValue(12);
        numberPicker_s_month.setValue(11);

        numberPicker_s_year.setMinValue(2011);
        numberPicker_s_year.setMaxValue(2025);
        numberPicker_s_year.setValue(2018);

        numberPicker_e_day.setMinValue(1);
        numberPicker_e_day.setMaxValue(31);
        numberPicker_e_day.setValue(20);

        numberPicker_e_month.setMinValue(1);
        numberPicker_e_month.setMaxValue(12);
        numberPicker_e_month.setValue(11);

        numberPicker_e_year.setMinValue(2011);
        numberPicker_e_year.setMaxValue(2025);
        numberPicker_e_year.setValue(2019);

    }
}
