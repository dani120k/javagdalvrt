package sokolov.model.common;

public class SupportMethods {
    /** Append a string to a StringList and return a pointer to the modified
     * StringList.
     *
     * If the input StringList is NULL, then a new StringList is created.
     * Note that CSLAddString performance when building a list is in O(n^2)
     * which can cause noticeable slow down when n > 10000.
     */
    public static String[] CSLAddString(String[] papszStrList, String pszNewString){
        if (papszStrList == null){
            papszStrList = new String[0];
        }

        String[] newPapszStrList = new String[papszStrList.length + 1];

        for(int i = 0; i < papszStrList.length; i++)
            newPapszStrList[i] = papszStrList[i];

        newPapszStrList[papszStrList.length] = pszNewString;

        return newPapszStrList;
    }
}
