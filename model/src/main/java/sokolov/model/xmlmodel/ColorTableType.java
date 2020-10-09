package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class ColorTableType {
    @JacksonXmlProperty(localName = "Entry")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ColorTableEntryType> colorTableEntryTypeList;

    public List<ColorTableEntryType> getColorTableEntryTypeList() {
        return colorTableEntryTypeList;
    }

    public void setColorTableEntryTypeList(List<ColorTableEntryType> colorTableEntryTypeList) {
        this.colorTableEntryTypeList = colorTableEntryTypeList;
    }
}
