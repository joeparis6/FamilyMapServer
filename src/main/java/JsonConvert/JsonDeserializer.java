package JsonConvert;


import com.google.gson.Gson;

public class JsonDeserializer {

    public static <T> T deserialize(String value, Class<T> returnType) {
        return (new Gson()).fromJson(value, returnType);
    }


}
