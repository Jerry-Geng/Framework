package me.jerry.framework.db;

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
 * Created by Jerry on 2017/8/5.
 */

public class Config {
    public final static String CONFIG_FILE_NAME = "db_config.xml";
    public final static String DB_PERFORM = "database";
    public final static String TABLE_PERFORM = "table";
    public final static String DB_NAME_PERFORM = "name";
    public final static String DB_VERSION_PERFORM = "version";

    public static class DB_CONIFG {
        public final String DB_NAME;
        public final int DB_VERSION;
        public final List<Class<? extends TableEntity>> tableList;

        public DB_CONIFG(String dbName, int dbVersion, List<Class<? extends TableEntity>> tableList) {
            DB_NAME = dbName;
            DB_VERSION = dbVersion;
            this.tableList = tableList;
        }

        @Override
        public String toString() {
            return "{DB_NAME=" + DB_NAME + "; DB_VERSION=" + DB_VERSION + "; tableList=" + tableList.toString();
        }
    }

    public static DB_CONIFG findDatabase(String tableName, Context context) {
        try {
            InputStream is = context.getAssets().open(CONFIG_FILE_NAME);
            if(is == null) {
                Log.e("db_config", "xml not found");
            }
            Log.e("db_config", "xml found");
            Document document = new SAXReader().read(is);
            Log.e("db_config", "document found");
            Element root = document.getRootElement();
            List<Element> elementList = root.elements();
            for(Element element : elementList) {
                if (DB_PERFORM.equals(element.getName())) {
                    // check if the element represents database
                    List<Element> tableList = element.elements();
                    for(Element table : tableList) {
                        if(TABLE_PERFORM.equals(table.getName())) {
                            // check if the element represents table
                            String tabbleClass = table.getText().trim();
                            if(tableName.equals(tabbleClass)) {
                                int version = Integer.valueOf(element.attribute(DB_VERSION_PERFORM).getValue());
                                String dbName = element.attribute(DB_NAME_PERFORM).getValue();
                                List<Class<? extends TableEntity>> list = new ArrayList<>();
                                for(Element t : tableList) {
                                    String clazz = t.getText().trim();
                                    list.add((Class<? extends TableEntity>) Class.forName(clazz));
                                }
                                return new DB_CONIFG(dbName, version, list);
                            }
                        }
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
