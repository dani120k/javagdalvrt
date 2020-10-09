package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class SourceType {
    @JacksonXmlProperty(localName = "SourceFilename")
    private SourceFilenameType sourceFilename;

    @JacksonXmlProperty(localName = "SourceArray")
    private String sourceArray;

    @JacksonXmlProperty(localName = "SourceBand")
    private String sourceBand;

    @JacksonXmlProperty(localName = "SourceTranspose")
    private String sourceTranspose;

    @JacksonXmlProperty(localName = "SourceView")
    private String sourceView;

    @JacksonXmlProperty(localName = "SourceSlab")
    private SourceSlabType sourceSlab;

    @JacksonXmlProperty(localName = "DestSlab")
    private DestSlabType destSlab;

    public SourceFilenameType getSourceFilename() {
        return sourceFilename;
    }

    public void setSourceFilename(SourceFilenameType sourceFilename) {
        this.sourceFilename = sourceFilename;
    }

    public String getSourceArray() {
        return sourceArray;
    }

    public void setSourceArray(String sourceArray) {
        this.sourceArray = sourceArray;
    }

    public String getSourceBand() {
        return sourceBand;
    }

    public void setSourceBand(String sourceBand) {
        this.sourceBand = sourceBand;
    }

    public String getSourceTranspose() {
        return sourceTranspose;
    }

    public void setSourceTranspose(String sourceTranspose) {
        this.sourceTranspose = sourceTranspose;
    }

    public String getSourceView() {
        return sourceView;
    }

    public void setSourceView(String sourceView) {
        this.sourceView = sourceView;
    }

    public SourceSlabType getSourceSlab() {
        return sourceSlab;
    }

    public void setSourceSlab(SourceSlabType sourceSlab) {
        this.sourceSlab = sourceSlab;
    }

    public DestSlabType getDestSlab() {
        return destSlab;
    }

    public void setDestSlab(DestSlabType destSlab) {
        this.destSlab = destSlab;
    }
}
