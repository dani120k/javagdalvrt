package sokolov.javagdalvrt;

public class GDALMultiDomainMetadata {
    String[] papszDomainList;
    CPLStringList[] papoMetadataLists;

    public void setMetadataItem(String pszName, String pszValue, String pszDomain){
        if (pszDomain == null)
            pszDomain = "";

        /* -------------------------------------------------------------------- */
        /*      Create the domain if it does not already exist.                 */
        /* -------------------------------------------------------------------- */
        int iDomain = CSLFindString( papszDomainList, pszDomain );

        if (iDomain == -1){
            setMetadata(null, pszDomain);
            iDomain = CSLFindString(papszDomainList, pszDomain);
        }

        papoMetadataLists[iDomain].SetNameValue( pszName, pszValue );
    }

    public void setMetadata(String[] papszMetadata,
                            String pszDomain){
        if( pszDomain == null )
            pszDomain = "";

        int iDomain = CSLFindString( papszDomainList, pszDomain );

        if( iDomain == -1 )
        {
            papszDomainList = CSLAddString( papszDomainList, pszDomain );
        const int nDomainCount = CSLCount( papszDomainList );

            papoMetadataLists = new CPLStringList[nDomainCount + 1];
            papoMetadataLists[nDomainCount] = null;
            papoMetadataLists[nDomainCount-1] = new CPLStringList();
            iDomain = nDomainCount-1;
        }

        papoMetadataLists[iDomain].Assign( papszMetadata  );

        // we want to mark name/value pair domains as being sorted for fast
        // access. TODO
        /*if( !STARTS_WITH_CI(pszDomain, "xml:") &&
                !STARTS_WITH_CI(pszDomain, "json:") &&
                !EQUAL(pszDomain, "SUBDATASETS") )
        {
            papoMetadataLists[iDomain]->Sort();
        }*/

    }

    /**
     * Find a string within a string list (case insensitive).
     *
     * Returns the index of the entry in the string list that contains the
     * target string.  The string in the string list must be a full match for
     * the target, but the search is case insensitive.
     *
     * @param papszList the string list to be searched.
     * @param pszTarget the string to be searched for.
     *
     * @return the index of the string within the list or -1 on failure.
     */
    private int CSLFindString(String[] papszList, String pszTarget){
        if( papszList == null )
            return -1;

        for( int i = 0; papszList[i] != null; ++i )
        {
            if( papszList[i].equals(pszTarget) )
                return i;
        }

        return -1;
    }

    /**
     * Return number of items in a string list.
     *
     * Returns the number of items in a string list, not counting the
     * terminating NULL.  Passing in NULL is safe, and will result in a count
     * of zero.
     *
     * Lists are counted by iterating through them so long lists will
     * take more time than short lists.  Care should be taken to avoid using
     * CSLCount() as an end condition for loops as it will result in O(n^2)
     * behavior.
     *
     * @param papszStrList the string list to count.
     *
     * @return the number of entries.
     */
    int CSLCount( String[] papszStrList )
    {
        if( papszStrList == null)
            return 0;

        int nItems = 0;

        while( *papszStrList != null )
        {
            ++nItems;
            ++papszStrList;
        }

        return nItems;
    }
}
