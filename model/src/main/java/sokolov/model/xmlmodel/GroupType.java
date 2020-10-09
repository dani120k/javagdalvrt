package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class GroupType {
    @JacksonXmlProperty(localName = "Dimension")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<DimensionType> dimensionTypeList;

    @JacksonXmlProperty(localName = "Attribute")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<AttributeType> attributeTypeList;

    @JacksonXmlProperty(localName = "Array")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ArrayType> arrayTypeList;

    @JacksonXmlProperty(localName = "Group")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<GroupType> groupTypeList;

    public List<DimensionType> getDimensionTypeList() {
        return dimensionTypeList;
    }

    public void setDimensionTypeList(List<DimensionType> dimensionTypeList) {
        this.dimensionTypeList = dimensionTypeList;
    }

    public List<AttributeType> getAttributeTypeList() {
        return attributeTypeList;
    }

    public void setAttributeTypeList(List<AttributeType> attributeTypeList) {
        this.attributeTypeList = attributeTypeList;
    }

    public List<ArrayType> getArrayTypeList() {
        return arrayTypeList;
    }

    public void setArrayTypeList(List<ArrayType> arrayTypeList) {
        this.arrayTypeList = arrayTypeList;
    }

    public List<GroupType> getGroupTypeList() {
        return groupTypeList;
    }

    public void setGroupTypeList(List<GroupType> groupTypeList) {
        this.groupTypeList = groupTypeList;
    }
}
