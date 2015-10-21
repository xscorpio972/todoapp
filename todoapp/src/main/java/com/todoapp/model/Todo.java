package com.todoapp.model;

import java.io.Serializable;
import java.util.Date;

import org.bson.Document;
import org.bson.types.ObjectId;


/**
 * @author JCZOBEIDE
 *
 */
public class Todo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7984357458670744519L;
	private String id;
	private String title;
	private boolean done;
	private Date createdOn = new Date();

	public Todo(Document document) {
		this.id = ((ObjectId) document.get("_id")).toString();
		this.title = document.getString("title");
		this.done = document.getBoolean("done");
		this.createdOn = document.getDate("createdOn");
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	private Todo(TodoBuilder builder) {
		this.id = builder.id;
		this.title = builder.title;
		this.done = builder.done;
		this.createdOn = builder.createdOn;
	}

	public static class TodoBuilder {
		private String id;
		private String title;
		private boolean done;
		private Date createdOn = new Date();

		public TodoBuilder(String id, String title, boolean done, Date createdOn) {
			super();
			this.id = id;
			this.title = title;
			this.done = done;
			this.createdOn = createdOn;
		}

		public Todo build() {
			return new Todo(this);
		}
	}

}
