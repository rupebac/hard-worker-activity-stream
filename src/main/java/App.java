import controller.ActController;
import auth.OAuth2Controller;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

public class App 
{

    public static void main( String[] args )
    {
	    port(8080);
	    staticFileLocation("/webapp");

		new ActController();
		new OAuth2Controller();

    }
}
