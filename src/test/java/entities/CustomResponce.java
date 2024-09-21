package entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomResponce {

    private int category_id;
    private String category_title;
    private int seller_id;
    private String email;


   private List<CustomResponce> responses;
}
