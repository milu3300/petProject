package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.CustomResponce;
import entities.RequestBody;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import utilities.CashwiseToken;
import utilities.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Test {
   @org.junit.Test
   public void testToken(){
      String endPoint = "https://backend.cashwise.us/api/myaccount/auth/login";
      RequestBody requestBody = new RequestBody();

      requestBody.setEmail("aigulpz00@gmail.com");
      requestBody.setPassword("Aigulpazylova00");

      Response response =  RestAssured.given().contentType(ContentType.JSON)
              .body(requestBody).post(endPoint);
      int statusCode = response.statusCode();

      Assert.assertEquals(200, statusCode);

     // String token = response.prettyPrint();
     // System.out.println(token);

      String token = response.jsonPath().getString("jwt_token");
      System.out.println(token);

   }
   @org.junit.Test
   public void GetSingSeller(){
      String url = Config.getProperty("cashWiseApiUrl")  + "/api/myaccount/sellers/" + 4728;
      String token = CashwiseToken.GetToken();
      Response response = RestAssured.given().auth().oauth2(token).get(url);
//      response.prettyPrint();

      String expectedEmail = response.jsonPath().getString("email");
      Assert.assertFalse(expectedEmail.isEmpty());
      Assert.assertTrue(expectedEmail.endsWith(".com"));

   }

   @org.junit.Test
   public void GetAllSellers(){
      String url = Config.getProperty("cashWiseApiUrl")+ "/api/myaccount/sellers";
      String token = CashwiseToken.GetToken();
      HashMap<String, Object> params = new HashMap<>();
      params.put("isArchived",false);
      params.put("size",10);
      params.put("page",1);

      Response response = RestAssured.given().auth().oauth2(token).params(params).get(url);
      int statusCode = response.statusCode();
      Assert.assertEquals(200,statusCode);
     // response.prettyPrint();
      String email = response.jsonPath().getString("responses[2].email");
      //System.out.println(response.prettyPrint());
      Assert.assertFalse(email.isEmpty());
      Assert.assertNotNull(email);
      System.out.println(email);
   }
   @org.junit.Test
   public void GetAllSellersLoop(){
      String url = Config.getProperty("cashWiseApiUrl")+ "/api/myaccount/sellers";
      String token = CashwiseToken.GetToken();
      Map<String, Object> params = new HashMap<>();
      params.put("isArchived", false);
      params.put("size", 6);
      params.put("page",1);
      Response response = RestAssured.given().auth().oauth2(token).params(params).get(url);
      int statusCode = response.statusCode();
      Assert.assertEquals(200,statusCode);
      // response.prettyPrint();
      String email = response.jsonPath().getString("responses[2].email");
      //System.out.println(response.prettyPrint());
      Assert.assertFalse(email.isEmpty());
      Assert.assertNotNull(email);
      System.out.println(email);

      List<String> listOfEmails = response.jsonPath().getList("responses.email");
      for (String emails: listOfEmails){
         Assert.assertFalse(emails.isEmpty());
   }
   }
   @org.junit.Test
   public void CreateSeller(){
      String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers";
      String token = CashwiseToken.GetToken();

      RequestBody requestBody = new RequestBody();
      requestBody.setCompany_name("seller4");
      requestBody.setSeller_name("Nurgazy");
      requestBody.setEmail("13wisecode@gmail.com");
      requestBody.setPhone_number("123456789");
      requestBody.setAddress("Earth");

      Response response = RestAssured.given().auth().oauth2(token).contentType(ContentType.JSON)
              .body(requestBody).post(url);
      int status = response.statusCode();
      Assert.assertEquals(201, status);
     String id = response.jsonPath().getString("seller_id");

     String url2 = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers/" + id;
     Response response1 = RestAssured.given().auth().oauth2(token).get(url2);
     Assert.assertEquals(200,response1.getStatusCode());
   }


}
