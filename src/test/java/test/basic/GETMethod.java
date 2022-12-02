package test.basic;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class GETMethod {
    public static void main(String[] args) {
        String baseUri ="https://jsonplaceholder.typicode.com";

        // Request Scope
        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.basePath("/todos");

        // Response Scope
        final String FIRST_TODO ="/1";
        Response response = request.get(FIRST_TODO);
        response.prettyPrint();
        response.then().body("userId",equalTo(5));
        response.then().body("id",equalTo(1));
        response.then().body("title",equalTo("delectus aut autem"));
        response.then().body("completed",equalTo(false));
    }
}
