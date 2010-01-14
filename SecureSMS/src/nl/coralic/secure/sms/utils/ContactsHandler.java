/**
 * 
 */
package nl.coralic.secure.sms.utils;

import java.util.Iterator;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.Contacts.People.Phones;
import android.util.Log;

/**
 * @author "Armin Čoralić"
 */
public class ContactsHandler
{

	public Contact getContact(ContentResolver contentResolver, Uri contactUri)
	{
		Contact contact = new Contact();

		Uri numbers_uri = Uri.withAppendedPath(contactUri, Contacts.People.Phones.CONTENT_DIRECTORY);
		Log.d("Contact", "URI: " + numbers_uri.toString());
		Cursor c = contentResolver.query(numbers_uri, null, null, null, Phones.DEFAULT_SORT_ORDER);
		if (c.moveToFirst())
		{
			while (!c.isAfterLast())
			{
				String tmp = removeEverything(c.getString(c.getColumnIndex(Contacts.Phones.NUMBER)));
				if (tmp.startsWith("0000"))
				{
					contact.setPincode(tmp.replace("0000", ""));
				}
				else
				{
					contact.addMobileNumbers(tmp);
				}
				c.moveToNext();
			}
		}
		return contact;
	}
	
	
	public Contact findContact(ContentResolver contentResolver, String phone)
	{
		Contact contact = new Contact();

		Cursor c = contentResolver.query(Uri.parse("content://contacts/people"), null, null, null, null);
		if (c.moveToFirst())
		{
			while (!c.isAfterLast())
			{
				contact = getContact(contentResolver, Uri.parse("content://contacts/people/"+c.getString(c.getColumnIndex("_id"))));
				Iterator<String> i = contact.getMobileNumbers().iterator();
				while(i.hasNext())
				{
					String tmpNumber = i.next();
					Log.d("TAG", "Contact phone: " + tmpNumber + " used phone number: "+ phone);
					if(tmpNumber.equalsIgnoreCase(removeEverything(phone)))
					{
						return contact;
					}
				}

				c.moveToNext();
			}
		}
		return null;
	}

	private String removeEverything(String number)
	{
		number = number.replace("-", "");
		return number;
	}
}
