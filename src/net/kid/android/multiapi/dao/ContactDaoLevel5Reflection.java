package net.kid.android.multiapi.dao;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;

import net.kid.android.multiapi.domain.Contact;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * base on android api-level5 using Java Reflection
 * @author Kid Feng <Kid.Stargazer@gmail.com>
 * create@ Aug 10, 2010 10:12:40 PM
 */
final class ContactDaoLevel5Reflection extends ContactDao {
	private static final int ID_INDEX = 0;
	private static final int DISPLAY_NAME_INDEX = 1;
	private static final int HAS_PHONE_NUMBER_INDEX = 2;
	private static final int NUMBER_INDEX = 0;
	
	private static boolean hasError;
	private static String[] PROJECTION;
	private static String[] PROJECTION_PHONES;
	private static String DISPLAY_NAME;
	private static String CONTACT_ID;
	private static Uri CONTACT_CONTENT_URI;
	private static Uri PHONE_CONTENT_URI;
	static {
		try {
			Class<?> contactsClazz = null;
			Class<?> commonDataKindsClazz = null;
			for(Class<?> clazz : Class.forName("android.provider.ContactsContract").getDeclaredClasses()) {
				if(clazz.getName().endsWith("Contacts")) {
					contactsClazz = clazz;
				} else if(clazz.getName().endsWith("CommonDataKinds")) {
					commonDataKindsClazz = clazz;
				}
			}
			if(contactsClazz == null) {
				throw new ClassNotFoundException("Class 'android.provider.ContactsContract.Contacts' not found");
			}
			if(commonDataKindsClazz == null) {
				throw new ClassNotFoundException("Class 'android.provider.ContactsContract.commonDataKinds' not found");
			}
			Class<?> phoneClazz = null;
			for(Class<?> clazz : commonDataKindsClazz.getDeclaredClasses()) {
				if(clazz.getName().endsWith("Phone")) {
					phoneClazz = clazz;
				}
			}
			if(phoneClazz == null) {
				throw new ClassNotFoundException("Class 'android.provider.ContactsContract.commonDataKinds.Phone' not found");
			}
			final String hasPhoneNumber = commonDataKindsClazz.getDeclaredField("HAS_PHONE_NUMBER").get(null).toString();
			final String number = phoneClazz.getDeclaredField("NUMBER").get(null).toString();
			
			DISPLAY_NAME = commonDataKindsClazz.getDeclaredField("DISPLAY_NAME").get(null).toString();
			CONTACT_ID =  phoneClazz.getDeclaredField("CONTACT_ID").get(null).toString();
			CONTACT_CONTENT_URI = (Uri)contactsClazz.getDeclaredField("CONTENT_URI").get(null);
			PHONE_CONTENT_URI = (Uri)phoneClazz.getDeclaredField("CONTENT_URI").get(null);
			
			PROJECTION = new String[]{_ID, DISPLAY_NAME, hasPhoneNumber};
			PROJECTION_PHONES = new String[]{number};
			hasError = false;
		} catch (ClassNotFoundException e) {
			Log.d(TAG, "Error on load class", e);
			hasError = true;
		} catch (IllegalArgumentException e) {
			Log.d(TAG, "Error on load class", e);
			hasError = true;
		} catch (SecurityException e) {
			Log.d(TAG, "Error on load class", e);
			hasError = true;
		} catch (IllegalAccessException e) {
			Log.d(TAG, "Error on load class", e);
			hasError = true;
		} catch (NoSuchFieldException e) {
			Log.d(TAG, "Error on load class", e);
			hasError = true;
		}
	}
	
	
	@Override
	public Contact[] getContacts() {
		Log.d(TAG, "read contacts by API-Level5 using Java Reflection");
		if(hasError) {
			Log.d(TAG, "Error on loading class, return empty contacts array");
			return new Contact[0];
		}
		
		Cursor cursor = mResolver.query(CONTACT_CONTENT_URI, PROJECTION, 
				null, null, DISPLAY_NAME + " COLLATE LOCALIZED asc");
		try {
			ArrayList<Contact> contacts = new ArrayList<Contact>(cursor.getCount());
			while(cursor.moveToNext()) {
				if(cursor.getInt(HAS_PHONE_NUMBER_INDEX) != 1) continue;
				final String name = cursor.getString(DISPLAY_NAME_INDEX);
				final long id = cursor.getLong(ID_INDEX);
				readPhoneNumbers(id, name, contacts);
			}
			return contacts.toArray(new Contact[contacts.size()]);
		} finally {
			cursor.close();
		}
	}
	
	private void readPhoneNumbers(long contactId, String name, ArrayList<Contact> contacts){
		Cursor cursor = mResolver.query(PHONE_CONTENT_URI, PROJECTION_PHONES,
				CONTACT_ID + "=?", new String[]{Long.toString(contactId)}, null);
		try {
			while (cursor.moveToNext()) {
				String number = cursor.getString(NUMBER_INDEX);
				contacts.add(new Contact(name, number));
			}
		} finally {
			cursor.close();
		}
	}
}

