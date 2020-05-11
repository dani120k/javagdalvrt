package sokolov.model.enums;

public enum  CPLValueType {
    CPL_VALUE_STRING(0),  /**< String */
    CPL_VALUE_REAL(1),    /**< Real number */
    CPL_VALUE_INTEGER(2);  /**< Integer */

    private int value;

    CPLValueType(int value) {
        this.value = value;
    }
}
