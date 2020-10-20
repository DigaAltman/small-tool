package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.diga.db.annotation.Column;
import com.diga.db.annotation.Id;

import java.io.Serializable;


@TableName("food_user")
public class FoodUser implements Serializable {

    @Id
    @Column("user_id")
    private String userId;

}
