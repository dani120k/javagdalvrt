package sokolov.model.common;

public class CPLStringList {
    public String[] papszList = null;
    int nCount = 0;
    int nAllocation = 0;
    boolean bOwnList = false;
    boolean bIsSorted = false;

    public CPLStringList Assign(String[] papszListIn, boolean bTakeOwnership) {
        //TODO Clear();

        papszList = papszListIn;
        bOwnList = bTakeOwnership;

        if (papszList == null || papszList == null)
            nCount = 0;
        else
            nCount = -1;      // unknown

        nAllocation = 0;
        bIsSorted = false;

        return this;
    }

    public CPLStringList SetNameValue(String pszKey, String pszValue) {
        int iKey = FindName(pszKey);

        if (iKey == -1)
            return AddNameValue(pszKey, pszValue);

        //TODO
        //Count();
        //MakeOurOwnCopy();

        if (pszValue == null) // delete entry
        {

            // shift everything down by one.
            do {
                papszList[iKey] = papszList[iKey + 1];
            }
            while (papszList[iKey++] != null);

            nCount--;
        } else {
            long nLen = pszKey.length() + pszValue.length() + 2;
            //TODO strange allocate
            String pszLine = "";

            papszList[iKey] = pszLine;
        }

        return this;
    }

    public int FindName(String pszKey) {
        if (!IsSorted())
            return CSLFindName(papszList, pszKey);

        // If we are sorted, we can do an optimized binary search.
        int iStart = 0;
        int iEnd = nCount - 1;
        int nKeyLen = pszKey.length();

        while (iStart <= iEnd) {
            int iMiddle = (iEnd + iStart) / 2;
            String pszMiddle = papszList[iMiddle];

            if (pszMiddle.equals(pszKey) && pszKey.equals(nKeyLen)
                    && (pszMiddle.toCharArray()[nKeyLen] == '=' || pszMiddle.toCharArray()[nKeyLen] == ':'))
                return iMiddle;

            if (CPLCompareKeyValueString(pszKey, pszMiddle) < 0)
                iEnd = iMiddle - 1;
            else
                iStart = iMiddle + 1;
        }

        return -1;
    }

    private int CSLFindName(String[] papszList, String pszKey) {
        return 0;
    }

    private int CPLCompareKeyValueString(String pszKey, String pszMiddle) {
        return 0;
    }

    private boolean IsSorted() {
        return false;
    }

    public CPLStringList AddNameValue(String pszKey,
                                      String pszValue) {
        if (pszKey == null || pszValue == null)
            return this;

        //MakeOurOwnCopy();

        /* -------------------------------------------------------------------- */
        /*      Format the line.                                                */
        /* -------------------------------------------------------------------- */
        int nLen = pszKey.length() + pszValue.length() + 2;
        //char *pszLine = static_cast < char *>(CPLMalloc(nLen));
        String pszLine = "";

        /* -------------------------------------------------------------------- */
        /*      If we don't need to keep the sort order things are pretty       */
        /*      straight forward.                                               */
        /* -------------------------------------------------------------------- */
        if (!IsSorted())
            return AddStringDirectly(pszLine);

        /* -------------------------------------------------------------------- */
        /*      Find the proper insertion point.                                */
        /* -------------------------------------------------------------------- */
        int iKey = FindSortedInsertionPoint(pszLine);
        InsertStringDirectly(iKey, pszLine);
        bIsSorted = true;  // We have actually preserved sort order.

        return this;
    }

    private int FindSortedInsertionPoint(String pszLine) {
        return 0;
    }

    public CPLStringList AddStringDirectly(String pszLine){
        return null;
    }

    public CPLStringList InsertStringDirectly(int nInsertAtLineNo,
                                              String pszNewLine) {
        if( nCount == -1 )
            Count();

        EnsureAllocation( nCount+1 );

        if( nInsertAtLineNo < 0 || nInsertAtLineNo > nCount )
        {
            System.out.println("CPLStringList::InsertString() requested beyond list end." );
            return this;
        }

        bIsSorted = false;

        for( int i = nCount; i > nInsertAtLineNo; i-- )
            papszList[i] = papszList[i-1];

        papszList[nInsertAtLineNo] = pszNewLine;
        papszList[++nCount] = null;

        return this;
    }

    private void EnsureAllocation(int i) {

    }

    private void Count() {

    }

    public void Sort() {

    }

    public String FetchNameValue(String pszName) {
        return null;
    }
}
