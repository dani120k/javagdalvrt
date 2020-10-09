package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class OOIType {
    @JacksonXmlProperty(localName = "key", isAttribute = true)
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
