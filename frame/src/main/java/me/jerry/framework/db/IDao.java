package me.jerry.framework.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public interface IDao<T extends TableEntity> {
    public int insertData(T data) throws SqlException;

    public int deleteData(int mainKey) throws SqlException;

    public void deleteAll() throws SqlException;

    public void deleteDataCustom(String selection) throws SqlException;

    public int modifyData(T data) throws SqlException;

    public int modifyDataSelected(T data) throws SqlException;

    public int modifyDataSelectedCustom(T data, String selection) throws SqlException;

    public T queryDataById(int mainKey) throws SqlException;

    public List<T> queryAllData() throws SqlException;

    public List<T> queryDataCustom(String selection) throws SqlException;

    public long countBySelection(String selection) throws SqlException;

    public SQLiteDatabase getDb();

    public void closeDb() throws SqlException;

    public void dropTable(SQLiteDatabase database) throws SqlException;

    public void onUpdateTable(Config.DB_CONIFG dbConfig, SQLiteDatabase database, Dao.ColumnTypeConverter converter) throws SqlException;
}
