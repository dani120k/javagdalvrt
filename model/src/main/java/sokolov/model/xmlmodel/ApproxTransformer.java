package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ApproxTransformer {
    @JacksonXmlProperty(localName = "MaxError")
    private Double maxError;

    @JacksonXmlProperty(localName = "BaseTransformer")
    private BaseTransformer baseTransformer;

    public Double getMaxError() {
        return maxError;
    }

    public void setMaxError(Double maxError) {
        this.maxError = maxError;
    }

    public BaseTransformer getBaseTransformer() {
        return baseTransformer;
    }

    public void setBaseTransformer(BaseTransformer baseTransformer) {
        this.baseTransformer = baseTransformer;
    }
}
