package sokolov.model.xmlmodel;

public enum VRTRasterBandSubClassType {
    VRTWarpedRasterBand("VRTWarpedRasterBand"),
    VRTDerivedRasterBand("VRTDerivedRasterBand"),
    VRTRawRasterBand("VRTRawRasterBand"),
    VRTPansharpenedRasterBand("VRTPansharpenedRasterBand");

    private String value;

    VRTRasterBandSubClassType(String value){
        this.value = value;
    }
}
