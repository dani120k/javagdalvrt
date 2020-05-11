package sokolov.model.supclasses;

import java.util.ArrayList;
import java.util.List;

public class GDALMultiDomainMetadata {
    String[] papszDomainList;
    List<String[]> papoMetadataLists;

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

        return papoMetadataLists.get(iDomain);
    }

    public void SetMetadata(String[] papszMetadata,
                            String pszDomain){
        if( pszDomain == null )
            pszDomain = "";

        int iDomain = CSLFindString( papszDomainList, pszDomain );

        if( iDomain == -1 )
        {
            papszDomainList = CSLAddString( papszDomainList, pszDomain );
        const int nDomainCount = CSLCount( papszDomainList );

            papoMetadataLists = new ArrayList<>(nDomainCount+1);
            papoMetadataLists[nDomainCount] = null;
            papoMetadataLists[nDomainCount-1] = new CPLStringList();
            iDomain = nDomainCount-1;
        }

        papoMetadataLists[iDomain].Assign( CSLDuplicate( papszMetadata ) );

        // we want to mark name/value pair domains as being sorted for fast
        // access.
        if( !STARTS_WITH_CI(pszDomain, "xml:") &&
                !STARTS_WITH_CI(pszDomain, "json:") &&
                !EQUAL(pszDomain, "SUBDATASETS") )
        {
            papoMetadataLists[iDomain].Sort();
        }

        return;
    }

    public String GetMetadataItem(String pszName, String pszDomain){
        if( pszDomain == null )
            pszDomain = "";

         int iDomain = CSLFindString( papszDomainList, pszDomain );

        if( iDomain == -1 )
            return null;

        return papoMetadataLists.get(iDomain).FetchNameValue( pszName );
    }

    public void SetMetadataItem(String pszName,
                                String pszValue,
                                String pszDomain){
        if( pszDomain == null )
            pszDomain = "";

        /* -------------------------------------------------------------------- */
        /*      Create the domain if it does not already exist.                 */
        /* -------------------------------------------------------------------- */
        int iDomain = CSLFindString( papszDomainList, pszDomain );

        if( iDomain == -1 )
        {
            SetMetadata( null, pszDomain );
            iDomain = CSLFindString( papszDomainList, pszDomain );
        }

        /* -------------------------------------------------------------------- */
        /*      Set the value in the domain list.                               */
        /* -------------------------------------------------------------------- */
        papoMetadataLists[iDomain].SetNameValue( pszName, pszValue );

        return CE_None;
    }



}
