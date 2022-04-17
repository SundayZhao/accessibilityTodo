package com.example.accessibilitytodo.accessibilitypackage;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import java.util.List;

public class strongAccessibilityService  extends AccessibilityService {
    public static String TAG = "无障碍服务测试";
    private TextToSpeech textToSpeech;

    //来自XYZ轴的手机旋转
    public static final int  EVENT_ROTATE_ACCELERATE_X_UP = 664834952;
    public static final int  EVENT_ROTATE_ACCELERATE_X_DOWN = 664834953;
    public static final int  EVENT_ROTATE_ACCELERATE_Y_UP = 664834954;
    public static final int  EVENT_ROTATE_ACCELERATE_Y_DOWN = 664834955;
    public static final int  EVENT_ROTATE_ACCELERATE_Z = 664834954;

    @Override
    protected void onServiceConnected() {
        Log.i(TAG, "开始连接");
        AccessibilityUtil.Back(this);
        super.onServiceConnected();
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        accessibilityServiceInfo.notificationTimeout = 0;
        accessibilityServiceInfo.flags = AccessibilityServiceInfo.DEFAULT;
        accessibilityServiceInfo.packageNames = new String[]{"com.example.accessibilitytodo"};
        setServiceInfo(accessibilityServiceInfo);
    }
    @Override
    public boolean onUnbind(Intent intent) {
        //关闭服务时,调用
        //如果有资源记得释放
        return super.onUnbind(intent);
    }
    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        System.out.println(event.getKeyCode());
        return super.onKeyEvent(event);

    }
        @SuppressLint("SwitchIntDef")
        @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.i(TAG, "TYPE_WINDOW_STATE_CHANGED 界面改变");
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.i(TAG, "TYPE_VIEW_CLICKED view被点击");
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                Log.i(TAG, "TYPE_VIEW_SCROLLED");
                break;
            case EVENT_ROTATE_ACCELERATE_X_UP:
                Log.i(TAG,"X轴向上，上翻");
                AccessibilityUtil.coordinateSliding(this,300,900,300,500,200);
                break;
            case EVENT_ROTATE_ACCELERATE_X_DOWN:
                Log.i(TAG,"X轴向下，下翻");
                AccessibilityUtil.coordinateSliding(this,300,500,300,900,200);
                break;
            case EVENT_ROTATE_ACCELERATE_Y_UP:
                Log.i(TAG,"Y轴向右，右翻");
                AccessibilityUtil.coordinateSliding(this,300,500,500,500,200);
                break;
            case EVENT_ROTATE_ACCELERATE_Y_DOWN:
                Log.i(TAG,"Y轴向左，左翻");
                AccessibilityUtil.coordinateSliding(this,500,500,300,500,200);
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }

    public static boolean isServiceON(Context context, String className){
        ActivityManager activityManager = (ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo>
                runningServices = activityManager.getRunningServices(100);
        if (runningServices.size() < 0 ){
            return false;
        }
        for (int i = 0;i<runningServices.size();i++){
            ComponentName service = runningServices.get(i).service;
            if (service.getClassName().contains(className)){
                return true;
            }
        }
        return false;
    }
}
