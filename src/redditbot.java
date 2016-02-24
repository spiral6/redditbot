import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.fluent.ManagerAggregation;
import net.dean.jraw.fluent.SubredditReference;
import net.dean.jraw.http.HttpRequest;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.managers.AccountManager;
import net.dean.jraw.managers.AccountManager.SubmissionBuilder;

public class redditbot {

	
	
	public static void main(String[] args) throws FileNotFoundException, NetworkException, OAuthException {
		UserAgent myUserAgent = UserAgent.of("desktop", "com.spiral6.redditbot", "v0.1", "spiral6bot");
		RedditClient redditClient = new RedditClient(myUserAgent);
		Credentials credentials = setCredentials();
		OAuthData authData = redditClient.getOAuthHelper().easyAuth(credentials);
		redditClient.authenticate(authData);
		redditClient.me();
		System.out.println(redditClient.me().toString());
		SubmissionBuilder sb = new SubmissionBuilder("lolbotest", "justnashmanthings", "WE ARE SENTIENT, FEAR US");
		try{
			ManagerAggregation managers = ManagerAggregation.newInstance(redditClient);
			managers.account().submit(sb);
		}catch(ApiException e){
			e.printStackTrace();
		}
		
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
		return credentials;
		
		
	}

}
