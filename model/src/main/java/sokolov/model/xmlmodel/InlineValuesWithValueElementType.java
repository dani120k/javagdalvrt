package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class InlineValuesWithValueElementType {
    @JacksonXmlProperty(localName = "Value")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> valueList;

    @JacksonXmlProperty(localName = "offset")
    private String offset;

    @JacksonXmlProperty(localName = "count")
    private String count;

    public List<String> getValueList() {
        return valueList;
    }

    public void setValueList(List<String> valueList) {
        this.valueList = valueList;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
