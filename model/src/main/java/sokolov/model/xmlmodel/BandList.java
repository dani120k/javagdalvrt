package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class BandList {
    @JacksonXmlProperty(localName = "BandMapping")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<BandMapping> bandMappingList;

    public List<BandMapping> getBandMappingList() {
        return bandMappingList;
    }

    public void setBandMappingList(List<BandMapping> bandMappingList) {
        this.bandMappingList = bandMappingList;
    }
}
