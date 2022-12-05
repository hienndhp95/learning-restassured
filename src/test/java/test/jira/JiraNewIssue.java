package test.jira;

import builder.IssueContentBuilder;
import builder.TransitionContentBuilder;
import common.BaseTest;
import actions.common.GlobalConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.IssueFields;
import model.RequestCapability;
import org.apache.commons.lang3.RandomStringUtils;
import utils.AuthenticationHandler;
import utils.IssueInfo;
import utils.ProjectInfo;
import utils.TransitionInfo;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class JiraNewIssue extends BaseTest implements RequestCapability {
    public static void main(String[] args) {
//        // App info
        String baseUri = GlobalConstants.BASE_URI;
        String path ="/rest/api/3/issue";
        String projectKey = GlobalConstants.PROJECT_KEY;
        String encodedCreStr = AuthenticationHandler.encodedCredStr(GlobalConstants.EMAIL, GlobalConstants.JIRA_TOKEN);
        // Request Object
        RequestSpecification request = given();
//        RequestSpecification request = getAppInfo(path);
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(acceptJSONHeader);
        request.header(getAuthenticatedHeader.apply(encodedCreStr));

        // Define body data
        ProjectInfo projectInfo = new ProjectInfo(baseUri,projectKey);
        String summary = "Summary | IssueContentBuilder";
        int desiredLength=20;
        boolean hasLetters = true;
        boolean hasNumbers = true;
        String randomSummary = RandomStringUtils.random(desiredLength,hasLetters,hasNumbers);
        String taskTypeId = projectInfo.getIssueTypeId("Task");
        IssueContentBuilder issueContentBuilder = new IssueContentBuilder();
        String issueFieldsContent = issueContentBuilder.build(projectKey,taskTypeId, randomSummary);

        // Send request CREATE JIRA TASK
        Response response = request.body(issueFieldsContent).post(path);

        // Check created task details
        Map<String,String> responseBody = JsonPath.from(response.asString()).get();
        final String CREATED_ISSUE_KEY =responseBody.get("key");
        System.out.println("CREATED_ISSUE_KEY:" + CREATED_ISSUE_KEY);
        IssueFields issueFields = issueContentBuilder.getIssueFields();
        String expectedSummary = issueFields.getFields().getSummary();
        String expectedStatus = "To Do";

        // READ CREATED JIRA TASK INFO
        IssueInfo issueInfo = new IssueInfo(baseUri,CREATED_ISSUE_KEY);
        Map<String, String> issueInfoDetail = issueInfo.getIssueInfoDetail();
        System.out.println("expectedSummary: " + expectedSummary);
        System.out.println("actualSummary: " + issueInfoDetail.get("summary"));
        System.out.println("expectedStatus: " + expectedStatus);
        System.out.println("actualStatus: " + issueInfoDetail.get("status"));

        // UPDATE CREATED JIRA TASK
        // Get TaskID have status 'DONE'
        TransitionContentBuilder transitionContentBuilder = new TransitionContentBuilder();
        TransitionInfo transitionInfo = new TransitionInfo(baseUri,CREATED_ISSUE_KEY);
        String transitionId = transitionInfo.getTransitionTypeId("Done");
        String transitionContent = transitionContentBuilder.build(transitionId);

        // Send request update status of task is done by issue key
        String getIssuePath = "/rest/api/3/issue/" + CREATED_ISSUE_KEY+"/transitions";
        request.body(transitionContent).post(getIssuePath).then().statusCode(204);

        // Check status of task updated
        issueInfo = new IssueInfo(baseUri,CREATED_ISSUE_KEY);
        issueInfoDetail = issueInfo.getIssueInfoDetail();
        String latestIssueStatus = issueInfoDetail.get("status");
        System.out.println("latestIssueStatus: " + latestIssueStatus);

        // DELETE CREATED JIRA TASK.
        String deleteIssuePath = "/rest/api/3/issue/" + CREATED_ISSUE_KEY;
        request.delete(deleteIssuePath).prettyPrint();
    }
}
