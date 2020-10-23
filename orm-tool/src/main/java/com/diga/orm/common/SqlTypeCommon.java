package com.diga.orm.common;

import lombok.Getter;

import java.math.BigDecimal;
import java.sql.*;

public interface SqlTypeCommon {

    enum mysqlEnum {
        VARCHAR(String.class),
        CHAR(String.class),
        BLOB(Byte[].class),
        INTEGER(Long.class),
        TINYINT(Integer.class),
        SMALLINT(Integer.class),
        MEDIUMINT(Integer.class),
        BIT(Boolean.class),
        BIGINT(BigDecimal.class),
        FLOAT(Float.class),
        DOUBLE(Double.class),
        DECIMAL(BigDecimal.class),
        PK(Long.class),
        DATE(Date.class),
        TIME(Time.class),
        DATETIME(java.util.Date.class),
        TIMESTAMP(Timestamp.class),
        YEAR(Date.class);



        @Getter
        private Class javaType;

        mysqlEnum(Class javaType) {
            this.javaType = javaType;
        }
    }

    enum oracleEnum {
        CHAR(String.class),
        VARCHAR2(String.class),
        LONG(String.class),
        NUMBER(java.math.BigDecimal.class),
        RAW(Byte[].class),
        LONGRAW(Byte[].class),
        DATE(Date.class),
        TIMESTAMP(Timestamp.class),
        BLOB(Blob.class),
        Clob(Clob.class);

        @Getter
        private Class javaType;

        oracleEnum(Class javaType) {
            this.javaType = javaType;
        }

    }


    enum IndexType {
        MUL,
        PRI,
        UNI
    }
}
