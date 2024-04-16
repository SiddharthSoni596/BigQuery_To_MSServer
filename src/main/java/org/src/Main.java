package org.src;

import com.google.api.services.bigquery.model.TableRow;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.jdbc.JdbcIO;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.src.datasource.DataSourceProvider;


import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

public class Main {
    static class LogOutput<T> extends DoFn<T,T> {

        @ProcessElement
        public void processElement(ProcessContext c) throws Exception {
            System.out.println(c.element());
        }
    }
    public static void TruncateTable(String tableName,DataSource dataSource){
        // Define your SQL query
        String sqlQuery = "TRUNCATE TABLE "+tableName;

        // Declare resources to be used
        try (
                // Acquire a connection from the DataSource
                Connection connection = dataSource.getConnection();
                // Create a PreparedStatement with your SQL query
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        ) {
            preparedStatement.execute();
            System.out.println("Table Truncated Successfully!!");
        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace();
        }
    }

    public static PCollection<TableRow> getDummyBqTableRowData(Pipeline pipeline){
        TableRow row1 = new TableRow();
        row1.put("id","77");
        row1.put("name","HASHING");
        row1.put("location","INDIA");
        TableRow row2 = new TableRow();
        row2.put("id","12");
        row2.put("name","HEAP");
        row2.put("location","AMERICA");
        TableRow row3 = new TableRow();
        row3.put("id","32");
        row3.put("name","TREE");
        row3.put("location","RUSSIA");
        return pipeline.apply("Creating Dummy Data ",Create.of(row1,row2,row3));
    }

    public static void TableRowToMSServer(PCollection<TableRow> tableRows,String sqlQuery , DataSource dataSource){
        tableRows.apply("Writing to MS-SERVER",
            JdbcIO.<TableRow>write()
            .withDataSourceConfiguration(JdbcIO.DataSourceConfiguration.create(dataSource))
            .withStatement(sqlQuery)
            .withPreparedStatementSetter((element, statement) -> {
                statement.setString(1, (String) element.getOrDefault("id",null));
                statement.setString(2, (String) element.getOrDefault("name",null));
                statement.setString(3, (String) element.getOrDefault("location",null));
            }));
    }

    public static void main(String[] args) {
        /*
        table schema:
        'id','varchar(10)','YES','',NULL,''
        'name','varchar(45)','YES','',NULL,''
        'location','varchar(45)','YES','',NULL,''
         */

        Pipeline pipeline = Pipeline.create();
        String tableName = "POC.test_table";
        DataSource dataSource = DataSourceProvider.getDataSource("com.mysql.cj.jdbc.Driver","jdbc:mysql://localhost:3306/POC","root","");
        String insQuery = "INSERT INTO "+ tableName +" (id, name, location) VALUES (?, ?, ?)";

        //Truncate Before Load
        TruncateTable(tableName,dataSource);

        //Mimic the BigQueryIO.read data type PCollection<TableRow>
        PCollection<TableRow> input = getDummyBqTableRowData(pipeline);

        //Calling writer with input pcollectiond , insert query and data source object
        TableRowToMSServer(input,insQuery,dataSource);


        input.apply(ParDo.of(new LogOutput<>()));
        pipeline.run().waitUntilFinish();

    }

}