package utils;

import actions.common.GlobalConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.RequestCapability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static io.restassured.RestAssured.given;

public class IssueInfo implements RequestCapability {
    private String baseUri;
    private String issueKey;
    Map<String, Object> fields;
    public IssueInfo(String baseUri, String issueKey) {
        this.baseUri = baseUri;
        this.issueKey = issueKey;
        getIssueInfo();
    }

    public Map<String,String> getIssueInfoDetail(){
        String actualSummary = fields.get("summary").toString();
        Map<String, Object> status = (Map<String, Object>) fields.get("status");
        Map<String, Object> statusCategory = (Map<String, Object>) status.get("statusCategory");
        String actualStatus = statusCategory.get("name").toString();
        Map<String,String> issueInfo = new HashMap<>();
        issueInfo.put("summary", actualSummary);
        issueInfo.put("status", actualStatus);
        return issueInfo;
    }

    private void getIssueInfo(){
        String getIssuePath = "/rest/api/3/issue/" + issueKey;
        String encodedCreStr = AuthenticationHandler.encodedCredStr(GlobalConstants.EMAIL, GlobalConstants.JIRA_TOKEN);

        // Form up
        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(getAuthenticatedHeader.apply(encodedCreStr));
        Response response = request.get(getIssuePath);
        fields = JsonPath.from(response.asString()).get("fields");
    }
}
