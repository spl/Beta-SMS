package nl.coralic.beta.sms.client.services.listissues;

import java.util.List;

import nl.coralic.beta.sms.client.model.Issues;

import com.google.gwt.user.client.ui.FlexTable;

public class IssuesTable extends FlexTable
{
	IssuesDataSource input;

	public IssuesTable(IssuesDataSource input)
	{
		super();
		this.setStyleName("issuesTable");
		//this.setCellPadding(1);
		//this.setCellSpacing(0);
		//this.setWidth("100%");
		this.setInput(input);
	}

	public void setInput(IssuesDataSource input)
	{
		for (int i = this.getRowCount(); i > 0; i--)
		{
			this.removeRow(0);
		}
		if (input == null)
		{
			return;
		}

		int row = 0;
		List<String> headers = input.getTableHeader();
		if (headers != null)
		{
			int i = 0;
			for (String string : headers)
			{
				this.setText(row, i, string);
				i++;
			}
			row++;
		}
		// Make the table header look nicer
		this.getRowFormatter().addStyleName(0, "tableHeader");

		List<Issues> rows = input.getIssues();
		int i = 1;
		for (Issues issues : rows)
		{
			this.setText(i, 0, issues.getId());
			this.setText(i, 1, issues.getDate().toString());
			this.setText(i, 2, issues.getStatus());
			this.setText(i, 3, issues.getType());
			this.setText(i, 4, issues.getContent());
			this.setText(i, 5, String.valueOf(issues.getComments().size()));
			i++;
		}
		this.input = input;
	}
}
