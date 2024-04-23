package E_Commerce_APITest;

import Pojo.LoginRequest;
import Pojo.LoginResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

import java.io.File;


public class Ecom_API_Test {

	public static void main(String[] args) {
		RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();
		
		// 1] Login 
		System.out.println("\n Logging in to Ecom Website ---> ");

		//Call Pojo Class Object
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("123456gk@gmail.com");
		loginRequest.setUserPassword("1234@Abcd");
		
		RequestSpecification reqLogin = given().spec(reqSpec).body(loginRequest);
		
		LoginResponse loginresponse = reqLogin.when().post("/api/ecom/auth/login").then().extract().response().as(LoginResponse.class);
		String token = loginresponse.getToken();
		String userID = loginresponse.getUserId();
		
		System.out.println("Authorization Token => " + token);
		System.out.println("User ID  => " + userID);	
		
		
		//2] Create Product 
		System.out.println("\n Creating New Product ---> ");
		RequestSpecification createProductBaseSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", token).build();
		
		//send body as Form Data (not as raw data) 
		RequestSpecification createProductAddSpec=
		given().spec(createProductBaseSpec)
		.param("productName","Laptop")
		.param("productAddedBy",userID )
		.param("productCategory", "Electric")
		.param("productSubCategory", "Ipad")
		.param("productPrice", "12000")
		.param("productDescription","Laptop")
		.param("productFor", "men")
		.multiPart("productImage", new File(System.getProperty("user.dir")+"\\laptop.jpg"));

		
		//user MultiPart to send Files
		 String newProductResponse = createProductAddSpec.when().post("/api/ecom/product/add-product")
		.then().extract().response().asString();
		 
		 JsonPath js = new JsonPath(newProductResponse);
		
		String productId = js.get("productId");
		String message = js.get("message");
		System.out.println("New Product Id => "+ productId);
		System.out.println("Message => "+ message);
		
		//3] Create Order of newly Created Product
		System.out.println("\n Creating New Order ---> ");
		
		
		
		
	}

}
