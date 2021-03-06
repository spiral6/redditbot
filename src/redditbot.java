import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.managers.AccountManager;
import net.dean.jraw.managers.AccountManager.SubmissionBuilder;
import net.dean.jraw.models.Comment;
import net.dean.jraw.models.CommentNode;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.Sorting;
import net.dean.jraw.paginators.SubredditPaginator;
import net.dean.jraw.paginators.TimePeriod;

public class redditbot {

	static RedditClient redditClient;
	static AccountManager am;
	
	public static void main(String[] args) throws NetworkException, ApiException, IOException {
		initialize();
		System.out.println(redditClient.me().toString());
		bapcsales();
		//testPost();
		//MessageHandler();
	}
	
	static void testPost(){
		SubmissionBuilder sb = new SubmissionBuilder("lolbotest", "justnashmanthings", "WE ARE SENTIENT, FEAR US");
		try{
			am.submit(sb);
			//ManagerAggregation managers = ManagerAggregation.newInstance(redditClient);
			//managers.account().submit(sb);
		}catch(ApiException e){
			e.printStackTrace();
		}
	}
	
	static void MessageHandler() throws NetworkException, ApiException, IOException{
		//redditClient.getSubreddit("justnashmanthings");
		SubredditPaginator pagesOfSubreddit = new SubredditPaginator(redditClient, "justnashmanthings");
		// Adjust the request parameters
		pagesOfSubreddit.setLimit(50);                    // Default is 25 (Paginator.DEFAULT_LIMIT)
		pagesOfSubreddit.setTimePeriod(TimePeriod.DAY); // Default is DAY (Paginator.DEFAULT_TIME_PERIOD)
		pagesOfSubreddit.setSorting(Sorting.NEW);         // Default is HOT (Paginator.DEFAULT_SORTING)
		// This Paginator is now set up to retrieve the highest-scoring links submitted within the past
		// month, 50 at a time

		// Since Paginator implements Iterator, you can use it just how you would expect to, using next() and hasNext()
		Listing<Submission> submissions = pagesOfSubreddit.next(); 
		for (Submission s : submissions) {
		    // Print some basic stats about the posts
			//s.getComments().loadFully(redditClient);
			if(s.getTitle().equals("WE ARE SENTIENT, FEAR US")){
				botReply(s);
				//because of the load on a popular sub, might want to consider threads.
			}
		    System.out.printf("[/r/%s - %s karma - %s comments] %s\n", s.getSubredditName(), s.getScore(), s.getCommentCount(), s.getTitle());
		}
	}
	
	static void bapcsales() throws NetworkException, ApiException, IOException{
		SubredditPaginator pagesOfSubreddit = new SubredditPaginator(redditClient, "buildapcsales");
		pagesOfSubreddit.setLimit(12);                    // Default is 25 (Paginator.DEFAULT_LIMIT)
		pagesOfSubreddit.setTimePeriod(TimePeriod.DAY); // Default is DAY (Paginator.DEFAULT_TIME_PERIOD)
		pagesOfSubreddit.setSorting(Sorting.NEW);         // Default is HOT (Paginator.DEFAULT_SORTING)
		Listing<Submission> submissions = pagesOfSubreddit.next(); 
		for (Submission s : submissions) {
			System.out.println(s.getTitle());
			String price = priceDetermination(s.getTitle());
			String product = productDetermination(s.getTitle());
			System.out.println();
			//databasechecker(String price, String product);
		}
	}
	
	static void viablePart(){
		//TODO determine if price is good using json database (or something else)
		//if it is, send notification
		//TODO make a post request to https://maker.ifttt.com/trigger/buildapcsales/with/key/b-LGh8gsWoadO0qI26hFEp
		//containing parameters redditURL, price and product
	}
	
	static String priceDetermination(String s){
		String test = s;
		
		Pattern originalprice = Pattern.compile("- ((\\$\\d{2,})(\\.\\d{2,})?)");
		Matcher m = originalprice.matcher(test);
		boolean foundogprice = false;
		if(m.find()){
			System.out.println("Price: " + m.group(1));
			foundogprice = true;
			return m.group(1);
		}
		else{
			System.out.println("Unable to determine actual price, will use the first price.");
		}
		
		Pattern prices = Pattern.compile("((\\$\\d{2,})(\\.\\d{2,})?)");
		m = prices.matcher(test);
		String price = "";
		while(m.find()){
			//System.out.print(m.group(2));
			price+=m.group(2);
			if(m.group(3) == null){
				System.out.print(", ");
				price+=", ";
			}
			else{
				//System.out.print(m.group(3) + ", ");
				price+=m.group(3) + ", ";
			}
		}
		//System.out.print("\b");
		//System.out.println();
		price = price.substring(0, price.indexOf(","));
		price.trim();
		System.out.println("Price: " + price);
		return price;
		
	}
	
	static String productDetermination(String s){
		String test = s;
		
		Pattern flair = Pattern.compile("(\\[GPU\\]|\\[gpu\\])");
		Matcher m = flair.matcher(test);
		if(m.find()){
			String manufacturer = "";
			String product = "";
			Pattern manufacturerpattern = Pattern.compile("(EVGA|MSI|ASUS|Gigabyte|Powercolor|Zotac|Sapphire|XFX|PNY)", Pattern.CASE_INSENSITIVE);
			m = manufacturerpattern.matcher(test);
			if(m.find()){
				manufacturer = m.group(0);
			}
			
			Pattern productpattern = Pattern.compile("(((GTX)?(R9?7?)?)? (9\\d\\d|3\\d\\d|2\\d\\d|7\\d\\d)X? ?(Ti)?)", Pattern.CASE_INSENSITIVE);
			m = productpattern.matcher(test);
			if(m.find()){
				product = m.group(0);
			}
			manufacturer.trim();
			product.trim();
			
			System.out.println(manufacturer + " - " + product);
			System.out.println("It's a GPU.");
		}
		else{
			System.out.println("Not a GPU.");
		}
		
		//return product;
		return null;
		
	}
	
	static void botReply(Submission s) throws NetworkException, ApiException, IOException{
		File AR = new File("src/replied.dat");
		if(!(AR.exists())){
			AR.createNewFile();
			try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(AR, true)))) {
    			out.println("Hello Database of comments!");
    			System.out.println("File doesn't exist, creating.");
    		}catch (IOException e) {
    			//exception handling left as an exercise for the reader
    		}
		}
		Scanner checker;
		Submission requestedSubmission = redditClient.getSubmission(s.getId());
		List<Comment> allComments = getAllComments(requestedSubmission);
		
	    for (Comment c: allComments) {
	        boolean itsHere = false;
	        checker = new Scanner(AR);
	        while(checker.hasNextLine()){
        		String line = checker.nextLine();
        		System.out.println(c.getId());
        		if(c.getId().equals(line)){
        			itsHere = true;
        			break;
        		}
        	}
        	if(itsHere){
        		System.out.println("Comment exists in database.");
        	}
        	else if(c.getBody().contains("scrub")){
	        	if(c.getAuthor().equals("spiral6bot")){
	        		//don't reply to yourself, stupid.
	        	}
	        	else{
	        		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(AR, true)))) {
	        			out.println("\n" + c.getId());
	        			System.out.println("Adding id to replied.dat");
	        			am.reply(c, "lol ur a scrub");
	        		}catch (IOException e) {
	        			//exception handling left as an exercise for the reader
	        		}
	        	}	
	        }
	    }
	}
	
	static void initialize() throws NetworkException, OAuthException, FileNotFoundException{
		UserAgent myUserAgent = UserAgent.of("desktop", "com.spiral6.redditbot", "v0.1", "spiral6bot");
		redditClient = new RedditClient(myUserAgent);
		Credentials credentials = setCredentials();
		OAuthData authData = redditClient.getOAuthHelper().easyAuth(credentials);
		redditClient.authenticate(authData);
		redditClient.me();
		am = new AccountManager(redditClient);
	}
	
	static Credentials setCredentials() throws FileNotFoundException{
		Scanner cred = new Scanner(new File("src/credentials.dat"));
		String line = cred.nextLine();
		String username = line.substring(line.indexOf(":")+1);
		line = cred.nextLine();
		String password = line.substring(line.indexOf(":")+1);
		line = cred.nextLine();
		String ID = line.substring(line.indexOf(":")+1);
		line = cred.nextLine();
		String Secret = line.substring(line.indexOf(":")+1);
		Credentials credentials = Credentials.script(username, password, ID, Secret);
		cred.close();
		return credentials;
	}

	public static List<Comment> getAllComments(Submission submission) { //https://github.com/thatJavaNerd/JRAW/issues/91
	    List<Comment> comments = new ArrayList<>();
	    for (CommentNode node : submission.getComments()) {
	      comments.addAll(getComments(node));
	    }
	    return comments;
	  }

	public static List<Comment> getComments(CommentNode node) {
	    List<Comment> comments = new ArrayList<>();
	    if (node != null) {
	      comments.add(node.getComment());
	      // add child comments
	      List<CommentNode> children = node.getChildren();
	      if (children != null) {
	        for (CommentNode child : children) {
	          comments.addAll(getComments(child));
	        }
	      }
	      // load any hidden comments
	      if (node.getMoreChildren() != null) {
	        List<CommentNode> moreComments = node.loadMoreComments(redditClient);
	        for (CommentNode commentNode : moreComments) {
	          List<Comment> comments1 = getComments(commentNode);
	          comments.addAll(comments1);
	        }
	      }
	    }

	    return comments;
	  }
}
