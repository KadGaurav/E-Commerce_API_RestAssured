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
		
		String NewProductImgPath ="F:\\Selenium\\E-Commerce_APITest_RestAssured\\laptop.jpg";
		
		//Create Product 
		RequestSpecification createProductBaseSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", token).build();
		
		//send body as Form Data 
		RequestSpecification createProductAddSpec=
		given().spec(createProductBaseSpec)
		.param("productName","Laptop")
		.param("productAddedBy",userID )
		.param("productCategory", "Electric")
		.param("productSubCategory", "Ipad")
		.param("productPrice", "12000")
		.param("productDescription","Laptop")
		.param("productFor", "men")
		.multiPart("productImage", new File(NewProductImgPath));
		
		//user MultiPart to send Files
		
		 String newProductResponse = createProductAddSpec.when().post("/api/ecom/product/add-product")
		.then().extract().response().asString();
		 
		 JsonPath js = new JsonPath(newProductResponse);
		
		String productId = js.get("productId");
		String message = js.get("messgae");
		System.out.println("New Product Id => "+ productId);
		System.out.println("Message => "+ message);
		
		
	}

}
