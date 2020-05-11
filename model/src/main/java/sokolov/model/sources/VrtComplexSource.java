package sokolov.model.sources;

public class VrtComplexSource extends VrtSimpleSource {
    public void SetLinearScaling(double dfOffset, double dfScale){
        m_eScalingType = VRT_SCALING_LINEAR;
        m_dfScaleOff = dfOffset;
        m_dfScaleRatio = dfScale;
    }

    public void SetColorTableComponent(int nComponent){
        m_nColorTableComponent = nComponent;
    }
}
