package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class BaseTransformer {
    @JacksonXmlProperty(localName = "GenImgProjTransformer")
    private GenImgProjTransformer genImgProjTransformer;

    public GenImgProjTransformer getGenImgProjTransformer() {
        return genImgProjTransformer;
    }

    public void setGenImgProjTransformer(GenImgProjTransformer genImgProjTransformer) {
        this.genImgProjTransformer = genImgProjTransformer;
    }
}
