package sokolov.model.common;

import com.ctc.wstx.shaded.msv_core.datatype.xsd.UnsignedByteType;

import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;

public class PixelValue {
    public byte byteValue;

    public short shortValue;

    public int intValue;

    public float floatValue;

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
            pixelValue.byteValue = (byte)resultedValue;
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
            pixelValue.intValue = (int)resultedValue;
        }

        if (type.equals("short")){
            int[] array = new int[3];
            data.getSampleModel().getPixel(
                    x,
                    y,
                    array,
                    data.getDataBuffer()
            );

            int resultedValue = array[bandNumber - 1];

            pixelValue.type = type;
            pixelValue.shortValue = (short)resultedValue;
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
            pixelValue.floatValue = (float) resultedValue;
        }

        return pixelValue;
    }

    public static PixelValue parse(String noDataValue, String type) {
        PixelValue pixelValue = new PixelValue();
        pixelValue.type = type;

        if (type.equals("byte")) {
            pixelValue.byteValue = (byte)Integer.parseInt(noDataValue);
        }

        if (type.equals("int")){
            pixelValue.intValue = Integer.parseInt(noDataValue);
        }

        if (type.equals("double")){
            pixelValue.doubleValue = Double.parseDouble(noDataValue);
        }

        if (type.equals("short")){
            pixelValue.shortValue = (short)Integer.parseInt(noDataValue);
        }

        if (type.equals("float")){
            pixelValue.floatValue = Float.parseFloat(noDataValue);
        }

        return pixelValue;
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
                    return this.intValue == pixelValue.intValue;
                case "double":
                    return this.doubleValue == pixelValue.doubleValue;
                case "short":
                    return this.shortValue == pixelValue.shortValue;
                case "float":
                    return this.floatValue == pixelValue.floatValue;
            }
        }

        return false;
    }

    public void setValueForSampleModel(int x, int y, int band, DataBuffer dataBuffer, SampleModel sampleModel) {
        if (type.equals("byte")) {
            sampleModel.setSample(x, y, band, this.byteValue, dataBuffer);
        }

        if (type.equals("int")){
            sampleModel.setSample(x, y, band, this.intValue, dataBuffer);
        }

        if (type.equals("double")){
            sampleModel.setSample(x, y, band, this.doubleValue, dataBuffer);
        }

        if (type.equals("short")){
            sampleModel.setSample(x, y, band, this.shortValue, dataBuffer);
        }

        if (type.equals("float")){
            sampleModel.setSample(x, y, band, this.floatValue, dataBuffer);
        }
    }

    public void setNoDataValue(int x, int y, int bandNumber, DataBuffer dataBuffer, SampleModel sampleModel, String noDataValue) {
        //TODO parse noDataValue

        sampleModel.setSample(x, y, bandNumber, (byte)Integer.parseInt(noDataValue), dataBuffer);
    }
}
