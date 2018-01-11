package me.jerry.framework.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jerry on 2017/8/4.
 */

public interface OnDatabaseUpdateListener {
    public void onUpdate(Config.DB_CONIFG dbConfig, SQLiteDatabase database, IDao dao)throws SqlException;
}
