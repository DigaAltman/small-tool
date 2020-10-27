package com.diga.orm.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token implements Serializable {
    // 类型
    private String title;

    // 主体
    private String body;

    // 有效期
    private Long termOfValidity;
}
