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
import java.util.ArrayList;
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
		String sql = "SELECT PalletNumber, CookieName, TimeOfProduction, companyName, Blocked " +
					 "FROM StoredIn " +
					 "JOIN Pallet " +
					 "USING (PalletNumber) " +
 					 "JOIN ShippedIn " +
					 "USING (PalletNumber) " +
					 "JOIN Orders " +
					 "USING (OrderNumber) " +
					 "ORDER BY TimeOfProduction DESC ";

		ArrayList<String> value = new ArrayList<String>();
		if(req.queryParams("from") != null ){
			sql += "WHERE TimeOfProduction > ? ";
			value.add(req.queryParams("from"));
		}	
		if(req.queryParams("to") != null ){
			sql += "AND TimeOfProduction < ? ";
			value.add(req.queryParams("to"));
		}
		if(req.queryParams("cookie") != null ){
			sql += "AND CookieName = ? ";
			value.add(req.queryParams("cookie"));
		}
		if(req.queryParams("Blocked") != null ){
			int s;
			if(req.queryParams("Blocked") == "yes"){
				s = 1;
			}else{
				s = 0;
			}
			sql += "AND Blocked = ? ";
			value.add(req.queryParams("Blocked"));
		}

		try (PreparedStatement stmt = conn.prepareStatement(sql)) { 
			for (int i = 0; i < value.size(); i++) { 
			  stmt.setString(i+1, value.get(i)); 
			} 
			 
		  } catch (SQLException e) { 
			e.printStackTrace();
		  } 
		

		return "{\"pallets\":[]}";
	}

	/*TODO: Fix syntax, properly utilize the Reset.sql file (or redo?)
	*/

	public String reset(Request req, Response res) throws IOException {
		String sql;
		String line;
		String currentDirectory = new File("").getAbsolutePath();	//Gets the current directory on the users's computer
		StringBuilder sb = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new FileReader
				(currentDirectory + "\\Reset.sql"))) {

			while((line = br.readLine()) !=null)
			{
				sb.append(line);
			}
		}
		catch(FileNotFoundException err){
			System.out.println(err);
		}

		sql = sb.toString();		// Assigns the string builder's contents to a string
		sb.setLength(0);			//Empty the string builder

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.executeUpdate(sql, ps.RETURN_GENERATED_KEYS);
			ResultSet resultSet = ps.getGeneratedKeys();
			return "Status OK";
/*
Executes the update,  clears and returns the auto-incremented keys
 */
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "{\"status\": \"ok\"}"; //For this endpoint, return the following JSON object: "status": "ok"
	}

	public String createPallet(Request req, Response res) {
		String sql =	"INSERT INTO Pallet(ProductName,TimeOfProduction,PalletLocation,Blocked) VALUES " +
						"(?,NOW(),?,0)";
		
		String sql2=	"select max(PalletNumber) as lastPallet " +
						"from Pallet";

		String cookieName = "";
		int palletID = 0;

		if(req.queryParams("cookie") != null ){
			cookieName = req.queryParams("CookieName");
		}

		try(PreparedStatement ps = conn.prepareStatement(sql);
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			){
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);

			ps.setString(1, cookieName); // ProductName
			ps.setString(2, "TestLocation"); // PalletLocation
			
			ps.executeQuery();

			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()){
				palletID = rs2.getInt("lastPallet");
			}

			conn.commit();
            conn.setAutoCommit(true);

		} catch(SQLException e) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                e.printStackTrace();
            } catch(SQLException e1) {
                e1.printStackTrace();
            }
        }
		
		return "{\"status\": \"ok\",\n\"id\": " + palletID + "}";
	}
}
