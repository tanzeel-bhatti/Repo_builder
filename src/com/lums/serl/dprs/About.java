package com.lums.serl.dprs;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Label;

public class About {

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

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
		shell.setText("About Desing Pattern Retrieval System");
		shell.setSize(450, 300);
		shell.setLayout(new GridLayout(2, false));
		
		Label lblImagelabel = new Label(shell, SWT.BORDER);
		lblImagelabel.setImage(new Image(Display.getCurrent(), "C:\\Users\\Tanzeel Bhatti\\Desktop\\cs1.png"));
		
		GridData gd_lblImagelabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblImagelabel.heightHint = 200;
		gd_lblImagelabel.widthHint = 180;
		lblImagelabel.setLayoutData(gd_lblImagelabel);
		formToolkit.adapt(lblImagelabel, true, true);
		
		StyledText styledText = new StyledText(shell, SWT.WRAP
				| SWT.MULTI
				| SWT.BORDER
		        | SWT.H_SCROLL
		        | SWT.V_SCROLL);
		styledText.setText("Eclipse Plug-In for Java Developers\r\n\r\nVersion: 1.0.0\r\nBuild id: 0.0.0.1\r\n(c) Copyright lums.edu.pk\r\n\r\nThis product includes software developed by SERL lab enigeeners for Design pattern recommendation system.");
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.adapt(styledText);
		formToolkit.paintBordersFor(styledText);
		
		Button btnDetails = new Button(shell, SWT.NONE);
		GridData gd_btnDetails = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDetails.widthHint = 111;
		btnDetails.setLayoutData(gd_btnDetails);
		formToolkit.adapt(btnDetails, true, true);
		btnDetails.setText("Details");
		
		Button btnClose = new Button(shell, SWT.NONE);
		GridData gd_btnClose = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnClose.widthHint = 111;
		btnClose.setLayoutData(gd_btnClose);
		formToolkit.adapt(btnClose, true, true);
		btnClose.setText("Close");
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
					shell.dispose();
			}
		});

	}

}
