package sokolov.model.xmlmodel;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JsonRootName(value = "VRTDataset")
public class VRTDataset {
    @JacksonXmlProperty(localName = "SRS")
    public SRSType srs;

    @JacksonXmlProperty(localName = "GeoTransform")
    private String geoTransform;

    @JacksonXmlProperty(localName = "GCPList")
    private GCPListType gcpList;

    @JacksonXmlProperty(localName = "BlockXSize")
    private Long blockXSize;

    @JacksonXmlProperty(localName = "BlockYSize")
    private Long blockYSize;

    @JacksonXmlProperty(localName = "Metadata")
    private List<MetadataType> metadata;

    @JacksonXmlProperty(localName = "VRTRasterBand")
    private List<VRTRasterBandType> vrtRasterBand;

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
    private Integer rasterXSize;

    @JacksonXmlProperty(localName = "rasterYSize")
    private Integer rasterYSize;

    public String getSubClass() {
        return subClass;
    }

    public void setSubClass(String subClass) {
        this.subClass = subClass;
    }

    public Integer getRasterXSize() {
        return rasterXSize;
    }

    public void setRasterXSize(Integer rasterXSize) {
        this.rasterXSize = rasterXSize;
    }

    public Integer getRasterYSize() {
        return rasterYSize;
    }

    public void setRasterYSize(Integer rasterYSize) {
        this.rasterYSize = rasterYSize;
    }

    public SRSType getSrs() {
        return srs;
    }

    public void setSrs(SRSType srs) {
        this.srs = srs;
    }

    public String getGeoTransform() {
        return geoTransform;
    }

    public void setGeoTransform(String geoTransform) {
        this.geoTransform = geoTransform;
    }

    public GCPListType getGcpList() {
        return gcpList;
    }

    public void setGcpList(GCPListType gcpList) {
        this.gcpList = gcpList;
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

    public List<MetadataType> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<MetadataType> metadata) {
        this.metadata = metadata;
    }

    public List<VRTRasterBandType> getVrtRasterBand() {
        return vrtRasterBand;
    }

    public void setVrtRasterBand(List<VRTRasterBandType> vrtRasterBand) {
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
