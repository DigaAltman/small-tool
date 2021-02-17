package com.diga.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据包装器
 */
@Data
@NoArgsConstructor
public class DataWrapper implements Serializable {
    private Object data;
    private long createdTime = System.currentTimeMillis();
    private long updatedTime = System.currentTimeMillis();
    private int version = 0;

    public DataWrapper(Object data) {
        this.data = data;
    }
}
