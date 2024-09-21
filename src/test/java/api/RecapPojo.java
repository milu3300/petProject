package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.CustomResponce;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import utilities.CashwiseToken;
import utilities.Config;

import java.util.HashMap;

public class RecapPojo {
    public static void main(String[] args) throws JsonProcessingException {

        String token = CashwiseToken.GetToken();
        String url = Config.getProperty("baseCashwiseUrl") + "/api/myaccount/sellers/all";

        Response response = RestAssured.given().auth().oauth2(token).get(url);
        System.out.println(response.statusCode());
        System.out.println(response.prettyPrint());

        ObjectMapper objectMapper = new ObjectMapper();

        CustomResponce [] customResponse = objectMapper.readValue(response.asString(), CustomResponce[].class);

        System.out.println(customResponse[1].getSeller_id());
    }


    @Test
    public void getAllSellerWithParameters() throws JsonProcessingException {
        String token = CashwiseToken.GetToken();
        String url = Config.getProperty("baseCashwiseUrl") + "/api/myaccount/sellers";

        HashMap<String, Object> param = new HashMap<>();
        param.put("isArchived", false);
        param.put("page", 1);
        param.put("size", 10);
        Response response = RestAssured.given().auth().oauth2(token).params(param).get(url);
        System.out.println(response.statusCode());
        System.out.println(response.prettyPrint());

        ObjectMapper objectMapper = new ObjectMapper();
        CustomResponce customResponse = objectMapper.readValue(response.asString(), CustomResponce.class);

        System.out.println(customResponse.getResponses().get(0).getSeller_id());

    }
}
