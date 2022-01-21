package JsonConvert;

import RequestAndResponse.ServiceResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerializer {

    public String serialize(ServiceResponse response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(response);
        System.out.println("JSON String: " + jsonString);
        return jsonString;
    }
}

