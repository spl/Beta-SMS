package nl.coralic.secure.sms;

import nl.coralic.secure.sms.utils.Const;
import nl.coralic.secure.sms.utils.Contact;
import nl.coralic.secure.sms.utils.ContactsHandler;
import nl.coralic.secure.sms.utils.EncryptionHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SecureSMS extends Activity
{
	private static final int ACTIVITY_CREATE=0;
	EditText to;
	EditText pincode;
	EditText text;
	Intent intent;
	ContactsHandler ch;
	String[] numbers;
	AlertDialog chooseNumberAlert;
	String pin;
	Button send;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send);

		to = (EditText) findViewById(R.id.txtPhoneNo);
		pincode = (EditText) findViewById(R.id.txtPincode);
		text = (EditText) findViewById(R.id.txtMessage);
		send = (Button) findViewById(R.id.btnSendSMS);

		// Set the intent
		intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);

		// When double tapd show Contacts
		to.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				startActivityForResult(intent, Const.PICK_CONTACT);
			}
		});

		send.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				onSend();
			}
		});
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data)
	{
		Log.d(Const.TAG_MAIN, "Double taped, show contacts");
		// super.onActivityResult(reqCode, resultCode, data);
		if (reqCode == Const.PICK_CONTACT && resultCode == RESULT_OK)
		{
			AsyncTask<Uri, Void, Contact> task = new AsyncTask<Uri, Void, Contact>()
			{
				@Override
				protected Contact doInBackground(Uri... uris)
				{
					ch = new ContactsHandler();
					return ch.getContact(getContentResolver(), uris[0]);
				}

				@Override
				protected void onPostExecute(Contact result)
				{
					chooseNumber(result);
				}
			};

			task.execute(data.getData());
		}
	}

	private void chooseNumber(Contact contact)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pick a number");

		numbers = contact.getMobileNumbersStringArray();
		pin = contact.getPincode();

		builder.setSingleChoiceItems(numbers, -1, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int item)
			{
				to.setText(numbers[item]);
				if (!pin.equalsIgnoreCase(""))
				{
					pincode.setText(pin);
				}
				chooseNumberAlert.dismiss();
			}
		});
		chooseNumberAlert = builder.create();
		chooseNumberAlert.show();
	}

	public void onSend()
	{
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context arg0, Intent arg1)
			{
				switch (getResultCode())
				{
					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
						break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context arg0, Intent arg1)
			{
				switch (getResultCode())
				{
					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
						break;
					case Activity.RESULT_CANCELED:
						Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
						break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		String msg = "";
		try
		{
			msg = EncryptionHandler.encrypt(pincode.getText().toString(), text.getText().toString());
			Log.d(Const.TAG_MAIN, "Size: " + msg.length());
		}
		catch (Exception e)
		{
			Log.d(Const.TAG_MAIN, e.getMessage());
		}
		sms.sendTextMessage(to.getText().toString(), null, msg, sentPI, deliveredPI);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add("Info");
		menu.add("Read");
		menu.add("Exit");
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{

		if (item.hasSubMenu() == false)
		{
			if (item.getTitle().toString().equalsIgnoreCase("Exit"))
			{
				this.finish();
			}
			if (item.getTitle().toString().equalsIgnoreCase("Info"))
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Info");
				builder.setMessage("This application is made by Armin Čoralić. Visit me at http://blog.coralic.nl");
				builder.setPositiveButton("Close", null);
				builder.create();
				builder.show();
			}
			if (item.getTitle().toString().equalsIgnoreCase("Read"))
			{
		        Intent i = new Intent(this, SecureSMSRead.class);
		        startActivityForResult(i, ACTIVITY_CREATE);
			}
		}
		return true;
	}


}