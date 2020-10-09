package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class OpenOptionsType {
    @JacksonXmlProperty(localName = "OOI")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<OOIType> ooi;

    public List<OOIType> getOoi() {
        return ooi;
    }

    public void setOoi(List<OOIType> ooi) {
        this.ooi = ooi;
    }
}
