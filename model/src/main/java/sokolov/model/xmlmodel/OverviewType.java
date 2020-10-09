package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class OverviewType {
    @JacksonXmlProperty(localName = "SourceFilename")
    private SourceFilenameType sourceFilename;

    @JacksonXmlProperty(localName = "SourceBand")
    private String sourceBand;

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
}
