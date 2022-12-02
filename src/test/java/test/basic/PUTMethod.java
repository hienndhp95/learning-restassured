package test.basic;

import com.google.gson.Gson;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.PostBody;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class PUTMethod {
    public static void main(String[] args) {
        String baseUri ="https://jsonplaceholder.typicode.com";

        // RequestSpecification object
        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(new Header("Content-type","application/json; charset-UTF-8"));

        // Construct body
        PostBody postBody1 = new PostBody(1,1,"New Title 1","New Body 1");
        PostBody postBody2 = new PostBody(1,1,"New Title 2","New Body 2");
        PostBody postBody3 = new PostBody(1,1,"New Title 3","New Body 3");
        PostBody postBody4 = new PostBody(1,1,"New Title 4","New Body 4");

        List<PostBody> postBodies = Arrays.asList(postBody1,postBody2,postBody3,postBody4);
        for(PostBody postBody :postBodies){
            System.out.println(postBody);
            Gson gson = new Gson();
            String postBodyStr = gson.toJson(postBody);
            // Send request
            final int TARGET_POST_NUM = 1;
            Response response = request.body(postBodyStr).put("/posts/".concat(String.valueOf(TARGET_POST_NUM)));
            response.prettyPrint();
            response.then().body("id", equalTo(postBody.getId()));
        }
    }
}
