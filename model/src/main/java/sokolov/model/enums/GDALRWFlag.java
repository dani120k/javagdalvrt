package sokolov.model.enums;

public enum GDALRWFlag {
    /*! Read data */   GF_Read(0),
    /*! Write data */  GF_Write(1);

    private int value;

    GDALRWFlag(int value){
        this.value = value;
    }
}
