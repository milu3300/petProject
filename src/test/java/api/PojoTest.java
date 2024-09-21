package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import entities.CustomResponce;
import entities.RequestBody;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import utilities.APIRunner;
import utilities.CashwiseToken;
import utilities.Config;

import java.util.*;

public class PojoTest {
    @Test
    public void CreateCategory() throws JsonProcessingException {
        Faker faker = new Faker();
        String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/categories";
        String token = CashwiseToken.GetToken();

        RequestBody requestBody = new RequestBody();

        requestBody.setCategory_title(faker.name().title());
        requestBody.setCategory_description(faker.name().firstName());
        requestBody.setFlag(false);

        Response response = RestAssured.given().auth().oauth2(token).contentType(ContentType.JSON)
                .body(requestBody).post(url);
        int status = response.getStatusCode();
        Assert.assertEquals(201, status);

        ObjectMapper mapper = new ObjectMapper();
        CustomResponce customResponce =  mapper.readValue(response.asString(), CustomResponce.class);
        System.out.println(customResponce.getCategory_id());
    }
    @Test
    public void CreateSecondCategory() throws JsonProcessingException {
        String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/categories";
        String token = CashwiseToken.GetToken();
        RequestBody requestBody = new RequestBody();
        requestBody.setCategory_title("Practice");
        requestBody.setCategory_description("Check");
        requestBody.setFlag(true);

        Response response = RestAssured.given().auth().oauth2(token).contentType(ContentType.JSON)
                .body(requestBody).post(url);
        int status = response.getStatusCode();
        Assert.assertEquals(201, status);
        ObjectMapper mapper = new ObjectMapper();
        CustomResponce customResponce = mapper.readValue(response.asString(), CustomResponce.class);
        int id = customResponce.getCategory_id();


        Response response1 = RestAssured.given().auth().oauth2(token).get(url + "/" + id);
        Assert.assertEquals(200, response1.getStatusCode());
        CustomResponce customResponce1 = mapper.readValue(response1.asString(), CustomResponce.class);
        Assert.assertEquals(customResponce.getCategory_id(), customResponce1.getCategory_id());

    }
    @Test
    public void CreateArchived(){
        String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers";
        String token = CashwiseToken.GetToken();
        RequestBody requestBody = new RequestBody();
        Faker faker = new Faker();
        List<Response> responses = new ArrayList<>();
        Integer [] array = new Integer[15];

        for (int i = 0; i < 16; i++){
            requestBody.setCompany_name(faker.company().name());
            requestBody.setSeller_name(faker.name().firstName());
            requestBody.setEmail(faker.internet().emailAddress());
            requestBody.setPhone_number(faker.number().digit());
            requestBody.setAddress(faker.address().fullAddress());
            Response response = RestAssured.given().auth().oauth2(token).contentType(ContentType.JSON)
                    .body(requestBody).post(url);
            responses.add(response);

        }
    }
    @Test
    public void GetAllSellers(){
        String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers";
        String token = CashwiseToken.GetToken();

        Map<String, Object> params = new HashMap<>();
        params.put("isArchived", false);
        params.put("page",1);
        params.put("size", 100);
        Response response = RestAssured.given().auth().oauth2(token).params(params).get(url);
        int status = response.statusCode();
        Assert.assertEquals(200, status);
        ObjectMapper mapper= new ObjectMapper();
//        CustomResponce customResponce = mapper.readValue(response.asString());
//        int size = customResponce.getResponses.size();
//        for (int i = 0; i < size; i++){
//            String email = customResponce.getResponses().get(i).getEmail();
//            Assert.assertFalse(email.isEmpty());
//        }
    }

    @Test
    public void CreateSellerNOEmail() throws JsonProcessingException {
        String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers";
        String token = CashwiseToken.GetToken();
        RequestBody requestBody = new RequestBody();
        Faker faker = new Faker();
        requestBody.setCompany_name(faker.company().name());
        requestBody.setSeller_name(faker.name().firstName());
        requestBody.setPhone_number(faker.number().digit());
        requestBody.setAddress(faker.address().fullAddress());
        Response response = RestAssured.given().auth().oauth2(token).contentType(ContentType.JSON)
                .body(requestBody).post(url);
        int status = response.getStatusCode();
        Assert.assertEquals(201, status);
        response.prettyPrint();
        ObjectMapper mapper = new ObjectMapper();
        CustomResponce customResponce = mapper.readValue(response.asString(), CustomResponce.class);
        System.out.println(customResponce.getSeller_id());
        int id = customResponce.getSeller_id();

        String url2 = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers/archive/unarchive";
        Map<String, Object> params = new HashMap<>();
        params.put("sellersIdsForArchive" , id);
        params.put("archive", true);

        Response response1 = RestAssured.given().auth().oauth2(token).params(params).post(url2 );
        response1.prettyPrint();
    }
    @Test
    public void createSeller() throws JsonProcessingException {
        String url = Config.getProperty("cashWiseBaseURL") + "/api/myaccount/sellers";
        String token = CashwiseToken.GetToken();
        Faker faker = new Faker();
        RequestBody requestBody = new RequestBody();
        List<Response> responses = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        Integer [] array = new Integer[15];
        for(int i = 0; i < 15; i ++) {
            requestBody.setCompany_name(faker.company().name());
            requestBody.setSeller_name(faker.name().firstName());
            requestBody.setEmail(faker.internet().emailAddress());
            requestBody.setPhone_number(faker.phoneNumber().phoneNumber());
            requestBody.setAddress(faker.address().streetAddress());

            Response response = RestAssured.given().auth().oauth2(token).contentType(ContentType.JSON)
                    .body(requestBody).post(url);
            responses.add((response));

           CustomResponce customResponse = mapper.readValue(response.asString(), CustomResponce.class);
            array[i] = customResponse.getSeller_id();
            ids.add(customResponse.getSeller_id());

        }

        System.out.println(Arrays.toString(array));
        System.out.println(ids);



        String url2 = Config.getProperty("cashWiseBaseURL") + "/api/myaccount/sellers/archive/unarchive";

        Response response1 = RestAssured.given().auth().oauth2(token).queryParam("sellersIdsForArchive", ids).queryParam("archive", true)
                .post(url2);

        response1.prettyPrint();


    }
    @Test
    public void archivedAllSellers() throws JsonProcessingException {
        String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers/archive/unarchive";
        String token = CashwiseToken.GetToken();
        String sellersUrl = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers/all";
        Response response = RestAssured.given().auth().oauth2(token).get(sellersUrl);

        ObjectMapper mapper = new ObjectMapper();
        CustomResponce customResponce = mapper.readValue(response.asString(), CustomResponce.class);
        List<CustomResponce> GDSCUJ = customResponce.getResponses();




    }

    @Test
    public void archiveTest(){
        String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers/archive/unarchive";
        String token = CashwiseToken.GetToken();

        Map<String, Object> params = new HashMap<>();
        params.put("sellersIdsForArchive", -1);
        params.put("archive", true);

        Response response = RestAssured.given().auth().oauth2(token).params(params).post(url);

        int status = response.statusCode();
        response.prettyPrint();

        Assert.assertEquals(200, status);
    }


    @Test
    public void ArchiveAll() throws JsonProcessingException {
        String token = CashwiseToken.GetToken();
        String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers";

        Map<String, Object> params = new HashMap<>();

        params.put("isArchived", false);
        params.put("page", 1 );
        params.put("size", 110 );
        Response response = RestAssured.given().auth().oauth2(token).params(params).get(url);
        int status = response.statusCode();
        Assert.assertEquals(200, status);
        ObjectMapper mapper = new ObjectMapper();
        CustomResponce customResponse = mapper.readValue(response.asString(), CustomResponce.class);
        String urlToArchive = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers/archive/unarchive";
        int size = customResponse.getResponses().size();
        for(int i = 0; i < size; i ++ ){
            int id = customResponse.getResponses().get(i).getSeller_id();
            Map<String, Object> paramsToArchive = new HashMap<>();
            paramsToArchive.put("sellersIdsForArchive",id );
            paramsToArchive.put("archive", true);
            Response response1 = RestAssured.given().auth().oauth2(token).params(paramsToArchive).post(urlToArchive);
            int status1 = response1.statusCode();
            Assert.assertEquals(200, status1);
        }
    }



        @Test
        public void GetAllActivate() throws JsonProcessingException {
            String token = CashwiseToken.GetToken();
            String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers";

            Map<String, Object> params = new HashMap<>();

            params.put("isArchived", true);
            params.put("page", 1 );
            params.put("size", 110 );

            Response response = RestAssured.given().auth().oauth2(token).params(params).get(url);

            int status = response.statusCode();
            Assert.assertEquals(200, status);

            ObjectMapper mapper = new ObjectMapper();

            CustomResponce customResponse = mapper.readValue(response.asString(), CustomResponce.class);

            String urlToArchive = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers/archive/unarchive";
            int size = customResponse.getResponses().size();
            for(int i = 0; i < size; i ++ ){
                String email = customResponse.getResponses().get(i).getEmail();

                if(email != null && email.endsWith("@hotmail.com")){
                    int id = customResponse.getResponses().get(i).getSeller_id();

                    Map<String, Object> paramsToArchive = new HashMap<>();

                    paramsToArchive.put("sellersIdsForArchive",id );
                    paramsToArchive.put("archive", false);

                    Response response1 = RestAssured.given().auth().oauth2(token).params(paramsToArchive).post(urlToArchive);

                    int status1 = response1.statusCode();

                    Assert.assertEquals(200, status1);
                }
            }
        }
        @Test
    public void verifyCreateSeller() throws JsonProcessingException {
        String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers";
        String token = CashwiseToken.GetToken();
        RequestBody requestBody = new RequestBody();
        requestBody.setCompany_name("Checking");
        requestBody.setSeller_name("Sellers123");
        requestBody.setEmail("check@gmail.com");
        requestBody.setPhone_number("123456789");
        requestBody.setAddress("wsedrftgyhui");
        Response response = RestAssured.given().auth().oauth2(token).contentType(ContentType.JSON)
                .body(requestBody).post(url);
        ObjectMapper mapper = new ObjectMapper();
        CustomResponce customResponce = mapper.readValue(response.asString(), CustomResponce.class);
        String url2 = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers";
            int size = customResponce.getResponses().size();
            for(int i = 0; i < size; i ++ ){
                String email = customResponce.getResponses().get(i).getEmail();

                if(email != null && email.equals("check@gmail.com")){
                    int id = customResponce.getResponses().get(i).getSeller_id();

                    Map<String, Object> params = new HashMap<>();
                    params.put("isArchived", true);
                    params.put("page", 1 );
                    params.put("size", 110 );

                    Response response1 = RestAssured.given().auth().oauth2(token).params(params).post(url2);

                    int status1 = response1.statusCode();

                    Assert.assertEquals(200, status1);
                }
            }



        }
    @Test
    public void CreateGetSeller() throws JsonProcessingException {
        Faker faker = new Faker();

        String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers";
        String token = CashwiseToken.GetToken();

        RequestBody requestBody = new RequestBody();
        requestBody.setCompany_name("CatWise");
        requestBody.setSeller_name("Meayw ");
        requestBody.setEmail(faker.internet().emailAddress());
        requestBody.setPhone_number("31274325234");
        requestBody.setAddress("Earth");

        Response response = RestAssured.given().auth().oauth2(token).contentType(ContentType.JSON)
                .body(requestBody).post(url);

        int status = response.statusCode();

        Assert.assertEquals(201, status);

        ObjectMapper mapper = new ObjectMapper();
        CustomResponce customResponse = mapper.readValue(response.asString(), CustomResponce.class);

        int ExpectedSellerId = customResponse.getSeller_id();


        String url2 = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers";

        Map<String, Object> params = new HashMap<>();
        params.put("isArchived", false);
        params.put("size", 1000 );
        params.put("page", 1 );

        Response response1 = RestAssured.given().auth().oauth2(token).params(params).get(url2);
        int statusCode = response.statusCode();
        Assert.assertEquals(201, statusCode);

        CustomResponce customResponse1 = mapper.readValue(response1.asString(), CustomResponce.class);

        int size = customResponse1.getResponses().size();

        boolean isPresent = false;

        for(int i = 0; i < size; i ++ ){
            if(customResponse1.getResponses().get(i).getSeller_id() == ExpectedSellerId){
                isPresent = true;
                break;
            }
        }

        Assert.assertTrue(isPresent);}
    @Test
    public void testGET(){
        APIRunner.runGET("/api/myaccount/sellers/2344");
        String email = APIRunner.getCustomResponce().getEmail();
    }

}












































