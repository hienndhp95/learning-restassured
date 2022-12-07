package api_flow;

import actions.common.GlobalConstants;
import builder.BodyJSONBuilder;
import builder.IssueContentBuilder;
import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.IssueFields;
import model.IssueTransition;
import org.apache.commons.lang3.RandomStringUtils;
import utils.ProjectInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class IssueFlow {
    private static final String issuePathPrefix="/rest/api/3/issue";
    private static Map<String, String> transitionTypeMap = new HashMap<>();
    private String createdIssueKey;
    private String issueTypeStr;
    private IssueFields issueFields;
    private RequestSpecification request;
    private Response response;
    private String projectKey;
    private String baseUri;
    private String status ;
    static {
        transitionTypeMap.put("41","Done");
    }

    public IssueFlow(RequestSpecification request,String baseUri, String projectKey, String issueTypeStr) {
        this.request = request;
        this.issueTypeStr = issueTypeStr;
        this.projectKey = projectKey;
        this.baseUri=baseUri;
        this.status = "To Do";
    }

    @Step("Creating Jira issue")
    public void createIssue(){
        // Define body data
        ProjectInfo projectInfo = new ProjectInfo(GlobalConstants.BASE_URI, projectKey);
        int desiredLength = 20;
        boolean hasLetters = true;
        boolean hasNumbers = true;
        String randomSummary = RandomStringUtils.random(desiredLength, hasLetters, hasNumbers);
        String taskTypeId = projectInfo.getIssueTypeId(issueTypeStr);
        IssueContentBuilder issueContentBuilder = new IssueContentBuilder();
        String issueFieldsContent = issueContentBuilder.build(projectKey, taskTypeId, randomSummary);
        issueFields = issueContentBuilder.getIssueFields();
        // Send request CREATE JIRA TASK
        this.response = request.body(issueFieldsContent).post(issuePathPrefix);

        // Get key of task created
        Map<String, String> responseBody = JsonPath.from(response.asString()).get();
        createdIssueKey = responseBody.get("key");
    }
    @Step("Verifying Jira issue")
    public void verifyIssueDetails(){
       Map<String, String> issueInfo = getIssueInfo();
       String expectedSummary = issueFields.getFields().getSummary();
       String expectedStatus = status;
       String actualSummary = issueInfo.get("summary");
       String actualStatus = issueInfo.get("status");
       System.out.println("expectedSummary: " + expectedSummary);
       System.out.println("actualSummary: " + actualSummary);
       System.out.println("expectedStatus: " + expectedStatus);
       System.out.println("actualStatus: " + actualStatus);
    }
    @Step("Updating Jira issue")
    public void updateIssue(String issueStatusStr){
        String targetTransitionId = null;
        for (String transitionId : transitionTypeMap.keySet()) {
            if(transitionTypeMap.get(transitionId).equalsIgnoreCase(issueStatusStr)){
                targetTransitionId = transitionId;
                break;
            }
        }
        if (targetTransitionId==null){
            throw new RuntimeException("[ERR] Issue status string provided is not supported");
        }
        String issueTransitionPath = issuePathPrefix + "/" + createdIssueKey + "/transitions";
        IssueTransition.Transition transition = new IssueTransition.Transition(targetTransitionId);
        IssueTransition issueTransition = new IssueTransition(transition);
        String transitionBody = BodyJSONBuilder.getJSONContent(issueTransition);
        request.body(transitionBody).post(issueTransitionPath).then().statusCode(204);
        Map<String, String> issueInfo = getIssueInfo();
        String actualIssueStatus = issueInfo.get("status");
        String expectIssueStatus = transitionTypeMap.get(targetTransitionId);
        System.out.println("latest Status: " + actualIssueStatus );
        System.out.println("expected Status: " + expectIssueStatus);
    }

    @Step("Deleting Jira issue")
    public void deleteIssue(){
        // DELETE CREATED JIRA TASK.
        String path = issuePathPrefix +"/" + createdIssueKey;
        request.delete(path);

        // Verify after delete
        response = request.get(path);
        Map<String, List<String>> notIssueExistingIssueRes = JsonPath.from(response.body().asString()).get();
        List<String> errorMessages = notIssueExistingIssueRes.get("errorMessages");
        System.out.println("Return msg: " + errorMessages.get(0));
    }
    private Map<String, String> getIssueInfo(){
        String getIssuePath = issuePathPrefix + "/" + createdIssueKey;
        Response response_ = request.get(getIssuePath);
        Map<String, Object> fields = JsonPath.from(response_.asString()).get("fields");
        String actualSummary = fields.get("summary").toString();
        Map<String, Object> status = (Map<String, Object>) fields.get("status");
        Map<String, Object> statusCategory = (Map<String, Object>) status.get("statusCategory");
        String actualStatus = statusCategory.get("name").toString();
        Map<String,String> issueInfo = new HashMap<>();
        issueInfo.put("summary", actualSummary);
        issueInfo.put("status", actualStatus);
        return issueInfo;
    }
}
