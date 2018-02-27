package gr.uom.java.pattern.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.tree.DefaultMutableTreeNode;

import java.util.Set;
import java.util.SortedSet;
import java.util.Vector;

import gr.uom.java.bytecode.BytecodeReader;
import gr.uom.java.bytecode.FieldObject;
import gr.uom.java.bytecode.MethodObject;
import gr.uom.java.bytecode.SystemObject;
import gr.uom.java.pattern.BehavioralData;
import gr.uom.java.pattern.ClusterResult;
import gr.uom.java.pattern.ClusterSet;
import gr.uom.java.pattern.MatrixContainer;
import gr.uom.java.pattern.PatternDescriptor;
import gr.uom.java.pattern.PatternEnum;
import gr.uom.java.pattern.PatternGenerator;
import gr.uom.java.pattern.PatternInstance;
import gr.uom.java.pattern.SimilarityAlgorithm;
import gr.uom.java.pattern.SystemGenerator;
import gr.uom.java.pattern.gui.LongTask.LongActualTask;
import gr.uom.java.pattern.gui.progress.DatabaseLayer;
import gr.uom.java.pattern.gui.progress.DetectionFinishedEvent;
import gr.uom.java.pattern.gui.progress.PatternDetectionSource;
import gr.uom.java.pattern.inheritance.Enumeratable;

public class Console {
	private LinkedHashMap<String, Vector<PatternInstance>> map = new LinkedHashMap<String, Vector<PatternInstance>>();
	
    public Console() throws Throwable {
        //detectPatternInstances(inputDir);   
       
        //saveDesignPatternInstances(); 

        //new XMLExporter(map,outputXML);
    }

	public void detectPatternInstances(File inputDir) {
		BytecodeReader br = new BytecodeReader(inputDir);
        SystemObject so = br.getSystemObject();
        SystemGenerator sg = new SystemGenerator(so);
        SortedSet<ClusterSet.Entry> clusterSet = sg.getClusterSet().getInvokingClusterSet();
        List<Enumeratable> hierarchyList = sg.getHierarchyList();
        

        PatternEnum[] patternEnum = PatternEnum.values();
        for(int i=0; i<patternEnum.length; i++) {
            String patternName = patternEnum[i].toString();
            PatternDescriptor patternDescriptor = PatternGenerator.getPattern(patternName);
            if(patternDescriptor.getNumberOfHierarchies() == 0) {
                MatrixContainer systemContainer = sg.getMatrixContainer();
                double[][] systemMatrix = null;
                BehavioralData behavioralData = null;
                if(patternName.equals(PatternEnum.SINGLETON.toString())) {
                    systemMatrix = systemContainer.getSingletonMatrix();
                    behavioralData = systemContainer.getSingletonBehavioralData();
                }
                else if(patternName.equals(PatternEnum.TEMPLATE_METHOD.toString())) {
                    systemMatrix = systemContainer.getTemplateMethodMatrix();
                    behavioralData = systemContainer.getTemplateMethodBehavioralData();
                }
                else if(patternName.equals(PatternEnum.FACTORY_METHOD.toString())) {
                    systemMatrix = systemContainer.getFactoryMethodMatrix();
                    behavioralData = systemContainer.getFactoryMethodBehavioralData();
                }

                Vector<PatternInstance> patternInstanceVector = new Vector<PatternInstance>();
                for(int j=0; j<systemMatrix.length; j++) {
                    if(systemMatrix[j][j] == 1.0) {
                        PatternInstance patternInstance = new PatternInstance();
                        patternInstance.addEntry(patternInstance.new Entry(patternDescriptor.getClassNameList().get(0),systemContainer.getClassNameList().get(j),j));
                        if(behavioralData != null) {
                        	if(patternDescriptor.getFieldRoleName() != null) {
                        		Set<FieldObject> fields = behavioralData.getFields(j, j);
                        		if(fields != null) {
                        			for(FieldObject field : fields) {
                        				patternInstance.addEntry(patternInstance.new Entry(patternDescriptor.getFieldRoleName(), field.toString(), -1));
                        			}
                        		}
                        	}
                        	if(patternDescriptor.getMethodRoleName() != null) {
                        		Set<MethodObject> methods = behavioralData.getMethods(j, j);
                        		if(methods != null) {
                        			for(MethodObject method : methods) {
                        				patternInstance.addEntry(patternInstance.new Entry(patternDescriptor.getMethodRoleName(), method.getSignature().toString(), -1));
                        			}
                        		}
                        	}
                        }
                        patternInstanceVector.add(patternInstance);
                    }
//                	for(PatternInstance instance : patternInstanceVector) {
//                		Iterator<PatternInstance.Entry> entryIterator = instance.getRoleIterator();
//                		while(entryIterator.hasNext()) {
//                			PatternInstance.Entry entry = entryIterator.next();
//                	    	String roleName = entry.getRoleName();
//                	    	String elementName = entry.getElementName();
//                		}
//                	}
                }
                map.put(patternName,patternInstanceVector);
            }
            else if(patternDescriptor.getNumberOfHierarchies() == 1) {
                Vector<PatternInstance> patternInstanceVector = new Vector<PatternInstance>();
                for(Enumeratable ih : hierarchyList) {
                    List<Enumeratable> tempList = new ArrayList<Enumeratable>();
                    tempList.add(ih);
                    MatrixContainer hierarchyMatrixContainer = sg.getHierarchiesMatrixContainer(tempList);
                    generateResults(hierarchyMatrixContainer, patternDescriptor, patternInstanceVector);
                }
                map.put(patternName,patternInstanceVector);
            }
            else if(patternDescriptor.getNumberOfHierarchies() == 2) {
                Iterator<ClusterSet.Entry> it = clusterSet.iterator();
                Vector<PatternInstance> patternInstanceVector = new Vector<PatternInstance>();
                while(it.hasNext()) {
                    ClusterSet.Entry entry = it.next();
                    MatrixContainer hierarchiesMatrixContainer = sg.getHierarchiesMatrixContainer(entry.getHierarchyList());
                    generateResults(hierarchiesMatrixContainer, patternDescriptor, patternInstanceVector);
                }
                map.put(patternName,patternInstanceVector);
            }
        }
//        LongTask longTask = new LongTask(sg);
//        longTask.go();
	}

	public boolean hasDesignPatternInstances()
	{
		//return !map.isEmpty();
		 Set<Entry<String, Vector<PatternInstance>>> entrySet = map.entrySet();
	     Iterator<Entry<String, Vector<PatternInstance>>> it = entrySet.iterator();
	     while(it.hasNext())
	        {
	        	Map.Entry me = (Map.Entry)it.next();
	        	System.out.println();
	            //System.out.println(me.getKey() + ": ");
	            //System.out.print(me.getValue());
	            Vector<PatternInstance> vector = (Vector<PatternInstance>) me.getValue();
	            if(!vector.isEmpty())
	            {
	            	return true;
	            }
	            
	        }
	     return false;
	        
	}
	public void saveDesignPatternInstances()
			throws Exception, Throwable {
		 //iterating the hashmap and inserting the pattern instances into the DB
        DatabaseLayer DBobj = new DatabaseLayer();
        Set<Entry<String, Vector<PatternInstance>>> entrySet = map.entrySet();
        Iterator<Entry<String, Vector<PatternInstance>>> it = entrySet.iterator();
               
		while(it.hasNext())
        {
        	Map.Entry me = (Map.Entry)it.next();
            //System.out.print(me.getKey() + ": ");
            //System.out.println(me.getValue());
            Vector<PatternInstance> piVector = (Vector<PatternInstance>) me.getValue();
            
            //DBobj.patternName = me.getKey();
            //DBobj.patternInstanceVector = piVector;
            DBobj.populatePatternInstance((String) me.getKey(), piVector);
            System.out.println(me.getKey()+" "+piVector);
            
        }
        DBobj.closeConnection();
	}

    private void generateResults(MatrixContainer systemContainer ,PatternDescriptor patternDescriptor, Vector<PatternInstance> patternInstanceVector) {
        double[][] results = SimilarityAlgorithm.getTotalScore(systemContainer,patternDescriptor);
        if(results != null) {
            ClusterResult clusterResult = new ClusterResult(results, patternDescriptor, systemContainer);
            List<PatternInstance> list = clusterResult.getPatternInstanceList();
            for (PatternInstance pi : list) {
                if (!patternInstanceVector.contains(pi))
                    patternInstanceVector.add(pi);
            }
        }
    }
}
