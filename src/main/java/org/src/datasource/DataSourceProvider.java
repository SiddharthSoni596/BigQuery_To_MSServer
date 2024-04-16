package org.src.datasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

public class DataSourceProvider {
    public static DataSource getDataSource(String jdbcDriver,String jdbcUrl,String user,String pwd){
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(jdbcDriver);
            dataSource.setJdbcUrl(jdbcUrl);
            dataSource.setMaxPoolSize(2);
            dataSource.setInitialPoolSize(2);
            dataSource.setUser(user);
            dataSource.setPassword(pwd);
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
    }
}
