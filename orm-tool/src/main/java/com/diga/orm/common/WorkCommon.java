package com.diga.orm.common;

import lombok.Getter;

public interface WorkCommon {

    String CURRENT_USER = "CURRENT_USER";

    @Getter
    enum SQLProductType {
        MYSQL(0),
        ORACLE(1);

        private int code;

        SQLProductType(int code) {
            this.code = code;
        }
    }
}
