package com.ilp.ilpschedule.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public interface Constants {
	String PREF_FILE_NAME = "com.tcs.preffile";
	String URL_BASE = "http://theinspirer.in/ilpscheduleapp/";
	String URL_BADGES = URL_BASE + "points_json.php?";
	String URL_NOTIFICATION = URL_BASE + "notify_json.php";
	String URL_SCHEDULE = URL_BASE + "schedulelist_json.php";
	String URL_CONTACTS = URL_BASE + "getEmergencyContacts.php?";
	String URL_FEEDBACK_SUBMIT = URL_BASE + "feedback_json.php";
	String URL_FEEDBACK_SUMMARY = URL_BASE + "faculty_json.php?";
	String URL_REGISTER = URL_BASE + "register.php";
	String URL_GOOGLE_MAP_SEARCH = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

	String QUESTIONMARK = "?";
	String AND = "&";
	String SPACE = " ";
	String EQUALS = "=";
	String CHARSET = "UTF-8";
	String GOOGLE_SENDER_ID = "1039038689673";
	String GOOGLE_MAP_API_KEY = "AIzaSyBefyHSIxtoiDf3GAN7z0WkI9nxTna12wA";

	SimpleDateFormat paramsDateFormat = new SimpleDateFormat("yyyy-MM-dd",
			Locale.US);

	public interface PREF_KEYS {
		String EMP_NAME = "key_name";
		String EMP_ID = "key_empid";
		String EMP_LOCATION = "key_location";
		String EMP_BATCH = "key_batch";
		String EMP_EMAIL = "key_email";
		String EMP_LG = "key_lg";
		String IS_LOGIN = "is_login";
		String GCM_REG_ID = "gcm_reg_id";
		String BADGE_POINTS = "my_points";
	}

	public interface EMP_ERRORS {
		public interface EMAIL {
			int BLANK = 1;
			int INVALID = 2;
		}

		public interface NAME {
			int BLANK = 3;
			int INVALID = 4;
		}

		public interface BATCH {
			int BLANK = 5;
			int INVALID = 6;
		}

		public interface LOCATION {
			int BLANK = 7;
			int INVALID = 8;
		}

		public interface EMP_ID {
			int BLANK = 9;
			int INVALID = 10;
		}

		public interface EMP_LG {
			int BLANK = 11;
			int INVALID = 12;
		}

		int NO_ERROR = 0;
	}

	public interface DRAWER_ITEM_TYPE {
		int HEADER = 1;
		int OPTION = 2;
	}

	public interface NETWORK_PARAMS {
		public interface SCHEDULE {
			String URL = URL_SCHEDULE + QUESTIONMARK;
			String BATCH = "batch";
			String DATE = "date";
		}

		public interface CONTACT {
			String URL = URL_CONTACTS + QUESTIONMARK;
			String ILP = "ilp";
		}

		public interface BADGE {
			String URL = URL_BADGES + QUESTIONMARK;
			String EMPID = "empid";
			String BATCH = "batch";
		}

		public interface FEEDBACK_SUMMARY {
			String FACULTY = "faculty";
			String COURSE = "course";
			String SLOT = "slot";
			String DATE = "date";
		}

		public interface FEEDBACK_SUBMIT {
			String FACULTY = "faculty";
			String COURSE = "course";
			String COMMENT = "comment";
			String RATE = "rate";
			String EMP_ID = "empid";
			String EMP_NAME = "empname";
			String EMP_LOC = "emploc";
			String EMP_BATCH = "empbatch";
			String SLOT = "slot";
			String DATE = "date";
		}

		public interface MAP {
			String LOCATION = "location";
			String KEY = "key";
			String RADIUS = "radius";
			String TYPES = "types";
			String SENSOR = "sensor";
		}

		public interface REGISTER {
			String IMEI = "imei";
			String NAME = "name";
			String EMIAL = "email";
			String EMP_ID = "id";
			String LOCATION = "loc";
			String BATCH = "batch";
		}
	}

	public interface BUNDLE_KEYS {
		public interface FEEDBACK_FRAGMENT {
			String FACULTY = "faculty";
			String COURSE = "course";
			String SLOT_ID = "slot_id";
			String IS_FACULTY = "is_faculty";
		}
	}

	public interface JSON_KEYS {

	}

	public interface LOCATIONS {
		/**
		 * types of locations
		 */
		public interface TYPE {
			String ILP = "ILP";
			String HOSTEL = "Hostel";
		}

		public interface TRIVANDRUM {
			String LOC_NAME = "Trivandrum";
		}

		public interface CHENNAI {
			String LOC_NAME = "Chennai";
		}

		public interface GUWAHATI {
			String LOC_NAME = "Guwahati";
		}

		public interface HYDERABAD {
			String LOC_NAME = "Hyderabad";
		}

		public interface AHMEDABAD {
			String LOC_NAME = "Ahmedabad";
		}
	}
}
