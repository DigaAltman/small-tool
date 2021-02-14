package com.diga.chat;

import com.diga.generic.utils.PropUtils;
import com.diga.generic.utils.URLUtils;

import java.io.File;

public class Server {
    private ConfigServer configServer;

    public Server() {
        PropUtils.load(URLUtils.classpath("config-server.properties"));

    }
}
