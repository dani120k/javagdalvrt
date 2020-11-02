package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class WarpOption {
    @JacksonXmlText
    private String option;

    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String nameAttribute;

    @JacksonXmlProperty(localName = "SourceDataset")
    private SourceFilenameType sourceDataset;

    @JacksonXmlProperty(localName = "BandList")
    private BandList bandList;

    @JacksonXmlProperty(localName = "Transformer")
    private Transformer transformer;

    public SourceFilenameType getSourceDataset() {
        return sourceDataset;
    }

    public void setSourceDataset(SourceFilenameType sourceDataset) {
        this.sourceDataset = sourceDataset;
    }

    public BandList getBandList() {
        return bandList;
    }

    public void setBandList(BandList bandList) {
        this.bandList = bandList;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getNameAttribute() {
        return nameAttribute;
    }

    public void setNameAttribute(String nameAttribute) {
        this.nameAttribute = nameAttribute;
    }
}
