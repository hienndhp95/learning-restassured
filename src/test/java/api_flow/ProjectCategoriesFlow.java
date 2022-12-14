package api_flow;

import actions.common.GlobalConstants;
import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;
import java.util.List;
import java.util.Map;

public class ProjectCategoriesFlow {
    private static final String issuePathPrefix="/rest/api/3/projectCategory";
    private RequestSpecification request;
    private Response response;
    private static String projectID;
    private String randomName;
    private String description;

    public ProjectCategoriesFlow(RequestSpecification request, String name, String description) {
        this.request = request;
        this.randomName = name;
        this.description = description;
    }

    @Step("Get all project categories")
    public void getAllProjectCategories(){
        Response response = request.get(issuePathPrefix);
        List<Map<String,String>> projectCategories = JsonPath.from(response.getBody().asString()).get();
        for (Map<String,String> projectCategory : projectCategories) {
            System.out.println("ID=" + projectCategory.get("id"));
            System.out.println("Name=" + projectCategory.get("name"));
        }
    }

    @Step("Get project category by ID")
    public void getProjectCategoryByID(){
        String getProjectCategoryByIdPath = issuePathPrefix + "/" + projectID;
        response = request.get(getProjectCategoryByIdPath);
    }
    @Step("Creating jira project category")
    public void createProjectCategory(){
        JSONObject data = new JSONObject();
        data.put("name",randomName);
        data.put("description",description);
        response = request.body(data.toJSONString()).post(issuePathPrefix);
        Map<String,String> responseBody = JsonPath.from(response.getBody().asString()).get();
        projectID = responseBody.get("id");
    }

    @Step("Updating project category")
    public void updateProjectCategory(){
        String updateProjectCategoryPath = issuePathPrefix + "/" + projectID;
        JSONObject data = new JSONObject();
        data.put("name", randomName);
        data.put("description",description);
        response = request.body(data.toJSONString()).put(updateProjectCategoryPath);
    }

    @Step("Deleting project category")
    public void deleteProjectCategory(){
        String path = issuePathPrefix + "/" + projectID;
        request.delete(path);
        response = request.get(path);
        String responseAfterDelete = response.getBody().asString();
        Assert.assertEquals(responseAfterDelete,"Project category with id: " + projectID + " not found.");
    }

    @Step("Verifying project category")
    public void verifyResponseDataDetail(){
        Map<String,String> projectCategoryBody = JsonPath.from(response.getBody().asString()).get();
        Assert.assertEquals(projectCategoryBody.get("self"), GlobalConstants.BASE_URI + issuePathPrefix + "/" + projectID);
        Assert.assertEquals(projectCategoryBody.get("id"), projectID);
        Assert.assertEquals(projectCategoryBody.get("name"),randomName);
        Assert.assertEquals(projectCategoryBody.get("description"),description);
    }
}
