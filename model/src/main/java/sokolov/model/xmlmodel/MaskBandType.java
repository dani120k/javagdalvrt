package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class MaskBandType {
    @JacksonXmlProperty(localName = "VRTRasterBand")
    private VRTRasterBandType vrtRasterBand;

    public VRTRasterBandType getVrtRasterBand() {
        return vrtRasterBand;
    }

    public void setVrtRasterBand(VRTRasterBandType vrtRasterBand) {
        this.vrtRasterBand = vrtRasterBand;
    }
}
