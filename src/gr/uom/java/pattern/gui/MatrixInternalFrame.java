package gr.uom.java.pattern.gui;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import gr.uom.java.pattern.PatternInstance;

public class MatrixInternalFrame extends JInternalFrame {
    //private DefaultTableModel model;
	private JTree patternInstanceTree;
	public int instanceCount = 0;
    public MatrixInternalFrame(String title) {
		super(title,true,true,true,true);
		
		this.patternInstanceTree = new JTree(new DefaultMutableTreeNode());
		
		JScrollPane scrollPane = new JScrollPane(patternInstanceTree);
        this.setContentPane(scrollPane);
		this.setVisible(true);
		this.setLocation(0,0);
	}
    /*
    public void addRow(String patternName, JComboBox comboBox) {
        model.addRow(new Object[] {patternName, comboBox.getItemCount(), comboBox} );
    }
    */
    //Natasha Khan
    public void populateDBwithPattern(PatternInstance.Entry entry, String patternName) throws Exception {
    	
    	String roleName = entry.getRoleName();
    	String elementName = entry.getElementName();
    	int patternID = 0;
    	int patternInstanceID = 0;
    	PreparedStatement insertStatement;
    	Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dprs_fact_repository","root","");
    	PreparedStatement getStatement1 = con.prepareStatement("select * from design_pattern where Name = ?");
    	getStatement1.setString(1, patternName);
    	ResultSet result1 = getStatement1.executeQuery();
    	while(result1.next()){
    		patternID=result1.getInt(2);
    	}
    	String project_path = "";
    	PreparedStatement getStatement = con.prepareStatement("SELECT Path FROM `project` ORDER BY `ProjectID`");
    	ResultSet result_path = getStatement.executeQuery();
    	while(result_path.next()){
    		project_path=result_path.getString(1);
    	}
    	int highest_instance_id=0;
    	PreparedStatement getPatternInstanceID = con.prepareStatement("SELECT `PatternInstanceID` FROM `pattern_instance_class` ORDER BY `pattern_instance_class`.`PatternInstanceID` ASC");
    	ResultSet result_instance_id = getPatternInstanceID.executeQuery();
    	while(result_instance_id.next()){
    		highest_instance_id=result_instance_id.getInt(1);
    	}
    	
    	PreparedStatement getStatement2 = con.prepareStatement("select PatternInstanceID, MetaData from pattern_instance where PatternID = ? AND ProjectPath =?");
    	getStatement2.setInt(1, patternID);
    	getStatement2.setString(2, project_path);
    	ResultSet result2 = getStatement2.executeQuery();
    	
    	int temp_Id;
    	String instanceMD;
    	while(result2.next()){
    		temp_Id=result2.getInt(1);
    		instanceMD = result2.getString(2);
    	
    		if(instanceMD.contains(elementName+" ")){
    			patternInstanceID=temp_Id;
    		}
    	}
    	if(roleName.contains("()")){
    	roleName.replace("()", "");
    	String element[] = elementName.split(":");
    	insertStatement =con.prepareStatement("INSERT INTO `pattern_instance_method` (`PatternInstanceID`,`PatternInstanceClassID`,`Name`) VALUES (?,?,?);");
    	insertStatement.setInt(1, patternInstanceID);
    	insertStatement.setString(3, element[2].replace("()", ""));
    	PreparedStatement getStatement3 = con.prepareStatement("select PatternInstanceClassID from pattern_instance_class where Name = ?");
    	getStatement3.setString(1, element[0]);
    	ResultSet result3 = getStatement3.executeQuery();
    	while(result3.next()){
    		insertStatement.setInt(2,result3.getInt(1));
    	}
    	
    	}
    	else{
    		insertStatement =con.prepareStatement("INSERT INTO `pattern_instance_class` (`PatternInstanceID`, `Name`, `Role`) VALUES (?,?,?);");
    		insertStatement.setInt(1, patternInstanceID);
        	insertStatement.setString(3, roleName);
        	insertStatement.setString(2, elementName);
    	}
    	
    	
    	int result = insertStatement.executeUpdate();
    	System.out.print(result);
    	con.close();
    	
    }
    public void addPatternNode(String patternName, Vector<PatternInstance> instances) throws Exception {
    	DefaultMutableTreeNode parent = new DefaultMutableTreeNode(patternName);
    	int counter = 1;
    	for(PatternInstance instance : instances) {
    		DefaultMutableTreeNode instanceChild = new DefaultMutableTreeNode("instance " + counter);
    		parent.add(instanceChild);
    		Iterator<PatternInstance.Entry> entryIterator = instance.getRoleIterator();
    		while(entryIterator.hasNext()) {
    			PatternInstance.Entry entry = entryIterator.next();
    			DefaultMutableTreeNode entryChild = new DefaultMutableTreeNode(entry);
    			instanceChild.add(entryChild);
    			populateDBwithPattern(entry,patternName);
    			
    		}
    		counter++;
    	}
    	DefaultMutableTreeNode root = (DefaultMutableTreeNode)patternInstanceTree.getModel().getRoot();
    	root.add(parent);
    	((DefaultTreeModel)patternInstanceTree.getModel()).reload();
    }
}