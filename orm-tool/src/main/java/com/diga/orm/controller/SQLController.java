package com.diga.orm.controller;

import com.diga.db.core.DB;
import com.diga.generic.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sql")
public class SQLController {

    @Autowired
    private DB db;

    @GetMapping("/{sql}")
    public List sql(@PathVariable("sql") String sql) {
        List<Map> mapList = null;

        try {
            mapList = db.selectList(sql, Map.class);
        } catch (Exception e) {
            Map err = new HashMap<String, String>();
            err.put("status", 500);
            err.put("message", "出错了,错误原因:" + e);
            return (List) CollectionUtils.to(err.values(), List.class);
        }

        return mapList;
    }
}
