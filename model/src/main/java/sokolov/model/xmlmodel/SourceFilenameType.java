package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class SourceFilenameType {
    @JacksonXmlProperty(localName = "relativeToVRT", isAttribute = true)
    private int relativeToVRT;

    @JacksonXmlProperty(localName = "relativetoVRT", isAttribute = true)
    private int relativetoVRT;

    @JacksonXmlProperty(localName = "shared", isAttribute = true)
    private OGRBooleanType shared;

    public int getRelativeToVRT() {
        return relativeToVRT;
    }

    public void setRelativeToVRT(int relativeToVRT) {
        this.relativeToVRT = relativeToVRT;
    }

    public int getRelativetoVRT() {
        return relativetoVRT;
    }

    public void setRelativetoVRT(int relativetoVRT) {
        this.relativetoVRT = relativetoVRT;
    }

    public OGRBooleanType getShared() {
        return shared;
    }

    public void setShared(OGRBooleanType shared) {
        this.shared = shared;
    }
}
