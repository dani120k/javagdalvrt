package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ConstantValueType {
    @JacksonXmlProperty(localName = "offset", isAttribute = true)
    private String offset;

    @JacksonXmlProperty(localName = "count", isAttribute = true)
    private String count;

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
