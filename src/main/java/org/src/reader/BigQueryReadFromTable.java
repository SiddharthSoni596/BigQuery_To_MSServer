package org.src.reader;


import com.google.api.services.bigquery.model.TableReference;
import com.google.api.services.bigquery.model.TableRow;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.values.PCollection;

public class BigQueryReadFromTable {

    public static PCollection<TableRow> readFromTable(Pipeline pipeline, TableReference tableSpec) {
        PCollection<TableRow> rows =
                pipeline.apply("Read from BigQuery query",
                        BigQueryIO.readTableRows().from(String.format("%s:%s.%s",tableSpec.getProjectId(),tableSpec.getDatasetId(),tableSpec.getTableId()))
                );
        return rows;
    }
}
