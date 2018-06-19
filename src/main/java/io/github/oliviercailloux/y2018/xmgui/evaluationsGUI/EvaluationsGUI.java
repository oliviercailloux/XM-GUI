package io.github.oliviercailloux.y2018.xmgui.evaluationsGUI;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;
import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;
import io.github.oliviercailloux.y2018.xmgui.file1.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.TableEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvaluationsGUI {
	
	private final Logger logger = LoggerFactory.getLogger(EvaluationsGUI.class);
	
    private final Display display = Display.getDefault();
    private final Shell shell = new Shell(display);
	
	private MCProblem mcp;
	private MCProblemMarshaller marshaller;
	private ArrayList<String> alternativesList = new ArrayList<>();
	private ArrayList<String> criteriaList = new ArrayList<>();
	private ArrayList<ArrayList> performanceMat = new ArrayList<ArrayList>();

    private Label label;
    
	private Table table;
    private TableEditor editor;
	
	private Button addAlternative;
	private Button addCriteria;

	private SelectionListener addCriteriaListener;
	private SelectionListener addAlternativeListener;
    
	private Listener alterTableListener;
	private Listener putTableValueListener;
	private ModifyListener textListener;
	
    public EvaluationsGUI() {
    	
    	// Compilation of the different Listener
        createAlterTableListener();
        createAlternativeListener();
        createCriteriaListener();
        
        // Compilation of the SWT window
        createShell();
    	createTableInstruction();
        createTable();
        createEditor();
        createAlternativeAdder();
        createCriteriaAdder();
        
    }
	
	public void activeLoop() {
		shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
	}
	
	private void createShell() {
		
	    shell.setSize(450, 150);
		shell.setMinimumSize(450, 150);
	    shell.setText("Alternatives and criteria evaluations");
	    
	}
	
	private void createTableInstruction() {
		
		label = new Label(shell, SWT.NULL);
	    
		label.setSize(400,25);
		label.setLocation(25, 25);
		label.setText("You need to enter your criterias names in the first row of the table.");
		
		label.pack();
		
	}
	
	private void createTable() {
		
		table = new Table(shell,SWT.NULL);
	    table.setLinesVisible(true);
	    
	    TableColumn colAlt = new TableColumn(table, SWT.NULL);
	    colAlt.setWidth(100);
	    
	    TableColumn column = new TableColumn(table, SWT.NULL);
	    column.setWidth(50);
	    
	    TableItem itemHeader = new TableItem(table, SWT.NULL);
		itemHeader.setText(0,"AlternativeID");
		
		TableItem item = new TableItem(table, SWT.NULL);
		 
		System.out.println(item.getBounds(0).height);
	    table.setBounds(25, 50, 150, 36); 
	    
		alternativesList.add(0, null);
		criteriaList.add(0, null);
	    createPerformanceMat();
	    
	    table.addListener(SWT.MouseDown, alterTableListener);
	    
	}
	
	private void createEditor() {
		
		editor = new TableEditor(table);
	    editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	
	    
	}
	
	private void createAlternativeAdder() {
		
	    addAlternative = new Button(shell, SWT.PUSH);
	    addAlternative.setText("Add Alternative");
	    addAlternative.setBounds(25, 91, 25, 25);
	    addAlternative.pack();
        addAlternative.addSelectionListener(addAlternativeListener);
		
	}
	
	private void createCriteriaAdder() {
		
	    addCriteria = new Button(shell, SWT.PUSH);
	    addCriteria.setText("Add Criteria");
	    addCriteria.setBounds(30 + addAlternative.getBounds().width, 91, 25, 25);
	    addCriteria.pack();
        addCriteria.addSelectionListener(addCriteriaListener);
		
	}
	
	private void setShellSize(Object object) {
		
		int widthNeeded = getTableWidth() + 25;
		
		if(object == addAlternative){
	        shell.setSize(getShellWidth(),getShellHeight() + 18);
		}
		if(object == addCriteria){
			if( widthNeeded >= getShellWidth()){
				shell.setSize(getShellWidth() + 50, getShellHeight());
			}
		}
		
	}
	
	private void setTableSize(Object object) {
		
		if(object == addAlternative){
	        table.setSize(getTableWidth(), getTableHeight() + 18);
		}
		if(object == addCriteria){
	        table.setSize(getTableWidth() + 50, getTableHeight());
		}
		
	}
	
	private void setButtonPosition(Object object) {
		
		if(object == addAlternative){
			addAlternative.setBounds(25, addAlternative.getBounds().y + 18, 25, 25);
	        addAlternative.pack();
			addCriteria.setBounds(addAlternative.getBounds().x + addAlternative.getBounds().width + 5,
	  			  	  addCriteria.getBounds().y + 18, 25, 25);
			addCriteria.pack();
		}
	}
	
    
    
    private int getTableWidth(){
    	return table.getBounds().width;
    }
    
    private int getTableHeight(){
    	return table.getBounds().height;
    }
    
    private int getShellWidth(){
    	return shell.getBounds().width;
    }
    
    private int getShellHeight(){
    	return shell.getBounds().height;
    }
    
    private int getTableIndex(Point pt){
    	int position;
    	
    	position = pt.y;
    	for(int i=0; i <= table.getItemCount()-1; i++){
    		position -= table.getItem(i).getBounds().height;
    		if(position <= 0){
    			return i;
    		}
    	}
    	return 0;
    }
    
    private int getTableColumn(Point pt){
    	int position;
    	
    	position = pt.x;
    	for(int i=0; i <= table.getColumnCount()-1; i++){
    		position -= table.getColumn(i).getWidth();
    		if(position <= 0){
    			return i;
    		}
    	}
    	return 0;
    }
    
    private void createAlterTableListener(){
    	
    	alterTableListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
			
		        Point pt = new Point(event.x, event.y);
	            int column = getTableColumn(pt);
		        int index = getTableIndex(pt);
		        TableItem item = table.getItem(index);
	            final Text text = new Text(table, SWT.BORDER);
	            
	            // Can't choose the header cell "AlternativeID"
	            if(column == 0 && index ==0){
	            	return;
	            }
	            
		        putTableValueListener = new Listener() {
		        	@Override
		            public void handleEvent(Event event) {
		        		putTableValue(event,item, index, column, text);
		            }
		        };
		        
		        textListener = new ModifyListener() {
		    		@Override
		            public void modifyText(ModifyEvent event) {
		    			Text text = (Text) event.widget;
			    		putMCPValue(item,index, column,text);
		            }
		        };
		            		
		        text.addListener(SWT.FocusOut, putTableValueListener);
		        text.addListener(SWT.Traverse, putTableValueListener);
		        text.addModifyListener(textListener);
		        editor.setEditor(text, item, column);
		        text.setText(item.getText(column));
		        text.selectAll();
		        text.setFocus();
		        
		        return;

			}
        };

    }
    
    private void createAlternativeListener(){
    	
        addAlternativeListener = new SelectionListener() {
        	
        	@Override
			public void widgetSelected(SelectionEvent event) {
        		new TableItem(table, SWT.NULL);
  
        		alternativesList.add(null);
        		addPerformanceMatAlt();
        		setTableSize(event.getSource());
        		setButtonPosition(event.getSource());
        		setShellSize(event.getSource());
        	}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
        }; 

    }
    
    private void createCriteriaListener(){
        addCriteriaListener = new SelectionListener() {
        	
        	@Override
			public void widgetSelected(SelectionEvent event) {
        		TableColumn column = new TableColumn(table, SWT.NULL);
            	column.setWidth(50);
        		
        		criteriaList.add(null);
        		addPerformanceMatCrit();
            	setTableSize(event.getSource());
            	setShellSize(event.getSource());
        	}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
        }; 
 
    }
    
    private void putTableValue(Event event, TableItem item, int index, int column, Text text){
    	
    	switch (event.type) {
	    	case SWT.FocusOut:
	    		item.setText(column, text.getText());
	            text.dispose();
	            break;
	                  
	        case SWT.Traverse:
	            switch (event.detail) {
	                      
	            	case SWT.TRAVERSE_RETURN:
	            		item.setText(column, text.getText());
	                            
	                case SWT.TRAVERSE_ESCAPE:
	                    text.dispose();
	                    event.doit = false;
	            }
	        break;	
    	}
    }
    
    private void putMCPValue(TableItem item,int index, int column, Text text){
    	 
        if(column == 0){
        	text.setData(index);
        }
        if(index == 0){
        	text.setData(column);
        }
        
        if(column==0 && index != 0){
	    	try {
				Integer.parseInt(text.getText());
				updateAlternativeList(text);
			}
			catch(Exception isNull)
			{	
			}
        }
        
        if(column!=0 && index == 0){
	    	try {
				Integer.parseInt(text.getText());
				updateCriteriaList(text);
			}
			catch(Exception isNull)
			{
			}
        }
        
        if(column!=0 && index != 0){
	    	try {
				Float.parseFloat(text.getText());
				updatePerformanceMat(index,column,text);
			}
			catch(Exception isNull)
			{
			}
        }
    	
    }

    private void createPerformanceMat(){
    	
    	ArrayList<Float> performanceList = new ArrayList<>();
    	
    	for(int i=0; i < alternativesList.size();i++){
	    	for(int j=0; j<criteriaList.size();j++){
	    		performanceList.add(j,null);
	    	}
	    	performanceMat.add(i,performanceList);
    	}
    	
    	logger.info("create : " + performanceMat.toString());
    	
    }
    
    private void addPerformanceMatAlt(){
    	
    	ArrayList<Float> performanceList = new ArrayList<>();
    	
    	for(int i=0; i < criteriaList.size();i++){
    		performanceList.add(i,null);
    	}
		performanceMat.add(performanceList);
    	logger.info("update alt : " + performanceMat.toString());
    	
    }
    
    private void addPerformanceMatCrit(){
    	
    	ArrayList<Float> performanceList = new ArrayList<>();
    	
    	for(int i=0; i<alternativesList.size();i++){
    		performanceList = performanceMat.get(i);
    		performanceList.add(i,null);
    		performanceMat.set(i, performanceList);
    	}
    	logger.info("update crit : " + performanceMat.toString());
    	
    }
    
    private void setPerformanceMat(int alt, int crit, Float perf){
    	
    	ArrayList<Float> performanceList = new ArrayList<>();
    		
    	for(int i=0; i<criteriaList.size();i++){
    		if(criteriaList.get(i) == null){
    			performanceList.add(i,null);
    		}
    		if(criteriaList.get(i) != null && i != crit){
    			performanceList.add(i, Float.parseFloat(table.getItem(alt+1).getText(i+1).toString()));
    		}
    		if(criteriaList.get(i) != null && i == crit){
    			performanceList.add(crit, perf);
    		}
    	}
    	
    	performanceMat.set(alt,performanceList);
    	logger.info(" set : " + performanceMat.toString());
    	
    }
    
    private void updateAlternativeList(Text altId) throws FileNotFoundException, JAXBException, IOException {
    	alternativesList.set(Integer.parseInt(altId.getData().toString())-1, altId.getText());
    	logger.info(" alternativesList : " + alternativesList.toString());
		marshall();
	}
	
	private void updateCriteriaList(Text critId) throws FileNotFoundException, JAXBException, IOException {
		criteriaList.set(Integer.parseInt(critId.getData().toString())-1, critId.getText());
    	logger.info(" criteriaList : " + criteriaList.toString());
		marshall();
	}
	
	private void updatePerformanceMat(int altId, int critId, Text perf) throws FileNotFoundException, JAXBException, IOException {
		setPerformanceMat(altId-1, critId-1, Float.parseFloat(perf.getText()));
		marshall();
	}
    
    private void marshall() throws FileNotFoundException, JAXBException, IOException {
    	mcp = new MCProblem();
    	marshaller = new MCProblemMarshaller(mcp);
    	
    	for (int i =0; i < alternativesList.size(); i++) {
			if (alternativesList.get(i) != null) {
				mcp.addAlt(new Alternative(Integer.parseInt(alternativesList.get(i))));
				logger.info("Alternatives in the MCP: {}." + mcp.getAlternatives());
			}
		}
    	for (int i =0; i < criteriaList.size(); i++) {
			if (criteriaList.get(i) != null) {
				mcp.addCrit(new Criterion(Integer.parseInt(criteriaList.get(i))));
				logger.info("Criteria in the MCP: {}." + mcp.getCriteria());
			}
		}

    	for (int i =0; i < alternativesList.size(); i++) {
			if (alternativesList.get(i) != null) {
	    		Alternative alt = new Alternative(Integer.parseInt(alternativesList.get(i)));
	    		for(int j=0; j < criteriaList.size(); j++){
	    			if (criteriaList.get(j) != null) {
		    			Criterion crit = new Criterion(Integer.parseInt(criteriaList.get(j)));
		    			try{
			    			mcp.putEvaluation(alt,crit,Float.parseFloat(performanceMat.get(i).get(j).toString()));
							logger.info("Performance in the MCP: {}." + mcp.getTableEval());
		    			}catch(Exception wrongInput){
		    			}
	    			}
				}
	    	}
		}
    	
    	
    	try (final FileOutputStream fos = new FileOutputStream("MCPEvaluationsGUI.xml")) {
			marshaller.marshalAndWrite(fos);
			System.out.println("Marshalled");
		}
    }
    
}