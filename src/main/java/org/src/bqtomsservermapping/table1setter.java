package org.src.bqtomsservermapping;

import com.google.api.services.bigquery.model.TableRow;
import org.apache.beam.sdk.io.jdbc.JdbcIO;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class table1setter implements JdbcIO.PreparedStatementSetter<TableRow> {

    @Override
    public void setParameters(TableRow rowMap, PreparedStatement preparedStatement) {
        try {
//            preparedStatement.setString(1,(String) rowMap.getOrDefault("id",null));
            preparedStatement.setNull(1, Types.NULL);
            preparedStatement.setString(2,(String) rowMap.getOrDefault("name",null));
            preparedStatement.setString(3,(String) rowMap.getOrDefault("location",null));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
