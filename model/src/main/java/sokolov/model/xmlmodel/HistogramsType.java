package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class HistogramsType {
    @JacksonXmlProperty(localName = "HistItem")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<HistItemType> histItemTypeList;

    public List<HistItemType> getHistItemTypeList() {
        return histItemTypeList;
    }

    public void setHistItemTypeList(List<HistItemType> histItemTypeList) {
        this.histItemTypeList = histItemTypeList;
    }
}
