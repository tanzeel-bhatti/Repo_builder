package dprs.handlers;

import javax.swing.SwingUtilities;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import java.io.PrintStream;
import com.lums.serl.dprs.Hello;
import com.lums.serl.dprs.helper;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class DPRSHandler extends AbstractHandler {
//	Display display = new Display();
//	Shell shell = new Shell(display);
	private Hello _hello = null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		/* Original Code Block
//		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
//		MessageDialog.openInformation(
//				window.getShell(),
//				"DPRS",
//				"Hello, from DPRS");
 
 End Original Code Block*/ 
		
		helper _helper = new helper();
		System.setOut(new PrintStream(_helper));
		System.setErr(new PrintStream(_helper));
		_hello = new Hello();
//		SwingUtilities.invokeLater(_hello);
		_hello.open();
		
		return null;
	}
}
