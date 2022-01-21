package JsonConvert;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class DataCollection {

    String[] data;

    public String[] getMaleNames() throws IOException {
        try(FileReader fileReader = new FileReader(new File ("json/mnames.json"));
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            Gson gson = new Gson();
            SurnameList surnames = gson.fromJson(bufferedReader, SurnameList.class);
            return surnames.getData();
        }
    }

    public String[] getFemaleNames() throws IOException {
        try(FileReader fileReader = new FileReader(new File ("json/fnames.json"));
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            Gson gson = new Gson();
            MaleNameList maleNames = gson.fromJson(bufferedReader, MaleNameList.class);
            return maleNames.getData();
        }
    }

    public String[] getSurnames() throws IOException {
        try(FileReader fileReader = new FileReader(new File ("json/snames.json"));
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            Gson gson = new Gson();
            FemaleNameList femaleNames = gson.fromJson(bufferedReader, FemaleNameList.class);
            return femaleNames.getData();
        }
    }

    public List<Location> getLocations() throws IOException {
        try(FileReader fileReader = new FileReader(new File ("json/locations.json"));
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            Gson gson = new Gson();
            LocationList locations = gson.fromJson(bufferedReader, LocationList.class);
            return locations.getLocations();
        }
    }
}
