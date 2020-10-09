package sokolov.model.datasets;

import sokolov.model.enums.GDALRATTableType;
import sokolov.model.supclasses.GDALRasterAttributeTable;

import java.util.ArrayList;
import java.util.List;

public class GDALDefaultRasterAttributeTable extends GDALRasterAttributeTable {
    List<GDALRasterAttributeField> aoFields = new ArrayList<>();

    boolean bLinearBinning = false;
    double dfRow0Min = -0.5;
    double dfBinSize = 1.0;
    GDALRATTableType eTableType;
    boolean   bColumnsAnalysed = false;
    int   nMinCol = -1;
    int   nMaxCol = -1;

    int   nRowCount = 0;


    public GDALDefaultRasterAttributeTable() {
        bLinearBinning = false;
        dfRow0Min = -0.5;
        dfBinSize = 1.0;
        eTableType = GDALRATTableType.GRTT_THEMATIC;
        bColumnsAnalysed = false;
        nMinCol = -1;
        nMaxCol = -1;
        nRowCount = 0;
    }
}
