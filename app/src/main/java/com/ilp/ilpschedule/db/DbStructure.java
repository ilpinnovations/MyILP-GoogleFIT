package com.ilp.ilpschedule.db;

import android.provider.BaseColumns;

public class DbStructure {
	public static abstract class LocationTable implements BaseColumns {
		public static final String TABLE_NAME = "location";

		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_LOCATION = "loc";
		public static final String COLUMN_LAT = "lat";
		public static final String COLUMN_LON = "lon";
		public static final String COMMAND_CREATE = new StringBuilder(
				DbConstants.CREATE_TABLE).append(TABLE_NAME)
				.append(DbConstants.BRACES_OPEN).append(_ID)
				.append(DbConstants.TYPE_INT)
				.append(DbConstants.CONSTRAIN_PRIMARY_KEY)
				.append(DbConstants.COMMA).append(COLUMN_NAME)
				.append(DbConstants.TYPE_TEXT).append(DbConstants.COMMA)
				.append(COLUMN_LOCATION).append(DbConstants.TYPE_TEXT)
				.append(DbConstants.COMMA).append(COLUMN_LAT)
				.append(DbConstants.TYPE_REAL).append(DbConstants.COMMA)
				.append(COLUMN_LON).append(DbConstants.TYPE_REAL)
				.append(DbConstants.BRACES_CLOSE).toString();
		public static final String COMMAND_DROP = new StringBuilder(
				DbConstants.DROP_TABLE).append(TABLE_NAME).toString();
	}

	public static abstract class ContactTable implements BaseColumns {
		public static final String TABLE_NAME = "contact";

		public static final String COLUMN_TITLE = "title";
		public static final String COLUMN_NUMBER = "number";

		public static final String COMMAND_CREATE = new StringBuilder(
				DbConstants.CREATE_TABLE).append(TABLE_NAME)
				.append(DbConstants.BRACES_OPEN).append(_ID)
				.append(DbConstants.TYPE_INT)
				.append(DbConstants.CONSTRAIN_PRIMARY_KEY)
				.append(DbConstants.COMMA).append(COLUMN_TITLE)
				.append(DbConstants.TYPE_TEXT).append(DbConstants.UNIQUE)
				.append(DbConstants.COMMA).append(COLUMN_NUMBER)
				.append(DbConstants.TYPE_TEXT).append(DbConstants.BRACES_CLOSE)
				.toString();
		public static final String COMMAND_DROP = new StringBuilder(
				DbConstants.DROP_TABLE).append(TABLE_NAME).toString();
	}

	public static abstract class ScheduleTable implements BaseColumns {
		public static final String TABLE_NAME = "schedule";

		public static final String COLUMN_DATE = "date";
		public static final String COLUMN_BATCH = "batch";
		public static final String COLUMN_SLOT = "slot";
		public static final String COLUMN_COURSE = "course";
		public static final String COLUMN_FACULTY = "faculty";
		public static final String COLUMN_ROOM = "room";

		public static final String COMMAND_CREATE = new StringBuilder(
				DbConstants.CREATE_TABLE).append(TABLE_NAME)
				.append(DbConstants.BRACES_OPEN).append(_ID)
				.append(DbConstants.TYPE_INT)
				.append(DbConstants.CONSTRAIN_PRIMARY_KEY)
				.append(DbConstants.COMMA).append(COLUMN_DATE)
				.append(DbConstants.TYPE_TEXT).append(DbConstants.COMMA)
				.append(COLUMN_BATCH).append(DbConstants.TYPE_TEXT)
				.append(DbConstants.COMMA).append(COLUMN_SLOT)
				.append(DbConstants.TYPE_TEXT).append(DbConstants.COMMA)
				.append(COLUMN_COURSE).append(DbConstants.TYPE_TEXT)
				.append(DbConstants.COMMA).append(COLUMN_FACULTY)
				.append(DbConstants.TYPE_TEXT).append(DbConstants.COMMA)
				.append(COLUMN_ROOM).append(DbConstants.TYPE_TEXT)
				.append(DbConstants.COMMA).append(DbConstants.UNIQUE)
				.append(DbConstants.BRACES_OPEN).append(COLUMN_DATE)
				.append(DbConstants.COMMA).append(COLUMN_SLOT)
				.append(DbConstants.COMMA).append(COLUMN_BATCH)
				.append(DbConstants.BRACES_CLOSE)
				.append(DbConstants.BRACES_CLOSE).toString();
		public static final String COMMAND_DROP = new StringBuilder(
				DbConstants.DROP_TABLE).append(TABLE_NAME).toString();
	}

	public static abstract class NotificationTable implements BaseColumns {
		public static final String TABLE_NAME = "notification";

		public static final String COLUMN_MSG = "msg";
		public static final String COLUMN_TIME = "time";

		public static final String COMMAND_CREATE = new StringBuilder(
				DbConstants.CREATE_TABLE).append(TABLE_NAME)
				.append(DbConstants.BRACES_OPEN).append(_ID)
				.append(DbConstants.TYPE_INT)
				.append(DbConstants.CONSTRAIN_PRIMARY_KEY)
				.append(DbConstants.COMMA).append(COLUMN_MSG)
				.append(DbConstants.TYPE_TEXT).append(DbConstants.COMMA)
				.append(COLUMN_TIME).append(DbConstants.TYPE_TEXT)
				.append(DbConstants.BRACES_CLOSE).toString();
		public static final String COMMAND_DROP = new StringBuilder(
				DbConstants.DROP_TABLE).append(TABLE_NAME).toString();
	}

	public static abstract class FeedbackTable implements BaseColumns {
		public static final String TABLE_NAME = "feedback";

		public static final String COLUMN_COMMENT = "comment";
		public static final String COLUMN_RATING = "rating";
		public static final String COLUMN_SLOT_REF = "slot_id";

		public static final String COMMAND_CREATE = new StringBuilder(
				DbConstants.CREATE_TABLE).append(TABLE_NAME)
				.append(DbConstants.BRACES_OPEN).append(_ID)
				.append(DbConstants.TYPE_INT)
				.append(DbConstants.CONSTRAIN_PRIMARY_KEY)
				.append(DbConstants.COMMA).append(COLUMN_COMMENT)
				.append(DbConstants.TYPE_TEXT).append(DbConstants.COMMA)
				.append(COLUMN_RATING).append(DbConstants.TYPE_INT)
				.append(DbConstants.COMMA).append(COLUMN_SLOT_REF)
				.append(DbConstants.TYPE_INT).append(DbConstants.UNIQUE)
				.append(DbConstants.BRACES_CLOSE).toString();
		public static final String COMMAND_DROP = new StringBuilder(
				DbConstants.DROP_TABLE).append(TABLE_NAME).toString();
	}

}
