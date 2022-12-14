package api_flow;

import actions.common.GlobalConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class ProjectCategoriesFlow {
    private static final String issuePathPrefix="/rest/api/3/projectCategory";
    private RequestSpecification request;
    private Response response;
    private static String projectCategoryName;
    private String description;
    private String baseUri;
    private static String projectID;

    private String randomName;

    public ProjectCategoriesFlow(RequestSpecification request, String name, String description) {
        this.request = request;
        this.randomName = name;
        this.description = description;
    }

    public void getAllProjectCategories(){
        Response response = request.get(issuePathPrefix);
        List<Map<String,String>> projectCategories = JsonPath.from(response.getBody().asString()).get();
        for (Map<String,String> projectCategory : projectCategories) {
            System.out.println("ID=" + projectCategory.get("id"));
            System.out.println("Name=" + projectCategory.get("name"));
        }
    }

    public void getProjectCategoryByID(){
        String getProjectCategoryByIdPath = issuePathPrefix + "/" + projectID;
        response = request.get(getProjectCategoryByIdPath);
    }

    public void createProjectCategory(){
        JSONObject data = new JSONObject();
        data.put("name",randomName);
        data.put("description",description);
        response = request.body(data.toJSONString()).post(issuePathPrefix);
        Map<String,String> responseBody = JsonPath.from(response.getBody().asString()).get();
        projectID = responseBody.get("id");
        projectCategoryName = responseBody.get("name");
        description = responseBody.get("description");
    }

    public void updateProjectCategory(){
        String updateProjectCategoryPath = issuePathPrefix + "/" + projectID;
        JSONObject data = new JSONObject();
        data.put("name", randomName);
        data.put("description",description);
        response = request.body(data.toJSONString()).put(updateProjectCategoryPath);
    }

    public void deleteProjectCategory(){
        String path = issuePathPrefix + "/" + projectID;
        request.delete(path);
        response = request.get(path);
        String responseAfterDelete = response.getBody().asString();
        Assert.assertEquals(responseAfterDelete,"Project category with id: " + projectID + " not found.");
    }

    public void verifyResponseDataDetail(){
        Map<String,String> projectCategoryBody = JsonPath.from(response.getBody().asString()).get();
        Assert.assertEquals(projectCategoryBody.get("self"), GlobalConstants.BASE_URI + issuePathPrefix + "/" + projectID);
        Assert.assertEquals(projectCategoryBody.get("id"), projectID);
        Assert.assertEquals(projectCategoryBody.get("name"),randomName);
        Assert.assertEquals(projectCategoryBody.get("description"),description);
    }
}
