package sokolov.model.datasets;

import sokolov.model.enums.OSRAxisMappingStrategy;

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
}
