package sokolov.model.common;

import sokolov.model.enums.GDALDataType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SupportMethods {
    /** Append a string to a StringList and return a pointer to the modified
     * StringList.
     *
     * If the input StringList is NULL, then a new StringList is created.
     * Note that CSLAddString performance when building a list is in O(n^2)
     * which can cause noticeable slow down when n > 10000.
     */
    public static String[] CSLAddString(String[] papszStrList, String pszNewString){
        if (papszStrList == null){
            papszStrList = new String[0];
        }

        String[] newPapszStrList = new String[papszStrList.length + 1];

        for(int i = 0; i < papszStrList.length; i++)
            newPapszStrList[i] = papszStrList[i];

        newPapszStrList[papszStrList.length] = pszNewString;

        return newPapszStrList;
    }

    public static String VRTSerializeNoData(Double dfVal, GDALDataType eDataType, int nPrecision) {
        if (dfVal.isNaN())
            return "nan";
        else if (eDataType == GDALDataType.GDT_Float32 && dfVal == -Double.MAX_VALUE){
            return "-3.4028234663852886e+38";
        } else if (eDataType == GDALDataType.GDT_Float32 && dfVal == Double.MAX_VALUE){
            return "3.4028234663852886e+38";
        } else {
            return String.format("%s", BigDecimal.valueOf(dfVal)
                    .setScale(nPrecision, RoundingMode.HALF_UP)
                    .doubleValue());
        }
    }
}
