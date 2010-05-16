package nl.coralic.beta.sms;

import java.util.List;

import javax.jdo.PersistenceManager;

import nl.coralic.beta.sms.dataobject.Issues;
import nl.coralic.beta.sms.db.PMF;

public class ShowIssueBean
{
	Issues issue;
	PersistenceManager pm;
	
	public ShowIssueBean()
	{
		super();
		pm = PMF.get().getPersistenceManager();
	}
	
	@SuppressWarnings("unchecked")
	public void findIssue(String id)
	{
		String query = "select from " + Issues.class.getName() + " WHERE key == " + id;
		List<Issues> fd = (List<Issues>) pm.newQuery(query).execute();
		issue = fd.get(0);
	}
	
	public Issues getIssues()
	{
		return issue;
	}
	
	public void closeConnection()
	{
		pm.close();
	}
}
