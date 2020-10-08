package sokolov.model.xmlmodel;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonRootName(value = "VRTDataset")
public class VRTDataset {
    @JacksonXmlProperty(localName = "SRS")
    public SRSType srsType;

    @JacksonXmlProperty(localName = "GeoTransform")
    private String geoTransform;

    @JacksonXmlProperty(localName = "GCPList")
    private GCPListType gcpListType;

    @JacksonXmlProperty(localName = "BlockXSize")
    private Long blockXSize;

    @JacksonXmlProperty(localName = "BlockYSize")
    private Long blockYSize;

    @JacksonXmlProperty(localName = "Metadata")
    private MetadataType metadata;

    @JacksonXmlProperty(localName = "VRTRasterBand")
    private VRTRasterBandType vrtRasterBand;

    @JacksonXmlProperty(localName = "MaskBand")
    private MaskBandType maskBand;

    @JacksonXmlProperty(localName = "GDALWarpOptions")
    private GDALWarpOptionsType GDALWarpOptions;

    @JacksonXmlProperty(localName = "PansharpeningOptions")
    private PansharpeningOptionsType pansharpeningOptions;

    @JacksonXmlProperty(localName = "Group")
    private GroupType Group;

    @JacksonXmlProperty(localName = "OverviewListType")
    private OverviewListType overviewList;

    @JacksonXmlProperty(localName = "subClass")
    private String subClass;

    @JacksonXmlProperty(localName = "rasterXSize")
    private int rasterXSize;

    @JacksonXmlProperty(localName = "rasterYSize")
    private int rasterYSize;

    public String getSubClass() {
        return subClass;
    }

    public void setSubClass(String subClass) {
        this.subClass = subClass;
    }

    public int getRasterXSize() {
        return rasterXSize;
    }

    public void setRasterXSize(int rasterXSize) {
        this.rasterXSize = rasterXSize;
    }

    public int getRasterYSize() {
        return rasterYSize;
    }

    public void setRasterYSize(int rasterYSize) {
        this.rasterYSize = rasterYSize;
    }

    public SRSType getSrsType() {
        return srsType;
    }

    public void setSrsType(SRSType srsType) {
        this.srsType = srsType;
    }

    public String getGeoTransform() {
        return geoTransform;
    }

    public void setGeoTransform(String geoTransform) {
        this.geoTransform = geoTransform;
    }

    public GCPListType getGcpListType() {
        return gcpListType;
    }

    public void setGcpListType(GCPListType gcpListType) {
        this.gcpListType = gcpListType;
    }

    public Long getBlockXSize() {
        return blockXSize;
    }

    public void setBlockXSize(Long blockXSize) {
        this.blockXSize = blockXSize;
    }

    public Long getBlockYSize() {
        return blockYSize;
    }

    public void setBlockYSize(Long blockYSize) {
        this.blockYSize = blockYSize;
    }

    public MetadataType getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataType metadata) {
        this.metadata = metadata;
    }

    public VRTRasterBandType getVrtRasterBand() {
        return vrtRasterBand;
    }

    public void setVrtRasterBand(VRTRasterBandType vrtRasterBand) {
        this.vrtRasterBand = vrtRasterBand;
    }

    public MaskBandType getMaskBand() {
        return maskBand;
    }

    public void setMaskBand(MaskBandType maskBand) {
        this.maskBand = maskBand;
    }

    public GDALWarpOptionsType getGDALWarpOptions() {
        return GDALWarpOptions;
    }

    public void setGDALWarpOptions(GDALWarpOptionsType GDALWarpOptions) {
        this.GDALWarpOptions = GDALWarpOptions;
    }

    public PansharpeningOptionsType getPansharpeningOptions() {
        return pansharpeningOptions;
    }

    public void setPansharpeningOptions(PansharpeningOptionsType pansharpeningOptions) {
        this.pansharpeningOptions = pansharpeningOptions;
    }

    public GroupType getGroup() {
        return Group;
    }

    public void setGroup(GroupType group) {
        Group = group;
    }

    public OverviewListType getOverviewList() {
        return overviewList;
    }

    public void setOverviewList(OverviewListType overviewList) {
        this.overviewList = overviewList;
    }
}
