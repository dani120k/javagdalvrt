package sokolov.model.enums;

public enum  GdalRatFieldType {
    /*! Integer field */                   GFT_Integer(0),
    /*! Floating point (double) field */   GFT_Real(1),
    /*! String field */                    GFT_String(2);

    private int value;

    GdalRatFieldType(int value){
        this.value = value;
    }

    public static GdalRatFieldType getByValue(int value){
        switch (value){
            case 0:
                return GdalRatFieldType.GFT_Integer;
            case 1:
                return GdalRatFieldType.GFT_Real;
            case 2:
                return GdalRatFieldType.GFT_String;
            default:
                return GdalRatFieldType.GFT_Integer;
        }
    }
}
