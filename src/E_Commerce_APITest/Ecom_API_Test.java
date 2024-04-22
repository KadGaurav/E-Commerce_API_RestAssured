package E_Commerce_APITest;

import Pojo.LoginRequest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;


public class Ecom_API_Test {

	public static void main(String[] args) {
		RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();
		
		//Call Pojo Class Object
		
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("123456gk@gmail.com");
		loginRequest.setUserPassword("1234@Abcd");
		
		RequestSpecification reqLogin = given().spec(reqSpec).body(loginRequest);
		
		reqLogin.when().post("/api/ecom/auth/login");
		

	}

}
