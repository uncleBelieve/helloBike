package com.coder520.mamabike.common.exception;

import com.coder520.mamabike.common.constants.Constants;

/**
 * Created by hjy[www.coder520.com] 2017/7/31.
 */
public class MaMaBikeException extends Exception{

    /**
     *
     */
    private static final long serialVersionUID = -7370331410579650067L;

    public MaMaBikeException(String message) {
        super(message);
    }

    public int getStatusCode() {
        return Constants.RESP_STATUS_INTERNAL_ERROR;
    }
}
