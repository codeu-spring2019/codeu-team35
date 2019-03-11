/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codeu.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.google.appengine.api.datastore.FetchOptions;

/** Provides access to the data stored in Datastore. */
public class Datastore {

	private DatastoreService datastore;

	public Datastore() {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}

	/**
	 * Stores the Message in Datastore.
	 */
	public void storeMessage(Message message) {
		Entity messageEntity = new Entity("Message", message.getId().toString());
		messageEntity.setProperty("user", message.getUser());
		messageEntity.setProperty("text", message.getText());
		messageEntity.setProperty("timestamp", message.getTimestamp());
		messageEntity.setProperty("recipient", message.getRecipient());
		datastore.put(messageEntity);
	}

	/**
	 * Gets messages with the specified recipient.
	 *
	 * @return a list of messages with the specified recipient, or empty list if user is not the recipient
	 * of any messages. List is sorted by time descending.
	 */
	public List<Message> getMessages(String recipient) {
		List<Message> messages = new ArrayList<>();
		Query query =
				new Query("Message")
						.setFilter(new Query.FilterPredicate("recipient", FilterOperator.EQUAL, recipient))
						.addSort("timestamp", SortDirection.DESCENDING);
		PreparedQuery results = datastore.prepare(query);
		for (Entity entity : results.asIterable()) {
			try {
				String idString = entity.getKey().getName();
				UUID id = UUID.fromString(idString);
				String user = (String) entity.getProperty("user");

				String text = (String) entity.getProperty("text");
				long timestamp = (long) entity.getProperty("timestamp");

				Message message = new Message(id, user, text, timestamp, recipient);
				messages.add(message);
			} catch (Exception e) {
				System.err.println("Error reading message.");
				System.err.println(entity.toString());
				e.printStackTrace();
			}
		}
		return messages;
	}

  /**
   * Gets messages with the specified recipient.
   *
   * @return a list of messages with the specified recipient, or empty list if user is not the recipient
   *    of any messages. List is sorted by time descending.
   */
  public List<Message> getMessages(String recipient) {
    List<Message> messages = new ArrayList<>();

    Query query =
        new Query("Message")
            .setFilter(new Query.FilterPredicate("recipient", FilterOperator.EQUAL, recipient))
            .addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
}


  /** Returns the total number of messages for all users. */
  public int getTotalMessageCount(){
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(1000));
  }

  /* Fetches messages for all users. */
  public List<Message> getAllMessages(){
  List<Message> messages = new ArrayList<>();

  Query query = new Query("Message")
    .addSort("timestamp", SortDirection.DESCENDING);
  PreparedQuery results = datastore.prepare(query);

  for (Entity entity : results.asIterable()) {
   try {
    String idString = entity.getKey().getName();
    UUID id = UUID.fromString(idString);
    String user = (String) entity.getProperty("user");
    String text = (String) entity.getProperty("text");
    long timestamp = (long) entity.getProperty("timestamp");
    String recipient = (String) entity.getProperty("recipient");

    Message message = new Message(id, user, text, timestamp, recipient);
    messages.add(message);
   } catch (Exception e) {
    System.err.println("Error reading message.");
    System.err.println(entity.toString());
    e.printStackTrace();
   }
  }

  return messages;
 }
}

