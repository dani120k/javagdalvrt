package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class PansharpeningOptionsType {
    @JacksonXmlProperty(localName = "Algorithm")
    private String alghorithm;

    @JacksonXmlProperty(localName = "AlgorithmOptions")
    private AlgorithmOptionsType algorithmOptionType;

    @JacksonXmlProperty(localName = "Resampling")
    private String resampling;

    @JacksonXmlProperty(localName = "NumThreads")
    private String numThreads;

    @JacksonXmlProperty(localName = "BitDepth")
    private String bitDepth;

    @JacksonXmlProperty(localName = "NoData")
    private NoDataOrNoneType noData;

    @JacksonXmlProperty(localName = "SpatialExtentAdjustment")
    private String spatialExtentAdjustment;

    @JacksonXmlProperty(localName = "PanchroBand")
    private PanchroBandType panchroBand;

    @JacksonXmlProperty(localName = "SpectralBand")
    private SpectralBandType spectralBand;

    public String getAlghorithm() {
        return alghorithm;
    }

    public void setAlghorithm(String alghorithm) {
        this.alghorithm = alghorithm;
    }

    public AlgorithmOptionsType getAlgorithmOptionType() {
        return algorithmOptionType;
    }

    public void setAlgorithmOptionType(AlgorithmOptionsType algorithmOptionType) {
        this.algorithmOptionType = algorithmOptionType;
    }

    public String getResampling() {
        return resampling;
    }

    public void setResampling(String resampling) {
        this.resampling = resampling;
    }

    public String getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(String numThreads) {
        this.numThreads = numThreads;
    }

    public String getBitDepth() {
        return bitDepth;
    }

    public void setBitDepth(String bitDepth) {
        this.bitDepth = bitDepth;
    }

    public NoDataOrNoneType getNoData() {
        return noData;
    }

    public void setNoData(NoDataOrNoneType noData) {
        this.noData = noData;
    }

    public String getSpatialExtentAdjustment() {
        return spatialExtentAdjustment;
    }

    public void setSpatialExtentAdjustment(String spatialExtentAdjustment) {
        this.spatialExtentAdjustment = spatialExtentAdjustment;
    }

    public PanchroBandType getPanchroBand() {
        return panchroBand;
    }

    public void setPanchroBand(PanchroBandType panchroBand) {
        this.panchroBand = panchroBand;
    }

    public SpectralBandType getSpectralBand() {
        return spectralBand;
    }

    public void setSpectralBand(SpectralBandType spectralBand) {
        this.spectralBand = spectralBand;
    }
}
