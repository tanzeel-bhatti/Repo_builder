package gr.uom.java.pattern.gui.progress;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import cc.mallet.util.Constants;
import gr.uom.java.pattern.PatternInstance;


public class DatabaseLayer {
	protected String patternName;
	//protected createIndex index=new createIndex();
    protected Vector<PatternInstance> patternInstanceVector;
   // private LuceneForComments CommentsExtractObj; 
    public String fileDirectory;
    private int fileID;
    private int projectID;
    public int patternID;
    public int patternInstanceID;
    public static Connection con;
    private PrintWriter writer;
    private FileReader reader;
    
    public DatabaseLayer() throws ClassNotFoundException, SQLException, FileNotFoundException, UnsupportedEncodingException
    {
    	Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(Constants.dbConnectionString,Constants.uname,Constants.passwd);	
//    	Class.forName("com.mysql.jdbc.Driver");
//    	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dprs_fact_repository","root","");
    }
 
    public static void closeConnection() throws Throwable{
    	con.close();
    }
    public int getPattternID(String patternName){
    	try {
    		PreparedStatement getPatternID=con.prepareStatement("select PatternID from design_pattern where name = ?");
    		getPatternID.setString(1, patternName);
    		ResultSet result=getPatternID.executeQuery();
    		setPatternID(result);
    		return this.patternID;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
    }
    
    public int getPattternInstanceID(String projectName){
    	try {
    		PreparedStatement getPatternID=con.prepareStatement("select PatternInstanceID from pattern_instance where ProjectPath = ?");
    		getPatternID.setString(1, projectName);
    		ResultSet result=getPatternID.executeQuery();
    		setPatternInstanceID(result);
    		return this.patternInstanceID;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
    }
    public void populatePatternInstance(String patternName, Vector<PatternInstance> piVector) throws Exception{
    	this.patternInstanceVector = piVector;
    	//this.con=DBobj.con;
    	this.patternInstanceVector = piVector;
    	getPattternID(patternName);
    	int instanceCount  = 0;
    	for(PatternInstance instance:patternInstanceVector){

    		PreparedStatement statement =con.prepareStatement("INSERT INTO `pattern_instance` (`PatternID`, `ProjectID`,`ProjectPath`,`MetaData`) VALUES (?,?,?,?);");
    		statement.setInt(1, this.patternID);
    		String[] instances = instance.toString().split(",");
    		if(instances.length > 1 && instanceCount == 0 ){
    			instanceCount = instances.length-1;
    		}
        	statement.setString(4, instance.toString());
          	statement.setString(3, this.getFileDir(con));
          	statement.setInt(2, this.projectID);
          	getPattternInstanceID(this.fileDirectory);    
        	int result = statement.executeUpdate();
        	instanceCount--;

        	populateInstanceClassAndInstanceMethod(instance, instance.getRoleIterator());
        }
    	
    }
    public void populatePatternInstance(DatabaseLayer DBobj) throws Exception{
    	this.patternInstanceVector = DBobj.patternInstanceVector;
    	//this.con=DBobj.con;
    	this.patternInstanceVector=DBobj.patternInstanceVector;
    	getPattternID(patternName);
    	int instanceCount  = 0;
    	for(PatternInstance instance:patternInstanceVector){

    		PreparedStatement statement =con.prepareStatement("INSERT INTO `pattern_instance` (`PatternID`, `ProjectID`,`ProjectPath`,`MetaData`) VALUES (?,?,?,?);");
    		statement.setInt(1, this.patternID);
    		String[] instances = instance.toString().split(",");
    		if(instances.length > 1 && instanceCount == 0 ){
    			instanceCount = instances.length-1;
    		}
        	statement.setString(4, instance.toString());
          	statement.setString(3, this.getFileDir(con));
          	statement.setInt(2, this.projectID);
          	getPattternInstanceID(this.fileDirectory);    
        	int result = statement.executeUpdate();
        	instanceCount--;
        	populateInstanceClassAndInstanceMethod(instance, instance.getRoleIterator());
        }
    	
    }
   
    public void populateInstanceClassAndInstanceMethod(PatternInstance instance, Iterator<PatternInstance.Entry> entryIterator) throws Exception {
		while (entryIterator.hasNext()) {
			PreparedStatement insertStatement;
			PatternInstance.Entry entry = entryIterator.next();
			String roleName = entry.getRoleName();
			String elementName = entry.getElementName();
			if (roleName.contains("()")) {
				roleName.replace("()", "");
				String element[] = elementName.split(":");
				insertStatement = con.prepareStatement(
						"INSERT INTO `pattern_instance_method` (`PatternInstanceID`,`PatternInstanceClassID`,`Name`) VALUES (?,?,?);");
				insertStatement.setInt(1, patternInstanceID);
				insertStatement.setString(3, element[2].replace("()", ""));
				PreparedStatement getStatement3 = con
						.prepareStatement("select PatternInstanceClassID from pattern_instance_class where Name = ?");
				getStatement3.setString(1, element[0]);
				ResultSet result3 = getStatement3.executeQuery();
				while (result3.next()) {
					insertStatement.setInt(2, result3.getInt(1));
				}

			} else {
				insertStatement = con.prepareStatement(
						"INSERT INTO `pattern_instance_class` (`PatternInstanceID`, `Name`, `Role`, `FilePath`) VALUES (?,?,?,?);");
				getPattternInstanceID(this.fileDirectory);
				insertStatement.setInt(1, this.patternInstanceID);
				insertStatement.setString(2, elementName);
				String[] names = null;
				if (elementName.contains(" "))
				{
					names = elementName.split(" ");
					for(String name:names)
					{
						if (name.contains("."))
						{
							elementName = name;
						}
					}
				}
				if (elementName.contains("$"))
				{
					elementName = elementName.split("$")[0];
				}
				insertStatement.setString(3, roleName);
				String path = elementName.replace(".", "/");
				path = path.concat(".java");
				insertStatement.setString(4, this.fileDirectory +"/src/" + path);
				getPattternInstanceID(this.fileDirectory);
			}
			int result = insertStatement.executeUpdate();
			System.out.println("Result : " + result);
		}
		
		
    }
    
    public void setPatternID(ResultSet result) throws SQLException{
    	while(result.next()){
    		this.patternID=result.getInt(1);
    	}
	}   
    
    public void setPatternInstanceID(ResultSet result) throws SQLException{
    	while(result.next()){
    		this.patternInstanceID=result.getInt(1);
    	}
	}   
	
	public String getName(String path) throws SQLException
	{
		String pn = path;
		String[] segments = pn.split("\\\\");
		String idStr = segments[segments.length-1];
		String idStr2 = segments[segments.length-2];
		return idStr;
	}
    
    public void setFileDir(String dir) {
    	PreparedStatement statement;
		try {
			statement = con.prepareStatement("INSERT INTO `project` (`ProjectID`, `Path`, `Name`) VALUES (NULL, ?, ?);");
			String name = getName(dir);
			statement.setString(1, dir);
			statement.setString(2, name);
	    	statement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	this.fileDirectory = dir;
    }
    public int getFileID(){
    	return this.fileID;
    }
    public String getFileDir(Connection con) throws IOException, SQLException{
    	PreparedStatement getPatternID=con.prepareStatement("select * from project");
		ResultSet result=getPatternID.executeQuery();
		while(result.next()){
			if(result.last()){
				this.fileDirectory=result.getString(2);
				this.projectID=result.getInt(1);
//				System.out.println(this.fileDirectory);
			}
		}
		return this.fileDirectory;
    	}
		
    }
    

