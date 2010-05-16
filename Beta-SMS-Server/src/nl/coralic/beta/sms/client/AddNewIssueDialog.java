package nl.coralic.beta.sms.client;
import nl.coralic.beta.sms.client.services.addissue.AddNewIssueService;
import nl.coralic.beta.sms.client.services.addissue.AddNewIssueServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddNewIssueDialog extends DialogBox
{
	
	private final AddNewIssueServiceAsync addNewIssueService = GWT.create(AddNewIssueService.class);
	private TextBox txtEmail;
	private ListBox lstType;
	private TextArea txtText;
	Beta_SMS_Server entry;
	
	public AddNewIssueDialog(Beta_SMS_Server entry)
	{
		this.entry = entry;
		this.setStyleName("addNewIssueDialog");
		this.center();
		
		this.setText("Add a new Issue/Request new feature.");
		VerticalPanel vp = new VerticalPanel();
		
		HorizontalPanel hpEmail = new HorizontalPanel();
		Label email = new Label("Email (Optional):");
		txtEmail = new TextBox();
		hpEmail.add(email);
		hpEmail.add(txtEmail);
		vp.add(hpEmail);
		
		HorizontalPanel hpType = new HorizontalPanel();
		Label type = new Label("Type (Mandatory):");
		lstType = new ListBox();
		lstType.addItem("Bug");
		lstType.addItem("Feature");
		lstType.addItem("Other");
		hpType.add(type);
		hpType.add(lstType);
		vp.add(hpType);
		
		HorizontalPanel hpText = new HorizontalPanel();
		Label text = new Label("Text (Mandatory):");
		txtText = new TextArea();
		txtText.setWidth("400px");
		hpText.add(text);
		hpText.add(txtText);
		vp.add(hpText);
		
		Button send = new Button("Submit",new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event)
			{
				addNewIssue();				
			}
		});
		vp.add(send);
		
		this.add(vp);
	}
	
	private void addNewIssue()
	{
		addNewIssueService.addNewIssue(txtEmail.getText(), lstType.getItemText(lstType.getSelectedIndex()), txtText.getText(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result)
			{
				entry.getAllIssues();
				AddNewIssueDialog.this.hide();
			}
			
			@Override
			public void onFailure(Throwable caught)
			{
				Window.alert(caught.getMessage());
			}
		});
	}
}
