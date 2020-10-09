package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class SimpleSourceType {
    //<xs:group ref="SimpleSourceElementsGroup"/>
    @JacksonXmlProperty(localName = "SourceFilename")
    private SourceFilenameType sourceFilename;

    @JacksonXmlProperty(localName = "OpenOptions")
    private OpenOptionsType openOptions;

    @JacksonXmlProperty(localName = "SourceBand")
    private String sourceBand;

    @JacksonXmlProperty(localName = "SourceProperties")
    private SourcePropertiesType sourceProperties;

    @JacksonXmlProperty(localName = "SrcRect")
    private RectType srcRect;

    @JacksonXmlProperty(localName = "DstRect")
    private RectType dstRect;

    @JacksonXmlProperty(localName = "resampling", isAttribute = true)
    private String resampling;

    public SourceFilenameType getSourceFilename() {
        return sourceFilename;
    }

    public void setSourceFilename(SourceFilenameType sourceFilename) {
        this.sourceFilename = sourceFilename;
    }

    public OpenOptionsType getOpenOptions() {
        return openOptions;
    }

    public void setOpenOptions(OpenOptionsType openOptions) {
        this.openOptions = openOptions;
    }

    public String getSourceBand() {
        return sourceBand;
    }

    public void setSourceBand(String sourceBand) {
        this.sourceBand = sourceBand;
    }

    public SourcePropertiesType getSourceProperties() {
        return sourceProperties;
    }

    public void setSourceProperties(SourcePropertiesType sourceProperties) {
        this.sourceProperties = sourceProperties;
    }

    public RectType getSrcRect() {
        return srcRect;
    }

    public void setSrcRect(RectType srcRect) {
        this.srcRect = srcRect;
    }

    public RectType getDstRect() {
        return dstRect;
    }

    public void setDstRect(RectType dstRect) {
        this.dstRect = dstRect;
    }

    public String getResampling() {
        return resampling;
    }

    public void setResampling(String resampling) {
        this.resampling = resampling;
    }
}
