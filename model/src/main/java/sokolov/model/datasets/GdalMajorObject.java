package sokolov.model.datasets;

import sokolov.model.supclasses.GDALMultiDomainMetadata;

/* ******************************************************************** */
/*                           GDALMajorObject                            */
/*                                                                      */
/*      Base class providing metadata, description and other            */
/*      services shared by major objects.                               */
/* ******************************************************************** */
public class GdalMajorObject {
    int nFlags; // GMO_* flags.
    String sDescription;
    GDALMultiDomainMetadata oMDMD;
    public double VRT_NODATA_UNSET = -1234.56;


    public String getDescription() {
        return sDescription;
    }

    /**
     * \brief Set single metadata item.
     *
     * The C function GDALSetMetadataItem() does the same thing as this method.
     *
     * @param pszName the key for the metadata item to fetch.
     * @param pszValue the value to assign to the key.
     * @param pszDomain the domain to set within, use NULL for the default domain.
     *
     * @return CE_None on success, or an error code on failure.
     */
    public void setMetadataItem(String pszName,
                                String pszValue,
                                String pszDomain){
        //nFlags |= GMO_MD_DIRTY;
        oMDMD.SetMetadataItem(pszName, pszValue, pszDomain);
    }

    public String[] GetMetadata(String pszDomain){
        return oMDMD.GetMetadata(pszDomain);
    }

    public void SetDescription(String pszNewDesc){
        this.sDescription = pszNewDesc;
    }

    protected String GetMetadataItem(String pszName) {
        //set default value
        String pszDomain = "";
        return oMDMD.GetMetadataItem(pszName, pszDomain);
    }

    protected String GetMetadataItem(String pszName, String pszDomain) {
        //set default value
        if (pszDomain == null)
            pszDomain = "";

        return oMDMD.GetMetadataItem(pszName, pszDomain);
    }
}
