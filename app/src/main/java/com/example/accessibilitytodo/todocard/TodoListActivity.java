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
    public static int ADD_RESULT_OK=1;
    public static int ADD_RESULT_ADD=2;
    public static int ADD_RESULT_NOTIFY_TODO=3;
    public static int ADD_RESULT_NOTIFY_DONE=4;

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
    //上一次触发传感器的时间戳
    private float mTimestamp = 0;
    //上一次触发传感器的XYZ
    private float mAngle[] = new float[3];
    private static final float NS2S = 1.0f / 1000000000.0f;

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
                startActivityForResult(intent,ADD_RESULT_ADD);
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
        todoTextView=(TextView)findViewById(R.id.textview_todo);
        doneTextView=(TextView)findViewById(R.id.textview_done);
        addTodoButton=(ImageButton)findViewById(R.id.button_addtodo) ;
        settingButton=(ImageButton)findViewById(R.id.button_setting) ;
        todolistview = (RecyclerView) findViewById(R.id.recyclerview_todo);
        donelistview=(RecyclerView)findViewById(R.id.recyclerview_done);
    }

    private void initFontColorAndSize() {
        todoTextView.setTextColor(CongratulationHelper.getTextColor());
        todoTextView.setTextSize(CongratulationHelper.getFrontSize()* CongratulationHelper.BASICFRONTSIZE_TEXT);
        doneTextView.setTextColor(CongratulationHelper.getTextColor());
        doneTextView.setTextSize(CongratulationHelper.getFrontSize()* CongratulationHelper.BASICFRONTSIZE_TEXT);
    }

    private void initSensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Log.i("旋转加速感应", "获取:" + mSensorManager);
    }
    @Override
    protected void onPause() {
        Log.w("界面状态", "onPause ");
        super.onPause();
        if (mSensorManager != null) {
            Log.w("旋转加速感应", "解除注册 ");
            mSensorManager.unregisterListener(this);
        }

    }

    @Override
    protected void onResume() {
        Log.w("界面状态", "onResume ");
        super.onResume();
        Log.w("旋转加速感应", "注册结果: " + mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_UI));
    }
    public void initCardListView() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        todolistview.setLayoutManager(mLayoutManager);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        donelistview.setLayoutManager(mLayoutManager);
        todoAdapter = new TodoCardViewAdapter(this);
        doneAdapter=new DoneCardViewAdapter(this);
        todolistview.setAdapter(todoAdapter);
        donelistview.setAdapter(doneAdapter);
        //模拟数据
        ArrayList<String> createTableSql= new ArrayList<>();
        createTableSql.add("CREATE TABLE cardlist(id CHAR(50)  NOT NULL,todoDate CHAR(50) NOT NULL, context TEXT ,  type INT NOT NULL);");
        DataBaseOpenHelper instance=DataBaseOpenHelper.getInstance(getApplicationContext(),"MyAccessibility",2,createTableSql);
        instance.delete("cardlist","1=1",new String[]{});
        ContentValues newV=new ContentValues();
        for (int i = 1; i < 7; i++) {
            newV.clear();
            newV.put("id",Integer.toString(i));
            newV.put("todoDate","2022-04-"+Integer.toString(10+i));
            newV.put("context","这是第"+i+"个代办事件列表，仅供测试。后面再来一点无意义的文本，延长一下文本长度。星期六，我和妈妈一起坐车去上街，我们先来到车站，等了一会车来了，我们上了车。这时上来了一位老爷爷。这时从后面传来一句话：老大爷，你到这里来坐。老爷爷连忙就答应了，并且不停的说谢谢，我回头一看原来是一个穿着非常普通的阿姨。经过这件事，我明白了人的美丽、善良，不是从外表可以看到的，而是从内心感受到的。");
            newV.put("type",0);
            instance.insert("cardlist",newV);
        }
        for (int i = 1; i < 5; i++) {
            newV.clear();
            newV.put("id",Integer.toString(6+i));
            newV.put("todoDate","2022-04-"+Integer.toString(16+i));
            newV.put("context","这是第"+i+"个已办事件列表，仅供测试。后面再来一点无意义的文本，延长一下文本长度。今天晚上放学，我依旧坐着8路公交车回家，坐在椅子上看着窗外略过的风景。一会儿，上来一位老奶奶，拄着拐杖见车上没有座位，叹气站在椅子旁边。我起身扶着老奶奶在我的座位上坐下。老奶奶慈祥的笑着说：“真是个懂事的好孩子”");
            newV.put("type",1);
            instance.insert("cardlist",newV);
        }
        Cursor cursor=instance.query("cardlist","");
        if(cursor !=null){
            cursor.moveToFirst();
            do{
                Card card = new Card();
                String id =cursor.getString(0);
                String date=cursor.getString(1);
                String content = cursor.getString(2);
                int type =cursor.getInt(3);
                card.setId(id);
                card.setContent(content);
                card.setDate(date);
                card.setListener(this);
                if(type==0){
                    card.setState(Card.TYPE_TODO);
                    todoList.add(card);
                }else{
                    card.setState(Card.TYPE_DONE);
                    doneList.add(card);
                }

            } while(cursor.moveToNext());
        }

        todoTextView.setContentDescription("当前还有"+todoList.size()+"项待完成事项");
        doneTextView.setContentDescription("当前已完成"+todoList.size()+"项事项");
        todoAdapter.setData(todoList);
        doneAdapter.setData(doneList);
        todoAdapter.setDoneCardViewAdapter(doneAdapter);
        doneAdapter.setTodoCardView(todoAdapter);
        todoAdapter.notifyDataSetChanged();
        doneAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {//陀螺仪角度发生变化
            final float dT = (sensorEvent.timestamp - mTimestamp);
            if (dT * NS2S > 2) {
                if (Math.abs(sensorEvent.values[0]) > 0.3 && Math.abs(sensorEvent.values[0]) > Math.abs(sensorEvent.values[1]) && Math.abs(sensorEvent.values[0]) > Math.abs(sensorEvent.values[2])) {
                    Log.i("旋转加速感应", "发送X");
                    mTimestamp = sensorEvent.timestamp;
                    if(sensorEvent.values[0]>0)
                        todolistview.sendAccessibilityEvent(strongAccessibilityService.EVENT_ROTATE_ACCELERATE_X_DOWN);
                    else
                        todolistview.sendAccessibilityEvent(strongAccessibilityService.EVENT_ROTATE_ACCELERATE_X_UP);
                } else if (Math.abs(sensorEvent.values[1]) > 0.3&& Math.abs(sensorEvent.values[1]) > Math.abs(sensorEvent.values[0]) && Math.abs(sensorEvent.values[1]) > Math.abs(sensorEvent.values[2])) {
                    Log.i("旋转加速感应", "发送Y");
                    mTimestamp = sensorEvent.timestamp;
                    if(sensorEvent.values[1]>0)
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
            Card card=new Card();
            card.setId(UUID.randomUUID().toString());
            card.setContent(data.getStringExtra("content"));
            card.setDate(data.getStringExtra("date"));
            card.setListener(this);
            todoList.add(card);
            todoAdapter.setData(todoList);
            todoAdapter.notifyDataSetChanged();
            todoTextView.setContentDescription("当前还有"+todoList.size()+"项待完成事项");
        }else  if ((requestCode == ADD_RESULT_NOTIFY_TODO || requestCode==ADD_RESULT_NOTIFY_DONE)&& resultCode == ADD_RESULT_OK) {
            if(requestCode == ADD_RESULT_NOTIFY_TODO ) {
                for (int i = 0; i < todoList.size(); i++) {
                    Card card = todoList.get(i);
                    if (card.getId().equals(data.getStringExtra("id"))) {
                        card.setContent(data.getStringExtra("content"));
                        card.setDate(data.getStringExtra("date"));
                        todoList.remove(i);
                        todoList.add(card);
                        todoAdapter.setData(todoList);
                        todoAdapter.notifyDataSetChanged();
                        todoTextView.setContentDescription("当前还有"+todoList.size()+"项待完成事项");
                        break;
                    }
                }
            }else{
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
                        todoTextView.setContentDescription("当前还有"+todoList.size()+"项待完成事项");
                        doneTextView.setContentDescription("当前已完成"+todoList.size()+"项事项");
                        break;
                    }
                }
            }

        }

    }

    @Override
    public void notifyItem(int Type,Card card) {
        Intent intent = new Intent();
        intent.putExtra("id",card.getId());
        intent.putExtra("content",card.getContent());
        intent.putExtra("date",card.getDate());
        intent.setClass(TodoListActivity.this, addNewListActivity.class);
        startActivityForResult(intent,Type);
    }
}
