package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class SourceSlabType {
    @JacksonXmlProperty(localName = "offset")
    private String offset;

    @JacksonXmlProperty(localName = "count")
    private String count;

    @JacksonXmlProperty(localName = "step")
    private String step;

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

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
