package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class GDALRasterAttributeTableType {
    @JacksonXmlProperty(localName = "FieldDefn")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FieldDefnType> fieldDefnTypeList;

    @JacksonXmlProperty(localName = "Row")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<RowType> rowTypeList;

    public List<FieldDefnType> getFieldDefnTypeList() {
        return fieldDefnTypeList;
    }

    public void setFieldDefnTypeList(List<FieldDefnType> fieldDefnTypeList) {
        this.fieldDefnTypeList = fieldDefnTypeList;
    }

    public List<RowType> getRowTypeList() {
        return rowTypeList;
    }

    public void setRowTypeList(List<RowType> rowTypeList) {
        this.rowTypeList = rowTypeList;
    }
}
