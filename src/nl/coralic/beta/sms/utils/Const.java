/**
 * 
 */
package nl.coralic.beta.sms.utils;

/**
 * @author "Armin Čoralić"
 *
 */
public class Const
{
	public static final int PICK_CONTACT = 1;
	public static final String TAG_MAIN = "MAIN";
	public static final String TAG_DB = "DB";
	public static final String TAG_SEND = "SEND";
	
	public static final String KEY_USER = "username";
	public static final String KEY_PASS = "password";
	public static final String KEY_FROM = "kfrom";
	public static final String KEY_URL = "url";
	public static final String KEY_ROWID = "_id";
	public static final int ID = 1;
	
	public static final String DATABASE_NAME = "beta_data";
	public static final String DATABASE_TABLE_USER = "user";
	public static final String DATABASE_TABLE_FROM = "tfrom";
	public static final String DATABASE_TABLE_URL = "url";
	
	public static final String DATABASE_CREATE_USER = "create table "+DATABASE_TABLE_USER+" ("+KEY_ROWID+" integer primary key, "+KEY_USER+" text not null, "+KEY_PASS+" text not null);";
	public static final String DATABASE_CREATE_FROM = "create table "+DATABASE_TABLE_FROM+" ("+KEY_ROWID+" integer primary key, "+KEY_FROM+" text not null);";
	public static final String DATABASE_CREATE_URL = "create table "+DATABASE_TABLE_URL+" ("+KEY_ROWID+" integer primary key, "+KEY_URL+" text not null);";
	



	public static final int DATABASE_VERSION = 2;
}
