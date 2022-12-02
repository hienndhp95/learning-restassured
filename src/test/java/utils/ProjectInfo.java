package utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.RequestCapability;
import org.apache.commons.codec.binary.Base64;

import javax.swing.*;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ProjectInfo implements RequestCapability {
    private String baseUri;
    private String projectKey;
    private List<Map<String, String>> issueTypes;
    private Map<String, List<Map<String, String>>> projectInfo;

    public ProjectInfo(String baseUri, String projectKey) {
        this.baseUri = baseUri;
        this.projectKey = projectKey;
        getProjectInfo();
    }

    public String getIssueTypeId(String issueTypeStr){
        getIssueTypes();
        String issueTypeId = null;
        for (Map<String, String> issueType:issueTypes){
            if (issueType.get("name").equalsIgnoreCase(issueTypeStr)){
                issueTypeId = issueType.get("id");
                break;
            }
        }
        if (issueTypeId == null){
            throw new RuntimeException("[ERR] Could not find the id for " + issueTypeStr);
        }
        return issueTypeId;
    }

    private void getIssueTypes(){
        issueTypes = projectInfo.get("issueTypes");
    }

    private void getProjectInfo(){
        String path = "/rest/api/3/project/".concat(projectKey);
        String email = "hiennd.tnhp@gmail.com";
        String apiToken = "cLXIhJJ2gL3pOphVZc4U1FA6";
        String encodedCreStr = AuthenticationHandler.encodedCredStr(email, apiToken);

        // Form up
        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(getAuthenticatedHeader.apply(encodedCreStr));
        Response response = request.get(path);
        projectInfo = JsonPath.from(response.asString()).get();
    }
}
