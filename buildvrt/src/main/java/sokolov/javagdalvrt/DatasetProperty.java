package sokolov.javagdalvrt;


import sokolov.model.enums.GDALDataType;

import java.util.concurrent.atomic.AtomicInteger;

public class DatasetProperty {
    public boolean bHasDatasetMask;
    private boolean isFileOk;
    //TODO fix to private was null but changed to double[6]
    public double[] adfGeoTransform = new double[6];
    public int nRasterXSize;
    public int nRasterYSize;
    public GDALDataType firstBandType;
    public AtomicInteger nBlockXSize;
    public AtomicInteger nBlockYSize;
    public boolean[] pabHasNoData;
    public double[] padfNoDataValues;
    public boolean[] pabHasOffset;
    public double[] padfOffset;
    public boolean[] pabHasScale;
    public double[] padfScale;
    public AtomicInteger nMaskBlockXSize;
    public AtomicInteger nMaskBlockYSize;

    /**
     * Возвращает результат анализа датасета
     * @return  true если все хорошо, false если плохо
     */
    public boolean isFileOK() {
        return isFileOk;
    }

    public void setIsFileOK(Boolean result) {
        this.isFileOk = result;
    }
}
