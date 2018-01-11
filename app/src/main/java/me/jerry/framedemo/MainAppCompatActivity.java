package me.jerry.framedemo;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.List;

import me.jerry.framework.android.AppCompatActivityFrame;
import me.jerry.framework.annotation.AutoFindView;
import me.jerry.framework.comm.CommEntity;
import me.jerry.framework.comm.CommManager;
import me.jerry.framework.comm.ICommEventListener;
import me.jerry.framework.comm.NetException;
import me.jerry.framework.db.Config;
import me.jerry.framework.db.Dao;
import me.jerry.framework.db.IDao;
import me.jerry.framework.db.OnDatabaseUpdateListener;
import me.jerry.framework.db.SqlException;
import me.jerry.framedemo.model.Book;

public class MainAppCompatActivity extends AppCompatActivityFrame implements View.OnClickListener {
    @AutoFindView
    TextView tv_content;
    @AutoFindView(listeners = View.OnClickListener.class)
    Button bt_add;
    @AutoFindView(listeners = View.OnClickListener.class)
    Button bt_delete;
    @AutoFindView(listeners = View.OnClickListener.class)
    Button bt_modify;
    @AutoFindView(listeners = View.OnClickListener.class)
    Button btQuery;
    @AutoFindView(listeners = View.OnClickListener.class)
    Button btCommunicate;
    @AutoFindView
    WebView webview;
    @AutoFindView
    ScrollView svTxt;
    CommManager commManager = CommManager.getInstance();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void loadData(){
        commManager.init(4, null, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState, PersistableBundle persistentState) {

    }

    @Override
    public void onClick(View v) {
        if(R.id.bt_add == v.getId()) {
            IDao<Book> bookDao = new Dao<>(Book.class, getApplicationContext(), new OnDatabaseUpdateListener() {
                @Override
                public void onUpdate(Config.DB_CONIFG dbConfig, SQLiteDatabase database, IDao dao) throws SqlException {

                }
            });
            long count = bookDao.countBySelection(null);
            Book book = new Book();
            book.setAuthor("Tom");
            book.setEbook(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 0});
            book.setNumber("T10-" + new DecimalFormat("000").format(count + 1));
            book.setPublishTime(System.currentTimeMillis());
            book.setStatus((short)1);
            book.setTitle("story");
            book.setTotalPage(581);
            bookDao.insertData(book);
            bookDao.closeDb();
        } else if(R.id.bt_delete == v.getId()) {
            IDao<Book> bookDao = new Dao<>(Book.class, getApplicationContext(), new OnDatabaseUpdateListener() {
                @Override
                public void onUpdate(Config.DB_CONIFG dbConfig, SQLiteDatabase database, IDao dao) throws SqlException {

                }
            });
            bookDao.deleteAll();
            bookDao.closeDb();
        } else if(R.id.bt_modify == v.getId()) {
            IDao<Book> bookDao = new Dao<>(Book.class, getApplicationContext(), new OnDatabaseUpdateListener() {
                @Override
                public void onUpdate(Config.DB_CONIFG dbConfig, SQLiteDatabase database, IDao dao) throws SqlException {

                }
            });
            long count = bookDao.countBySelection(null);
            Book book = new Book();
            book.setAuthor("Tom");
            book.setEbook(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 0});
            book.setNumber("T10-" + new DecimalFormat("000").format(count + 1));
            book.setPublishTime(System.currentTimeMillis());
            book.setStatus((short)1);
            book.setTitle("story");
            book.setTotalPage(581);
            bookDao.insertData(book);
            bookDao.closeDb();
        } else if(R.id.bt_query == v.getId()) {
            IDao<Book> bookDao = new Dao<>(Book.class, getApplicationContext(), new OnDatabaseUpdateListener() {
                @Override
                public void onUpdate(Config.DB_CONIFG dbConfig, SQLiteDatabase database, IDao dao) throws SqlException {

                }
            });
            List<Book> list = bookDao.queryAllData();
            svTxt.setVisibility(View.VISIBLE);
            webview.setVisibility(View.GONE);
            tv_content.setText("");
            for(Book book : list) {
                tv_content.append(book.toString() + "\n");
            }
            bookDao.closeDb();
        } else if(R.id.bt_communicate == v.getId()) {
            CommEntity ce = new CommEntity();
            CommEntity.RequestBean rb = new CommEntity.RequestBean();
//            rb.url = "http://192.168.1.184:8080/me.jerry.personal_website/test.gsp";
            rb.url = "https://www.baidu.com";
            ce.setRequestBean(rb);
            ce.setRequestType(CommEntity.ERequestType.REQUEST_TYPE_GET);
            ce.setEventListener(new ICommEventListener() {
                @Override
                public void onStart(CommEntity commEntity) {
                    Log.i("comm", "start");
                }

                @Override
                public void onEnd(final CommEntity commEntity) {
                    Log.i("comm", "end");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            svTxt.setVisibility(View.VISIBLE);
                            webview.setVisibility(View.VISIBLE);
                            String html = new String(commEntity.getResponseBean().body, Charset.forName("UTF-8"));
                            html = html.substring(html.indexOf("<html>"), html.indexOf("</html>") + 7);
                            tv_content.setText(html);
                            webview.loadData(html, "text/html; charset=UTF-8", null);
                        }
                    });
                }

                @Override
                public void onError(NetException e, CommEntity commEntity) {
                    Log.i("comm", "error");
                }
            });
            commManager.sendMessage(ce);
        }
    }
}
