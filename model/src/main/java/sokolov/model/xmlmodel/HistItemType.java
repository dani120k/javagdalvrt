package sokolov.model.xmlmodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class HistItemType {
    @JacksonXmlProperty(localName = "HistMin")
    private Double histMin;

    @JacksonXmlProperty(localName = "HistMax")
    private Double histMax;

    @JacksonXmlProperty(localName = "BucketCount")
    private int bucketCount;

    @JacksonXmlProperty(localName = "IncludeOutOfRange")
    private int includeOutOfRange;

    @JacksonXmlProperty(localName = "Approximate")
    private int approximate;

    @JacksonXmlProperty(localName = "HistCounts")
    private String histCounts;

    public Double getHistMin() {
        return histMin;
    }

    public void setHistMin(Double histMin) {
        this.histMin = histMin;
    }

    public Double getHistMax() {
        return histMax;
    }

    public void setHistMax(Double histMax) {
        this.histMax = histMax;
    }

    public int getBucketCount() {
        return bucketCount;
    }

    public void setBucketCount(int bucketCount) {
        this.bucketCount = bucketCount;
    }

    public int getIncludeOutOfRange() {
        return includeOutOfRange;
    }

    public void setIncludeOutOfRange(int includeOutOfRange) {
        this.includeOutOfRange = includeOutOfRange;
    }

    public int getApproximate() {
        return approximate;
    }

    public void setApproximate(int approximate) {
        this.approximate = approximate;
    }

    public String getHistCounts() {
        return histCounts;
    }

    public void setHistCounts(String histCounts) {
        this.histCounts = histCounts;
    }
}
