package utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.CustomResponce;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.Data;
import lombok.Getter;

@Data
public class APIRunner {
    @Getter
    private static CustomResponce customResponce;

    public  static void runGET(String path){
        String token = CashwiseToken.GetToken();
        String url = Config.getProperty("cashWiseApiUrl") + path;

        Response response = RestAssured.given().auth().oauth2(token).get(url);
        System.out.println("status code : " + response.getStatusCode());
        ObjectMapper mapper = new ObjectMapper();
        try {
            customResponce = mapper.readValue(response.asString(), CustomResponce.class);
        }catch (JsonProcessingException e){
            System.out.println("this is a list response");
        }

    }

}
