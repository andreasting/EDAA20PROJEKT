package krusty;

import java.io.IOError;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static spark.Spark.*;

public class ServerMain {
	public static int PORT = 8888;
	public static String API_ENTRYPOINT = "/api/v1";
	
	private Database db;
	
	public void startServer() {
		staticFiles.location("/public");

		db = new Database();
		db.connect();

		port(PORT);
		
		enableCORS();

		initIndex();
		initRoutes();
	}

	private void initIndex() {
		try {
			byte[] indexData = getClass().getResource("/public/index.html").openStream().readAllBytes();
			final String index = new String(indexData, StandardCharsets.UTF_8);

			get("/", (req, res) -> index);
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	private void initRoutes() {
		get(API_ENTRYPOINT + "/customers", (req, res) -> db.getCustomers(req, res));
		get(API_ENTRYPOINT + "/raw-materials", (req, res) -> db.getRawMaterials(req, res));
		get(API_ENTRYPOINT + "/cookies", (req, res) -> db.getCookies(req, res));
		get(API_ENTRYPOINT + "/recipes", (req, res) -> db.getRecipes(req, res));
		get(API_ENTRYPOINT + "/pallets", (req, res) -> db.getPallets(req, res));
		
		post(API_ENTRYPOINT + "/reset", (req, res) -> db.reset(req, res));
		post(API_ENTRYPOINT + "/pallets", (req, res) -> db.createPallet(req, res));
	}
	
	public void stopServer() {
		stop();
	}
	
	/**
	 * Setup CORS, see:
	 * - https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
	 * - http://sparkjava.com/tutorials/cors
	 */
	private void enableCORS() {
	    options("/*", (request, response) -> {
	        String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
	        if (accessControlRequestHeaders != null) {
	            response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
	        }
	        String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
	        if (accessControlRequestMethod != null) {
	            response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
	        }
	        return "OK";
	    });

	    before((request, response) -> {
	        response.header("Access-Control-Allow-Origin", "*");
	        response.header("Access-Control-Allow-Headers", "Content-Type, Accept");
	        response.type("application/json");
	    });
	}

	public static void main(String[] args) throws InterruptedException {
		new ServerMain().startServer();
	}
}