package org.mifos.integrationtest;


import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mifos.integrationtest.common.CollectionHelper;
import org.mifos.integrationtest.common.Utils;
import org.mifos.integrationtest.common.dto.CollectionResponse;
import org.mifos.integrationtest.common.dto.operationsapp.TransactionRequest;

import java.io.DataInput;
import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CollectionVariablesTest {

    private ResponseSpecification statusOkResponseSpec;
    private RequestSpecification requestSpec;

    private String transactionId = "95baa180901aJk6jcDHB";
    private String instanceKey;

    @BeforeAll
    public void setup() {
        this.requestSpec = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        this.requestSpec.header(Utils.TENANT_PARAM_NAME, Utils.DEFAULT_TENANT);
        this.statusOkResponseSpec = new ResponseSpecBuilder().expectStatusCode(200).build();
    }

    @Test
    public void testSendCollectionRequest() throws JSONException {
        JSONObject collectionRequestBody = CollectionHelper.getCollectionRequestBody("1", "254708374149", "24450523");
        System.out.println(collectionRequestBody);
        String json = RestAssured.given(requestSpec)
                .baseUri("http://localhost:5001")
                .body(collectionRequestBody.toString())
                .expect()
                .spec(statusOkResponseSpec)
                .when()
                .post("/channel/collection")
                .andReturn().asString();
        CollectionResponse response = (new Gson()).fromJson(json, CollectionResponse.class);
        assertThat(response.getTransactionId()).isNotEmpty();
        System.out.println(response.getTransactionId());
        this.transactionId = response.getTransactionId();
    }

    @Test
    public void testGetTransactionRequestApi() throws JSONException, IOException {
        Utils.sleep(5);
        System.out.println("Getting transactionRequestObject with transactionId " + this.transactionId);
        RequestSpecification localSpec = requestSpec;
        localSpec.queryParam("transactionId", this.transactionId);
        String json = RestAssured.given(localSpec)
                .baseUri("http://localhost:5002")
                .expect()
                .spec(statusOkResponseSpec)
                .when()
                .get("/api/v1/transactionRequests")
                .andReturn().asString();

        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonObject1 = jsonObject.getJSONArray("content");
        System.out.println( jsonObject1);
        TransactionRequest transactionRequest = (new Gson()).fromJson(jsonObject1.getJSONObject(0).toString(), TransactionRequest.class);
        instanceKey = transactionRequest.getWorkflowInstanceKey();
        System.out.println("worklflow" + instanceKey);
    }

    @Test
    public void testZeebeOpsVarsApi() throws JSONException {
        Utils.sleep(10);
        System.out.println("Zeebe vars check using zeebe ops api using instance key " + instanceKey);
        RequestSpecification localSpec = requestSpec;
        String json = RestAssured.given(localSpec)
                .baseUri("http://localhost:5003")
                .expect()
                .spec(statusOkResponseSpec)
                .when()
                .get("/channel/process/variable/" + instanceKey)
                .andReturn().asString();
        System.out.println(json);
        JSONObject jsonObject = new JSONObject(json);
        assertThat(jsonObject.get("ams")).isNotNull();
    }




}
