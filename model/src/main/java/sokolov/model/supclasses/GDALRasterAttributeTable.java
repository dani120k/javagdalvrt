package sokolov.model.supclasses;

import sokolov.model.enums.GdalRatFieldType;
import sokolov.model.enums.GdalRatFieldUsage;
import sokolov.model.xmlmodel.FieldDefnType;
import sokolov.model.xmlmodel.GDALRasterAttributeTableType;
import sokolov.model.xmlmodel.RowType;

import java.util.List;

public class GDALRasterAttributeTable {
    public void XMLInit(GDALRasterAttributeTableType psRAT, String pszVRTPath) {
        /* TODO this is deprecated shit? xml model doesnt contain any of this
        / -------------------------------------------------------------------- /
        /      Linear binning.                                                 /
        / -------------------------------------------------------------------- /
        if( CPLGetXMLValue( psTree, "Row0Min", nullptr )
                && CPLGetXMLValue( psTree, "BinSize", nullptr ) )
        {
            SetLinearBinning( CPLAtof(CPLGetXMLValue( psTree, "Row0Min","" )),
                    CPLAtof(CPLGetXMLValue( psTree, "BinSize","" )) );
        }

        / -------------------------------------------------------------------- /
        /      Table Type                                                      /
        / -------------------------------------------------------------------- /
        if( CPLGetXMLValue( psTree, "tableType", nullptr ) )
        {
        const char* pszValue = CPLGetXMLValue(psTree, "tableType", "thematic");
            if (EQUAL(pszValue, "athematic"))
            {
                SetTableType(GRTT_ATHEMATIC);
            }
            else
            {
                SetTableType(GRTT_THEMATIC);
            }
        }*/

        /* -------------------------------------------------------------------- */
        /*      Column definitions                                              */
        /* -------------------------------------------------------------------- */
        List<FieldDefnType> fieldDefnTypeList = psRAT.getFieldDefnTypeList();
        for (FieldDefnType fieldDefnType : fieldDefnTypeList) {
            CreateColumn(
                    (fieldDefnType.getName() != null)? fieldDefnType.getName(): "",
                    GdalRatFieldType.getByValue((fieldDefnType.getType() != null)? fieldDefnType.getType() : 1),
                    GdalRatFieldUsage.getByValue((fieldDefnType.getUsage() != null) ? fieldDefnType.getUsage() : 0));
        }

        /* -------------------------------------------------------------------- */
        /*      Row data.                                                       */
        /* -------------------------------------------------------------------- */
        List<RowType> rowTypeList = psRAT.getRowTypeList();

        for (RowType rowType : rowTypeList) {
            int iRow = rowType.getIndex();
            int iField = 0;

            List<Object> objects = rowType.getfList();
            for (Object object : objects) {
                if (object != null)
                    SetValue(iRow, iField++, (String)object);
                else
                    SetValue(iRow, iField++, "");
            }
        }
    }

    private void SetValue(int iRow, int i, String object) {
        //TODO
    }

    private void CreateColumn(String s, GdalRatFieldType byValue, GdalRatFieldUsage byValue1) {
        //TODO return CE_Failure;
    }


    public GDALRasterAttributeTableType Serialize() {
        //TODO
        /*
        if( ( GetColumnCount() == 0 ) && ( GetRowCount() == 0 ) )
            return nullptr;

        CPLXMLNode *psTree
                = CPLCreateXMLNode( nullptr, CXT_Element, "GDALRasterAttributeTable" );

        / -------------------------------------------------------------------- /
        /      Add attributes with regular binning info if appropriate.        /
        /-------------------------------------------------------------------- /
        char szValue[128] = { '\0' };
        double dfRow0Min = 0.0;
        double dfBinSize = 0.0;

        if( GetLinearBinning(&dfRow0Min, &dfBinSize) )
        {
            CPLsnprintf( szValue, sizeof(szValue), "%.16g", dfRow0Min );
            CPLCreateXMLNode(
                    CPLCreateXMLNode( psTree, CXT_Attribute, "Row0Min" ),
                    CXT_Text, szValue );

            CPLsnprintf( szValue, sizeof(szValue), "%.16g", dfBinSize );
            CPLCreateXMLNode(
                    CPLCreateXMLNode( psTree, CXT_Attribute, "BinSize" ),
                    CXT_Text, szValue );
        }

        / -------------------------------------------------------------------- /
        /      Store table type                                                /
        / --------------------------------------------------------------------/
    const GDALRATTableType tableType = GetTableType();
        if (tableType == GRTT_ATHEMATIC)
        {
            CPLsnprintf( szValue, sizeof(szValue), "athematic" );
        }
        else
        {
            CPLsnprintf( szValue, sizeof(szValue), "thematic" );
        }
        CPLCreateXMLNode(
                CPLCreateXMLNode( psTree, CXT_Attribute, "tableType" ),
                CXT_Text, szValue );

        / -------------------------------------------------------------------- /
        /      Define each column.                                             /
        / -------------------------------------------------------------------- /
    const int iColCount = GetColumnCount();

        for( int iCol = 0; iCol < iColCount; iCol++ )
        {
            CPLXMLNode *psCol
                    = CPLCreateXMLNode( psTree, CXT_Element, "FieldDefn" );

            snprintf( szValue, sizeof(szValue), "%d", iCol );
            CPLCreateXMLNode(
                    CPLCreateXMLNode( psCol, CXT_Attribute, "index" ),
                    CXT_Text, szValue );

            CPLCreateXMLElementAndValue( psCol, "Name",
                    GetNameOfCol(iCol) );

            snprintf( szValue, sizeof(szValue),
                    "%d", static_cast<int>(GetTypeOfCol(iCol)) );
            CPLCreateXMLElementAndValue( psCol, "Type", szValue );

            snprintf( szValue, sizeof(szValue),
                    "%d", static_cast<int>(GetUsageOfCol(iCol)) );
            CPLCreateXMLElementAndValue( psCol, "Usage", szValue );
        }

        / -------------------------------------------------------------------- /
        /      Write out each row.                                             /
        / -------------------------------------------------------------------- /
    const int iRowCount = GetRowCount();
        CPLXMLNode *psTail = nullptr;
        CPLXMLNode *psRow = nullptr;

        for( int iRow = 0; iRow < iRowCount; iRow++ )
        {
            psRow = CPLCreateXMLNode( nullptr, CXT_Element, "Row" );
            if( psTail == nullptr )
                CPLAddXMLChild( psTree, psRow );
            else
                psTail->psNext = psRow;
            psTail = psRow;

            snprintf( szValue, sizeof(szValue), "%d", iRow );
            CPLCreateXMLNode(
                    CPLCreateXMLNode( psRow, CXT_Attribute, "index" ),
                    CXT_Text, szValue );

            for( int iCol = 0; iCol < iColCount; iCol++ )
            {
            const char *pszValue = szValue;

                if( GetTypeOfCol(iCol) == GFT_Integer )
                    snprintf( szValue, sizeof(szValue),
                            "%d", GetValueAsInt(iRow, iCol) );
                else if( GetTypeOfCol(iCol) == GFT_Real )
                    CPLsnprintf( szValue, sizeof(szValue),
                            "%.16g", GetValueAsDouble(iRow, iCol) );
                else
                    pszValue = GetValueAsString(iRow, iCol);

                CPLCreateXMLElementAndValue( psRow, "F", pszValue );
            }
        }

        return psTree;*/

        return null;
    }
}
