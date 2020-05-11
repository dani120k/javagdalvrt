package sokolov.model.enums;


public class BandProperty {
    public GDALColorInterp colorInterpretation;
    public GDALDataType dataType;
    public GDALColorTableH colorTable; //link to gdal.h
    public boolean bHasNoData;
    public double noDataValue;
    public boolean bHasOffset;
    public double dfOffset;
    public boolean bHasScale;
    public double dfScale;

    public GDALColorInterp getColorInterpretation() {
        return colorInterpretation;
    }

    public void setColorInterpretation(GDALColorInterp colorInterpretation) {
        this.colorInterpretation = colorInterpretation;
    }

    public GDALDataType getDataType() {
        return dataType;
    }

    public void setDataType(GDALDataType dataType) {
        this.dataType = dataType;
    }

    public GDALColorTableH getColorTable() {
        return colorTable;
    }

    public void setColorTable(GDALColorTableH colorTable) {
        this.colorTable = colorTable;
    }

    public boolean isbHasNoData() {
        return bHasNoData;
    }

    public void setbHasNoData(boolean bHasNoData) {
        this.bHasNoData = bHasNoData;
    }

    public double getNoDataValue() {
        return noDataValue;
    }

    public void setNoDataValue(double noDataValue) {
        this.noDataValue = noDataValue;
    }

    public boolean isbHasOffset() {
        return bHasOffset;
    }

    public void setbHasOffset(boolean bHasOffset) {
        this.bHasOffset = bHasOffset;
    }

    public double getDfOffset() {
        return dfOffset;
    }

    public void setDfOffset(double dfOffset) {
        this.dfOffset = dfOffset;
    }

    public boolean isbHasScale() {
        return bHasScale;
    }

    public void setbHasScale(boolean bHasScale) {
        this.bHasScale = bHasScale;
    }

    public double getDfScale() {
        return dfScale;
    }

    public void setDfScale(double dfScale) {
        this.dfScale = dfScale;
    }
}
