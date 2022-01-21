package JsonConvert;

import java.util.List;

public class LocationList {

    private List<Location> data;

    public LocationList(List<Location> locations) {
        data = locations;
    }

    public List<Location> getLocations() {
        return data;
    }

}
