package utils;

import actions.common.GlobalConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.RequestCapability;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TransitionInfo implements RequestCapability {
    private String baseUri;
    private String issueKey;
    private List<Map<String, String>> transitionTypes;
    private Map<String, List<Map<String, String>>> TransitionInfo;

    public TransitionInfo(String baseUri, String issueKey) {
        this.baseUri = baseUri;
        this.issueKey = issueKey;
        getTransitionInfo();
    }

    public String getTransitionTypeId(String transitionTypeStr){
        getTransitionTypes();
        String transitionTypeId = null;
        for (Map<String, String> transitionType:transitionTypes){
            if (transitionType.get("name").equalsIgnoreCase(transitionTypeStr)){
                transitionTypeId = transitionType.get("id");
                break;
            }
        }
        if (transitionTypeId == null){
            throw new RuntimeException("[ERR] Could not find the id for " + transitionTypeStr);
        }
        return transitionTypeId;
    }

    private void getTransitionTypes(){
        transitionTypes = TransitionInfo.get("transitions");
    }

    private void getTransitionInfo(){
        String getTransitionPath = "/rest/api/3/issue/"+issueKey+"/transitions";
        String encodedCreStr = AuthenticationHandler.encodedCredStr(GlobalConstants.EMAIL, GlobalConstants.JIRA_TOKEN);

        // Form up
        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(getAuthenticatedHeader.apply(encodedCreStr));
        Response response = request.get(getTransitionPath);
        TransitionInfo = JsonPath.from(response.asString()).get();
    }
}
