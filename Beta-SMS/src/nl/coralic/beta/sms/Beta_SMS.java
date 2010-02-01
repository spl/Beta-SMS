package nl.coralic.beta.sms;

import nl.coralic.android.utils.contact.PhoneNumbers;
import nl.coralic.android.utils.contact.PhonesHandler;
import nl.coralic.beta.sms.R;
import nl.coralic.beta.sms.utils.Const;
import nl.coralic.beta.sms.utils.Response;
import nl.coralic.beta.sms.utils.SendHandler;
import nl.coralic.beta.sms.utils.Properties;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Beta_SMS extends Activity
{
	Intent intent;

	EditText to;
	EditText text;
	Button send;
	TextView warningText;	
	
	SharedPreferences properties;
	PhonesHandler ch;
	PhoneNumbers ph;
	SendHandler sh;

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
		send = (Button) findViewById(R.id.btnSend);
		
		//send is disabled until we know if all preferences are filled correct
		send.setEnabled(false);
		warningText = (TextView) findViewById(R.id.lblTextWarning);

		// check if data exists
		checkForProperties();

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
		if (reqCode == Const.PICK_CONTACT && resultCode == RESULT_OK)
		{
			AsyncTask<Uri, Void, PhoneNumbers> task = new AsyncTask<Uri, Void, PhoneNumbers>()
			{
				@Override
				protected PhoneNumbers doInBackground(Uri... uris)
				{
					ch = new PhonesHandler();
					return ch.getPhoneNumbersForSelectedContact(getContentResolver(), uris[0]);
				}

				@Override
				protected void onPostExecute(PhoneNumbers result)
				{
					chooseNumber(result);
				}
			};

			task.execute(data.getData());
		}
	}

	private void chooseNumber(PhoneNumbers contact)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pick a number?");

		this.ph = contact;

		builder.setSingleChoiceItems(ph.getPhoneNumbersLabelArray(), -1, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int item)
			{
				to.setText(ph.getCleanPhoneNumber(item));
				ph = null;
				chooseNumberAlert.dismiss();
			}
		});
		chooseNumberAlert = builder.create();
		chooseNumberAlert.show();
	}

	public void onSend()
	{
		sh = new SendHandler(properties.getString("PasswordKey",""), properties.getString("UsernameKey",""), properties.getString("PhoneKey",""), to.getText().toString(), text
				.getText().toString(), properties.getString("ServiceKey",""));

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("In progres...");
		builder.setMessage("Trying to send your SMS.");
		showStatusAlert = builder.create();

		AsyncTask<Void, Void, Response> task = new AsyncTask<Void, Void, Response>()
		{
			@Override
			protected void onPreExecute()
			{
				showStatusAlert.show();
			}

			@Override
			protected Response doInBackground(Void... v)
			{
				return sh.send();
			}

			@Override
			protected void onPostExecute(Response anwser)
			{
				if (anwser.isSucceful() == true)
				{
					showStatusAlert.setMessage(anwser.getResponse());
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
					showStatusAlert.setMessage(anwser.getError());
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

	private void checkForProperties()
	{
		warningText.setText("");
		properties = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isEveryPropertieFilled = true;
		StringBuffer sb = new StringBuffer();
		send.setEnabled(false);
		if(!properties.contains("UsernameKey") || properties.getString("UsernameKey", "").equalsIgnoreCase(""))
		{
			sb.append("No username. \n");
			Log.d(Const.TAG_MAIN, "No username");
			isEveryPropertieFilled = false;
		}
		
		if(!properties.contains("PasswordKey") || properties.getString("PasswordKey", "").equalsIgnoreCase(""))
		{
			sb.append("No password. \n");
			Log.d(Const.TAG_MAIN, "No password");
			isEveryPropertieFilled = false;
		}
		
		if(!properties.contains("PhoneKey") || properties.getString("PhoneKey", "").equalsIgnoreCase(""))
		{
			sb.append("No phonenumber or username. \n");
			Log.d(Const.TAG_MAIN, "No phone");
			isEveryPropertieFilled = false;
		}
		
		if(!properties.contains("ServiceKey") || properties.getString("ServiceKey", "").equalsIgnoreCase(""))
		{
			sb.append("No service url. \n");
			Log.d(Const.TAG_MAIN, "No service url");
			isEveryPropertieFilled = false;
		}
		
		if(isEveryPropertieFilled)
		{
			send.setEnabled(true);
		}
		else
		{
			sb.append("Please fill the properties!");
			warningText.setText(sb.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add("Info");
		menu.add("Settings");
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
			if(item.getTitle().toString().equalsIgnoreCase("Settings"))
			{
				
				startActivity(new Intent(this, Properties.class));
			}
		}
		return true;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		Log.d(Const.TAG_MAIN, "onResume enterd");
		checkForProperties();
	}

}