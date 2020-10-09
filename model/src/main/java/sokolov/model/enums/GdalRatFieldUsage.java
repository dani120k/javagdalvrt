package sokolov.model.enums;

public enum  GdalRatFieldUsage {
    /*! General purpose field. */          GFU_Generic(0),
    /*! Histogram pixel count */           GFU_PixelCount(1),
    /*! Class name */                      GFU_Name(2),
    /*! Class range minimum */             GFU_Min(3),
    /*! Class range maximum */             GFU_Max(4),
    /*! Class value (min=max), */           GFU_MinMax(5),
    /*! Red class color (0-255), */         GFU_Red(6),
    /*! Green class color (0-255), */       GFU_Green(7),
    /*! Blue class color (0-255), */        GFU_Blue(8),
    /*! Alpha (0=transparent,255=opaque),*/ GFU_Alpha(9),
    /*! Color Range Red Minimum */         GFU_RedMin(10),
    /*! Color Range Green Minimum */       GFU_GreenMin(11),
    /*! Color Range Blue Minimum */        GFU_BlueMin(12),
    /*! Color Range Alpha Minimum */       GFU_AlphaMin(13),
    /*! Color Range Red Maximum */         GFU_RedMax(14),
    /*! Color Range Green Maximum */       GFU_GreenMax(15),
    /*! Color Range Blue Maximum */        GFU_BlueMax(16),
    /*! Color Range Alpha Maximum */       GFU_AlphaMax(17),
    /*! Maximum GFU value (equals to GFU_AlphaMax+1 currently), */ GFU_MaxCount(18);

    int value;

    GdalRatFieldUsage(int value){
        this.value = value;
    }

    public static GdalRatFieldUsage getByValue(int value){
        switch (value){
            case 0:
                return GdalRatFieldUsage.GFU_Generic;
            case 1:
                return GdalRatFieldUsage.GFU_PixelCount;
            case 2:
                return GdalRatFieldUsage.GFU_Name;
            case 3:
                return GdalRatFieldUsage.GFU_Min;
            case 4:
                return GdalRatFieldUsage.GFU_Max;
            case 5:
                return GdalRatFieldUsage.GFU_MinMax;
            case 6:
                return GdalRatFieldUsage.GFU_Red;
            case 7:
                return GdalRatFieldUsage.GFU_Green;
            case 8:
                return GdalRatFieldUsage.GFU_Blue;
            case 9:
                return GdalRatFieldUsage.GFU_Alpha;
            case 10:
                return GdalRatFieldUsage.GFU_RedMin;
            case 11:
                return GdalRatFieldUsage.GFU_GreenMin;
            case 12:
                return GdalRatFieldUsage.GFU_BlueMin;
            case 13:
                return GdalRatFieldUsage.GFU_AlphaMin;
            case 14:
                return GdalRatFieldUsage.GFU_RedMax;
            case 15:
                return GdalRatFieldUsage.GFU_GreenMax;
            case 16:
                return GdalRatFieldUsage.GFU_BlueMax;
            case 17:
                return GdalRatFieldUsage.GFU_AlphaMax;
            case 18:
                return GdalRatFieldUsage.GFU_MaxCount;
            default:
                return GdalRatFieldUsage.GFU_Generic;

        }
    }

}
