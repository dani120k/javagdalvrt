package sokolov.model.xmlmodel;

public enum ColorInterpType {
    Gray("Gray"),
    Palette("Palette"),
    Red("Red"),
    Green("Green"),
    Blue("Blue"),
    Alpha("Alpha"),
    Hue("Hue"),
    Saturation("Saturation"),
    Lightness("Lightness"),
    Cyan("Cyan"),
    Magenta("Magenta"),
    Yellow("Yellow"),
    Black("Black"),
    YCbCr_Y("YCbCr_Y"),
    YCbCr_Cb("YCbCr_Cb"),
    YCbCr_Cr("YCbCr_Cr"),
    Undefined("Undefined");

    private String value;

    ColorInterpType(String value) {
        this.value = value;
    }

    public static ColorInterpType getByValue(String value) {
        switch (value) {
            case "Undefined":
                return Undefined;

            case "Gray":
                return Gray;

            case "Palette":
                return Palette;

            case "Red":
                return Red;

            case "Green":
                return Green;

            case "Blue":
                return Blue;

            case "Alpha":
                return Alpha;

            case "Hue":
                return Hue;
            case "Saturation":
                return Saturation;

            case "Lightness":
                return Lightness;

            case "Cyan":
                return Cyan;

            case "Magenta":
                return Magenta;

            case "Yellow":
                return Yellow;

            case "Black":
                return Black;

            case "YCbCr_Y":
                return YCbCr_Y;

            case "YCbCr_Cb":
                return YCbCr_Cb;

            case "YCbCr_Cr":
                return YCbCr_Cr;

            default:
                return Undefined;
        }
    }

    public String getValue() {
        return this.value;
    }
}
