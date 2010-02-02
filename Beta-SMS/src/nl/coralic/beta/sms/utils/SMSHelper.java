/**
 * 
 */
package nl.coralic.beta.sms.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

/**
 * @author "Armin Čoralić"
 *
 */
public class SMSHelper
{ 
	
	public void addSMS(ContentResolver cr, String body, String phonenumber)
	{
	 ContentValues values = new ContentValues();
     values.put(Const.ADDRESS, phonenumber);
     values.put(Const.DATE, getDate());
     values.put(Const.READ, 1);
     values.put(Const.STATUS, -1);
     values.put(Const.TYPE, 2);
     values.put(Const.BODY, body);
     Uri inserted = cr.insert(Uri.parse("content://sms"), values);
	}
    
	private String getDate()
	{
		return String.valueOf(System.currentTimeMillis());
	}
}
