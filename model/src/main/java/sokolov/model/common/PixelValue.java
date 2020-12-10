package sokolov.model.common;

import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;

public class PixelValue {
    public int byteValue;

    public int uInt16Value;

    public int int16Value;

    public int uIntValue;

    public long uInt32Value;

    public float float32Value;

    public float uFloat32Value;

    public float uFloat64Value;

    public double doubleValue;

    public String type;

    public static PixelValue getPixelValue(int x, int y, String type, Integer bandNumber, Raster data) {
        PixelValue pixelValue = new PixelValue();

        if (type.equals("byte")){
            int[] array = new int[3];
            data.getSampleModel().getPixel(
                    x,
                    y,
                    array,
                    data.getDataBuffer()
            );

            int resultedValue = array[bandNumber - 1];

            pixelValue.type = type;
            pixelValue.byteValue = resultedValue;
        }

        if (type.equals("int")){
            int[] array = new int[3];
            data.getSampleModel().getPixel(
                    x,
                    y,
                    array,
                    data.getDataBuffer()
            );

            int resultedValue = array[bandNumber - 1];

            pixelValue.type = type;
            pixelValue.uIntValue = (int)resultedValue;
        }

        if (type.equals("ushort")){
            int[] array = new int[3];
            data.getSampleModel().getPixel(
                    x,
                    y,
                    array,
                    data.getDataBuffer()
            );

            int resultedValue = array[bandNumber - 1];

            pixelValue.type = type;
            pixelValue.uInt16Value = resultedValue;
        }

        if (type.equals("double")){
            double[] array = new double[3];
            data.getSampleModel().getPixel(
                    x,
                    y,
                    array,
                    data.getDataBuffer()
            );

            double resultedValue = array[bandNumber - 1];

            pixelValue.type = type;
            pixelValue.doubleValue = (double) resultedValue;
        }

        if (type.equals("float")){
            float[] array = new float[3];
            data.getSampleModel().getPixel(
                    x,
                    y,
                    array,
                    data.getDataBuffer()
            );

            float resultedValue = array[bandNumber - 1];

            pixelValue.type = type;
            pixelValue.float32Value = (float) resultedValue;
        }

        return pixelValue;
    }

    public static PixelValue parse(String noDataValue, String type) {
        PixelValue pixelValue = new PixelValue();
        pixelValue.type = type;

        if (noDataValue != null) {
            if (type.equals("byte")) {
                pixelValue.byteValue =  Integer.parseInt(noDataValue);
            }

            if (type.equals("int")) {
                pixelValue.uIntValue = Integer.parseInt(noDataValue);
            }

            if (type.equals("double")) {
                pixelValue.doubleValue = Double.parseDouble(noDataValue);
            }

            if (type.equals("ushort")) {
                pixelValue.uInt16Value = Integer.parseInt(noDataValue);
            }

            if (type.equals("float")) {
                pixelValue.float32Value = Float.parseFloat(noDataValue);
            }
        }

        return pixelValue;
    }

    public static String getStringType(int type) {
        //TODO
        switch (type){
            case 0-100:
                return "int";
        }

        return null;
    }

    public static PixelValue getEmptyForType(String type) {
        PixelValue pixelValue = new PixelValue();
        pixelValue.type = type;

        switch (type){
            case "byte":
                pixelValue.byteValue = 0;
            case "int":
                pixelValue.uIntValue =0;
            case "double":
                pixelValue.doubleValue = 0.0;
            case "ushort":
                pixelValue.uInt16Value = 0;
            case "float":
                pixelValue.float32Value = 0;
        }

        return pixelValue;
    }

    public static double calcDiff(PixelValue e, PixelValue s) {
        switch (e.type){
            case "byte":
                return e.byteValue - s.byteValue;
            case "int":
                return e.uIntValue - s.uIntValue;
            case "double":
                return e.doubleValue - s.doubleValue;
            case "ushort":
                return e.uInt16Value - s.uInt16Value;
            case "float":
                return e.float32Value - s.float32Value;
            default:
                return 0;
        }
    }

    public static double getMaxForType(String type) {
        switch (type){
            case "byte":
                return 255;
            case "int":
                return 2147483647;
            case "double":
                return Double.MAX_VALUE;
            case "short":
                return 32767;
            case "ushort":
                return 65535;
            case "float":
                return Float.MAX_VALUE;
            default:
                return 0;
        }
    }

    public static double getMinForType(String type) {
        switch (type){
            case "byte":
                return 0;
            case "int":
                return 0;
            case "double":
                return Double.MIN_VALUE;
            case "short":
                return -32768;
            case "ushort":
                return 0;
            case "float":
                return Float.MIN_VALUE;
            default:
                return 0;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        PixelValue pixelValue = (PixelValue) obj;

        if (this.type.equals(pixelValue.type)){
            switch (type){
                case "byte":
                    return this.byteValue == pixelValue.byteValue;
                case "int":
                    return this.uIntValue == pixelValue.uIntValue;
                case "double":
                    return this.doubleValue == pixelValue.doubleValue;
                case "ushort":
                    return this.uInt16Value == pixelValue.uInt16Value;
                case "float":
                    return this.float32Value == pixelValue.float32Value;
            }
        }

        return false;
    }

    public void setValueForSampleModel(int x, int y, int band, DataBuffer dataBuffer, SampleModel sampleModel) {
        if (type.equals("byte")) {
            sampleModel.setSample(x, y, band, this.byteValue, dataBuffer);
        }

        if (type.equals("int")){
            sampleModel.setSample(x, y, band, this.uIntValue, dataBuffer);
        }

        if (type.equals("double")){
            sampleModel.setSample(x, y, band, this.doubleValue, dataBuffer);
        }

        if (type.equals("ushort")){
            sampleModel.setSample(x, y, band, this.uInt16Value, dataBuffer);
        }

        if (type.equals("float")){
            sampleModel.setSample(x, y, band, this.float32Value, dataBuffer);
        }
    }

    public void setNoDataValue(int x, int y, int bandNumber, DataBuffer dataBuffer, SampleModel sampleModel, String noDataValue) {
        //TODO parse noDataValue

        if (noDataValue == null)
            noDataValue = "0";

        sampleModel.setSample(x, y, bandNumber, Integer.parseInt(noDataValue), dataBuffer);
    }

    public double getAnyValue() {
        switch (type){
            case "byte":
                return this.byteValue;
            case "int":
                return this.uIntValue;
            case "double":
                return this.doubleValue;
            case "ushort":
                return this.uInt16Value;
            case "float":
                return this.float32Value;
            default:
                return 0;
        }
    }

    public void setDoubleValue(double value) {
        switch (type){
            case "byte":
                this.byteValue = (int)value;
            case "int":
                this.uIntValue = (int)value;
            case "double":
                this.doubleValue = (double) value;
            case "ushort":
                this.uInt16Value = (int)value;
            case "float":
                this.float32Value = (float) value;
        }
    }
}
