package net.kid.android.multiapi.dao;

import static android.provider.Contacts.Phones.CONTENT_URI;

import net.kid.android.multiapi.domain.Contact;
import android.database.Cursor;
import android.provider.Contacts.People;
import android.provider.Contacts.Phones;
import android.util.Log;
/**
 * base on android api-level3
 * @author Kid Feng <Kid.Stargazer@gmail.com>
 * create@ Aug 10, 2010 7:32:26 PM
 */
@SuppressWarnings("deprecation")
final class ContactDaoLevel3 extends ContactDao {
	private static final int DISPLAY_NAME_INDEX = 0;
	private static final int NUMBER_INDEX = 1;
	private static final String[] PROJECTION = {Phones.DISPLAY_NAME, Phones.NUMBER};
	
	@Override
	public Contact[] getContacts() {
		Log.d(TAG, "read contacts by API-Level3");
		Cursor cursor = mResolver.query(CONTENT_URI, PROJECTION, null, null, 
				People.DISPLAY_NAME + " COLLATE LOCALIZED asc");
		try {
			Contact[] contacts = new Contact[cursor.getCount()];
			while(cursor.moveToNext()) {
				String name = cursor.getString(DISPLAY_NAME_INDEX);
				String number = cursor.getString(NUMBER_INDEX);
				contacts[cursor.getPosition()] = new Contact(name, number);
			}
			return contacts;
		} finally {
			cursor.close();
		}
	}
}
