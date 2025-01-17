package com.coin.api_automation;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class CoinDeskApiTest {

    private static final String API_ENDPOINT = "https://api.coindesk.com/v1/bpi/currentprice.json";

   @Test
    public void testBpiDetails() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(API_ENDPOINT);

        HttpResponse response = httpClient.execute(request);
        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "API request failed with non 200 status code!");

        String jsonString = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonString);

        JsonNode bpiNode = rootNode.get("bpi");
		
        Assert.assertNotNull(bpiNode, "The BPI node does not exist in the response");
		
        Assert.assertEquals(bpiNode.size(),3,"There are not 3 BPIs!");
		
        Assert.assertNotNull(bpiNode.get("USD"),"USD node does not exist");
		Assert.assertNotNull(bpiNode.get("GBP"),"GBP node does not exist");
		Assert.assertNotNull(bpiNode.get("EUR"),"EUR node does not exist");

		String gbpDescription = bpiNode.get("GBP").get("description").asText();
        Assert.assertEquals(gbpDescription,"British Pound Sterling", "GBP description is incorrect.");

        httpClient.close();
    }
}