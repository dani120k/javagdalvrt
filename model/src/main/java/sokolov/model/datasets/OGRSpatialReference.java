package sokolov.model.datasets;

import sokolov.model.enums.OSRAxisMappingStrategy;

import java.util.ArrayList;
import java.util.List;

public class OGRSpatialReference {
    public String exportToWkt() {
        return null;
    }

    public void SetAxisMappingStrategy(OSRAxisMappingStrategy oamsTraditionalGisOrder) {

    }

    public boolean SetFromUserInput(String pszProjection) {
        return false;
    }

    public void importFromWkt(String pszProjectionRefIn) {

    }

    public boolean isEmpty() {
        //TODO
        //d.refreshProjObj();

        //return d.m_pg_crs == null;
        return true;

    }

    public List<Integer> GetDataAxisToSRSAxisMapping() {
        //TODO
        //return d.m_axisMapping;

        return new ArrayList<>();
    }
}
