package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class GCPType {
    @JacksonXmlProperty(localName = "Id")
    private String id;

    @JacksonXmlProperty(localName = "Info")
    private String info;

    @JacksonXmlProperty(localName = "Pixel")
    private Double pixel;

    @JacksonXmlProperty(localName = "Line")
    private Double line;

    @JacksonXmlProperty(localName = "X")
    private Double x;

    @JacksonXmlProperty(localName = "Y")
    private Double y;

    @JacksonXmlProperty(localName = "Z")
    private Double z;

    @Deprecated
    @JacksonXmlProperty(localName = "GCPZ")
    private Double gcpz;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Double getPixel() {
        return pixel;
    }

    public void setPixel(Double pixel) {
        this.pixel = pixel;
    }

    public Double getLine() {
        return line;
    }

    public void setLine(Double line) {
        this.line = line;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public Double getGcpz() {
        return gcpz;
    }

    public void setGcpz(Double gcpz) {
        this.gcpz = gcpz;
    }
}
