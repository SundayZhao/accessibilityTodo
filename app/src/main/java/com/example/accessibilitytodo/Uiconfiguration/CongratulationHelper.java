package com.example.accessibilitytodo.Uiconfiguration;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.example.accessibilitytodo.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public  class CongratulationHelper {

    public static int BASICFRONTSIZE_TITLE=30;
    public static int BASICFRONTSIZE_TEXT=20;
    public static int BASICFRONTSIZE_SECTION=10;

    private static float frontSize=1;
    private static int textColor= R.color.black;

    private static boolean openVoice=true;

    public static String TAG="xmlconf";
    private  static String FILENAME="configuration.xml";

    public static  void saveConfig(Context context) {
        try {
            String filePath="";
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
                //外部存储可用
                filePath = context.getExternalFilesDir(null).getPath() ;
            } else {
                //外部存储不可用
                filePath = context.getFilesDir().getPath() ;
            }
            File file = new File(filePath,FILENAME);
            if(!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file,false);
            // 获得一个序列化工具
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "utf-8");
            // 设置文件头
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "configurations");
            serializer.startTag(null, "fontconfiguration");

            serializer.startTag(null, "frontSize");
            serializer.text(Float.toString(frontSize));
            serializer.endTag(null, "frontSize");

            serializer.startTag(null, "textColor");
            serializer.text(Integer.toString(textColor));
            serializer.endTag(null, "textColor");

            serializer.endTag(null, "fontconfiguration");
            serializer.endTag(null, "configurations");
            serializer.endDocument();
            fos.close();
            Log.i( TAG,"保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "保存失败");
        }
    }
    /**
     * 读取本地配置文件
     */
    public static void getConfig(Context context) {
        try {
            String filePath="";
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
                //外部存储可用
                filePath = context.getExternalFilesDir(null).getPath() + "/" + FILENAME;
            } else {
                //外部存储不可用
                filePath = context.getFilesDir().getPath() + "/" + FILENAME;
            }
            File path = new File(filePath);

            if(!path.exists())
                return;
            FileInputStream fis = new FileInputStream(path);

            // 获得pull解析器对象
            XmlPullParser parser = Xml.newPullParser();
            // 指定解析的文件和编码格式
            parser.setInput(fis, "utf-8");

            int eventType = parser.getEventType(); // 获得事件类型


            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName(); // 获得当前节点的名称

                switch (eventType) {
                    case XmlPullParser.START_TAG: // 当前等于开始节点
                        if ("configurations".equals(tagName)) {
                        } else if ("fontconfiguration".equals(tagName)) {
                        } else if ("frontSize".equals(tagName)) {
                            frontSize = Float.parseFloat(parser.nextText());
                        } else if ("textColor".equals(tagName)) {
                            textColor = Integer.parseInt(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG: // </persons
                        break;
                    default:
                        break;
                }
                eventType = parser.next(); // 获得下一个事件类型

            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static float getFrontSize() {
        return frontSize;
    }

    public static void setFrontSize(float frontSize1) {
        frontSize = frontSize1;
    }

    public static int getTextColor() {
        return textColor;
    }

    public static void setTextColor(int textColor) {
        CongratulationHelper.textColor = textColor;
    }

    public static boolean isOpenVoice() {
        return openVoice;
    }

    public static void setOpenVoice(boolean openVoice) {
        CongratulationHelper.openVoice = openVoice;
    }
}
