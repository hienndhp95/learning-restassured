package common;
import actions.common.GlobalConstants;
import io.restassured.specification.RequestSpecification;
import utils.AuthenticationHandler;

import static io.restassured.RestAssured.given;
import static model.RequestCapability.*;

public class BaseTest {

    protected static RequestSpecification getAppInfo(String path){
        // Api info
        String baseUri = GlobalConstants.BASE_URI;
        String email = "hiennd.tnhp@gmail.com";
        String apiToken = "cLXIhJJ2gL3pOphVZc4U1FA6";
        String encodedCreStr = AuthenticationHandler.encodedCredStr(email, apiToken);
        // Request Object
        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(acceptJSONHeader);
        request.header(getAuthenticatedHeader.apply(encodedCreStr));
        return request;
    }
}
