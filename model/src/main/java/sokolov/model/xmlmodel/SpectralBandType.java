package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class SpectralBandType {
    @JacksonXmlProperty(localName = "SourceFilename")
    private SourceFilenameType sourceFilename;

    @JacksonXmlProperty(localName = "SourceBand")
    private String sourceBand;

    @JacksonXmlProperty(localName = "dstBand", isAttribute = true)
    private Long dstBand;

    public SourceFilenameType getSourceFilename() {
        return sourceFilename;
    }

    public void setSourceFilename(SourceFilenameType sourceFilename) {
        this.sourceFilename = sourceFilename;
    }

    public String getSourceBand() {
        return sourceBand;
    }

    public void setSourceBand(String sourceBand) {
        this.sourceBand = sourceBand;
    }

    public Long getDstBand() {
        return dstBand;
    }

    public void setDstBand(Long dstBand) {
        this.dstBand = dstBand;
    }
}
