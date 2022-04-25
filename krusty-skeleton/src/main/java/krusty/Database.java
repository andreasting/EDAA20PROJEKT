package krusty;

import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import static krusty.Jsonizer.toJson;

public class Database {
	/**
	 * Modify it to fit your environment and then use this string when connecting to your database!
	 */
	private static final String jdbcString = "jdbc:mysql://localhost/krusty";

	// For use with MySQL or PostgreSQL
	private static final String jdbcUsername = "";
	private static final String jdbcPassword = "";
	private Connection conn = null;


	public void connect() {
			try {
				conn = DriverManager.getConnection (jdbcString, jdbcUsername, jdbcPassword);
			}
			catch (SQLException e) {
				System.err.println(e);
				e.printStackTrace();
			}
		}

	// TODO: Implement and change output in all methods below!

	public String getCustomers(Request req, Response res) {

		try (Statement statement = conn.createStatement()) {
			String sql = "SELECT * FROM Company";
			ResultSet resultSet = statement.executeQuery(sql);
			return JSONizer.toJSON(resultSet, "Company");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{}";	//If query does not succesfully return results, return placeholder
	}

	public String getRawMaterials(Request req, Response res) {
		return "{}";
	}

	public String getCookies(Request req, Response res) {
		return "{\"cookies\":[]}";
	}

	public String getRecipes(Request req, Response res) {
		return "{}";
	}

	public String getPallets(Request req, Response res) {
		return "{\"pallets\":[]}";
	}

	public String reset(Request req, Response res) {
		return "{}";
	}

	public String createPallet(Request req, Response res) {
		return "{}";
	}
}
