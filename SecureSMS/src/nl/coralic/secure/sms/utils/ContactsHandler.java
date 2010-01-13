/**
 * 
 */
package nl.coralic.secure.sms.utils;

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

	private String removeEverything(String number)
	{
		number = number.replace("-", "");
		return number;
	}
}
