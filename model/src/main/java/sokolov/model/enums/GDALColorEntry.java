package sokolov.model.enums;

public class GDALColorEntry {
    /*! gray, red, cyan or hue */
    public short c1;

    /*! green, magenta, or lightness */
    public short c2;

    /*! blue, yellow, or saturation */
    public short c3;

    /*! alpha or blackband */
    public short c4;

    public GDALColorEntry(){

    }

    public GDALColorEntry(int c1, int c2, int c3, int c4) {
        this.c1 = (short)c1;
        this.c2 = (short)c2;
        this.c3 = (short)c3;
        this.c4 = (short)c4;
    }

}
