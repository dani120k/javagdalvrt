package sokolov.model.xmlmodel;

public enum OGRBooleanType {

    OGR_TRUE(true),
    OGR_FALSE(false);

    private boolean value;
    private static String[] trueTokens = {"1", "ON", "on", "YES", "yes", "TRUE", "true", "True"};
    private static String[] falseTokens = {"0", "OFF", "off", "NO", "no", "FALSE", "false", "False"};


    OGRBooleanType(boolean value){
        this.value = value;
    }

    public OGRBooleanType getValue(String value){
        for (String trueToken : trueTokens) {
            if (trueToken.equals(value))
                return OGR_TRUE;
        }

        for (String falseToken : falseTokens) {
            if (falseToken.equals(value))
                return OGR_FALSE;
        }

        return null;
    }
}
