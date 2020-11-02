package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Transformer {
    @JacksonXmlProperty(localName = "ApproxTransformer")
    private ApproxTransformer approxTransformer;

    public ApproxTransformer getApproxTransformer() {
        return approxTransformer;
    }

    public void setApproxTransformer(ApproxTransformer approxTransformer) {
        this.approxTransformer = approxTransformer;
    }
}
