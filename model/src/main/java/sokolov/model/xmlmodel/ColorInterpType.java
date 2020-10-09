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

    ColorInterpType(String value){
        this.value = value;
    }
}
