/**
 * 
 */
package nl.coralic.secure.sms.utils;

import java.util.ArrayList;

/**
 * @author "Armin Čoralić"
 *
 */
public class Contact
{
	private ArrayList<String>mobileNumbers = new ArrayList<String>();
	private String pincode = new String();

	/**
	 * @return the mobileNumber
	 */
	public ArrayList<String> getMobileNumbers()
	{
		return mobileNumbers;
	}
	
	/**
	 * @return the mobileNumber
	 */
	public String[] getMobileNumbersStringArray()
	{
		String tmpStr[] = new String[this.size()];
		this.mobileNumbers.toArray(tmpStr);
		return tmpStr;
	}
	
	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber(int id)
	{
		return this.mobileNumbers.get(id);
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void addMobileNumbers(String mobileNumber)
	{
		this.mobileNumbers.add(mobileNumber);
	}
	
	public int size()
	{
		return this.mobileNumbers.size();
	}

	/**
	 * @return the pincode
	 */
	public String getPincode()
	{
		return pincode;
	}

	/**
	 * @param pincode the pincode to set
	 */
	public void setPincode(String pincode)
	{
		this.pincode = pincode;
	}
	
}
