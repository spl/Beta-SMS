package nl.coralic.secure.sms;

import java.util.Date;
import java.util.HashMap;

import nl.coralic.secure.sms.utils.Contact;
import nl.coralic.secure.sms.utils.ContactsHandler;
import nl.coralic.secure.sms.utils.EncryptionHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SecureSMSRead extends Activity
{
	LinearLayout ll;
	HashMap<String, String> map;
	AlertDialog pin;
	String id = "";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		ScrollView sv = new ScrollView(this);
		ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);
		map = new HashMap<String, String>();
		showSms();
		setContentView(sv);
	}

	public void showSms()
	{

		Uri sms = Uri.parse("content://sms");
		Cursor c = getContentResolver().query(sms, null, null, null, null);
		if (c.moveToFirst())
		{
			while (!c.isAfterLast())
			{
				map.put(c.getString(c.getColumnIndex("_id")), c.getString(c.getColumnIndex("body")));

				TextView tx = new TextView(this);
				tx.setText(c.getString(c.getColumnIndex("_id")) + " " + c.getString(c.getColumnIndex("address")) + " "
						+ new Date(Long.parseLong(c.getString(c.getColumnIndex("date")))).toLocaleString());
				tx.setOnClickListener(new View.OnClickListener()
				{
					public void onClick(View v)
					{
						readSms(((TextView) v).getText().toString());
					}
				});
				ll.addView(tx);

				c.moveToNext();
			}
		}
	}

	public void readSms(String text)
	{
		AsyncTask<String, Void, Contact> task = new AsyncTask<String, Void, Contact>()
		{
			@Override
			protected Contact doInBackground(String... uris)
			{
				ContactsHandler ch = new ContactsHandler();
				return ch.findContact(getContentResolver(), uris[0]);
			}

			@Override
			protected void onPostExecute(Contact result)
			{
				if (result != null && !result.getPincode().equalsIgnoreCase(""))
				{
					showSmsAsAlert(result.getPincode());
				}
				else
				{
					readPincodeAlert();
				}
			}
		};

		task.execute(getPhonenumber(text));
	}

	private String removeEverything(String number)
	{
		number = number.replace("-", "");
		return number;
	}

	public String getPhonenumber(String text)
	{
		id = text.substring(0, text.indexOf(" "));
		String tmpNumber = text.substring(text.indexOf(" "));
		tmpNumber = text.substring(0, text.indexOf(" "));
		return removeEverything(tmpNumber);
	}

	public void showSmsAsAlert(String pincode)
	{
		Log.d("Armin", "pincode: " + pincode);
		Log.d("Armin", "body: " + map.get("body"));
		String enc = "";
		try
		{
			enc = EncryptionHandler.decrypt(pincode, map.get(id));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Log.d("Armin", "enc: " + enc);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("SMS");
		builder.setMessage(enc);
		builder.setPositiveButton("Close", null);
		builder.create();
		builder.show();
	}

	public void readPincodeAlert()
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pincode");
		builder.setMessage("Fill in your pincode:");
		EditText et = new EditText(this);
		et.setId(9191);
		builder.setView(et);
		builder.setPositiveButton("Oke", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface d, int i)
			{
				showSmsAsAlert(((EditText) pin.findViewById(9191)).getText().toString());
				pin.dismiss();
			}
		});
		pin = builder.create();
		pin.show();
	}
}