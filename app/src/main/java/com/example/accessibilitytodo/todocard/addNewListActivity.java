package com.example.accessibilitytodo.todocard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.accessibilitytodo.R;
import com.example.accessibilitytodo.Uiconfiguration.CongratulationHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class addNewListActivity extends AppCompatActivity   {
    private static String TAG = addNewListActivity.class.getSimpleName();
    private int year, month, day;
    private FloatingActionButton fab;
    EditText mEt;
    TextView mTvTime;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityaddnewlist);
        initView();
        initeData();
        initFontColorAndSize();
    }
    private void initFontColorAndSize() {
        mTvTime.setTextColor(CongratulationHelper.getTextColor());
        mTvTime.setTextSize(CongratulationHelper.getFrontSize()* CongratulationHelper.BASICFRONTSIZE_TITLE);
        mEt.setTextColor(CongratulationHelper.getTextColor());
        mEt.setTextSize(CongratulationHelper.getFrontSize()* CongratulationHelper.BASICFRONTSIZE_TEXT);
    }

    private void initeData() {
        Intent intent=getIntent();
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if(intent.getStringExtra("date")==null){
            mTvTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            mTvTime.setContentDescription("当前代表事项日期:"+year+"年"+month+"月"+day+"日，再次单击便可以修改时间");
        }
        else{
            mTvTime.setText(intent.getStringExtra("date"));
            String[] splitTime=intent.getStringExtra("date").split("-",3);
            mTvTime.setContentDescription("当前代表事项日期:"+splitTime[0]+"年"+splitTime[1]+"月"+splitTime[2]+"日，再次单击便可以修改时间");
        }
        if(intent.getStringExtra("date")==null){
            mEt.setText("");
            mEt.setHint("在这里输入您的代办事项");
        }
        else{
            mEt.setText(intent.getStringExtra("content"));
        }
        datePicker.setMinDate(System.currentTimeMillis());
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                addNewListActivity.this.year = year;
                addNewListActivity.this.month = monthOfYear+1;
                addNewListActivity.this.day = dayOfMonth;
                mTvTime.setText(year + " - " + (monthOfYear+1) + " - " + dayOfMonth);
                datePicker.setVisibility(View.GONE);
                //  show(year, monthOfYear, dayOfMonth);
            }
        });
        datePicker.setVisibility(View.GONE);
    }


    private void initView() {
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mEt = (EditText) findViewById(R.id.et);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        mTvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.VISIBLE);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "编辑完成");
                Intent intent=getIntent();
                intent.putExtra("date",mTvTime.getText());
                intent.putExtra("content",mEt.getText().toString());
                setResult(TodoListActivity.ADD_RESULT_OK,intent);
                finish();
            }
        });
    }

    private void show(int year, int month, int day) {
        String data = year + "年" + (month + 1) + "月" + day + "日";
        Toast.makeText(addNewListActivity.this, data, Toast.LENGTH_LONG).show();
    }
}
