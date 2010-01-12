/**
 * 
 */
package nl.coralic.beta.sms.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author "Armin Čoralić"
 */
public class DbHandler
{

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Database creation sql statement
	 */

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper
	{

		DatabaseHelper(Context context)
		{
			super(context, Const.DATABASE_NAME, null, Const.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			Log.i(Const.TAG_DB, "Database will be created NOW");
			db.execSQL(Const.DATABASE_CREATE_USER);
			db.execSQL(Const.DATABASE_CREATE_FROM);
			db.execSQL(Const.DATABASE_CREATE_URL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.w(Const.TAG_DB, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + Const.DATABASE_CREATE_USER);
			db.execSQL("DROP TABLE IF EXISTS " + Const.DATABASE_CREATE_FROM);
			db.execSQL("DROP TABLE IF EXISTS " + Const.DATABASE_CREATE_URL);
			onCreate(db);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public DbHandler(Context ctx)
	{
		Log.d(Const.TAG_DB, "DB handler created");
		this.mCtx = ctx;
	}

	/**
	 * Open the notes database. If it cannot be opened, try to create a new instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public DbHandler open() throws SQLException
	{
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		mDbHelper.close();
	}

	public long createUpdateUser(String user, String pass)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(Const.KEY_ROWID, Const.ID);
		initialValues.put(Const.KEY_USER, user);
		initialValues.put(Const.KEY_PASS, pass);
		if (mDb.insert(Const.DATABASE_TABLE_USER, null, initialValues) != Const.ID)
		{
			if (updateUser(user, pass))
			{
				return Const.ID;
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return Const.ID;
		}

	}

	public long createUpdateFrom(String from)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(Const.KEY_ROWID, Const.ID);
		initialValues.put(Const.KEY_FROM, from);
		if (mDb.insert(Const.DATABASE_TABLE_FROM, null, initialValues) != Const.ID)
		{
			if (updateFrom(from))
			{
				return Const.ID;
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return Const.ID;
		}

	}

	public long createUpdateUrl(String url)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(Const.KEY_ROWID, Const.ID);
		initialValues.put(Const.KEY_URL, url);
		if (mDb.insert(Const.DATABASE_TABLE_URL, null, initialValues) != Const.ID)
		{
			if (updateUrl(url))
			{
				return Const.ID;
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return Const.ID;
		}

	}

	public boolean updateUser(String user, String pass)
	{
		ContentValues args = new ContentValues();
		args.put(Const.KEY_USER, user);
		args.put(Const.KEY_PASS, pass);

		return mDb.update(Const.DATABASE_TABLE_USER, args, Const.KEY_ROWID + "=" + Const.ID, null) > 0;
	}

	public boolean updateFrom(String from)
	{
		ContentValues args = new ContentValues();
		args.put(Const.KEY_FROM, from);

		return mDb.update(Const.DATABASE_TABLE_FROM, args, Const.KEY_ROWID + "=" + Const.ID, null) > 0;
	}

	public boolean updateUrl(String url)
	{
		ContentValues args = new ContentValues();
		args.put(Const.KEY_URL, url);

		return mDb.update(Const.DATABASE_TABLE_URL, args, Const.KEY_ROWID + "=" + Const.ID, null) > 0;
	}

	public Cursor fetchUser() throws SQLException
	{

		Cursor mCursor = mDb.query(true, Const.DATABASE_TABLE_USER, new String[] {Const.KEY_USER, Const.KEY_PASS }, Const.KEY_ROWID
				+ "=" + Const.ID, null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetchFrom() throws SQLException
	{

		Cursor mCursor = mDb.query(true, Const.DATABASE_TABLE_FROM, new String[] {Const.KEY_FROM }, Const.KEY_ROWID + "="
				+ Const.ID, null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetchUrl() throws SQLException
	{

		Cursor mCursor = mDb.query(true, Const.DATABASE_TABLE_URL, new String[] {Const.KEY_URL }, Const.KEY_ROWID + "=" + Const.ID,
				null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
}
