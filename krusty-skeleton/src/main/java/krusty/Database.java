package krusty;

import spark.Request;
import spark.Response;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class Database {
	/**
	 * Modify it to fit your environment and then use this string when connecting to your database!
	 */
	private static final String jdbcUsername = "root";
	private static final String jdbcPassword = "password";
	private static final String jdbcServer = "localhost";
	private static final String jdbcDatabase = "project";
	private static final String jdbcString = "jdbc:mysql://" + jdbcServer + "/" + jdbcDatabase;

	// For use with MySQL or PostgreSQL
	
	private Connection conn = null;

	private static int COOKIE_MULT = 54; // amount of cookies in a pallet


	public void connect() {
			try {
				conn = DriverManager.getConnection (jdbcString, jdbcUsername, jdbcPassword);
			}
			catch (SQLException e) {
				System.err.println(e);
				e.printStackTrace();
			}
		}


	public String getCustomers(Request req, Response res) {
		String sql = "SELECT companyName as name, address FROM Company";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet resultSet = ps.executeQuery();
			return Jsonizer.toJson(resultSet, "customers");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{}";	//If query does not succesfully return results, return placeholder
	}

	public String getRawMaterials(Request req, Response res) {
		String sql = "SELECT IngredientName as name, StoredAmount as amount, Unit as unit FROM Ingredient";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet resultSet = ps.executeQuery();
			return Jsonizer.toJson(resultSet, "raw-materials");
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
				"IngAmount AS amount FROM Quantity GROUP BY cookie,ingredient ";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet resultSet = ps.executeQuery();
			return Jsonizer.toJson(resultSet, "recipes");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{}";
	}


	public String getPallets(Request req, Response res) {

		Statement stmt;
		try{
		stmt = conn.createStatement();
		 
		
		String sql = "SELECT PalletNumber as id, ProductName as cookie, TimeOfProduction as production_date, companyName as customer, IF(Blocked, 'yes', 'no') as blocked " +	
					 "FROM Pallet " +
					 "RIGHT JOIN StoredIn " +
					 "USING (PalletNumber) " +
 					 "LEFT JOIN ShippedIn " +
					 "USING (PalletNumber) " +
					 "LEFT JOIN Orders " +
					 "USING (OrderNumber) " +
					 "WHERE PalletNumber IS NOT NULL "; 
		if(req.queryParams("from") != null ){
			sql += " AND TimeOfProduction > " + req.queryParams("from");

		}	
		if(req.queryParams("to") != null ){
			sql += " AND TimeOfProduction < " + req.queryParams("to") ;

		}
		if(req.queryParams("cookie") != null ){
			sql += " AND ProductName = '" + req.queryParams("cookie") + "'" ;

		}
		if(req.queryParams("blocked") != null ){
			
			sql += " AND Blocked = " + getBlockedArg(req.queryParams("blocked"));;

		}

	sql += ";";

	// System.out.println(sql); for troubleshooting

		try {
			ResultSet rs = stmt.executeQuery(sql);
			return Jsonizer.toJson(rs, "pallets");

			
		
		} catch (SQLException e) {
				e.printStackTrace();
				return "{\"status\": \"error\"}";
		} finally {
			try { 
				stmt.close();
			 } 	
				catch (Exception foo) { 

				}
		  

		}
	}catch(SQLException e) {
		e.printStackTrace();
	}
		
	return "{\"status\": \"Pallet fetch ok\"}";
}



	public String reset(Request req, Response res) throws IOException {
		String sqlRead = readFile("Reset.sql");
		String[] sqlSplit = sqlRead.split(Pattern.quote(";"));
		for (String sql : sqlSplit) {
			sql += ";" ;
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.executeUpdate();				
			} catch (SQLException e) {
				e.printStackTrace();
				return "{\"status\": \"error\"}";
			}
		}
		return "{\"status\": \"reset ok\"}";

	}

	public String createPallet(Request req, Response res) {
		String sql0 = 	"SELECT count(*) as inList FROM Cookie " +
						"WHERE CookieName = ? ";

		String sql =	"INSERT INTO Pallet(ProductName,TimeOfProduction,PalletLocation,Blocked) VALUES " +
						"(?,NOW(),?,0)";

		String sql2=	"SELECT IngredientName, IngAmount "+
						"FROM Quantity " +
						"WHERE CookieName = ? ";

		String sql4=	"SELECT max(PalletNumber) as lastPallet " +
						"FROM Pallet";

		String sql5 = 	"INSERT INTO StoredIN(CookieName,PalletNumber)" +
						"VALUES (?, ?)";


		String cookieName = "";
		int palletID = 0;
		//System.out.println(sql5); for troubleshooting


		if(req.queryParams("cookie") != null ){
			cookieName = req.queryParams("cookie");
		} else {
			System.out.print("-------1------------"+cookieName); //For troubleshooting
			return "{\"status\": \"error no cookieName\"}";
		}

		try(PreparedStatement ps0 = conn.prepareStatement(sql0);
			PreparedStatement ps = conn.prepareStatement(sql);
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			PreparedStatement ps4 = conn.prepareStatement(sql4);
			PreparedStatement ps5 = conn.prepareStatement(sql5)) {
			{
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);

		//	System.out.print("--------------------------"+cookieName); For troubleshooting
			ps0.setString(1, cookieName);

			ResultSet rs0 = ps0.executeQuery();
			rs0.next();
			if (rs0.getInt("inList") == 0) {
				conn.rollback();
                conn.setAutoCommit(true);
				return "{\"status\": \"error cookieName doesn't exist\"}";
			}

			ps.setString(1, cookieName); // ProductName
			ps.setString(2, "TestLocation"); // PalletLocation
			
			ps.executeUpdate();

			ps2.setString(1, cookieName);
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()){
				if (!updateIngredient(rs2.getString("IngredientName"), rs2.getInt("IngAmount"))) {
					conn.rollback();
                	conn.setAutoCommit(true);
					return "{\"status\": \"error could not update ingredient\"}";
				}
				
			}

			ResultSet rs4 = ps4.executeQuery();
			while(rs4.next()){
				palletID = rs4.getInt("lastPallet");
			}
			ps5.setString(1, cookieName);
			ps5.setInt(2, palletID); 
			ps5.executeUpdate();

			conn.commit();
            conn.setAutoCommit(true);
			
		}

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

	

	

	public boolean updateIngredient(String name,int amount){
		String sql = 	"UPDATE Ingredient SET " + "StoredAmount = StoredAmount - ? "+
						"WHERE IngredientName = ?";

		try(PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setInt(1, amount*COOKIE_MULT);
			ps.setString(2, name);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected String readFile(String file) {
		try {
			String path = "krusty-skeleton\\src\\main\\java\\krusty\\" + file;
			return new String(Files.readAllBytes(Paths.get(path)));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return "";
	}
	// Repurposed method from krustytests


protected String getBlockedArg(String val){
	
	switch(val){
		case "yes":
		return "1";

		case "no":
		return "0";

		default:
		return null;
	}
	
}

}
