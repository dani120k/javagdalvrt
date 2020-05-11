package sokolov.javagdalvrt;


import sokolov.model.enums.GDALDataType;

public class DatasetProperty {
    public boolean bHasDatasetMask;
    private boolean isFileOk;
    //TODO fix to private
    public double[] adfGeoTransform;
    public int nRasterXSize;
    public int nRasterYSize;
    public GDALDataType firstBandType;
    public int nBlockXSize;
    public int nBlockYSize;
    public boolean[] pabHasNoData;
    public double[] padfNoDataValues;
    public boolean[] pabHasOffset;
    public double[] padfOffset;
    public boolean[] pabHasScale;
    public double[] padfScale;
    public int nMaskBlockXSize;
    public int nMaskBlockYSize;

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
