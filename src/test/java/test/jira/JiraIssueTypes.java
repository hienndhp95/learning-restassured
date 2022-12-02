package test.jira;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.RequestCapability;
import org.apache.commons.codec.binary.Base64;
import utils.ProjectInfo;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class JiraIssueTypes implements RequestCapability {
    public static void main(String[] args) {
        String baseUri = "https://hiennd-learning.atlassian.net";
        String projectKey = "HIENNDKB";
        ProjectInfo projectInfo = new ProjectInfo(baseUri,projectKey);
        System.out.println("Task ID: " + projectInfo.getIssueTypeId("Task"));
//        String email = "hiennd.tnhp@gmail.com";
//        String apiToken = "cLXIhJJ2gL3pOphVZc4U1FA6";
//        String cred = email.concat(":").concat(apiToken);
//        byte[] encodedCred = Base64.encodeBase64(cred.getBytes());
//        String encodedCreStr = new String(encodedCred);
//
//        // Form up
//        RequestSpecification request = given();
//        request.baseUri(baseUri);
////        request.header(defaultHeader);
//
//        // Authorization
////        request.header(RequestCapability.getAuthenticatedHeader(encodedCreStr));
//        request.header(getAuthenticatedHeader.apply(encodedCreStr));
//        Response response = request.get(path);
////        response.prettyPrint();
//
//        // {} -> Use Map
//        //[] -> Use List
//        Map<String, List<Map<String, String>>> projectInfo = JsonPath.from(response.asString()).get();
//        List<Map<String, String>> issueTypes = projectInfo.get("issueTypes");
//        for (Map<String, String> issueType : issueTypes) {
//            System.out.println(issueType.get("id"));
//            System.out.println(issueType.get("name"));
//            System.out.println("--------------------");
//        }
    }
}
