package krusty;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOError;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Auxiliary class for automatically translating a ResultSet to JSON
 */
public class Jsonizer {
	private static final ObjectMapper mapper = new ObjectMapper();

	static {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mapper.setDateFormat(df);
	}

	/**
	 * Convert any POJO or plain Java data to JSON supported by Jackson
	 *
	 * @param name name of returned data
	 * @return {name: jackson serialized json representation}
	 */
	public static String anythingToJson(Object data, String name) {
		try {
			Map<String, Object> entries = new HashMap<>();
			entries.put(name, data);
			return mapper.writeValueAsString(entries);
		} catch (JsonProcessingException e) {
			throw new IOError(e);
		}
	}

	/**
	 * Convert JDBC Result to JSON
	 *
	 * @param rs   open and unused ResultSet
	 * @param name name of the resultset
	 * @return JSON object with one entry: {name: result of ResultSet}
	 * @throws SQLException
	 */
	public static String toJson(ResultSet rs, String name) throws SQLException {
		try {
			ResultSetMetaData meta = rs.getMetaData();

			StringWriter sw = new StringWriter();
			JsonGenerator writer = mapper.getFactory().createGenerator(sw);

			writer.writeStartObject();
			writer.writeFieldName(name);
			writer.writeStartArray();
			while (rs.next()) {
				writer.writeStartObject();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					writer.writeFieldName(meta.getColumnLabel(i));
					writer.writeObject(rs.getObject(i));
				}
				writer.writeEndObject();
			}
			writer.writeEndArray();
			writer.writeEndObject();
			writer.flush();
			return sw.toString();
		} catch (IOException e) {
			throw new IOError(e);
		}
	}
}
