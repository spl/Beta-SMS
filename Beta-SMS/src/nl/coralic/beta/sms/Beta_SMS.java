package nl.coralic.beta.sms;

import nl.coralic.beta.sms.R;
import nl.coralic.beta.sms.utils.Const;
import nl.coralic.beta.sms.utils.Contact;
import nl.coralic.beta.sms.utils.ContactsHandler;
import nl.coralic.beta.sms.utils.DbHandler;
import nl.coralic.beta.sms.utils.SendHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Beta_SMS extends Activity
{
	Intent intent;

	EditText to;
	EditText text;
	EditText from;
	EditText username;
	EditText password;
	EditText url;
	CheckBox saveUsrPass;
	CheckBox saveFrom;
	CheckBox saveUrl;
	Button send;
	ContactsHandler ch;
	SendHandler sh;
	DbHandler db;

	AlertDialog chooseNumberAlert;
	AlertDialog showStatusAlert;
	String[] numbers;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// Set the view
		Log.d(Const.TAG_MAIN, "Creating the view and the rest of the GUI.");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.betasmsmain);
		//
		to = (EditText) findViewById(R.id.txtTo);
		text = (EditText) findViewById(R.id.txtText);
		from = (EditText) findViewById(R.id.txtFrom);
		username = (EditText) findViewById(R.id.txtUsername);
		password = (EditText) findViewById(R.id.txtPassword);
		url = (EditText) findViewById(R.id.txtUrl);
		saveUsrPass = (CheckBox) findViewById(R.id.chkUsrPass);
		saveFrom = (CheckBox) findViewById(R.id.chkFrom);
		saveUrl = (CheckBox) findViewById(R.id.chkUrl);
		send = (Button) findViewById(R.id.btnSend);

		// open DB
		db = new DbHandler(this);

		// check if data exists
		checkForSavedData();

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

		builder.setSingleChoiceItems(numbers, -1, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int item)
			{
				to.setText(numbers[item]);
				chooseNumberAlert.dismiss();
			}
		});
		chooseNumberAlert = builder.create();
		chooseNumberAlert.show();
	}

	public void onSend()
	{
		saveIfNeeded();
		sh = new SendHandler(password.getText().toString(), username.getText().toString(), from.getText().toString(), to.getText().toString(), text
				.getText().toString(), url.getText().toString());

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("In progres...");
		builder.setMessage("Trying to send your SMS.");
		showStatusAlert = builder.create();

		AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>()
		{
			@Override
			protected void onPreExecute()
			{
				showStatusAlert.show();
			}

			@Override
			protected Boolean doInBackground(Void... v)
			{
				return new Boolean(sh.send());
			}

			@Override
			protected void onPostExecute(Boolean anwser)
			{
				if (anwser.booleanValue() == true)
				{
					showStatusAlert.setMessage("SMS send.");
					CountDownTimer cdt = new CountDownTimer(5000, 1000)
					{
						@Override
						public void onFinish()
						{
							showStatusAlert.dismiss();
						}

						@Override
						public void onTick(long millisUntilFinished)
						{
							showStatusAlert.setTitle("Closing in: " + millisUntilFinished / 1000);
						}
					};
					cdt.start();
				}
				else
				{
					showStatusAlert.setMessage("Faild to send SMS.");
					CountDownTimer cdt = new CountDownTimer(5000, 1000)
					{
						@Override
						public void onFinish()
						{
							showStatusAlert.dismiss();
						}

						@Override
						public void onTick(long millisUntilFinished)
						{
							showStatusAlert.setTitle("Closing in: " + millisUntilFinished / 1000);
						}
					};
					cdt.start();
				}
			}
		};

		task.execute();

	}

	private void saveIfNeeded()
	{
		db.open();
		if (saveUsrPass.isChecked())
		{
			db.createUpdateUser(username.getText().toString(), password.getText().toString());
		}
		if (saveFrom.isChecked())
		{
			db.createUpdateFrom(from.getText().toString());
		}
		if (saveUrl.isChecked())
		{
			db.createUpdateUrl(url.getText().toString());
		}
		db.close();
	}

	private void checkForSavedData()
	{
		db.open();
		Log.d(Const.TAG_MAIN, "Check if there are saved username + password combo");
		Cursor c = db.fetchUser();
		if (c != null && c.getCount() != 0)
		{
			username.setText(c.getString(c.getColumnIndex(Const.KEY_USER)));
			password.setText(c.getString(c.getColumnIndex(Const.KEY_PASS)));
		}
		Log.d(Const.TAG_MAIN, "Check if there is a from saved");
		c = db.fetchFrom();
		if (c != null && c.getCount() != 0)
		{
			from.setText(c.getString(c.getColumnIndex(Const.KEY_FROM)));
		}
		Log.d(Const.TAG_MAIN, "Check if there is a url saved");
		c = db.fetchUrl();
		if (c != null && c.getCount() != 0)
		{
			url.setText(c.getString(c.getColumnIndex(Const.KEY_URL)));
		}
		db.close();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add("Info");
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
		}
		return true;
	}

}