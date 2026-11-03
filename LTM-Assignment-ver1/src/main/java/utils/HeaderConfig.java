package utils;

import java.util.HashMap;
import java.util.Map;

public class HeaderConfig {

   public static Map<String, String> headers;

    public static Map<String, String> getDefaultHeaders(){
        headers = new HashMap<String,String>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "*/*");

        return headers;
    }
}
