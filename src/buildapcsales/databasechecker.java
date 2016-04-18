package buildapcsales;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import comments.redditComment;

public class databasechecker {

	static String[] linkandcomment = new String[2];

	public static void main(String[] args) throws JsonParseException, MalformedURLException, IOException {
		File file = new File("src/gpudb.json");

		ArrayList<Component> components = new ArrayList<Component>();

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode node = objectMapper.readValue(file, JsonNode.class);
		JsonNode array = node.get("products");
		for(JsonNode product:array){
			Component c = new Component();
			c.setmanufacturer(product.get("manufacturer").textValue());
			c.setprice(product.get("price").textValue());
			c.setmodel(product.get("model").textValue());
			components.add(c);
		}
		for(Component c: components){
			System.out.println(c + "\n");
		}
		//TODO create a sort and search? for finding the part in the database that has the corrent model and manufacturer
	}

	public boolean comparison(String manufacturer, String model, String price){
		//TODO Create a comparison between database and parsed product from reddit, and return boolean.
		//Component parsed = new Component(manufacturer, model, price);
		return false;
	}
}
