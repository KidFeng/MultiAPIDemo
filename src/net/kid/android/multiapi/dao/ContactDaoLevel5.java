package net.kid.android.multiapi.dao;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;

import net.kid.android.multiapi.domain.Contact;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;

/**
 * base on android api-level5
 * @author Kid Feng <Kid.Stargazer@gmail.com>
 * create@ Aug 10, 2010 7:32:44 PM
 */
final class ContactDaoLevel5 extends ContactDao {
	private static final int ID_INDEX = 0;
	private static final int DISPLAY_NAME_INDEX = 1;
	private static final int HAS_PHONE_NUMBER_INDEX = 2;
	private static final int NUMBER_INDEX = 0;
	
	private static final String[] PROJECTION = new String[]{_ID, Contacts.DISPLAY_NAME, Contacts.HAS_PHONE_NUMBER};
	private static final String[] PROJECTION_PHONES = new String[]{Phone.NUMBER};
	@Override
	public Contact[] getContacts() {
		Log.d(TAG, "read contacts by API-Level5");
		Cursor cursor = mResolver.query(Contacts.CONTENT_URI, PROJECTION, 
				null, null, Contacts.DISPLAY_NAME + " COLLATE LOCALIZED asc");
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
		Cursor cursor = mResolver.query(Phone.CONTENT_URI, PROJECTION_PHONES,
				Phone.CONTACT_ID + "=?", new String[]{Long.toString(contactId)}, null);
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
