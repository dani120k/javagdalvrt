package sokolov.model.datasets;

public class VrtRasterBand extends GdalRasterBand {

    public void SetOffset(double dfNewOffset){
        poDs.SetNeedsFlush();

        m_dfOffset = dfNewOffset;
    }

    public void SetScale(double dfNewScale){
        poDs.SetNeedsFlush();

        m_dfScale = dfNewScale;
    }
}
