package com.diga.orm.vo;

import com.diga.orm.common.CodeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author      DigaAltman
 * @date        {date}
 * @description {description}
 */
@Data
public class CodeNode implements Serializable {

    // 下一种类型的代码
    private CodeNode next;

    // 代码类型, 是 .xml, .java, 还是 .properties
    private CodeTypeEnum codeTypeEnum;

    // 当前类型的代码的内容
    private List<Code> codeList = new ArrayList();

    @Data
    @AllArgsConstructor
    public static class Code implements Serializable {
        // 代码名称
        private String name;

        // 代码内容
        private String body;
    }
}
