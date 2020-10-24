package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ComplexSourceType {
    @JacksonXmlProperty(localName = "SourceFilename")
    private SourceFilenameType sourceFilename;

    @JacksonXmlProperty(localName = "OpenOptions")
    private OpenOptionsType openOptions;

    @JacksonXmlProperty(localName = "SourceBand")
    private Integer sourceBand;

    @JacksonXmlProperty(localName = "SourceProperties")
    private SourcePropertiesType sourceProperties;

    @JacksonXmlProperty(localName = "SrcRect")
    private RectType srcRect;

    @JacksonXmlProperty(localName = "DstRect")
    private RectType dstRect;

    @JacksonXmlProperty(localName = "ScaleOffset")
    private Double scaleOffset;

    @JacksonXmlProperty(localName = "ScaleRatio")
    private Double scaleRatio;

    @JacksonXmlProperty(localName = "ColorTableComponent")
    private int colorTableComponent;

    @JacksonXmlProperty(localName = "Exponent")
    private Double exponent;

    @JacksonXmlProperty(localName = "SrcMin")
    private Double srcMin;

    @JacksonXmlProperty(localName = "SrcMax")
    private Double srcMax;

    @JacksonXmlProperty(localName = "DstMin")
    private Double dstMin;

    @JacksonXmlProperty(localName = "DstMax")
    private Double dstMax;

    @JacksonXmlProperty(localName = "NODATA")
    private String NODATA;

    @JacksonXmlProperty(localName = "LUT")
    private String LUT;

    @JacksonXmlProperty(localName = "resampling", isAttribute = true)
    private String resampling;

    public ComplexSourceType(SimpleSourceType simpleSourceType) {
        this.dstRect = simpleSourceType.getDstRect();
        this.openOptions = simpleSourceType.getOpenOptions();
        this.srcRect = simpleSourceType.getSrcRect();
        this.sourceProperties = simpleSourceType.getSourceProperties();
        this.sourceFilename = simpleSourceType.getSourceFilename();
        this.sourceBand = simpleSourceType.getSourceBand();
        this.resampling = simpleSourceType.getResampling();
    }

    public ComplexSourceType() {
    }

    public SourceFilenameType getSourceFilename() {
        return sourceFilename;
    }

    public void setSourceFilename(SourceFilenameType sourceFilename) {
        this.sourceFilename = sourceFilename;
    }

    public OpenOptionsType getOpenOptions() {
        return openOptions;
    }

    public void setOpenOptions(OpenOptionsType openOptions) {
        this.openOptions = openOptions;
    }

    public Integer getSourceBand() {
        return sourceBand;
    }

    public void setSourceBand(Integer sourceBand) {
        this.sourceBand = sourceBand;
    }

    public SourcePropertiesType getSourceProperties() {
        return sourceProperties;
    }

    public void setSourceProperties(SourcePropertiesType sourceProperties) {
        this.sourceProperties = sourceProperties;
    }

    public RectType getSrcRect() {
        return srcRect;
    }

    public void setSrcRect(RectType srcRect) {
        this.srcRect = srcRect;
    }

    public RectType getDstRect() {
        return dstRect;
    }

    public void setDstRect(RectType dstRect) {
        this.dstRect = dstRect;
    }

    public Double getScaleOffset() {
        return scaleOffset;
    }

    public void setScaleOffset(Double scaleOffset) {
        this.scaleOffset = scaleOffset;
    }

    public Double getScaleRatio() {
        return scaleRatio;
    }

    public void setScaleRatio(Double scaleRatio) {
        this.scaleRatio = scaleRatio;
    }

    public int getColorTableComponent() {
        return colorTableComponent;
    }

    public void setColorTableComponent(int colorTableComponent) {
        this.colorTableComponent = colorTableComponent;
    }

    public Double getExponent() {
        return exponent;
    }

    public void setExponent(Double exponent) {
        this.exponent = exponent;
    }

    public Double getSrcMin() {
        return srcMin;
    }

    public void setSrcMin(Double srcMin) {
        this.srcMin = srcMin;
    }

    public Double getSrcMax() {
        return srcMax;
    }

    public void setSrcMax(Double srcMax) {
        this.srcMax = srcMax;
    }

    public Double getDstMin() {
        return dstMin;
    }

    public void setDstMin(Double dstMin) {
        this.dstMin = dstMin;
    }

    public Double getDstMax() {
        return dstMax;
    }

    public void setDstMax(Double dstMax) {
        this.dstMax = dstMax;
    }

    public String getNODATA() {
        return NODATA;
    }

    public void setNODATA(String NODATA) {
        this.NODATA = NODATA;
    }

    public String getLUT() {
        return LUT;
    }

    public void setLUT(String LUT) {
        this.LUT = LUT;
    }

    public String getResampling() {
        return resampling;
    }

    public void setResampling(String resampling) {
        this.resampling = resampling;
    }
}
