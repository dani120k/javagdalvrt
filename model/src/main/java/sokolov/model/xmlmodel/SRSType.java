package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class SRSType {
    @JacksonXmlProperty(localName = "dataAxisToSRSAxisMapping")
    private String dataAxisToSRSAxisMapping;

    public String getDataAxisToSRSAxisMapping() {
        return dataAxisToSRSAxisMapping;
    }

    public void setDataAxisToSRSAxisMapping(String dataAxisToSRSAxisMapping) {
        this.dataAxisToSRSAxisMapping = dataAxisToSRSAxisMapping;
    }
}
