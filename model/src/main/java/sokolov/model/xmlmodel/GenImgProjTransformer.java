package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class GenImgProjTransformer {
    @JacksonXmlProperty(localName = "SrcGeoTransform")
    private String srcGeoTransform;

    @JacksonXmlProperty(localName = "SrcInvGeoTransform")
    private String srcInvGeoTransform;

    @JacksonXmlProperty(localName = "DstGeoTransform")
    private String dstGeoTransform;

    @JacksonXmlProperty(localName = "DstInvGeoTransform")
    private String dstInvGeoTransform;

    public String getSrcGeoTransform() {
        return srcGeoTransform;
    }

    public void setSrcGeoTransform(String srcGeoTransform) {
        this.srcGeoTransform = srcGeoTransform;
    }

    public String getSrcInvGeoTransform() {
        return srcInvGeoTransform;
    }

    public void setSrcInvGeoTransform(String srcInvGeoTransform) {
        this.srcInvGeoTransform = srcInvGeoTransform;
    }

    public String getDstGeoTransform() {
        return dstGeoTransform;
    }

    public void setDstGeoTransform(String dstGeoTransform) {
        this.dstGeoTransform = dstGeoTransform;
    }

    public String getDstInvGeoTransform() {
        return dstInvGeoTransform;
    }

    public void setDstInvGeoTransform(String dstInvGeoTransform) {
        this.dstInvGeoTransform = dstInvGeoTransform;
    }
}
