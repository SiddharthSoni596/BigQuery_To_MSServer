package org.src.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.beam.sdk.transforms.SerializableFunction;

import javax.sql.DataSource;

public class MyDataSourceProvider implements SerializableFunction<Void, DataSource> {
    private static transient DataSource dataSource;

    @Override
    public synchronized DataSource apply(Void input) {
        if (dataSource == null)
        {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3306/POC");
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setUsername("root");
            config.setPassword("");
            config.setMaximumPoolSize(1);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000); // 30 seconds
            config.addDataSourceProperty("oracle.jdbc.ReadTimeout","90000");

            // Add more custom properties as needed
            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }
}
