package com.example.accessibilitytodo.Uiconfiguration;

import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

public class XmlConfigurationHelper {
    /**
     * 保存配置文件到本地
     */
    public static String TAG="xmlconf";

    private static String FILEPATH=Environment.getExternalStorageDirectory() + File.separator + "Settings";
    private  static String FILENAME="configuration.xml";

    public void setConfig(String key,String value) {
        try {
            File file = new File(FILEPATH,FILENAME);
            Log.e(TAG, "" + file);
            FileOutputStream fos = new FileOutputStream(file);
            // 获得一个序列化工具
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "utf-8");
            // 设置文件头
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "configurations");
            serializer.startTag(null, "fontconfiguration");
            serializer.attribute(null, "id", String.valueOf(0));
            serializer.startTag(null, key);
            serializer.text(value);
            serializer.endTag(null, key);
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
}
