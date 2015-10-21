package todoapp;

import static spark.Spark.ipAddress;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.todoapp.resources.TodoResource;
import com.todoapp.service.TodoService;

public class Bootstrap {
	private static final String IP_ADDRESS = "localhost";
	private static final int PORT = 8080;

	public static void main(String[] args) throws Exception {

		ipAddress(IP_ADDRESS);
		port(PORT);
		staticFileLocation("/public");
		new TodoResource(new TodoService(mongo()));
	}

	private static MongoDatabase mongo() {
		return new MongoClient("localhost").getDatabase("todoapp");
	}
}
