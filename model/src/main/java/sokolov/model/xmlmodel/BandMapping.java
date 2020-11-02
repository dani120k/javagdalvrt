package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class BandMapping {
    @JacksonXmlProperty(localName = "src", isAttribute = true)
    private String src;

    @JacksonXmlProperty(localName = "dst", isAttribute = true)
    private String dst;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }
}
