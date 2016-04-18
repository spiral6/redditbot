package comments;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class redditComment {

	private String body;
	private String link;
	
    public Object getlink() {
        return link;
    }

    public void setlink(String link) {
        this.link = link;
    }
    
    public Object getbody() {
        return body;
    }

    public void setbody(String body) {
        this.body = body;
    }
    
	public String toString(){
		return "Body: " + body + "\n" + "Link: " + link; 
	}

}
