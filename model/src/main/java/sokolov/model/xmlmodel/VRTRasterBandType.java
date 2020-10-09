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
    private String noDataValue;

    @JacksonXmlProperty(localName = "NodataValue")
    private Double nodataValue;

    @JacksonXmlProperty(localName = "HideNoDataValue")
    private Integer hideNoDataValue;

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
    private Integer bufferRadius;


    //for a VRTRawRasterBand
    @JacksonXmlProperty(localName = "SourceFilename")
    private SourceFilenameType sourceFilename;

    @JacksonXmlProperty(localName = "ImageOffset")
    private Integer imageOffset;

    @JacksonXmlProperty(localName = "PixelOffset")
    private Integer pixelOffset;

    @JacksonXmlProperty(localName = "LineOffset")
    private Integer lineOffset;

    @JacksonXmlProperty(localName = "ByteOrder")
    private Integer byteOrder;

    @JacksonXmlProperty(localName = "dataType", isAttribute = true)
    private DataTypeType dataType;

    @JacksonXmlProperty(localName = "band", isAttribute = true)
    private Integer band;

    @JacksonXmlProperty(localName = "subClass", isAttribute = true)
    private VRTRasterBandSubClassType subClass;

    @JacksonXmlProperty(localName = "BlockXSize", isAttribute = true)
    private Integer blockXSize;

    @JacksonXmlProperty(localName = "BlockYSize", isAttribute = true)
    private Integer blockYSize;

    public Integer getHideNoDataValue() {
        return hideNoDataValue;
    }

    public void setHideNoDataValue(Integer hideNoDataValue) {
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

    public String getNoDataValue() {
        return noDataValue;
    }

    public void setNoDataValue(String noDataValue) {
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

    public Integer getBufferRadius() {
        return bufferRadius;
    }

    public void setBufferRadius(Integer bufferRadius) {
        this.bufferRadius = bufferRadius;
    }

    public SourceFilenameType getSourceFilename() {
        return sourceFilename;
    }

    public void setSourceFilename(SourceFilenameType sourceFilename) {
        this.sourceFilename = sourceFilename;
    }

    public Integer getImageOffset() {
        return imageOffset;
    }

    public void setImageOffset(Integer imageOffset) {
        this.imageOffset = imageOffset;
    }

    public Integer getPixelOffset() {
        return pixelOffset;
    }

    public void setPixelOffset(Integer pixelOffset) {
        this.pixelOffset = pixelOffset;
    }

    public Integer getLineOffset() {
        return lineOffset;
    }

    public void setLineOffset(Integer lineOffset) {
        this.lineOffset = lineOffset;
    }

    public Integer getByteOrder() {
        return byteOrder;
    }

    public void setByteOrder(Integer byteOrder) {
        this.byteOrder = byteOrder;
    }

    public DataTypeType getDataType() {
        return dataType;
    }

    public void setDataType(DataTypeType dataType) {
        this.dataType = dataType;
    }

    public Integer getBand() {
        return band;
    }

    public void setBand(Integer band) {
        this.band = band;
    }

    public VRTRasterBandSubClassType getSubClass() {
        return subClass;
    }

    public void setSubClass(VRTRasterBandSubClassType subClass) {
        this.subClass = subClass;
    }

    public Integer getBlockXSize() {
        return blockXSize;
    }

    public void setBlockXSize(Integer blockXSize) {
        this.blockXSize = blockXSize;
    }

    public Integer getBlockYSize() {
        return blockYSize;
    }

    public void setBlockYSize(Integer blockYSize) {
        this.blockYSize = blockYSize;
    }
}
