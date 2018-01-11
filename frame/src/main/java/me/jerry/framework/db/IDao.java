package me.jerry.framework.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * 通用数据库操作接口
 * @author JerryGeng
 * @param <T> 表类型
 */
public interface IDao<T extends TableEntity> {
	/**
	 * 插入一条数据库记录
	 * @param data 数据
	 * @return 新插入的数据的id
	 * @throws SqlException
	 */
    public int insertData(T data) throws SqlException;
    /**
     * 根据主键删除数据库记录
     * @param mainKey
     * @return 返回被删除的记录数，通常为1
     * @throws SqlException
     */
    public int deleteData(int mainKey) throws SqlException;
    /**
     * 删除表中所有数据
     * @throws SqlException
     */
    public void deleteAll() throws SqlException;
    /**
     * 根据指定的筛选条件删除数据
     * @param selection sql语句'where'后面的部分
     * @throws SqlException
     */
    public void deleteDataCustom(String selection) throws SqlException;
    /**
     * 根据主键修改数据，数据中的空字段也将被更新
     * @param data 待修改的数据
     * @return 被修改的记录数
     * @throws SqlException 主键不正确或数据库表实体类中有数据类型不被支持
     */
    public int modifyData(T data) throws SqlException;
    /**
     * 根据主键修改数据，不更新数据中的空字段
     * @param data 待修改的数据
     * @return 被修改的记录数
     * @throws SqlException 主键不正确或数据库表实体类中有数据类型不被支持
     */
    public int modifyDataSelected(T data) throws SqlException;
    /**
     * 根据指定的筛选条件修改数据，不更新数据中的空字段
     * @param data 待修改的数据
     * @param selection sql语句'where'后面的部分
     * @return 被修改的记录数
     * @throws SqlException 数据库表实体类中有数据类型不被支持
     */
    public int modifyDataSelectedCustom(T data, String selection) throws SqlException;
    /**
     * 根据主键查询数据
     * @param mainKey
     * @return
     * @throws SqlException 数据找不到，记录不存在
     */
    public T queryDataById(int mainKey) throws SqlException;
    /**
     * 查询数据库中所有数据
     * @return
     * @throws SqlException
     */
    public List<T> queryAllData() throws SqlException;
    /**
     * 根据指定的筛选条件查询数据
     * @param selection sql语句'where'开始以及往后面的部分
     * @return
     * @throws SqlException
     */
    public List<T> queryDataCustom(String selection) throws SqlException;
    /**
     * 根据指定的筛选条件统计表中数据数量
     * @param selection sql语句'where'后面的部分
     * @return
     * @throws SqlException
     */
    public long countBySelection(String selection) throws SqlException;
    /**
     * 获取{@link SQLiteDatabase}实例
     * @return
     */
    public SQLiteDatabase getDb();
    /**
     * 关闭数据库，需手动执行
     * @throws SqlException
     */
    public void closeDb() throws SqlException;
    /**
     * 删除数据表
     * @param database
     * @throws SqlException
     */
    public void dropTable(SQLiteDatabase database) throws SqlException;
    /**
     * 更新数据表
     * @param dbConfig 数据库信息
     * @param database 数据库操作实例
     * @param converter 数据类型转换器
     * @throws SqlException 
     */
    public void onUpdateTable(Config.DB_CONIFG dbConfig, SQLiteDatabase database, Dao.ColumnTypeConverter converter) throws SqlException;
}
