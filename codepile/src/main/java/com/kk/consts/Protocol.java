package com.kk.consts;

public enum Protocol {

    // 正文
    CONTENT("content"),

    // 上下线
    ONLINE("online"),;

    private String value;

    Protocol(String value) {
        this.value = value;
    }

    public static Protocol getProtocol(String p) {
        for (Protocol protocol : Protocol.values()) {
            if (protocol.value.equals(p)) {
                return protocol;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
