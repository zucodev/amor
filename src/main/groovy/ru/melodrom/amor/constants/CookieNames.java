package ru.melodrom.amor.constants;

import grails.util.Environment;

public class CookieNames {

    private static final String SESSION = "amor_session";
    private static final String REMEMBER_ME = "amor_remember_me";

    public static String getSession() {
        return getSession(Environment.getCurrent());
    }

    public static String getRememberMe() {
        return getRememberMe(Environment.getCurrent());
    }

    private static String getSession(Environment environment) {
        return getCookieWithPostfix(environment, SESSION);
    }

    private static String getRememberMe(Environment environment) {
        return getCookieWithPostfix(environment, REMEMBER_ME);
    }

    private static String getCookieWithPostfix(Environment environment, String cookieName) {
        String postfix = "";

        if (environment.equals(Environment.PRODUCTION)) {
            return cookieName + postfix;
        }
        // dev mode
        return cookieName + postfix;
    }
}

