package com.sparta.simplerestassured;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkPostcodeLookup {

    private static final String url = "https://api.postcodes.io";
    private static Response response;
    private static Gson gson = new Gson();

    static Map<String,String[]> postcodes = new HashMap<>();

    @BeforeAll
    static void beforeAll() {
        postcodes.put("postcodes",new String[]{"PR3 0SG","M45 6GN","EX165BL"});
            response = RestAssured
                    .given()
                    .baseUri(url)
                    .basePath("/postcodes")
                    .body(postcodes)
                    .contentType("application/json")
                    .when()
                    .post()
                    .then()
                    .log().all()
                    .extract().response();
    }

    @Test
    @DisplayName("Assert that the status code is 200")
    void testStatusCodeIs200() {
        MatcherAssert.assertThat(response.getStatusCode(), Matchers.equalTo(200));
    }

    @Test
    @DisplayName("Server name is cloudflare")
    void testServerNameIsCloudflare() {
        MatcherAssert.assertThat(response.getHeader("server"), Matchers.equalTo("Cloudflare"));
    }

    @Test
    @DisplayName("Correct Postcodes Returned in response")
    void testCorrectPostcodesReturnedInResponse() {
        String[] expected = {"PR3 0SG","M45 6GN","EX165BL"};
        List<HashMap<String,String>> responses = response.jsonPath().getList("result");
        for(int i=0;i<responses.size();i++) {
            MatcherAssert.assertThat(responses.get(i).get("query"), Matchers.equalTo(expected[i]));


        }
    }



}
