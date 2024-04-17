package org.src.PipeOptionCustom;
import org.apache.beam.sdk.options.*;

public interface MainPipelineOptions extends PipelineOptions {


    @Description("Project")
    @Default.String("burner-sidsoni1")
    @Validation.Required
    String getProjectId();
    void setProjectId(String proj);

    @Description("Dataset")
    @Default.String("ApiToBqStg")
    @Validation.Required
    String getDataSetId();
    void setDataSetId(String value);

    @Description("Table")
    @Default.String("Employee")
    @Validation.Required
    String gettableName();
    void setTableName(String val);


}