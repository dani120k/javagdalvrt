package sokolov.model.sources;

import sokolov.model.common.SupportMethods;
import sokolov.model.datasets.GdalRasterBand;
import sokolov.model.enums.VRTComplexSourceScaling;
import sokolov.model.xmlmodel.ComplexSourceType;
import sokolov.model.xmlmodel.SimpleSourceType;
import sokolov.model.xmlmodel.VRTRasterBandType;

import java.util.ArrayList;

public class VrtComplexSource extends VrtSimpleSource {
    VRTComplexSourceScaling m_eScalingType;
    double m_dfScaleOff;  // For linear scaling.
    double m_dfScaleRatio;  // For linear scaling.

    // For non-linear scaling with a power function.
    int m_bSrcMinMaxDefined;
    double m_dfSrcMin;
    double m_dfSrcMax;
    double m_dfDstMin;
    double m_dfDstMax;
    double m_dfExponent;

    boolean m_nColorTableComponent;

    public void SetLinearScaling(double dfOffset, double dfScale) {
        m_eScalingType = VRTComplexSourceScaling.VRT_SCALING_LINEAR;
        m_dfScaleOff = dfOffset;
        m_dfScaleRatio = dfScale;
    }

    public void SetColorTableComponent(boolean nComponent) {
        m_nColorTableComponent = nComponent;
    }

    public void serializeToXML(VRTRasterBandType vrtRasterBandType, GdalRasterBand gdalRasterBand, String pszVRTPath) {
        SimpleSourceType simpleSourceType = super.serializeToXml(vrtRasterBandType, gdalRasterBand, pszVRTPath);

        if (simpleSourceType == null)
            return;

        ComplexSourceType complexSourceType = new ComplexSourceType(simpleSourceType);

        if (m_bNoDataSet)
            complexSourceType.setNODATA(SupportMethods.VRTSerializeNoData(m_dfNoDataValue, m_poRasterBand.GetRasterDataType(), 16));

        if (m_eScalingType != null) {
            switch (m_eScalingType) {
                case VRT_SCALING_NONE:
                    break;
                case VRT_SCALING_LINEAR:
                    complexSourceType.setScaleOffset(m_dfScaleOff);
                    complexSourceType.setScaleRatio(m_dfScaleRatio);
                    break;
                case VRT_SCALING_EXPONENTIAL:
                    complexSourceType.setExponent(m_dfExponent);
                    complexSourceType.setSrcMin(m_dfSrcMin);
                    complexSourceType.setSrcMax(m_dfSrcMax);
                    complexSourceType.setDstMin(m_dfDstMin);
                    complexSourceType.setDstMax(m_dfDstMax);
                    break;
            }
        }

        /*TODO
            if( m_nLUTItemCount )
    {
        // Make sure we print with sufficient precision to address really close
        // entries (#6422).
        CPLString osLUT;
        // TODO(schwehr): How is this not a read past the end of the array if
        // m_nLUTItemCount is 0 or 1?  Added in
        // https://trac.osgeo.org/gdal/changeset/33779
        if( m_nLUTItemCount > 0 &&
            CPLString().Printf("%g", m_padfLUTInputs[0]) ==
            CPLString().Printf("%g", m_padfLUTInputs[1]) )
        {
            osLUT = CPLString().Printf(
                "%.18g:%g", m_padfLUTInputs[0], m_padfLUTOutputs[0]);
        }
        else
        {
            osLUT = CPLString().Printf(
                "%g:%g", m_padfLUTInputs[0], m_padfLUTOutputs[0]);
        }
        for ( int i = 1; i < m_nLUTItemCount; i++ )
        {
            if( CPLString().Printf("%g", m_padfLUTInputs[i]) ==
                CPLString().Printf("%g", m_padfLUTInputs[i-1]) ||
                (i + 1 < m_nLUTItemCount &&
                 CPLString().Printf("%g", m_padfLUTInputs[i]) ==
                 CPLString().Printf("%g", m_padfLUTInputs[i+1])) )
            {
                // TODO(schwehr): An explanation of the 18 would be helpful.
                // Can someone distill the issue down to a quick comment?
                // https://trac.osgeo.org/gdal/ticket/6422
                osLUT += CPLString().Printf(
                    ",%.18g:%g", m_padfLUTInputs[i], m_padfLUTOutputs[i]);
            }
            else
            {
                osLUT += CPLString().Printf(
                    ",%g:%g", m_padfLUTInputs[i], m_padfLUTOutputs[i]);
            }
        }
        CPLSetXMLValue( psSrc, "LUT", osLUT );
    }
         */

        if (m_nColorTableComponent){
            complexSourceType.setColorTableComponent((m_nColorTableComponent) ? 1 : 0);
        }

        if (vrtRasterBandType.getComplexSource() == null)
            vrtRasterBandType.setComplexSource(new ArrayList<>());

        vrtRasterBandType.getComplexSource().add(complexSourceType);
    }
}
