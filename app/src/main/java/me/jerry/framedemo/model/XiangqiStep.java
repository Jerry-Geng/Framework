package me.jerry.framedemo.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import me.jerry.framework.db.Column;
import me.jerry.framework.db.Config;
import me.jerry.framework.db.Dao;
import me.jerry.framework.db.IDao;
import me.jerry.framework.db.OnDatabaseUpdateListener;
import me.jerry.framework.db.SqlException;
import me.jerry.framework.db.TableEntity;

/**
 * Created by Jerry on 2017/8/31.
 */

public class XiangqiStep extends TableEntity {
    @Column
    private String otherMac;
    @Column
    private String ownSide;
    @Column
    private int startXIndex;
    @Column
    private int startYIndex;
    @Column
    private int endXIndex;
    @Column
    private int endYIndex;
    @Column(nullable = true)
    private String eatenPiece;
    @Column
    private String selectedPiece;
    @Column
    private String actionSide;
    @Column
    private int stepIndex;
    @Column
    private short roolbacked;

    public int getStartXIndex() {
        return startXIndex;
    }

    public void setStartXIndex(int startXIndex) {
        this.startXIndex = startXIndex;
    }

    public int getStartYIndex() {
        return startYIndex;
    }

    public void setStartYIndex(int startYIndex) {
        this.startYIndex = startYIndex;
    }

    public int getEndXIndex() {
        return endXIndex;
    }

    public void setEndXIndex(int endXIndex) {
        this.endXIndex = endXIndex;
    }

    public int getEndYIndex() {
        return endYIndex;
    }

    public void setEndYIndex(int endYIndex) {
        this.endYIndex = endYIndex;
    }

    public String getEatenPiece() {
        return eatenPiece;
    }

    public void setEatenPiece(String eatenPiece) {
        this.eatenPiece = eatenPiece;
    }

    public String getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(String selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public String getOtherMac() {
        return otherMac;
    }

    public void setOtherMac(String otherMac) {
        this.otherMac = otherMac;
    }

    public String getOwnSide() {
        return ownSide;
    }

    public void setOwnSide(String ownSide) {
        this.ownSide = ownSide;
    }

    public String getActionSide() {
        return actionSide;
    }

    public void setActionSide(String actionSide) {
        this.actionSide = actionSide;
    }

    public short getRoolbacked() {
        return roolbacked;
    }

    public void setRoolbacked(short roolbacked) {
        this.roolbacked = roolbacked;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    private static IDao<XiangqiStep> getDao(Context context) {
        IDao<XiangqiStep> dao = new Dao<>(XiangqiStep.class, context, new OnDatabaseUpdateListener() {
            @Override
            public void onUpdate(Config.DB_CONIFG dbConfig, SQLiteDatabase database, IDao dao) throws SqlException {

            }
        });
        return dao;
    }

    public static List<XiangqiStep> load(String otherMac, Context context) {
        IDao<XiangqiStep> dao = getDao(context);
        List<XiangqiStep> dataList = dao.queryDataCustom("other_mac=" + otherMac + " order by step_index");
        dao.closeDb();
        return dataList;
    }

    public static XiangqiStep roolback(String otherMac, Context context) {
        IDao<XiangqiStep> dao = getDao(context);
        List<XiangqiStep> dataList = dao.queryDataCustom("other_mac=" + otherMac + " and roolbacked=0 order by step_index");
        if(dataList.size() > 0) {
            XiangqiStep xiangqiStep = dataList.get(dataList.size() - 1);
            xiangqiStep.roolbacked = 1;
            dao.modifyData(xiangqiStep);
            dao.closeDb();
            return xiangqiStep;
        } else {
            dao.closeDb();
            return null;
        }
    }

    public static XiangqiStep perform(String otherMac, Context context) {
        IDao<XiangqiStep> dao = getDao(context);
        List<XiangqiStep> dataList = dao.queryDataCustom("other_mac=" + otherMac + " and roolbacked=1 order by step_index");
        if(dataList.size() > 0) {
            XiangqiStep xiangqiStep = dataList.get(0);
            xiangqiStep.roolbacked = 0;
            dao.modifyData(xiangqiStep);
            dao.closeDb();
            return xiangqiStep;
        } else {
            dao.closeDb();
            return null;
        }
    }

    public void save(Context context, String otherMac) {
        IDao<XiangqiStep> dao = getDao(context);
        dao.deleteDataCustom("other_mac=" + otherMac + " and roolbacked=1");
        long count = dao.countBySelection("other_mac=" + otherMac);
        setStepIndex((int)(count + 1));
        this.roolbacked = 0;
        dao.insertData(this);
        dao.closeDb();
    }

    public void clear(String otherMac, Context context) {
        IDao<XiangqiStep> dao = getDao(context);
        dao.deleteDataCustom("other_mac=" + otherMac);
        dao.closeDb();
    }

}
