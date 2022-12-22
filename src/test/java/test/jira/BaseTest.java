package test.jira;

import actions.common.GlobalConstants;
import io.restassured.specification.RequestSpecification;
import model.RequestCapability;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import utils.AuthenticationHandler;

import java.io.File;

import static io.restassured.RestAssured.given;

public class BaseTest implements RequestCapability {

    protected String baseUri;
    protected String projectKey;
    protected String encodedCreStr;
    protected RequestSpecification request;
    @BeforeSuite
    public void beforeSuite(){
        encodedCreStr = AuthenticationHandler.encodedCredStr(GlobalConstants.EMAIL, GlobalConstants.JIRA_TOKEN);
        baseUri = GlobalConstants.BASE_URI;
        projectKey = GlobalConstants.PROJECT_KEY;
//        deleteAllureReport();
    }

    @BeforeTest
    public void beforeTest(){
        // Request Object
        request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(acceptJSONHeader);
        request.header(getAuthenticatedHeader.apply(encodedCreStr));
    }

    public void deleteAllureReport() {
        try {
            String pathFolderDownload = GlobalConstants.PROJECT_PATH + "/allure-results";
            File file = new File(pathFolderDownload);
            File[] listOfFiles = file.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    new File(listOfFiles[i].toString()).delete();
                }
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }
}
