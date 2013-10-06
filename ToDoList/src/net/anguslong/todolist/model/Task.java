package net.anguslong.todolist.model;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {
	private UUID mId;
	private String mTitle;
	private String mNotes;

	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_DETAILS = "details";
	private static final String JSON_COMPLETE = "complete";
	private boolean mComplete;

	public Task() {
		mId = UUID.randomUUID();

	}

	public Task(JSONObject json) throws JSONException {
		if (json.has(JSON_ID)) {
		mId = UUID.fromString(json.getString(JSON_ID));
		}
		if (json.has(JSON_TITLE)) {
			mTitle = json.getString(JSON_TITLE);
		}
		if (json.has(JSON_DETAILS)) {
			mNotes = json.getString(JSON_DETAILS);
		}
		if (json.has(JSON_COMPLETE)) {
			mComplete = json.getBoolean(JSON_COMPLETE);
		}
	}

	@Override
	public String toString() {
		return mTitle;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public UUID getId() {
		return mId;
	}

	public boolean isComplete() {
		return mComplete;
	}

	public void setComplete(boolean complete) {
		mComplete = complete;
	}

	public String getNotes() {
		return mNotes;
	}

	public void setNotes(String text) {
		mNotes = text;
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_DETAILS, mNotes);
		json.put(JSON_COMPLETE, mComplete);
		return json;
	}

}
