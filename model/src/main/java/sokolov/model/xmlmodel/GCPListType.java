package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class GCPListType {
    @JacksonXmlProperty(localName = "GCP")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<GCPType> gcpTypeList;

    @JacksonXmlProperty(localName = "Projection")
    private String projection;

    @JacksonXmlProperty(localName = "dataAxisToSRSAxisMapping")
    private String dataAxisToSRSAxisMapping;

    public List<GCPType> getGcpTypeList() {
        return gcpTypeList;
    }

    public void setGcpTypeList(List<GCPType> gcpTypeList) {
        this.gcpTypeList = gcpTypeList;
    }

    public String getProjection() {
        return projection;
    }

    public void setProjection(String projection) {
        this.projection = projection;
    }

    public String getDataAxisToSRSAxisMapping() {
        return dataAxisToSRSAxisMapping;
    }

    public void setDataAxisToSRSAxisMapping(String dataAxisToSRSAxisMapping) {
        this.dataAxisToSRSAxisMapping = dataAxisToSRSAxisMapping;
    }
}
