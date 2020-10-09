package sokolov.model.enums;

import sokolov.model.xmlmodel.DataTypeType;

public enum GDALDataType {
    GDT_Unknown(0), /*! Unknown or unspecified type */

    GDT_Byte(1),        /*! Eight bit unsigned integer */

    GDT_UInt16(2),/*! Sixteen bit unsigned integer */

    GDT_Int16(3),/*! Sixteen bit signed integer */

    GDT_UInt32(4),/*! Thirty two bit unsigned integer */

    GDT_Int32(5),/*! Thirty two bit signed integer */

    GDT_Float32(6),/*! Thirty two bit floating point */

    GDT_Float64(7),/*! Sixty four bit floating point */

    GDT_CInt16(8),/*! Complex Int16 */

    GDT_CInt32(9), /*! Complex Int32 */

    GDT_CFloat32(10),   /*! Complex Float32 */

    GDT_CFloat64(11),    /*! Complex Float64 */

    GDT_TypeCount(12);          /* maximum type # + 1 */

    private int value;

    GDALDataType(int value) {
        this.value = value;
    }

    public static String getValue(GDALDataType eDataType) {
        switch (eDataType){
            case GDT_Unknown:
                return "Unknown";

            case GDT_Byte:
                return "Byte";

            case GDT_UInt16:
                return "UInt16";

            case GDT_Int16:
                return "Int16";

            case GDT_UInt32:
                return "UInt32";

            case GDT_Int32:
                return "Int32";

            case GDT_Float32:
                return "Float32";

            case GDT_Float64:
                return "Float64";

            case GDT_CInt16:
                return "CInt16";

            case GDT_CInt32:
                return "CInt32";

            case GDT_CFloat32:
                return "CFloat32";

            case GDT_CFloat64:
                return "CFloat64";

            default:
                return null;
        }
    }

    public String getDataTypeName() {
        //TODO swith by values for debug
        return null;
    }
}
