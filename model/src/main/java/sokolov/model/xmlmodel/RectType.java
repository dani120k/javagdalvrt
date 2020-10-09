package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class RectType {
    @JacksonXmlProperty(localName = "xOff")
    private Double xOff;

    @JacksonXmlProperty(localName = "yOff")
    private Double yOff;

    @JacksonXmlProperty(localName = "xSize")
    private Double xSize;

    @JacksonXmlProperty(localName = "ySize")
    private Double ySize;

    public Double getxOff() {
        return xOff;
    }

    public void setxOff(Double xOff) {
        this.xOff = xOff;
    }

    public Double getyOff() {
        return yOff;
    }

    public void setyOff(Double yOff) {
        this.yOff = yOff;
    }

    public Double getxSize() {
        return xSize;
    }

    public void setxSize(Double xSize) {
        this.xSize = xSize;
    }

    public Double getySize() {
        return ySize;
    }

    public void setySize(Double ySize) {
        this.ySize = ySize;
    }
}
