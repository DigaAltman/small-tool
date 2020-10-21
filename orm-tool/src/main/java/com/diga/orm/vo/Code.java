package com.diga.orm.vo;

import com.diga.orm.common.CodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author      DigaAltman
 * @date        {date}
 * @description {description}
 */
@Data
public class Code <T> implements Serializable {

    // 代码类型, 是 .xml, .java, 还是 .properties
    private CodeEnum codeType;

    // 代码名称
    private String fileName;

    // 代码内容
    private T body;

    public Code(CodeEnum codeType, String fileName, T body) {
    	this.codeType = codeType;
    	this.fileName = fileName;
    	this.body = body;
    }
}
