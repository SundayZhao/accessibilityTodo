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
import android.widget.Toast;

import java.util.List;

public class strongAccessibilityService  extends AccessibilityService {
    public static String TAG = "无障碍服务测试";
    private TextToSpeech textToSpeech;

    //来自XYZ轴的手机旋转
    public static final int  EVENT_ROTATE_ACCELERATE_X = 664834952;
    public static final int  EVENT_ROTATE_ACCELERATE_Y = 664834953;
    public static final int  EVENT_ROTATE_ACCELERATE_Z = 664834954;

    @Override
    protected void onServiceConnected() {
        Log.i(TAG, "开始连接");
        super.onServiceConnected();
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED
                | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                | AccessibilityEvent.TYPE_VIEW_CLICKED
                | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                | AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
                |AccessibilityEvent.TYPE_VIEW_SCROLLED ;
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
        // 此方法是在主线程中回调过来的，所以消息是阻塞执行的
            Log.i(TAG,event.getEventType()+"");
        switch (event.getEventType()) {
            /**
             * AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED，
             * 而会调用AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED，
             * 而AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED只要内容改变后都会调用，
             * 所以一般是使用AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED来作为监测事件的
             */
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.i(TAG, "TYPE_WINDOW_STATE_CHANGED 界面改变");
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.i(TAG, "TYPE_VIEW_CLICKED view被点击");
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                Log.i(TAG, "TYPE_VIEW_SCROLLED");
                break;
            case EVENT_ROTATE_ACCELERATE_X:
                Log.i(TAG,"X轴旋转");
                break;
            case EVENT_ROTATE_ACCELERATE_Y:
                Log.i(TAG,"Y轴旋转");
                break;
            case EVENT_ROTATE_ACCELERATE_Z:
                Log.i(TAG,"Z轴旋转");
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
