package test.jira;

import actions.common.GlobalConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.RequestCapability;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;
import org.testng.Assert;
import utils.AuthenticationHandler;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class JiraProjectCategory implements RequestCapability {
    private static final String issuePathPrefix="/rest/api/3/projectCategory";
    public static void main(String[] args) {
        // App info
        String baseUri = GlobalConstants.BASE_URI;
        String encodedCreStr = AuthenticationHandler.encodedCredStr(GlobalConstants.EMAIL, GlobalConstants.JIRA_TOKEN);
        // Request Object
        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(acceptJSONHeader);
        request.header(getAuthenticatedHeader.apply(encodedCreStr));

        // Get all project category
        Response response = request.get(issuePathPrefix);
        List<Map<String,String>> projectCategories = JsonPath.from(response.getBody().asString()).get();
        for (Map<String,String> projectCategory : projectCategories) {
            System.out.println("ID=" + projectCategory.get("id"));
            System.out.println("Name=" + projectCategory.get("name"));
        }

        // Create project category
        int desiredLength = 20;
        boolean hasLetters = true;
        boolean hasNumbers = false;
        String randomName = RandomStringUtils.random(desiredLength, hasLetters, hasNumbers);
        JSONObject data = new JSONObject();
        data.put("name",randomName);
        data.put("description",randomName);
        response = request.body(data.toJSONString()).post(issuePathPrefix);
        Map<String,String> responseBody = JsonPath.from(response.getBody().asString()).get();
        String id = responseBody.get("id");
        String name = responseBody.get("name");

        // Get project category by ID
        String getProjectCategoryByIdPath = issuePathPrefix + "/" + id;
        response = request.get(getProjectCategoryByIdPath);
        Map<String,String> projectCategoryBody = JsonPath.from(response.getBody().asString()).get();
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertEquals(projectCategoryBody.get("id"),id);
        Assert.assertEquals(projectCategoryBody.get("name"),name);

        //Update project category
        String updateProjectCategoryPath = issuePathPrefix + "/" + id;
        data.put("name", randomName);
        data.put("description","Description for Project name: " + randomName);
        response = request.body(data.toJSONString()).put(updateProjectCategoryPath);
        Map<String,String> responseBodyUpdate = JsonPath.from(response.getBody().asString()).get();
        Assert.assertEquals(responseBodyUpdate.get("self"),GlobalConstants.BASE_URI + updateProjectCategoryPath);
        Assert.assertEquals(responseBodyUpdate.get("id"),id);
        Assert.assertEquals(responseBodyUpdate.get("name"),randomName);
        Assert.assertEquals(responseBodyUpdate.get("description"),"Description for Project name: " + randomName);

        // Delete Project Category
        String deleteProjectCategoryPath = issuePathPrefix + "/" + id;
        request.delete(deleteProjectCategoryPath);
        response = request.get(getProjectCategoryByIdPath);
        String afterDelete = response.getBody().asString();
        Assert.assertEquals(afterDelete,"Project category with id: " + id + " not found.");
    }
}
