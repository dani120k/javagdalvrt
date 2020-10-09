package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class DimensionType {
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;

    @JacksonXmlProperty(localName = "type" ,isAttribute = true)
    private String type;

    @JacksonXmlProperty(localName = "direction", isAttribute = true)
    private String direction;

    @JacksonXmlProperty(localName = "size", isAttribute = true)
    private int size;

    @JacksonXmlProperty(localName = "indexingVariable", isAttribute = true)
    private String indexingVariable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getIndexingVariable() {
        return indexingVariable;
    }

    public void setIndexingVariable(String indexingVariable) {
        this.indexingVariable = indexingVariable;
    }
}
