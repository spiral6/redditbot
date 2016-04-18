package comments;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class redditCommentCreator {

	public static void main(String[] args) throws IOException {
		ArrayList<redditComment> comments = new ArrayList<redditComment>();
		
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        JsonNode node = mapper.readValue(new URL("https://api.pushshift.io/reddit/search/comment?q=yang&subreddit=rwby&limit=3"), JsonNode.class);
        JsonNode array = node.get("data");
        for(JsonNode comment:array){
        	redditComment rc = new redditComment();
        	rc.setbody(comment.get("body").textValue());
        	rc.setlink(comment.get("link_permalink").textValue());
        	comments.add(rc);
        }

        for(redditComment comment:comments){
        	System.out.println(comment);
        	System.out.println("-------------------");
        }
        
	}

}
