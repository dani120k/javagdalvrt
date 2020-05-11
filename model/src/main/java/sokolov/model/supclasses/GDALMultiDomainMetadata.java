package sokolov.model.supclasses;

import sokolov.model.common.CPLStringList;
import sokolov.model.common.SupportMethods;

import java.util.ArrayList;
import java.util.List;

public class GDALMultiDomainMetadata {
    String[] papszDomainList;
    CPLStringList[] papoMetadataLists;

    public GDALMultiDomainMetadata() {
        papoMetadataLists = null;
        papszDomainList = null;
    }

    public String[] GetMetadata(String pszDomain) {
        if (pszDomain == null)
            pszDomain = "";

        int iDomain = CSLFindString(papszDomainList, pszDomain);

        if (iDomain == -1)
            return null;

        return papoMetadataLists[iDomain].papszList;
    }

    public void SetMetadata(String[] papszMetadata,
                            String pszDomain) {
        if (pszDomain == null)
            pszDomain = "";

        int iDomain = CSLFindString(papszDomainList, pszDomain);

        if (iDomain == -1) {
            papszDomainList = SupportMethods.CSLAddString(papszDomainList, pszDomain);
            int nDomainCount = CSLCount(papszDomainList);

            papoMetadataLists = new CPLStringList[nDomainCount + 1];
            papoMetadataLists[nDomainCount] = null;
            papoMetadataLists[nDomainCount - 1] = new CPLStringList();
            iDomain = nDomainCount - 1;
        }

        //TODO self added false
        papoMetadataLists[iDomain].Assign(papszMetadata, false);

        // we want to mark name/value pair domains as being sorted for fast
        // access.
        if (!STARTS_WITH_CI(pszDomain, "xml:") &&
                !STARTS_WITH_CI(pszDomain, "json:") &&
                !pszDomain.equals("SUBDATASETS")) {
            papoMetadataLists[iDomain].Sort();
        }

        return;
    }

    private int CSLFindString(String[] papszDomainList, String pszDomain) {
        return 0;
    }

    private int CSLCount(String[] papszDomainList) {
        return 0;
    }

    private boolean STARTS_WITH_CI(String pszDomain, String s) {
        return false;
    }

    public String GetMetadataItem(String pszName, String pszDomain) {
        if (pszDomain == null)
            pszDomain = "";

        int iDomain = CSLFindString(papszDomainList, pszDomain);

        if (iDomain == -1)
            return null;

        return papoMetadataLists[iDomain].FetchNameValue(pszName);
    }

    public void SetMetadataItem(String pszName,
                                String pszValue,
                                String pszDomain) {
        if (pszDomain == null)
            pszDomain = "";

        /* -------------------------------------------------------------------- */
        /*      Create the domain if it does not already exist.                 */
        /* -------------------------------------------------------------------- */
        int iDomain = CSLFindString(papszDomainList, pszDomain);

        if (iDomain == -1) {
            SetMetadata(null, pszDomain);
            iDomain = CSLFindString(papszDomainList, pszDomain);
        }

        /* -------------------------------------------------------------------- */
        /*      Set the value in the domain list.                               */
        /* -------------------------------------------------------------------- */
        papoMetadataLists[iDomain].SetNameValue(pszName, pszValue);
    }


}
