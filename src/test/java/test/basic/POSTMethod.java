package test.basic;

import com.google.gson.Gson;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.PostBody;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.equalTo;

public class POSTMethod {
    public static void main(String[] args) {
        String baseUri ="https://jsonplaceholder.typicode.com";

        // RequestSpecification object
        RequestSpecification request = given();
        request.baseUri(baseUri);

        // Content-type -> Header
        request.header(new Header("Content-type","application/json; charset-UTF-8"));

//        // Form up request body
//        String postBody ="{\n" +
//                "  \"userId\": 1,\n" +
//                "  \"id\": 1,\n" +
//                "  \"title\": \"The req's title\",\n" +
//                "  \"body\": \"The req's body\"\n" +
//                "}";

        // Gson
        Gson gson= new Gson();
        PostBody postBody = new PostBody();
        postBody.setUserId(1);
        postBody.setTitle("The req's title");
        postBody.setBody("The req's body");

        // Send POST request
        Response response = request.body(gson.toJson(postBody)).post("/posts");
        response.prettyPrint();

        System.out.println(response.statusCode());
        System.out.println(response.statusLine());

        // Verification
        response.then().statusCode(equalTo(201));
        response.then().statusLine(containsStringIgnoringCase("201 Created"));
        response.then().body("id",equalTo(101));
    }
}
