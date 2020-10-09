package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ColorTableEntryType {
    @JacksonXmlProperty(localName = "c1")
    private Short c1;

    @JacksonXmlProperty(localName = "c2")
    private Short c2;

    @JacksonXmlProperty(localName = "c3")
    private Short c3;

    @JacksonXmlProperty(localName = "c4")
    private Short c4;

    public Short getC1() {
        return c1;
    }

    public void setC1(Short c1) {
        this.c1 = c1;
    }

    public Short getC2() {
        return c2;
    }

    public void setC2(Short c2) {
        this.c2 = c2;
    }

    public Short getC3() {
        return c3;
    }

    public void setC3(Short c3) {
        this.c3 = c3;
    }

    public Short getC4() {
        return c4;
    }

    public void setC4(Short c4) {
        this.c4 = c4;
    }
}
