package com.example.accessibilitytodo.Uiconfiguration;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.accessibilitytodo.R;
import com.example.accessibilitytodo.accessibilitypackage.AccessibilityUtil;
import com.example.accessibilitytodo.accessibilitypackage.strongAccessibilityService;
import com.example.accessibilitytodo.todocard.TodoListActivity;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.kyleduo.switchbutton.SwitchButton;
import com.xw.repo.BubbleSeekBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UIconfigurationActivity extends AppCompatActivity implements SensorEventListener {
    @BindView(R.id.seekBar_frontsize)
    BubbleSeekBar seekBar;
    @BindView(R.id.textview_selectfront)
    TextView textview_FrontSize;
    @BindView(R.id.textview_voice)
    TextView textview_voice;
    @BindView(R.id.textview_selectfrontcolor)
    TextView textview_FrontColor;
    @BindView(R.id.color_picker_view)
    ColorPickerView colorPickerView;
    @BindView(R.id.openVoiceButton)
    SwitchButton openVoiceButton;
    @BindView(R.id.opentravel)
    Button nextButton;

    private ArrayList<Fragment> fragmentList;

    private SensorManager mSensorManager;
    //上一次触发传感器的时间戳
    private float mTimestamp=0;
    //上一次触发传感器的XYZ
    private float mAngle[]=new float[3];
    private static final float NS2S=1.0f/1000000000.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiconfiguration);
        ButterKnife.bind(this);

        initPermission();
        CongratulationHelper.getConfig(getApplicationContext());
        initColorPickerView();
        initSeekbar();
        changeTextColor();
        changeTextSize();
        initSensor();
        openVoiceButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CongratulationHelper.setOpenVoice(b);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:开启主界面
                CongratulationHelper.saveConfig(getApplicationContext());
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(),TodoListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initSensor() {
        mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
    }

    private void initPermission(){
        if(!AccessibilityUtil.isService(getApplicationContext(),strongAccessibilityService.class))
        {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            List<String> permissionList = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(this, Manifest.
                    permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!permissionList.isEmpty()) {
                String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }
    private void initSeekbar() {
        //todo:动态设置滑动条位置
        seekBar.getConfigBuilder().max(4).min(0).progress((CongratulationHelper.getFrontSize()-1)/0.3f).build();
        seekBar.setContentDescription("左滑减小字体大小，右滑增加字体大小。当前字号倍率:"+CongratulationHelper.getFrontSize()+"倍");
        seekBar.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
                array.clear();
                array.put(0, "小");
                array.put(4, "大");
                return array;
            }
        });


        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                CongratulationHelper.setFrontSize(progress * 0.3f +1 );
                DecimalFormat decimalFormat=new DecimalFormat(".0");
                seekBar.setContentDescription("当前字号倍率:"+decimalFormat.format(CongratulationHelper.getFrontSize())+"倍");
                //seekBar.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
                seekBar.requestFocus();
                changeTextSize();
                 //todo：动态设置开关按钮与文本的间距，防止重叠

            }

        });
    }

    private void initColorPickerView() {
        colorPickerView.setInitialColor(CongratulationHelper.getTextColor(),true);
        colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {
                CongratulationHelper.setTextColor(selectedColor);
                changeTextColor();
            }
        });
        colorPickerView.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                //计算与白色的对比值，太小则不能改
                CongratulationHelper.setTextColor(selectedColor);
                changeTextColor();
            }
        });
    }

    private void changeTextColor(){
        textview_FrontSize.setTextColor(CongratulationHelper.getTextColor());
        textview_FrontColor.setTextColor(CongratulationHelper.getTextColor());
        textview_voice.setTextColor(CongratulationHelper.getTextColor());
        nextButton.setTextSize(CongratulationHelper.getTextColor());
        //TODO:改变按钮背景颜色，用边框白
        seekBar.getConfigBuilder().sectionTextColor(CongratulationHelper.getTextColor()).build();
        seekBar.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
                array.clear();
                array.put(0, "小");
                array.put(4, "大");
                return array;
            }
        });
    }
    private  void changeTextSize(){
        textview_FrontSize.setTextSize(CongratulationHelper.getFrontSize() * CongratulationHelper.BASICFRONTSIZE_TEXT);
        textview_FrontColor.setTextSize(CongratulationHelper.getFrontSize() * CongratulationHelper.BASICFRONTSIZE_TEXT);
        textview_voice.setTextSize(CongratulationHelper.getFrontSize() * CongratulationHelper.BASICFRONTSIZE_TEXT);
        nextButton.setTextSize(CongratulationHelper.getFrontSize() * CongratulationHelper.BASICFRONTSIZE_TEXT);
    }


    @Override
    protected void onPause(){
        super.onPause();
        if(mSensorManager!=null) mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(mSensorManager!=null) mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        System.out.println("触发");
        if(sensorEvent.sensor.getType()==Sensor.TYPE_GYROSCOPE){//陀螺仪角度发生变化
            if(mTimestamp!=0){
                final float dT=(sensorEvent.timestamp-mTimestamp);
                mAngle[0]+=sensorEvent.values[0]*dT;
                mAngle[1]+=sensorEvent.values[1]*dT;
                mAngle[2]+=sensorEvent.values[2]*dT;
                //X轴的旋转角度，手机平放，绕侧边旋转
                float angleX=(float)Math.toDegrees(mAngle[0]);
                //Y轴的旋转角度，手机平放，绕底边旋转
                float angleY=(float)Math.toDegrees(mAngle[1]);
                //Z轴的旋转角度，手机平放，水平旋转
                float angleZ=(float)Math.toDegrees(mAngle[2]);

                if (Math.abs(sensorEvent.values[0]) >0 && sensorEvent.values[0] > sensorEvent.values[1] && sensorEvent.values[0] > sensorEvent.values[2] ) {
                    seekBar.sendAccessibilityEvent(strongAccessibilityService.EVENT_ROTATE_ACCELERATE_X);
                } else if (Math.abs(sensorEvent.values[1]) >0 && sensorEvent.values[1] > sensorEvent.values[0] && sensorEvent.values[1] > sensorEvent.values[2] ) {
                    seekBar.sendAccessibilityEvent(strongAccessibilityService.EVENT_ROTATE_ACCELERATE_Y);
                }else if (Math.abs(sensorEvent.values[2]) >0){
                    seekBar.sendAccessibilityEvent(strongAccessibilityService.EVENT_ROTATE_ACCELERATE_Z);
                }

            }
            mTimestamp=sensorEvent.timestamp;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
