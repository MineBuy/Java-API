package xyz.minebuy.MineBuyAPI.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import xyz.minebuy.MineBuyAPI.MineBuyExpection;

public class MineBuyRequestBuilder {
	
	private RequestType type;
	private URL url;
	private String action;
	private String typeString;
	
	public MineBuyRequestBuilder setRequestType(RequestType type) {
		this.type = type;
		return this;
	}
	
	public MineBuyRequestBuilder setURL(URL url) {
		this.url = url;
		return this;
	}
	
	public MineBuyRequestBuilder setAction(String action) {
		this.action = action;
		return this;
	}
	
	public MineBuyRequestBuilder setTypeString(String typeString) {
		this.typeString = typeString;
		return this;
	}
	
	// Basically "Send"
	public MineBuyRequest request() {
		if (url == null) {
			throw new IllegalArgumentException("URL must not be null");
		}
		
		if(action == null) throw new IllegalArgumentException("Action cannot be null");
		else if(action.equals("")) throw new IllegalArgumentException("Action cannot be blank");
			
		if (!type.equals(RequestType.GET)) {
			if(typeString == null) throw new IllegalArgumentException("Type String cannot be null");
			else if(typeString.equals("")) throw new IllegalArgumentException("Type String cannot be blank");
		}
		
		try {
			url = new URL(appendCharacter(url.toString(), '/') + action);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("URL or action is malformed: " + e.getMessage());
		}
		
		switch(type) {
			case POST: return sendPostRequest();
			case GET: return sendGetRequest();
			case DELETE: return sendDeleteRequest();
			case PUT: return sendPutRequest();
			default: throw new IllegalArgumentException("Type Cannot be Null!");
		}
	}
	
	private MineBuyRequest sendPostRequest() {
		Exception exception;
		JsonObject response;

		if (url.toString().startsWith("https://")) {

			try {
				HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Length", Integer.toString(typeString.length()));
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setDoOutput(true);
				connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

				// Initialize output stream
				DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

				// Write request
				outputStream.writeBytes(typeString);

				// Initialize input stream
				InputStream inputStream = connection.getInputStream();

				// Handle response
				BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				StringBuilder responseBuilder = new StringBuilder();

				String responseString;
				while ((responseString = streamReader.readLine()) != null)
					responseBuilder.append(responseString);

				JsonParser parser = new JsonParser();

				response = parser.parse(responseBuilder.toString()).getAsJsonObject();

				if (response.has("error")) {
					// Error with request
					String errorMessage = response.get("message").getAsString();
					exception = new MineBuyExpection(errorMessage);
				}

				// Close output/input stream
				outputStream.flush();
				outputStream.close();
				inputStream.close();

				// Disconnect
				connection.disconnect();

				exception = null;
			} catch (Exception e) {
				exception = e;
				response = null;
			}
		} else {

			try {
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Length", Integer.toString(typeString.length()));
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setDoOutput(true);
				connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

				// Initialize output stream
				DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

				// Write request
				outputStream.writeBytes(typeString);

				// Initialize input stream
				InputStream inputStream = connection.getInputStream();

				// Handle response
				BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				StringBuilder responseBuilder = new StringBuilder();

				String responseString;
				while ((responseString = streamReader.readLine()) != null)
					responseBuilder.append(responseString);

				JsonParser parser = new JsonParser();

				response = parser.parse(responseBuilder.toString()).getAsJsonObject();

				if (response.has("error")) {
					// Error with request
					String errorMessage = response.get("message").getAsString();
					exception = new MineBuyExpection(errorMessage);
				}

				// Close output/input stream
				outputStream.flush();
				outputStream.close();
				inputStream.close();

				// Disconnect
				connection.disconnect();

				exception = null;
			} catch (Exception e) {
				exception = e;
				response = null;
			}
		}

		return new MineBuyRequest(exception, response, type);
	}
	
	private MineBuyRequest sendGetRequest() {
		// *TODO*
		return null;
	}
	
	private MineBuyRequest sendPutRequest() {
		// *TODO*
		return null;
	}
	
	private MineBuyRequest sendDeleteRequest() {
		// *TODO*
		return null;
	}
	
	private String appendCharacter(String string, char c) {
		if (string.endsWith(c + "")) {
			return string;
		} else {
			return string + c;
		}
	}
	
	public static class MineBuyRequest {
		
		private Exception exception;
		private JsonObject response;
		private RequestType type;
		
		public MineBuyRequest(Exception exception, JsonObject response, RequestType type) {
			this.exception = exception;
			this.response = response;
			this.type = type;
		}
		
		public Exception getException() {
			return exception;
		}
		
		public boolean hasSucceeded() {
			return exception == null;
		}
		
		public JsonObject getResponse() {
			return response;
		}
		
		public RequestType getType() {
			return type;
		}
		
	}
	
	public enum RequestType{
		POST,
		GET,
		PUT,
		DELETE;
	}

}