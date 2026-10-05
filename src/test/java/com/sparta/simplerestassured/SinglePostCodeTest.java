package com.sparta.simplerestassured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SinglePostCodeTest {

    private final static String url = "https://api.postcodes.io";

    private static Response response;

    @BeforeAll
    static void beforeAll(){
        response = RestAssured
                .given()
                .baseUri(url)
                .basePath("/postcodes")
                .header("Accept","text/json")
                .when()
                .get("EC2Y5AS")
                .then()
                .log().all()
                .extract().response();
    }

    @Test
    void testStatusCode200(){
        MatcherAssert.assertThat(response.getStatusCode(), Matchers.is(200));
    }

//    @Test
//    void testStatusCode404(){
//        Response response = RestAssured
//                .given()
//                    .baseUri(url)
//                    .basePath("/postcodes")
//                    .header("Accept","text/json")
//                .when()
//                .get("Et656C2Y5AS")
//                    .then().extract().response();
//        MatcherAssert.assertThat(response.getStatusCode(), Matchers.is(404));
//    }
    @Test
    @DisplayName("The server name in the headers is cloudflare")
    void testServerNameInHeadersIsCloudFlare(){

        MatcherAssert.assertThat(response.getHeader("Server"),Matchers.containsString("cloudflare"));
    }
    @Test
    @DisplayName("Correct postcode returned in response")
    void testCorrectPostcodeReturnedInResponse() {
        MatcherAssert.assertThat(response.jsonPath().getString("result.postcode"),Matchers.equalTo("EC2Y 5AS"));
    }


    @Test
    @DisplayName("Correct Primary and Care Trust")
    void testCorrectPrimaryAndCareTrust() {
        MatcherAssert.assertThat(response.jsonPath().getString("result.primary_care_trust"),Matchers.equalTo("City and Hackney Teaching"));
    }

    @Test
    @DisplayName("number of codes in result is equal to 14")
    void testNumberOfCodesInResultIsEqualTo14() {
        //String codes = response.jsonPath().getString("result.codes");
        Map<String,String> codes = response.jsonPath().getMap("result.codes",String.class,String.class);
       MatcherAssert.assertThat(codes.size(),Matchers.equalTo(29));
    }



}
