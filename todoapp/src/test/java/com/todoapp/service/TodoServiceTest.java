package com.todoapp.service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.todoapp.model.Todo;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongoCmdOptionsBuilder;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;

import static org.assertj.core.api.Assertions.*;

/**
 * Classe de test du service.
 * @author JCZOBEIDE
 *
 */
public class TodoServiceTest {
	/**
	 * Instance du process mongod
	 */
	private static MongodProcess mongodProcess;
	/**
	 * Instance de l'exécutable
	 */
	private static MongodExecutable mongodExecutable;
	private static final String IP = "127.0.0.1";
	private static final int PORT = 27018;
	private static final String DB_NAME = "todoapp";
	private static TodoService todoService;
	private static MongoClient mongoClient;
	private static MongoCollection<Document> collection;

	/**
	 * Initialise l'exécutable et le process mongod avant l'exécution des tests
	 * 
	 * @throws IOException
	 */
	@BeforeClass
	public static void beforeClass() throws IOException {
		MongodStarter starter = MongodStarter.getDefaultInstance();
		IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION).net(new Net(IP, PORT, false))
				.cmdOptions(new MongoCmdOptionsBuilder().useSmallFiles(true).useNoJournal(true).useNoPrealloc(true).build()).build();
		System.out.println("Récupération de l'exécutable");
		mongodExecutable = starter.prepare(mongodConfig);
		System.out.println("Lancement à la volée du serveur MongoDB");
		mongodProcess = mongodExecutable.start();
		System.out.println("Serveur MongoDB lancé avec succès");
		mongoClient = new MongoClient(IP, PORT);
		MongoDatabase database = mongoClient.getDatabase("todoapp");
		todoService = new TodoService(database);
		collection = database.getCollection("todos");
		collection.insertOne(new Document("title", "test").append("done", true).append("createdOn", new Date()));
	}

	/**
	 * Stop l'exécutable et le process mongod après l'exécution des tests
	 * 
	 * @throws UnknownHostException
	 */
	@AfterClass
	public static void destroyMongodInstance() throws UnknownHostException {

		System.out.println("Fin du test. Suppression de la base " + DB_NAME);
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		db.drop();
		mongoClient.close();
		System.out.println("Base " + DB_NAME + " supprimée avec succès");
		System.out.println("Arrêt du serveur MongoDB");
		if (mongodProcess != null) {
			mongodProcess.stop();
		}
		System.out.println("Arrêt de l'exécutable");
		if (mongodExecutable != null) {
			mongodExecutable.stop();
		}
		System.out.println("Fin des tests");
	}

	@Test
	public void findAllShouldBeNotEmpty() {
		assertThat(todoService.findAll()).isNotEmpty();
	}

	@Test
	public void createNewTodo() {
		todoService.createNewTodo("{title:test6, done: false}");
		assertThat(todoService.findAll()).hasSize(2);
	}

	@Test
	public void update() {
		Todo todo = new Todo(collection.find(new Document().append("title", "test")).first());
		todoService.update(todo.getId(), "{title:testUpdate}");
		assertThat(collection.find(new Document().append("title", "testUpdate"))).hasSize(1);
	}
}
