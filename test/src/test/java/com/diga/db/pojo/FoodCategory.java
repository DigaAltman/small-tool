package com.diga.db.pojo;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@ToString
public class FoodCategory implements Serializable {
    private Integer id;
    private String name;
    private Integer type;
    private Integer fatherId;
    private String logo;
    private String slogan;
    private String catImage;
    private String bgColor;
    private Date createTime;
    private Date updateTime;
}
