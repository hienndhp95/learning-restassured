package test.basic;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.BuildModelJSON;
import model.PostBody;

import static io.restassured.RestAssured.given;
import static model.RequestCapability.defaultHeader;
import static org.hamcrest.CoreMatchers.equalTo;

public class PATCHMethod {
    public static void main(String[] args) {
        String baseUri ="https://jsonplaceholder.typicode.com";

        // Form up request instance header and baseUri
        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);

        // Form up body
        PostBody postBody = new PostBody();
        postBody.setTitle("New Title");
        String postBodyStr = BuildModelJSON.parseJSONString(postBody);
        System.out.println(postBodyStr);
        final String TARGET_POST_ID = "1";
        Response response = request.body(postBodyStr).patch("/posts/".concat(TARGET_POST_ID));
        response.then().body("title", equalTo(postBody.getTitle()));
        response.prettyPrint();
    }
}
