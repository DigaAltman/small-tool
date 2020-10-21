package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diga.db.annotation.Column;

import java.io.Serializable;


@TableName("food_user")
public class FoodUser implements Serializable {

    @TableId
    @TableField("user_id")
    private String userId;

}
