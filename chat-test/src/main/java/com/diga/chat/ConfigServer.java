package com.diga.chat;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConfigServer implements Serializable {
    private int port = 10086;
    private int threadWorkNum = 10;
    private int messageTime = 3600;
    private int maxConnection = 10;
}
