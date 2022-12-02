package builder;

import com.google.gson.Gson;

public class BodyJSONBuilder {
    public static <T> String getJSONContent(T dataTypeObject){
        return new Gson().toJson(dataTypeObject);
    }
}
