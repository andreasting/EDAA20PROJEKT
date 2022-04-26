package krusty;

import spark.Request;
import spark.Response;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

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
		String sql = "SELECT companyName as name, address FROM Company";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet resultSet = ps.executeQuery();
			return Jsonizer.toJson(resultSet, "companies");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{}";	//If query does not succesfully return results, return placeholder
	}

	public String getRawMaterials(Request req, Response res) {
		String sql = "SELECT IngredientName as name, StoredAmount as amount, Unit as unit FROM Ingredient";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet resultSet = ps.executeQuery();
			return Jsonizer.toJson(resultSet, "ingredients");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{}";    //If query does not succesfully return results, return placeholder
	}

	public String getCookies(Request req, Response res) {
		String sql = "SELECT CookieName as name FROM Cookie";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet resultSet = ps.executeQuery();
			return Jsonizer.toJson(resultSet, "cookies");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{}";
	}

	public String getRecipes(Request req, Response res) {
		String sql = "SELECT CookieName AS cookie, IngredientName AS ingredient," +
				"Quantity AS amount, FROM Quantity GROUP BY cookie,ingredient ";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet resultSet = ps.executeQuery();
			return Jsonizer.toJson(resultSet, "cookies");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{}";
	}

	public String getPallets(Request req, Response res) {
		

		return "{\"pallets\":[]}";
	}

	/*TODO: Fix syntax, properly utilize the Reset.sql file (or redo?)
	*/

	public String reset(Request req, Response res) throws IOException {
		String sql;
		String currentDirectory = new File("").getAbsolutePath();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(currentDirectory + "\\Reset.sql"));
		while(br!=null){
			sb.append(br.readLine());
		}
		sql = sb.toString();
		sb.setLength(0);
		br.close();
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.executeUpdate(sql, ps.RETURN_GENERATED_KEYS);
			ResultSet resultSet = ps.getGeneratedKeys();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{}";
	}

	public String createPallet(Request req, Response res) {
		return "{}";
	}
}
