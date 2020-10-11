package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class SRSType {
    @JacksonXmlProperty(localName = "dataAxisToSRSAxisMapping", isAttribute = true)
    private String dataAxisToSRSAxisMapping;

    @JacksonXmlText
    private String srs;

    public String getSrs() {
        return srs;
    }

    public void setSrs(String srs) {
        this.srs = srs;
    }

    public String getDataAxisToSRSAxisMapping() {
        return dataAxisToSRSAxisMapping;
    }

    public void setDataAxisToSRSAxisMapping(String dataAxisToSRSAxisMapping) {
        this.dataAxisToSRSAxisMapping = dataAxisToSRSAxisMapping;
    }
}
