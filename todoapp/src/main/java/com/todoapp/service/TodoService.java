package com.todoapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.todoapp.model.Todo;

/**
 * Service
 * @author JCZOBEIDE
 *
 */
public class TodoService {
	private final MongoDatabase db;
	private final MongoCollection<Document> collection;

	public TodoService(MongoDatabase db) {
		this.db = db;
		this.collection = this.db.getCollection("todos");
	}

	/**
	 * Renvoie tous les documents Todo.
	 * @return
	 */
	public List<Todo> findAll() {
		List<Todo> todos = new ArrayList<>();
		FindIterable<Document> dbObjects = collection.find();
		dbObjects.forEach((Document doc) -> {todos.add(new Todo.TodoBuilder(((ObjectId) doc.get("_id")).toString(), (String) doc.get("title"), (Boolean) doc.get("done"), (Date) doc.get("createdOn"))
					.build());});
		return todos;
	}

	/**
	 * Créé un document Todo.
	 * @param body
	 */
	public void createNewTodo(String body) {
		Todo todo = new Gson().fromJson(body, Todo.class);
		collection.insertOne(new Document("title", todo.getTitle()).append("done",
				todo.isDone()).append("createdOn", new Date()) );
	}

	/**
	 * Renvoie un document Todo par son Id.
	 * @param id
	 * @return
	 */
	public Todo findById(String id) {
		FindIterable<Document> find = (FindIterable<Document>) collection.find(new BasicDBObject("_id", new ObjectId(id)));
		return find.first() != null ? new Todo(find.first()) : null;
	}

	/**
	 * Modifie un document Todo.
	 * @param todoId
	 * @param body
	 * @return
	 */
	public Todo update(String todoId, String body) {
		Todo todo = new Gson().fromJson(body, Todo.class);
		collection.replaceOne(new Document("_id", new ObjectId(todoId)), new Document().append("title", todo.getTitle()).append("done",
				todo.isDone()));
		return this.findById(todoId);
	}
}
