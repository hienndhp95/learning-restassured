package model;

import io.restassured.http.Header;

import java.util.function.Function;

public interface RequestCapability {
    Header defaultHeader = new Header("Content-type","application/json; charset=utf-8");
    Header acceptJSONHeader = new Header("Accept","application/json");

    static Header getAuthenticatedHeader(String encodedCredStr){
        if (encodedCredStr==null||encodedCredStr.isEmpty()||encodedCredStr.isBlank()){
            throw new IllegalArgumentException("[ERR] encodedCredStr can't be null!");
        }
        return new Header("Authorization","Basic " + encodedCredStr);
    }

    Function<String, Header> getAuthenticatedHeader = encodedCredStr -> {
        if (encodedCredStr==null||encodedCredStr.isEmpty()||encodedCredStr.isBlank()){
            throw new IllegalArgumentException("[ERR] encodedCredStr can't be null!");
        }
        return new Header("Authorization","Basic " + encodedCredStr);
    };
}
