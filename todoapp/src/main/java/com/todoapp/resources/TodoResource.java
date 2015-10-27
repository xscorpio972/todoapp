package com.todoapp.resources;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.bson.Document;

import com.google.gson.Gson;
import com.todoapp.service.TodoService;

/**
 * Exposition des resources de l'api.
 * @author JCZOBEIDE
 *
 */
public class TodoResource {
	private static final String API_CONTEXT = "/api/v1";

	private final TodoService todoService;

	public TodoResource(TodoService todoService) {
		this.todoService = todoService;
		setupEndpoints();
	}

	private void setupEndpoints() {
		post(API_CONTEXT + "/todos", "application/json", (request, response) -> {
			Document document = todoService.createNewTodo(request.body());
			response.status(201);
			response.type("application/json");
			return document.toJson();
		});

		get(API_CONTEXT + "/todos/:id", "application/json", (request, response)

		-> todoService.findById(request.params(":id")), new Gson()::toJson);

		get(API_CONTEXT + "/todos", "application/json", (request, response)

		-> todoService.findAll(), new Gson()::toJson);

		put(API_CONTEXT + "/todos/:id", "application/json", (request, response)

		-> todoService.update(request.params(":id"), request.body()), new Gson()::toJson);
		
		exception(Exception.class, (e, request, response) -> {
		    response.status(500);
		    response.body("Internal error");
		});
	}
}
