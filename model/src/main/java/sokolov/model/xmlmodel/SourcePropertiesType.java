package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class SourcePropertiesType {
    @JacksonXmlProperty(localName = "RasterXSize", isAttribute = true)
    private int rasterXSize;

    @JacksonXmlProperty(localName = "RasterYSize", isAttribute = true)
    private int rasterYSize;

    @JacksonXmlProperty(localName = "DataType", isAttribute = true)
    private DataTypeType dataTypeType;

    @JacksonXmlProperty(localName = "BlockXSize", isAttribute = true)
    private int blockXSize;

    @JacksonXmlProperty(localName = "BlockYSize", isAttribute = true)
    private int blockYSize;

    public int getRasterXSize() {
        return rasterXSize;
    }

    public void setRasterXSize(int rasterXSize) {
        this.rasterXSize = rasterXSize;
    }

    public int getRasterYSize() {
        return rasterYSize;
    }

    public void setRasterYSize(int rasterYSize) {
        this.rasterYSize = rasterYSize;
    }

    public DataTypeType getDataTypeType() {
        return dataTypeType;
    }

    public void setDataTypeType(DataTypeType dataTypeType) {
        this.dataTypeType = dataTypeType;
    }

    public int getBlockXSize() {
        return blockXSize;
    }

    public void setBlockXSize(int blockXSize) {
        this.blockXSize = blockXSize;
    }

    public int getBlockYSize() {
        return blockYSize;
    }

    public void setBlockYSize(int blockYSize) {
        this.blockYSize = blockYSize;
    }
}
