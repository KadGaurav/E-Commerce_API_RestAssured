package E_Commerce_APITest;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Pojo.CreateOrders;
import Pojo.LoginRequest;
import Pojo.LoginResponse;
import Pojo.Orders_SubClass;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;


public class Ecom_API_Test {

	public static void main(String[] args) {
		
		// 1] Login 
		System.out.println("\n Logging in to Ecomerce Website ---> ");
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
		
		RequestSpecification createNewOrder =  new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", token).setContentType(ContentType.JSON).build();
		//Set Values Using POJO Classes
		Orders_SubClass orderDetail = new Orders_SubClass();
		orderDetail.setCountry("India");
		orderDetail.setProductOrderedId(productId);
		List<Orders_SubClass> orderDetailList = new ArrayList<>();
		orderDetailList.add(orderDetail);
		CreateOrders createOrder = new CreateOrders();
		createOrder.setOrders(orderDetailList);
		
		RequestSpecification newOrder = given().spec(createNewOrder).body(createOrder);
		
		String Response= newOrder.when().post("/api/ecom/order/create-order")
		.then().extract().response().asString();
		
		System.out.println(Response);
		
		
		//4] View/Get Order Product Details
		System.out.println("\n Viewing New Order ---> ");
		RequestSpecification viewNewOrder =  new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("id", productId).addHeader("Authorization", token).setContentType(ContentType.JSON).build();
		
		String viewProd = given().spec(viewNewOrder).when().get("/api/ecom/order/get-orders-details").then().extract().response().asString();

		System.out.print(viewProd);
		
		//5] Delete Product
		System.out.println("\n Deleting New Product ---> ");
		RequestSpecification deleteOrder =  new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("id", productId).addHeader("Authorization", token).setContentType(ContentType.JSON).build();
		String deleteResponse = given().spec(deleteOrder).pathParam("productId", productId).when().delete("/api/ecom/product/delete-product/{productId}").then().extract().response().asString();
		JsonPath js2 = new JsonPath(deleteResponse);
		String deletedMsg = js2.get("message");
		System.out.println(deletedMsg);
	}

}
