package nl.coralic.beta.sms;

import java.util.List;

import javax.jdo.PersistenceManager;

import nl.coralic.beta.sms.dataobject.Issues;
import nl.coralic.beta.sms.db.PMF;

public class ShowAllBean
{
	List<Issues> allIssues = null;
	PersistenceManager pm;
	
	@SuppressWarnings("unchecked")
	public ShowAllBean()
	{
		super();
		pm = PMF.get().getPersistenceManager();
		String query = "select from " + Issues.class.getName();
		allIssues = (List<Issues>) pm.newQuery(query).execute();
	}
	
	public List<Issues> getAllIssues()
	{
		return allIssues;
	}
	
	public void closeConnection()
	{
		pm.close();
	}
}
