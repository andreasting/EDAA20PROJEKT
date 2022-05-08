package krusty;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.skyscreamer.jsonassert.JSONAssert;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class KrustyTests {
	public static final String BASE_URL = "http://localhost:" + ServerMain.PORT + ServerMain.API_ENTRYPOINT + "/";
	
	/**
	 *
	 * Test cases
	 *
	 * Note that they are ordered in alphabetical order,
	 * this because one test case creates pallets that other use.
	 *
	 */
	@Test
	public void test01Customers() throws JSONException {
		String expected = readFile("ExpectedCustomers.json");
		String actual = getURL("customers");
		JSONAssert.assertEquals(expected, actual, false);
	}
	
	@Test
	public void test02Cookies() throws JSONException {
		String expected = readFile("ExpectedCookies.json");
		String actual = getURL("cookies");
		JSONAssert.assertEquals(expected, actual, false);
	}

	@Test
	public void test03RawMaterials() throws JSONException {
		String expected = readFile("ExpectedRawMaterialsStart.json");
		String actual = getURL("raw-materials");
		JSONAssert.assertEquals(expected, actual, false);
	}

	@Test
	public void test04CreatePallets() throws JSONException {
		createPallet("Nut ring");
		createPallet("Nut ring");
		createPallet("Tango");
		createPallet("Amneris");
		createPallet("Amneris");
		createPallet("Amneris");
		createPallet("Berliner");
		
		String expected = readFile("ExpectedRawMaterialsAfterCreatingPallets.json");
		String actual = getURL("raw-materials");

		JSONAssert.assertEquals(expected, actual, false);
	}
	

	@Test
	public void test05Pallets() throws JSONException {
		String expected = readFile("ExpectedPallets.json");
		String actual = getURL("pallets");
		JSONAssert.assertEquals(expected, actual, false);
	}
	
	@Test
	public void test06PalletsByCookie() throws JSONException, UnirestException {
		String expected = readFile("ExpectedPalletsByCookie.json");
		String actual = Unirest.get(BASE_URL + "pallets")
				.queryString("cookie",  "Nut ring")
				.asString().getBody();
		JSONAssert.assertEquals(expected, actual, false);
	}
	
	@Test
	public void test07PalletsByCookieAndDate() throws JSONException, UnirestException {
		String expected = readFile("ExpectedPalletsByCookie.json");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String today = formatter.format(new Date());
		
		String actual = Unirest.get(BASE_URL + "pallets")
				.queryString("cookie",  "Nut ring")
				.queryString("from", today)
				.asString().getBody();
		
		JSONAssert.assertEquals(expected, actual, false);
	}
	
	@Test
	public void test08PalletsByCookieAndDate2() throws JSONException, UnirestException {
		String expected = readFile("ExpectedPalletsEmpty.json");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
		String nextYear = formatter.format(calendar.getTime());
		
		String actual = Unirest.get(BASE_URL + "pallets")
				.queryString("cookie",  "Nut ring")
				.queryString("from", nextYear)
				.asString().getBody();
		
		JSONAssert.assertEquals(expected, actual, false);
	}
	
	/**
	 *
	 * Auxiliary methods
	 *
	 */
	protected String readFile(String file) {
		try {
			String path = "src/test/resources/" + file;
			return new String(Files.readAllBytes(Paths.get(path)));
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		return "";
	}
	
	protected String getURL(String url) {
		try {
			HttpResponse<String> res = Unirest.get(BASE_URL + url).asString();
			return res.getBody();
		} catch (UnirestException e) {
			fail("Connection failed.\n" + e.getMessage());
		}
		return "";
	}
	
	protected String createPallet(String cookie) {
		try {
			HttpResponse<String> res =  Unirest.post(BASE_URL + "pallets")
					.queryString("cookie",  cookie)
					.asString();
			return res.getBody();
		} catch (UnirestException e) {
			fail("Connection failed.\n" + e.getMessage());
		}
		return "";
	}
	
	

	/**
	 *
	 * Automatically start REST server if it is not running and reset database.
	 *
	 */
	private static ServerMain server;
	
	@BeforeClass
	public static void startServer() throws InterruptedException {
		try {
			// Check if rest server is running
			Unirest.get(BASE_URL).asString();
		} catch (UnirestException e) {
			// Start REST server and sleep a bit before start running test cases
			server = new ServerMain();
			server.startServer();
			Thread.sleep(250);
		}
		
		// Reset database
		try {
			Unirest.post(BASE_URL + "reset").asString();
		} catch (UnirestException e2) {
			fail(e2.getMessage());
		}
	}

	@AfterClass
	public static void stopServer() {
		if (server != null) {
			server.stopServer();
		}
	}
}
