package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class MetadataType {
    @JacksonXmlProperty(localName = "domain", isAttribute = true)
    private String domain;

    @JacksonXmlProperty(localName = "format", isAttribute = true)
    private String format;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
