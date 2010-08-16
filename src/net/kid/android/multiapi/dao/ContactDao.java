package net.kid.android.multiapi.dao;

import android.content.ContentResolver;
import android.content.Context;
import net.kid.android.multiapi.domain.Contact;
/**
 * read contacts data
 * @author Kid Feng <Kid.Stargazer@gmail.com>
 * create@ Aug 10, 2010 7:32:07 PM
 */
public abstract class ContactDao {
	protected static final String TAG = "ContactDao";	
	protected static ContentResolver mResolver;
	
	private static final ContactDao dao;
	
	static {
		final String levelStr = android.os.Build.VERSION.SDK;
		dao = new ContactDaoLevel5Reflection();
	}
	
	public static final ContactDao getInstance(Context context) {
		mResolver = context.getContentResolver();
		return dao;
	}
	
	public abstract Contact[] getContacts();
}
