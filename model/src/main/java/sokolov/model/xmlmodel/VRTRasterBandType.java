package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class VRTRasterBandType {
    @JacksonXmlProperty(localName = "Description")
    private String description;

    @JacksonXmlProperty(localName = "UnitType")
    private String unitType;

    @JacksonXmlProperty(localName = "Offset")
    private Double offset;

    @JacksonXmlProperty(localName = "Scale")
    private Double scale;

    @JacksonXmlProperty(localName = "CategoryNames")
    private CategoryNamesType categoryNames;

    @JacksonXmlProperty(localName = "ColorTable")
    private ColorTableType colorTable;

    @JacksonXmlProperty(localName = "GDALRasterAttributeTable")
    private GDALRasterAttributeTableType gdalRasterAttributeTable;

    @JacksonXmlProperty(localName = "NoDataValue")
    private Double noDataValue;

    @JacksonXmlProperty(localName = "NodataValue")
    private Double nodataValue;

    @JacksonXmlProperty(localName = "HideNoDataValue")
    private int hideNoDataValue;

    @JacksonXmlProperty(localName = "Metadata")
    private MetadataType metadata;

    @JacksonXmlProperty(localName = "ColorInterp")
    private ColorInterpType colorInterp;

    @JacksonXmlProperty(localName = "Overview")
    private OverviewType overview;

    @JacksonXmlProperty(localName = "MaskBand")
    private MaskBandType maskBand;

    @JacksonXmlProperty(localName = "Histograms")
    private HistogramsType histograms;

    //for a VRTSourcedRasterBand. Each element may be repeated
    @JacksonXmlProperty(localName = "SimpleSource")
    private SimpleSourceType simpleSource;

    @JacksonXmlProperty(localName = "ComplexSource")
    private ComplexSourceType complexSource;

    @JacksonXmlProperty(localName = "AveragedSource")
    private SimpleSourceType averagedSource;

    @JacksonXmlProperty(localName = "KernelFilteredSource")
    private KernelFilteredSourceType kernelFilteredSource;

    //for a VRTDerivedRasterBand
    @JacksonXmlProperty(localName = "PixelFunctionType")
    private String pixelFunctionType;

    @JacksonXmlProperty(localName = "SourceTransferType")
    private DataTypeType sourceTransferType;

    @JacksonXmlProperty(localName = "PixelFunctionLanguage")
    private String pixelFunctionLanguage;

    @JacksonXmlProperty(localName = "PixelFunctionCode")
    private String pixelFunctionCode;

    /**
     *  <xs:element name="PixelFunctionArguments">
     *                     <xs:complexType>
     *                         <xs:anyAttribute processContents="lax"/>
     *                     </xs:complexType>
     *                 </xs:element>
     */
    //@JacksonXmlProperty(localName = "PixelFunctionArguments")

    @JacksonXmlProperty(localName = "BufferRadius")
    private int bufferRadius;


    //for a VRTRawRasterBand
    @JacksonXmlProperty(localName = "SourceFilename")
    private SourceFilenameType sourceFilename;

    @JacksonXmlProperty(localName = "ImageOffset")
    private int imageOffset;

    @JacksonXmlProperty(localName = "PixelOffset")
    private int pixelOffset;

    @JacksonXmlProperty(localName = "LineOffset")
    private int lineOffset;

    @JacksonXmlProperty(localName = "ByteOrder")
    private int byteOrder;

    @JacksonXmlProperty(localName = "dataType", isAttribute = true)
    private DataTypeType dataType;

    @JacksonXmlProperty(localName = "band", isAttribute = true)
    private int band;

    @JacksonXmlProperty(localName = "subClass", isAttribute = true)
    private VRTRasterBandSubClassType subClass;

    @JacksonXmlProperty(localName = "BlockXSize", isAttribute = true)
    private int blockXSize;

    @JacksonXmlProperty(localName = "BlockYSize", isAttribute = true)
    private int blockYSize;

    public int getHideNoDataValue() {
        return hideNoDataValue;
    }

    public void setHideNoDataValue(int hideNoDataValue) {
        this.hideNoDataValue = hideNoDataValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public Double getOffset() {
        return offset;
    }

    public void setOffset(Double offset) {
        this.offset = offset;
    }

    public Double getScale() {
        return scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public CategoryNamesType getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(CategoryNamesType categoryNames) {
        this.categoryNames = categoryNames;
    }

    public ColorTableType getColorTable() {
        return colorTable;
    }

    public void setColorTable(ColorTableType colorTable) {
        this.colorTable = colorTable;
    }

    public GDALRasterAttributeTableType getGdalRasterAttributeTable() {
        return gdalRasterAttributeTable;
    }

    public void setGdalRasterAttributeTable(GDALRasterAttributeTableType gdalRasterAttributeTable) {
        this.gdalRasterAttributeTable = gdalRasterAttributeTable;
    }

    public Double getNoDataValue() {
        return noDataValue;
    }

    public void setNoDataValue(Double noDataValue) {
        this.noDataValue = noDataValue;
    }

    public Double getNodataValue() {
        return nodataValue;
    }

    public void setNodataValue(Double nodataValue) {
        this.nodataValue = nodataValue;
    }


    public MetadataType getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataType metadata) {
        this.metadata = metadata;
    }

    public ColorInterpType getColorInterp() {
        return colorInterp;
    }

    public void setColorInterp(ColorInterpType colorInterp) {
        this.colorInterp = colorInterp;
    }

    public OverviewType getOverview() {
        return overview;
    }

    public void setOverview(OverviewType overview) {
        this.overview = overview;
    }

    public MaskBandType getMaskBand() {
        return maskBand;
    }

    public void setMaskBand(MaskBandType maskBand) {
        this.maskBand = maskBand;
    }

    public HistogramsType getHistograms() {
        return histograms;
    }

    public void setHistograms(HistogramsType histograms) {
        this.histograms = histograms;
    }

    public SimpleSourceType getSimpleSource() {
        return simpleSource;
    }

    public void setSimpleSource(SimpleSourceType simpleSource) {
        this.simpleSource = simpleSource;
    }

    public ComplexSourceType getComplexSource() {
        return complexSource;
    }

    public void setComplexSource(ComplexSourceType complexSource) {
        this.complexSource = complexSource;
    }

    public SimpleSourceType getAveragedSource() {
        return averagedSource;
    }

    public void setAveragedSource(SimpleSourceType averagedSource) {
        this.averagedSource = averagedSource;
    }

    public KernelFilteredSourceType getKernelFilteredSource() {
        return kernelFilteredSource;
    }

    public void setKernelFilteredSource(KernelFilteredSourceType kernelFilteredSource) {
        this.kernelFilteredSource = kernelFilteredSource;
    }

    public String getPixelFunctionType() {
        return pixelFunctionType;
    }

    public void setPixelFunctionType(String pixelFunctionType) {
        this.pixelFunctionType = pixelFunctionType;
    }

    public DataTypeType getSourceTransferType() {
        return sourceTransferType;
    }

    public void setSourceTransferType(DataTypeType sourceTransferType) {
        this.sourceTransferType = sourceTransferType;
    }

    public String getPixelFunctionLanguage() {
        return pixelFunctionLanguage;
    }

    public void setPixelFunctionLanguage(String pixelFunctionLanguage) {
        this.pixelFunctionLanguage = pixelFunctionLanguage;
    }

    public String getPixelFunctionCode() {
        return pixelFunctionCode;
    }

    public void setPixelFunctionCode(String pixelFunctionCode) {
        this.pixelFunctionCode = pixelFunctionCode;
    }

    public int getBufferRadius() {
        return bufferRadius;
    }

    public void setBufferRadius(int bufferRadius) {
        this.bufferRadius = bufferRadius;
    }

    public SourceFilenameType getSourceFilename() {
        return sourceFilename;
    }

    public void setSourceFilename(SourceFilenameType sourceFilename) {
        this.sourceFilename = sourceFilename;
    }

    public int getImageOffset() {
        return imageOffset;
    }

    public void setImageOffset(int imageOffset) {
        this.imageOffset = imageOffset;
    }

    public int getPixelOffset() {
        return pixelOffset;
    }

    public void setPixelOffset(int pixelOffset) {
        this.pixelOffset = pixelOffset;
    }

    public int getLineOffset() {
        return lineOffset;
    }

    public void setLineOffset(int lineOffset) {
        this.lineOffset = lineOffset;
    }

    public int getByteOrder() {
        return byteOrder;
    }

    public void setByteOrder(int byteOrder) {
        this.byteOrder = byteOrder;
    }

    public DataTypeType getDataType() {
        return dataType;
    }

    public void setDataType(DataTypeType dataType) {
        this.dataType = dataType;
    }

    public int getBand() {
        return band;
    }

    public void setBand(int band) {
        this.band = band;
    }

    public VRTRasterBandSubClassType getSubClass() {
        return subClass;
    }

    public void setSubClass(VRTRasterBandSubClassType subClass) {
        this.subClass = subClass;
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
