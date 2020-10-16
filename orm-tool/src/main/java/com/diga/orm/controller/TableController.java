package com.diga.orm.controller;

import com.diga.orm.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tab")
public class TableController {

    @Autowired
    private TableService tableService;


}
