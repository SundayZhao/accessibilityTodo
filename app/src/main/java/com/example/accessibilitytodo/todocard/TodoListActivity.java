package com.example.accessibilitytodo.todocard;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accessibilitytodo.R;
import com.example.accessibilitytodo.Uiconfiguration.CongratulationHelper;
import com.example.accessibilitytodo.Uiconfiguration.UIconfigurationActivity;
import com.example.accessibilitytodo.accessibilitypackage.strongAccessibilityService;
import com.example.accessibilitytodo.sqliteUtils.DataBaseOpenHelper;
import com.example.accessibilitytodo.todocard.cardview.adapter.DoneCardViewAdapter;
import com.example.accessibilitytodo.todocard.cardview.adapter.TodoCardViewAdapter;
import com.example.accessibilitytodo.todocard.cardview.entity.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TodoListActivity extends AppCompatActivity implements SensorEventListener, DoneCardViewAdapter.OnItemClickListener {
    public static int ADD_RESULT_OK = 1;
    public static int ADD_RESULT_ADD = 2;
    public static int ADD_RESULT_NOTIFY_TODO = 3;
    public static int ADD_RESULT_NOTIFY_DONE = 4;

    TextView todoTextView;
    TextView doneTextView;
    ImageButton addTodoButton;
    ImageButton settingButton;
    RecyclerView todolistview;
    RecyclerView donelistview;
    LinearLayoutManager mLayoutManager;
    TodoCardViewAdapter todoAdapter;
    DoneCardViewAdapter doneAdapter;
    List<Card> todoList = new ArrayList<>();
    List<Card> doneList = new ArrayList<>();

    private SensorManager mSensorManager;
    //????????????????????????????????????
    private float mTimestamp = 0;
    //???????????????????????????XYZ
    private double oldDegree[] = new double[]{0, 0, 0};
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float[] mRotationMatrix = new float[9];
    private float[] geomagnetic = new float[3];
    private float[] values = new float[3];
    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewInst();
        initCardListView();
        initSensor();
        initFontColorAndSize();
        initButtonListioner();
    }

    private void initButtonListioner() {
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(TodoListActivity.this, addNewListActivity.class);
                startActivityForResult(intent, ADD_RESULT_ADD);
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(TodoListActivity.this, UIconfigurationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initViewInst() {
        ScrollView view = (ScrollView) LayoutInflater.from(this).inflate(R.layout.activity_todolist, null);
        setContentView(view);
        todoTextView = (TextView) findViewById(R.id.textview_todo);
        doneTextView = (TextView) findViewById(R.id.textview_done);
        addTodoButton = (ImageButton) findViewById(R.id.button_addtodo);
        settingButton = (ImageButton) findViewById(R.id.button_setting);
        todolistview = (RecyclerView) findViewById(R.id.recyclerview_todo);
        donelistview = (RecyclerView) findViewById(R.id.recyclerview_done);
    }

    private void initFontColorAndSize() {
        todoTextView.setTextColor(CongratulationHelper.getTextColor());
        todoTextView.setTextSize(CongratulationHelper.getFrontSize() * CongratulationHelper.BASICFRONTSIZE_TEXT);
        doneTextView.setTextColor(CongratulationHelper.getTextColor());
        doneTextView.setTextSize(CongratulationHelper.getFrontSize() * CongratulationHelper.BASICFRONTSIZE_TEXT);
    }

    private void initSensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onPause() {
        Log.i("????????????", "onPause ");
        super.onPause();
        if (mSensorManager != null) {
            Log.i("??????????????????", "???????????? ");
            mSensorManager.unregisterListener(this);
        }

    }

    @Override
    protected void onResume() {
        Log.i("????????????", "onResume ");
        super.onResume();
        //Log.i("??????????????????", "????????????: " + mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_UI));
        Log.i("????????????", "????????????: " + mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI));
        Log.i("????????????", "????????????: " + mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI));

    }

    public void initCardListView() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        todolistview.setLayoutManager(mLayoutManager);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        donelistview.setLayoutManager(mLayoutManager);
        todoAdapter = new TodoCardViewAdapter(this);
        doneAdapter = new DoneCardViewAdapter(this);
        todolistview.setAdapter(todoAdapter);
        donelistview.setAdapter(doneAdapter);
        todolistview.setFocusable(true);
        //????????????
        ArrayList<String> createTableSql = new ArrayList<>();
        createTableSql.add("CREATE TABLE cardlist(id CHAR(50)  NOT NULL,todoDate CHAR(50) NOT NULL, context TEXT ,  type INT NOT NULL);");
        DataBaseOpenHelper instance = DataBaseOpenHelper.getInstance(getApplicationContext(), "MyAccessibility", 2, createTableSql);
        instance.delete("cardlist", "1=1", new String[]{});
        ContentValues newV = new ContentValues();
        for (int i = 1; i < 7; i++) {
            newV.clear();
            newV.put("id", Integer.toString(i));
            newV.put("todoDate", "2022-04-" + Integer.toString(10 + i));
            newV.put("context", "?????????" + i + "????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
            newV.put("type", 0);
            instance.insert("cardlist", newV);
        }
        for (int i = 1; i < 5; i++) {
            newV.clear();
            newV.put("id", Integer.toString(6 + i));
            newV.put("todoDate", "2022-04-" + Integer.toString(16 + i));
            newV.put("context", "?????????" + i + "?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????8???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
            newV.put("type", 1);
            instance.insert("cardlist", newV);
        }
        Cursor cursor = instance.query("cardlist", "");
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                Card card = new Card();
                String id = cursor.getString(0);
                String date = cursor.getString(1);
                String content = cursor.getString(2);
                int type = cursor.getInt(3);
                card.setId(id);
                card.setContent(content);
                card.setDate(date);
                card.setListener(this);
                if (type == 0) {
                    card.setState(Card.TYPE_TODO);
                    todoList.add(card);
                } else {
                    card.setState(Card.TYPE_DONE);
                    doneList.add(card);
                }

            } while (cursor.moveToNext());
        }

        todoTextView.setContentDescription("????????????" + todoList.size() + "??????????????????");
        doneTextView.setContentDescription("???????????????" + doneList.size() + "?????????");
        todoAdapter.setData(todoList);
        doneAdapter.setData(doneList);
        todoAdapter.setDoneCardViewAdapter(doneAdapter);
        doneAdapter.setTodoCardView(todoAdapter);
        todoAdapter.notifyDataSetChanged();
        doneAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = sensorEvent.values;
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticFieldValues = sensorEvent.values;
        }
        final float dd = (sensorEvent.timestamp - mTimestamp);
        if (dd * NS2S > 2) {
            float[] values = new float[3];
            float[] R = new float[9];
            SensorManager.getRotationMatrix(R, null, accelerometerValues,
                    magneticFieldValues);
            SensorManager.getOrientation(R, values);
            double degreeX = (float) Math.toDegrees(values[1]);
            double degreeY = (float) Math.toDegrees(values[2]);
            if (Math.abs(degreeX - oldDegree[0]) > 30) {
                oldDegree[0] = degreeX;
                mTimestamp = sensorEvent.timestamp;
                if (Math.abs(degreeX) > 30)
                    if (oldDegree[0] > 0) {
                        Log.i("????????????", "X??????" + oldDegree[0]);
                        todolistview.sendAccessibilityEvent(strongAccessibilityService.EVENT_ROTATE_ACCELERATE_X_UP);
                    } else {
                        Log.i("????????????", "X??????" + oldDegree[0]);
                        todolistview.sendAccessibilityEvent(strongAccessibilityService.EVENT_ROTATE_ACCELERATE_X_DOWN);
                    }
            }
            if (Math.abs(oldDegree[1] - degreeY) > 30) {
                oldDegree[1] = degreeY;
                mTimestamp = sensorEvent.timestamp;
                if (Math.abs(degreeY) > 30)
                    if (oldDegree[1] > 0) {
                        Log.i("????????????", "Y??????" + oldDegree[1]);
                        todolistview.sendAccessibilityEvent(strongAccessibilityService.EVENT_ROTATE_ACCELERATE_Y_UP);
                    } else {
                        Log.i("????????????", "Y??????" + oldDegree[1]);
                        todolistview.sendAccessibilityEvent(strongAccessibilityService.EVENT_ROTATE_ACCELERATE_Y_DOWN);
                    }

            }
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {//???????????????????????????
            final float dT = (sensorEvent.timestamp - mTimestamp);
            if (dT * NS2S > 2) {
                if (Math.abs(sensorEvent.values[0]) > 0.3 && Math.abs(sensorEvent.values[0]) > Math.abs(sensorEvent.values[1]) && Math.abs(sensorEvent.values[0]) > Math.abs(sensorEvent.values[2])) {
                    Log.i("??????????????????", "??????X");
                    mTimestamp = sensorEvent.timestamp;
                    if (sensorEvent.values[0] > 0)
                        todolistview.sendAccessibilityEvent(strongAccessibilityService.EVENT_ROTATE_ACCELERATE_X_DOWN);
                    else
                        todolistview.sendAccessibilityEvent(strongAccessibilityService.EVENT_ROTATE_ACCELERATE_X_UP);
                } else if (Math.abs(sensorEvent.values[1]) > 0.3 && Math.abs(sensorEvent.values[1]) > Math.abs(sensorEvent.values[0]) && Math.abs(sensorEvent.values[1]) > Math.abs(sensorEvent.values[2])) {
                    Log.i("??????????????????", "??????Y");
                    mTimestamp = sensorEvent.timestamp;
                    if (sensorEvent.values[1] > 0)
                        todolistview.sendAccessibilityEvent(strongAccessibilityService.EVENT_ROTATE_ACCELERATE_Y_UP);
                    else
                        todolistview.sendAccessibilityEvent(strongAccessibilityService.EVENT_ROTATE_ACCELERATE_Y_DOWN);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_RESULT_ADD && resultCode == ADD_RESULT_OK) {
            Card card = new Card();
            card.setId(UUID.randomUUID().toString());
            card.setContent(data.getStringExtra("content"));
            card.setDate(data.getStringExtra("date"));
            card.setListener(this);
            todoList.add(card);
            todoAdapter.setData(todoList);
            todoAdapter.notifyDataSetChanged();
            todoTextView.setContentDescription("????????????" + todoList.size() + "??????????????????");
        } else if ((requestCode == ADD_RESULT_NOTIFY_TODO || requestCode == ADD_RESULT_NOTIFY_DONE) && resultCode == ADD_RESULT_OK) {
            if (requestCode == ADD_RESULT_NOTIFY_TODO) {
                for (int i = 0; i < todoList.size(); i++) {
                    Card card = todoList.get(i);
                    if (card.getId().equals(data.getStringExtra("id"))) {
                        card.setContent(data.getStringExtra("content"));
                        card.setDate(data.getStringExtra("date"));
                        todoList.remove(i);
                        todoList.add(card);
                        todoAdapter.setData(todoList);
                        todoAdapter.notifyDataSetChanged();
                        todoTextView.setContentDescription("????????????" + todoList.size() + "??????????????????");
                        break;
                    }
                }
            } else {
                for (int i = 0; i < doneList.size(); i++) {
                    Card card = doneList.get(i);
                    if (card.getId().equals(data.getStringExtra("id"))) {
                        card.setContent(data.getStringExtra("content"));
                        card.setDate(data.getStringExtra("date"));
                        todoList.add(card);
                        doneList.remove(i);
                        doneAdapter.setData(doneList);
                        doneAdapter.notifyItemRemoved(i);
                        doneAdapter.notifyItemRangeChanged(i, doneList.size());
                        todoAdapter.setData(todoList);
                        todoAdapter.notifyDataSetChanged();
                        todoTextView.setContentDescription("????????????" + todoList.size() + "??????????????????");
                        doneTextView.setContentDescription("???????????????" + todoList.size() + "?????????");
                        break;
                    }
                }
            }

        }

    }

    @Override
    public void notifyItem(int Type, Card card) {
        Intent intent = new Intent();
        intent.putExtra("id", card.getId());
        intent.putExtra("content", card.getContent());
        intent.putExtra("date", card.getDate());
        intent.setClass(TodoListActivity.this, addNewListActivity.class);
        startActivityForResult(intent, Type);
    }

    @Override
    public void changeItem(int Type) {
        if (Type == 0) {
            todoList = todoAdapter.getData();
            todoTextView.setContentDescription("????????????" + todoList.size() + "??????????????????");
        } else {
            doneList = doneAdapter.getData();
            doneTextView.setContentDescription("???????????????" + doneList.size() + "?????????");
        }
    }

}
