package sokolov.model.sources;

import sokolov.model.enums.VRTComplexSourceScaling;

public class VrtComplexSource extends VrtSimpleSource {
    VRTComplexSourceScaling m_eScalingType;
    double         m_dfScaleOff;  // For linear scaling.
    double         m_dfScaleRatio;  // For linear scaling.

    // For non-linear scaling with a power function.
    int            m_bSrcMinMaxDefined;
    double         m_dfSrcMin;
    double         m_dfSrcMax;
    double         m_dfDstMin;
    double         m_dfDstMax;
    double         m_dfExponent;

    int            m_nColorTableComponent;

    public void SetLinearScaling(double dfOffset, double dfScale){
        m_eScalingType = VRTComplexSourceScaling.VRT_SCALING_LINEAR;
        m_dfScaleOff = dfOffset;
        m_dfScaleRatio = dfScale;
    }

    public void SetColorTableComponent(int nComponent){
        m_nColorTableComponent = nComponent;
    }
}
