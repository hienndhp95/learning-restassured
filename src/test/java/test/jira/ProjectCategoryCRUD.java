package test.jira;

import api_flow.ProjectCategoriesFlow;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ProjectCategoryCRUD extends BaseTest{

    @BeforeTest
    public String randomName(){
        int desiredLength = 20;
        boolean hasLetters = true;
        boolean hasNumbers = false;
        return RandomStringUtils.random(desiredLength, hasLetters, hasNumbers);
    }

//    @BeforeTest
    public String randomDescription(){
        int desiredLength = 20;
        boolean hasLetters = true;
        boolean hasNumbers = false;
        return RandomStringUtils.random(desiredLength, hasLetters, hasNumbers);
    }

    @Test
    public void TC_01_TestE2EFlow(){
        ProjectCategoriesFlow projectCtg = new ProjectCategoriesFlow(request,randomName(),randomDescription());
        projectCtg.getAllProjectCategories();
        projectCtg.createProjectCategory();
        projectCtg.verifyResponseDataDetail();
        projectCtg.updateProjectCategory();
        projectCtg.verifyResponseDataDetail();
        projectCtg.deleteProjectCategory();
    }

    @Test
    public void TC_02_CreateAndGetProjectCategory(){
        ProjectCategoriesFlow projectCtg = new ProjectCategoriesFlow(request,randomName(),randomDescription());
        projectCtg.createProjectCategory();
        projectCtg.verifyResponseDataDetail();
        projectCtg.getProjectCategoryByID();
        projectCtg.verifyResponseDataDetail();
    }

    @Test
    public void TC_03_UpdateProjectCategory(){
        ProjectCategoriesFlow projectCtg = new ProjectCategoriesFlow(request,randomName(),randomDescription());
        projectCtg.updateProjectCategory();
        projectCtg.verifyResponseDataDetail();
    }

    @Test
    public void TC_04_DeleteProjectCategory(){
        ProjectCategoriesFlow projectCtg = new ProjectCategoriesFlow(request,randomName(),randomDescription());
        projectCtg.deleteProjectCategory();
    }

}
