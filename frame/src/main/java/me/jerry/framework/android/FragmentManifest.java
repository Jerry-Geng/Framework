package me.jerry.framework.android;

import android.content.Context;
import android.util.Log;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jerry on 2017/8/21.
 */

public class FragmentManifest {
    public final static String XML_FILE_NAME = "fragment_manifest.xml";
    public final static String FRAGMENT_PERFORM = "fragment";
    public final static String FRAGMENT_NAME_PERFORM = "name";
    public final static String FRAGMENT_MAIN_PERFORM = "main";

    public static List<Class<? extends FragmentFrame>> findFragments(Context context) {

        try {
            InputStream is = context.getAssets().open(XML_FILE_NAME);
            if(is == null) {
                Log.e("fragment_manifest", "xml not found");
            }
            Document document = new SAXReader().read(is);
            Element root = document.getRootElement();
            List<Element> elementList = root.elements();
            List<Class<? extends FragmentFrame>> list = new ArrayList<>();
            for(Element element : elementList) {
                if(FRAGMENT_PERFORM.equals(element.getName())) {
                    // found fragment
                    Class<? extends FragmentFrame> clazz = null;
                    try {
                        clazz = (Class<? extends FragmentFrame>) Class.forName(element.attribute(FRAGMENT_NAME_PERFORM).getValue());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();

                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
                    if(clazz != null) {
                        if("true".equals(element.attributeValue(FRAGMENT_MAIN_PERFORM))) {
                            list.add(0, clazz);
                        } else {
                            list.add(clazz);
                        }
                    }
                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
