package net.kid.android.multiapi;

import net.kid.android.multiapi.dao.ContactDao;
import net.kid.android.multiapi.domain.Contact;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
/**
 * This demo will show you how to read contacts on different android api-platform
 * @author Kid Feng <Kid.Stargazer@gmail.com>
 * create@ Aug 9, 2010 8:38:13 PM
 */
public class Home extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loadList();
    }

	private void loadList() {
		ListView listView = (ListView) findViewById(R.id.list);
		ContactDao dao = ContactDao.getInstance(this);
		listView.setAdapter(new ContactAdapter(dao.getContacts()));
	}
	
	private final class ContactAdapter extends BaseAdapter {
		private final Contact[] contacts;
		
		private ContactAdapter(Contact[] contacts) {
			this.contacts = contacts;
		}
		
		@Override
		public int getCount() {
			return contacts.length;
		}

		@Override
		public Contact getItem(int position) {
			return contacts[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null) {
				convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, null);
				holder = new ViewHolder();
				convertView.setTag(holder);
				holder.name = (TextView)convertView.findViewById(android.R.id.text1);
				holder.number = (TextView)convertView.findViewById(android.R.id.text2);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			
			Contact contact = contacts[position];
			holder.name.setText(contact.name);
			holder.number.setText(contact.number);
			
			return convertView;
		}
	}
	
	private static final class ViewHolder {
		private TextView name;
		private TextView number;
	} 
}