package com.lums.serl.dprs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;

public class DBConnection{
	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setText("Set-up DB Connection");
		shell.setSize(450, 300);
		shell.setLayout(new GridLayout(1, false));
		
		Combo dbNameCombo = new Combo(shell, SWT.NONE);
		dbNameCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		formToolkit.adapt(dbNameCombo);
		formToolkit.paintBordersFor(dbNameCombo);
		
		Group grpSettings = new Group(shell, SWT.NONE);
		grpSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		grpSettings.setText("Settings");
		formToolkit.adapt(grpSettings);
		formToolkit.paintBordersFor(grpSettings);
		
		text = new Text(grpSettings, SWT.BORDER);
		text.setBounds(132, 25, 150, 21);
		formToolkit.adapt(text, true, true);
		
		text_1 = new Text(grpSettings, SWT.BORDER);
		text_1.setBounds(132, 65, 150, 21);
		formToolkit.adapt(text_1, true, true);
		
		text_2 = new Text(grpSettings, SWT.BORDER);
		text_2.setBounds(132, 105, 150, 21);
		formToolkit.adapt(text_2, true, true);
		
		text_3 = new Text(grpSettings, SWT.BORDER);
		text_3.setBounds(132, 145, 150, 21);
		formToolkit.adapt(text_3, true, true);
		
		text_4 = new Text(grpSettings, SWT.BORDER);
		text_4.setBounds(132, 185, 150, 21);
		formToolkit.adapt(text_4, true, true);
		
		Label lblHost = new Label(grpSettings, SWT.NONE);
		lblHost.setBounds(10, 25, 75, 15);
		formToolkit.adapt(lblHost, true, true);
		lblHost.setText("Host:");
		
		Label lblPort = new Label(grpSettings, SWT.NONE);
		lblPort.setBounds(10, 65, 75, 15);
		formToolkit.adapt(lblPort, true, true);
		lblPort.setText("Port:");
		
		Label lblDbName = new Label(grpSettings, SWT.NONE);
		lblDbName.setBounds(10, 105, 75, 15);
		formToolkit.adapt(lblDbName, true, true);
		lblDbName.setText("DB Name:");
		
		Label lblUserName = new Label(grpSettings, SWT.NONE);
		lblUserName.setBounds(10, 145, 75, 15);
		formToolkit.adapt(lblUserName, true, true);
		lblUserName.setText("User Name:");
		
		Label lblPassword = new Label(grpSettings, SWT.NONE);
		lblPassword.setBounds(10, 185, 75, 15);
		formToolkit.adapt(lblPassword, true, true);
		lblPassword.setText("Password:");

	}
}
