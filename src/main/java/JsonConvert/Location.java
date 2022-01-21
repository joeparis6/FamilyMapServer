package JsonConvert;

public class Location {

    String country;
    String city;
    float latitude;
    float longitude;

    public Location(String country, String city, float latitude, float longitude) {
        this.country = country;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString() {
        return "Location{" +
                "country=" + country +'\'' +
                ", city=" + city + '\'' +
                ", latitude=" + latitude +'\'' +
                ", longitude=" + longitude +'\'' ;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}
