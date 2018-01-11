package me.jerry.framework.db;

import android.database.sqlite.SQLiteDatabase;

/**开发者需要在数据库有更新的时候实现这个回掉接口，并且在其中调用{@link Dao#onUpdateTable(me.jerry.framework.db.Config.DB_CONIFG, SQLiteDatabase, me.jerry.framework.db.Dao.ColumnTypeConverter)}
 * 使用框架内提供的更新方案更加方便快捷，也更易于多版本更新维护，当然开发者也可以根据实际需求在此使用自己的更新方案
 * @author JerryGeng
 */
public interface OnDatabaseUpdateListener {
	/**
	 * @param dbConfig 新版本数据库信息
	 * @param database 数据库操作对象
	 * @param dao
	 * @throws SqlException
	 */
    public void onUpdate(Config.DB_CONIFG dbConfig, SQLiteDatabase database, IDao dao)throws SqlException;
}
