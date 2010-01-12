/**
 * 
 */
package nl.coralic.beta.sms.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.Contacts.People.Phones;

/**
 * @author "Armin Čoralić"
 */
public class ContactsHandler
{

	public Contact getContact(ContentResolver contentResolver, Uri contactUri)
	{
		Contact contact = new Contact();

		Uri numbers_uri = Uri.withAppendedPath(contactUri, Contacts.People.Phones.CONTENT_DIRECTORY);

		Cursor c = contentResolver.query(numbers_uri, null, null, null, Phones.DEFAULT_SORT_ORDER);
		if (c.moveToFirst())
		{
			while (!c.isAfterLast())
			{

				contact.addMobileNumbers(removeEverything(c.getString(c.getColumnIndex(Contacts.Phones.NUMBER))));
				c.moveToNext();
			}
		}
		return contact;
	}
	
	private String removeEverything(String number)
	{
		number = number.replace("-", "");
		return number;
	}
}
