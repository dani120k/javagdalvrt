package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class KernelType {
    @JacksonXmlProperty(localName = "Size")
    private int size;

    @JacksonXmlProperty(localName = "Coefs")
    private String coefs;

    @JacksonXmlProperty(localName = "normalized")
    private int normalized;

    public int getNormalized() {
        return normalized;
    }

    public void setNormalized(int normalized) {
        this.normalized = normalized;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCoefs() {
        return coefs;
    }

    public void setCoefs(String coefs) {
        this.coefs = coefs;
    }
}
