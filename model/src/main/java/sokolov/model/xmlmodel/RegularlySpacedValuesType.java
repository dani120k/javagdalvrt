package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class RegularlySpacedValuesType {
    @JacksonXmlProperty(localName = "start", isAttribute = true)
    private Double start;

    @JacksonXmlProperty(localName = "increment", isAttribute = true)
    private Double increment;

    public Double getIncrement() {
        return increment;
    }

    public void setIncrement(Double increment) {
        this.increment = increment;
    }

    public Double getStart() {
        return start;
    }

    public void setStart(Double start) {
        this.start = start;
    }
}
