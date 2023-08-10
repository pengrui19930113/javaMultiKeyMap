package com.chess.landlord;

public enum Protocol {
    START(Protocol.S_TO_C,"服务器向客户端通知游戏开始")
    ;
    public static final int C_TO_S = 0x01;
    public static final int S_TO_C = 0x02;
    public static final int BOTH = C_TO_S|S_TO_C;
    public final int method;
    public final String desc;

    Protocol(int method, String desc) {
        this.method = method;
        this.desc = desc;
    }
}
