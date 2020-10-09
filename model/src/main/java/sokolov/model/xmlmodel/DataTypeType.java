package sokolov.model.xmlmodel;

public enum DataTypeType {
    Byte("Byte"),
    UInt16("UInt16"),
    Int16("Int16"),
    UInt32("UInt32"),
    Int32("Int32"),
    Float32("Float32"),
    Float64("Float64"),
    CInt16("CInt16"),
    CInt32("CInt32"),
    CFloat32("CFloat32"),
    CFloat64("CFloat64");

    private String value;

    DataTypeType(String value){
        this.value = value;
    }
}
