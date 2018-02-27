package cc.mallet.javaTotext;

import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.awt.*;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;

import com.lums.serl.dprs.Hello;
import com.lums.serl.dprs.helper;

import javax.swing.GroupLayout.*;


public class DemoJFileChooser extends JPanel
implements ActionListener {
	JButton go;

	JFileChooser chooser;
	public static JLabel statusLabel;
	private static Hello _hello = null;
	
	String choosertitle;

	public static void setStatusLabel(String s)
	{
		statusLabel.setText(s);
	}
	public DemoJFileChooser() {
		
		
		go = new JButton("Please select the source folder of java files!!");
		go.addActionListener(this);	
		statusLabel= new JLabel();
		add(statusLabel);
		add(go);       
		
	}

	public void actionPerformed(ActionEvent e) {
		int result;

		chooser = new JFileChooser(); 
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle(choosertitle);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//
		// disable the "All files" option.
		//
		chooser.setAcceptAllFileFilterUsed(false);
		//    
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
			System.out.println("getCurrentDirectory(): " 
					+  chooser.getCurrentDirectory());
			System.out.println("getSelectedFile() : " 
					+  chooser.getSelectedFile());
			try {
				//javaTotext.run(chooser.getSelectedFile());
				javaTotext.run();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else {
			System.out.println("No Selection ");
		}
	}

	public Dimension getPreferredSize(){
		return new Dimension(400, 100);
	}

	public static void main(String args[]) {
		helper _helper = new helper();
		System.setOut(new PrintStream(_helper));
		System.setErr(new PrintStream(_helper));

		_hello = new Hello();
//		SwingUtilities.invokeLater(new Runnable() {
//			
//			@Override
//			public void run() {
//				JFrame frame = new JFrame("");
//				DemoJFileChooser panel = new DemoJFileChooser();
//				File file = new File("");
//				frame.addWindowListener(new WindowAdapter() {public void windowClosing(WindowEvent e) {System.exit(0);}});
//				frame.getContentPane().add(panel,"Center");
//				frame.setSize(panel.getPreferredSize());
//				frame.setVisible(true);
//			setStatusLabel("Welcome!");     				
//			}
//		});
//		

		SwingUtilities.invokeLater(_hello);		
		try {
		System.out.println("Initializing folder scan...");
			javaTotext.run();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}