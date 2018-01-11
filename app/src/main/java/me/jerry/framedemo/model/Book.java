package me.jerry.framedemo.model;

import me.jerry.framework.db.Column;
import me.jerry.framework.db.TableEntity;

/**
 * Created by Jerry on 2017/8/16.
 */

public class Book extends TableEntity {
    @Column
    private String title;
    @Column(unique = true)
    private String number;
    @Column
    private String author;
    @Column
    private Integer totalPage;
    @Column
    private Short status;
    @Column(nullable = true)
    private byte[] ebook;
    @Column
    private Long publishTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public byte[] getEbook() {
        return ebook;
    }

    public void setEbook(byte[] ebook) {
        this.ebook = ebook;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }
}
