package sokolov.model.enums;


import java.util.List;

public class GDALColorTableH {
    public List<GDALColorEntry> aoEntries;

    public int getColorEntryPoint() {
        return 0;
    }

    public GDALColorEntry getColorEntry(int i) {
        return null;
    }

    public void setColorEntry(int i, GDALColorEntry poEntry) {
        if (i < 0)
            return;

        try{
            if ( i > aoEntries.size()){
                GDALColorEntry oBlack = new GDALColorEntry(0, 0, 0, 0);

                aoEntries.set(i + 1, poEntry);
            }

            aoEntries.set(i, poEntry);
        }catch (Exception ex){
            String.format(ex.getMessage());
        }
    }

    public int GetColorEntryCount() {
        return aoEntries.size();
    }

    public boolean GetColorEntryAsRGB(int i, GDALColorEntry poEntry) {
        if (/*TODO eInterp != GPI_RGB ||*/ i < 0 || i >= aoEntries.size())
            return false;

        poEntry.c1 = aoEntries.get(i).c1;
        poEntry.c2 = aoEntries.get(i).c2;
        poEntry.c3 = aoEntries.get(i).c3;
        poEntry.c4 = aoEntries.get(i).c4;

        return true;
    }
}
