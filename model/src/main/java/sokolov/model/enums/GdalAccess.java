package sokolov.model.enums;

public enum GdalAccess {
    /*! Read only (no update) access */ GA_ReadOnly(0),
    /*! Read/write access. */           GA_Update(1);

    private int value;

    GdalAccess(int value){
        this.value = value;
    }
}
