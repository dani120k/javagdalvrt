package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class RowType {
    @JacksonXmlProperty(localName = "F")
    private List<Object> fList;

    @JacksonXmlProperty(localName = "index", isAttribute = true)
    private int index;

    public List<Object> getfList() {
        return fList;
    }

    public void setfList(List<Object> fList) {
        this.fList = fList;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
