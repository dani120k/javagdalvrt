package sokolov.model.enums;

public enum GDALColorInterp {
    /*! Undefined */                                      GCI_Undefined(0),
    /*! Greyscale */                                      GCI_GrayIndex(1),
    /*! Paletted (see associated color table) */          GCI_PaletteIndex(2),
    /*! Red band of RGBA image */                         GCI_RedBand(3),
    /*! Green band of RGBA image */                       GCI_GreenBand(4),
    /*! Blue band of RGBA image */                        GCI_BlueBand(5),
    /*! Alpha (0=transparent, 255=opaque) */              GCI_AlphaBand(6),
    /*! Hue band of HLS image */                          GCI_HueBand(7),
    /*! Saturation band of HLS image */                   GCI_SaturationBand(8),
    /*! Lightness band of HLS image */                    GCI_LightnessBand(9),
    /*! Cyan band of CMYK image */                        GCI_CyanBand(10),
    /*! Magenta band of CMYK image */                     GCI_MagentaBand(11),
    /*! Yellow band of CMYK image */                      GCI_YellowBand(12),
    /*! Black band of CMYK image */                       GCI_BlackBand(13),
    /*! Y Luminance */                                    GCI_YCbCr_YBand(14),
    /*! Cb Chroma */                                      GCI_YCbCr_CbBand(15),
    /*! Cr Chroma */                                      GCI_YCbCr_CrBand(16),
    /*! Max current value (equals to GCI_YCbCr_CrBand currently) */ GCI_Max(16);

    private int value;

    GDALColorInterp(int value){
        this.value = value;
    }

    public String getColorInterpretationName() {
        switch (value){
            case 0:
                return "GCI_Undefined";
            case 1:
                return "GCI_GrayIndex";
            case 2:
                return "GCI_PaletteIndex";
            case 3:
                return "GCI_RedBand";
            case 4:
                return "GCI_GreenBand";
            case 5:
                return "GCI_BlueBand";
            case 6:
                return "GCI_AlphaBand";
            case 7:
                return "GCI_HueBand";
            case 8:
                return "GCI_SaturationBand";
            case 9:
                return "GCI_LightnessBand";
            case 10:
                return "GCI_CyanBand";
            case 11:
                return "GCI_MagentaBand";
            case 12:
                return "GCI_YellowBand";
            case 13:
                return "GCI_BlackBand";
            case 14:
                return "GCI_YCbCr_YBand";
            case 15:
                return "GCI_YCbCr_CbBand";
            case 16:
                return "GCI_YCbCr_CrBand";
            default:
                return null;
        }
    }
}
