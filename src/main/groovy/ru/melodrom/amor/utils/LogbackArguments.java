package ru.melodrom.amor.utils;

import net.logstash.logback.argument.StructuredArgument;

import static net.logstash.logback.argument.StructuredArguments.kv;

public class LogbackArguments {

    public static final String ev = "event";
    public static final String uId = "userId";
    public static final String email = "email";
    public static final String sessId = "sessionId";

    public static StructuredArgument ev(Object value) {
        return kv(ev, value);
    }

    public static StructuredArgument uId(Object value) {
        return kv(uId, value);
    }

    public static StructuredArgument email(Object value) {
        return kv(email, value);
    }

    public static StructuredArgument sessId(Object value) {
        return kv(sessId, value);
    }

}
