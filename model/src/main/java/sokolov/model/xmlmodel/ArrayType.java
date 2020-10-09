package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ArrayType {
    @JacksonXmlProperty(localName = "DataType")
    private String dataType;

    @JacksonXmlProperty(localName = "Dimension")
    private DimensionType dimension;

    @JacksonXmlProperty(localName = "DimensionRef")
    private DimensionRefType dimensionRef;

    @JacksonXmlProperty(localName = "SRS")
    private SRSType srs;

    @JacksonXmlProperty(localName = "Unit")
    private String unit;

    @JacksonXmlProperty(localName = "NoDataValue")
    private Double noDataValue;

    @JacksonXmlProperty(localName = "Offset")
    private Double offset;

    @JacksonXmlProperty(localName = "Scale")
    private Double scale;

    @JacksonXmlProperty(localName = "RegularlySpacedValues")
    private RegularlySpacedValuesType regularlySpacedValues;

    @JacksonXmlProperty(localName = "ConstantValue")
    private ConstantValueType constantValue;

    @JacksonXmlProperty(localName = "InlineValues")
    private InlineValuesType inlineValues;

    @JacksonXmlProperty(localName = "InlineValuesWithValueElement")
    private InlineValuesWithValueElementType inlineValuesWithValueElement;

    @JacksonXmlProperty(localName = "Source")
    private SourceType source;

    @JacksonXmlProperty(localName = "Attribute")
    private AttributeType attribute;

    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public DimensionType getDimension() {
        return dimension;
    }

    public void setDimension(DimensionType dimension) {
        this.dimension = dimension;
    }

    public DimensionRefType getDimensionRef() {
        return dimensionRef;
    }

    public void setDimensionRef(DimensionRefType dimensionRef) {
        this.dimensionRef = dimensionRef;
    }

    public SRSType getSrs() {
        return srs;
    }

    public void setSrs(SRSType srs) {
        this.srs = srs;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getNoDataValue() {
        return noDataValue;
    }

    public void setNoDataValue(Double noDataValue) {
        this.noDataValue = noDataValue;
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

    public RegularlySpacedValuesType getRegularlySpacedValues() {
        return regularlySpacedValues;
    }

    public void setRegularlySpacedValues(RegularlySpacedValuesType regularlySpacedValues) {
        this.regularlySpacedValues = regularlySpacedValues;
    }

    public ConstantValueType getConstantValue() {
        return constantValue;
    }

    public void setConstantValue(ConstantValueType constantValue) {
        this.constantValue = constantValue;
    }

    public InlineValuesType getInlineValues() {
        return inlineValues;
    }

    public void setInlineValues(InlineValuesType inlineValues) {
        this.inlineValues = inlineValues;
    }

    public InlineValuesWithValueElementType getInlineValuesWithValueElement() {
        return inlineValuesWithValueElement;
    }

    public void setInlineValuesWithValueElement(InlineValuesWithValueElementType inlineValuesWithValueElement) {
        this.inlineValuesWithValueElement = inlineValuesWithValueElement;
    }

    public SourceType getSource() {
        return source;
    }

    public void setSource(SourceType source) {
        this.source = source;
    }

    public AttributeType getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeType attribute) {
        this.attribute = attribute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
