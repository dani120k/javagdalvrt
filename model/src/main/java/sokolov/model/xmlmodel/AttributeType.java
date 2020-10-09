package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class AttributeType {
    @JacksonXmlProperty(localName = "DataType")
    private String dataType;

    @JacksonXmlProperty(localName = "Value")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> valueList;

    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public List<String> getValueList() {
        return valueList;
    }

    public void setValueList(List<String> valueList) {
        this.valueList = valueList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
