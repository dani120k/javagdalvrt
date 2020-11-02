package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class GDALWarpOptionsType {
    //TODO didnt understand xsd for any sequence
    @JacksonXmlProperty(localName = "WarpMemoryLimit")
    private String warpMemoryLimit;

    @JacksonXmlProperty(localName = "ResampleAlg")
    private String resampleAlg;

    @JacksonXmlProperty(localName = "WorkingDataType")
    private String workingDataType;

    @JacksonXmlProperty(localName = "Option")
    private WarpOption option;
}
