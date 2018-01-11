package me.jerry.framework.db;

import me.jerry.framework.exception.CustomException;

public class SqlException extends CustomException {


    public SqlException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        // TODO Auto-generated constructor stub
    }

    public SqlException(String detailMessage) {
        super(detailMessage);
        // TODO Auto-generated constructor stub
    }

    public SqlException(Throwable throwable) {
        super(throwable);
        // TODO Auto-generated constructor stub
    }

}
