package ru.melodrom.amor.utils;

import grails.util.Holders;
import groovy.lang.Closure;

import javax.sql.DataSource;
import java.sql.SQLException;

public class ScSql {

    public static void withInstance(DataSource dataSource, Closure c) throws SQLException {
        groovy.sql.Sql sql = null;
        try {
            sql = new groovy.sql.Sql(dataSource);
            c.call(sql);
        } finally {
            if (sql != null) sql.close();
        }
    }

    public static void execute(DataSource dataSource, String query) throws SQLException {
        groovy.sql.Sql sql = null;
        try {
            sql = new groovy.sql.Sql(dataSource);
            sql.execute(query);
        } finally {
            if (sql != null) sql.close();
        }
    }

    public static void withInstance(Closure c) throws SQLException {
        groovy.sql.Sql sql = null;
        try {
            sql = new groovy.sql.Sql((DataSource) Holders.getApplicationContext().getBean("dataSource"));
            c.call(sql);
        } finally {
            if (sql != null) sql.close();
        }
    }

    public static void execute(String query) throws SQLException {
        groovy.sql.Sql sql = null;
        try {
            sql = new groovy.sql.Sql((DataSource) Holders.getApplicationContext().getBean("dataSource"));
            sql.execute(query);
        } finally {
            if (sql != null) sql.close();
        }
    }

}
