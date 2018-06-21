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
	private ArrayList<ArrayList<Float>> performanceMat = new ArrayList<>();

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
	
	/**
	 * Create a window showing an evaluation table to create a MCProblem.
	 * You can define your alternatives, criteria and performance values.
	 * The MCProblem obtained is marshal in a XML file.
	 */
    public EvaluationsGUI() {
    	
    	// Construction of the different Listener
        createAlterTableListener();
        createAlternativeListener();
        createCriteriaListener();
        
        // Construction of the window
        initShell();
    	createTableInstruction();
        createTable();
        createEditor();
        createAlternativeAdder();
        createCriteriaAdder();
        
    }
	
    // EVALUATIONS GUI ACTIVATOR //
    
    /**
	 * Open the window of an EvaluationsGUI instance and keep it open
	 * while it is not closed.
	 */
	public void activeLoop() {
		shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
	}
	
	// CREATOR AND INITIALIZER FOR ITEMS //
	
    /**
	 * Initialize the dimension of the shell.
	 */
	private void initShell() {
		
	    shell.setSize(450, 150);
		shell.setMinimumSize(450, 150);
	    shell.setText("Alternatives and criteria evaluations");
	    
	}

    /**
	 * Create the label containing the instructions for the user.
	 */
	private void createTableInstruction() {
		
		label = new Label(shell, SWT.NULL);
	    
		label.setSize(400,25);
		label.setLocation(25, 25);
		label.setText("You need to enter your criterias names in the first row of the table.");
		
		label.pack();
		
	}

    /**
	 * Create the table and initialize it with one row and one column.
	 * The first row will contains the criteria header.
	 * The first column will contains the alternatives header.
	 */
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
	
	/**
	 * Create the table's performance matrix.
	 * This matrix keep actualized the criteria performances of 
	 * the different alternatives in the table.
	 */
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
	
    /**
	 * Create an editor for the table to handle the user inputs.
	 */
	private void createEditor() {
		
		editor = new TableEditor(table);
	    editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	
	}
	
    /**
	 * Create a button to add a new row on the table.
	 * Adding a row means adding an alternative slot.
	 */
	private void createAlternativeAdder() {
		
	    addAlternative = new Button(shell, SWT.PUSH);
	    addAlternative.setText("Add Alternative");
	    addAlternative.setBounds(25, 91, 25, 25);
	    addAlternative.pack();
        addAlternative.addSelectionListener(addAlternativeListener);
		
	}

    /**
	 * Create a button to add a new column on the table.
	 * Adding a column means adding a criteria slot.
	 */
	private void createCriteriaAdder() {
		
	    addCriteria = new Button(shell, SWT.PUSH);
	    addCriteria.setText("Add Criteria");
	    addCriteria.setBounds(30 + addAlternative.getBounds().width, 91, 25, 25);
	    addCriteria.pack();
        addCriteria.addSelectionListener(addCriteriaListener);
		
	}
	
	// GETTER FOR ITEMS DIMENSIONS //
    
	/** 
	 * Get the current width of the shell.
	 * 
	 * @return shell width.
	 */
    private int getShellWidth(){
    	return shell.getBounds().width;
    }
    
	/** 
	 * Get the current height of the sell.
	 * 
	 * @return shell height.
	 */
    private int getShellHeight(){
    	return shell.getBounds().height;
    }
    
	/** 
	 * Get the current width of the table.
	 * 
	 * @return table width.
	 */
    private int getTableWidth(){
    	return table.getBounds().width;
    }
    
	/** 
	 * Get the current height of the table.
	 * 
	 * @return table height.
	 */
    private int getTableHeight(){
    	return table.getBounds().height;
    }
    
	/** 
	 * Get the current width of the addAlternative button.
	 * 
	 * @return addAlternative width.
	 */
    private int getAddAltWidth(){
    	return addAlternative.getBounds().width;
    }
    
    // MODIFIER FOR ITEMS DIMENSION // 
    
    /**
	 * Increase the width of both shell and table, depending on their current width.
	 * Used when a column is added (new criterion).
	 */
	private void updateGUIWidth() {
		
		int widthNeeded = getTableWidth() + 25;
		
		if( widthNeeded >= getShellWidth()){
			shell.setSize(getShellWidth() + 50, getShellHeight());
		}

	    table.setSize(getTableWidth() + 50, getTableHeight());
	}

    /**
	 * Increase the height of both shell and table, depending on their current height.
	 * Used when a row is added (new alternative).
	 */
	private void updateGUIHeight() {
		
		shell.setSize(getShellWidth(),getShellHeight() + 18);
	    table.setSize(getTableWidth(), getTableHeight() + 18);
		
	}
	
    /**
	 * Update the position of both addAlternative button and addCriterion button.
	 * Used when a row is added (new alternative).
	 */
	private void updateAdderPosition() {
		
			addAlternative.setBounds(25, addAlternative.getBounds().y + 18, 25, 25);
	        addAlternative.pack();
	        
			addCriteria.setBounds(addAlternative.getBounds().x + getAddAltWidth() + 5, addCriteria.getBounds().y + 18, 25, 25);
			addCriteria.pack();
		
	}
	
	// LISTENER FOR THE TABLE STRUCTURE ALTERATION //

    // Creator for the listener //
	
	/**
	 * Create a listener for the addAlternative button.
	 * Handle the creation of a new row in the table.
	 */ 
    private void createAlternativeListener(){
    	
        addAlternativeListener = new SelectionListener() {
        	
        	@Override
			public void widgetSelected(SelectionEvent event) {
        		new TableItem(table, SWT.NULL);
  
        		alternativesList.add(null);
        		addPerformanceMatAlt();
        		updateGUIHeight();
        		updateAdderPosition();
        	}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
        }; 

    }
    
	/**
	 * Create a listener for the addCriteria button.
	 * Handle the creation of a new column in the table.
	 */ 
    private void createCriteriaListener(){
    	
        addCriteriaListener = new SelectionListener() {
        	
        	@Override
			public void widgetSelected(SelectionEvent event) {
        		TableColumn column = new TableColumn(table, SWT.NULL);
            	column.setWidth(50);
        		
        		criteriaList.add(null);
        		addPerformanceMatCrit();
        		updateGUIWidth();
        	}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
        }; 
 
    }
    
    // Methods used by the listener //
    
    /**
     * Add to the performance matrix a new row.
     * This row is an array and represents a new alternative.
     * Every value of the array is set to null, representing the current value of the criteria.
     */
    private void addPerformanceMatAlt(){
    	
    	ArrayList<Float> performanceList = new ArrayList<>();
    	
    	for(int i=0; i < criteriaList.size();i++){
    		performanceList.add(i,null);
    	}
		performanceMat.add(performanceList);
    	logger.info("update alt : " + performanceMat.toString());
    	
    }
    
    /**
     * Add to the performance matrix a new column.
     * This column represents a new criterion.
     * Every alternative array have a null value added, representing the current value of the new criterion.
     */
    private void addPerformanceMatCrit(){
    	
    	ArrayList<Float> performanceList = new ArrayList<>();
    	
    	for(int i=0; i<alternativesList.size();i++){
    		performanceList = performanceMat.get(i);
    		performanceList.add(i,null);
    		performanceMat.set(i, performanceList);
    	}
    	logger.info("update crit : " + performanceMat.toString());
    	
    }
    
	// LISTENER FOR THE TABLE VALUE ALTERATION //

    // Creator for the listener //
    
	/**
	 * Create a listener to handle the alterations of the table.
	 * When a cell is clicked by the user, a text editor pop. 
	 */ 
    private void createAlterTableListener(){
    	
    	alterTableListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
			
		        Point pt = new Point(event.x, event.y);
		        
	            int column = getTableColumn(pt);
		        int index = getTableIndex(pt);
		        
		        TableItem item = table.getItem(index);
	            final Text text = new Text(table, SWT.BORDER);
	            
	            // Can't choose the header's cell "AlternativeID"
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
	
	// Methods used by the listener //
	
	/**
	 * Get the index of the table's cell depending on the user's click position.
	 * 
	 * @param pt must not be null
	 * @return the index of the table's cell.
	 */
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
    
	/**
	 * Get the column of the table's cell depending on the user's click position.
	 * 
	 * @param pt must not be null
	 * @return the column of the table's cell.
	 */
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
    
    /**
	 * Create a listener for the addCriteria button.
	 * Handle the creation of a new column in the table.
	 */ 
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
    
    /**
	 * Create a listener for the addCriteria button.
	 * Handle the creation of a new column in the table.
	 */ 
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
    
    /**
     * Update the alternatives list. Put the alternative Id contained by the text.
     * Then call the marshaler to get a MCProblem.
     *
     * @param altId can not be null
     * @throws FileNotFoundException
     * @throws JAXBException
     * @throws IOException
     */
    private void updateAlternativeList(Text altId) throws FileNotFoundException, JAXBException, IOException {
    	alternativesList.set(Integer.parseInt(altId.getData().toString())-1, altId.getText());
    	logger.info(" alternativesList : " + alternativesList.toString());
		marshall();
	}
	
    /**
     * Update the criteria list. Put the criteria Id contained by the text.
     * Then call the marshaler to get a MCProblem.
     *
     * @param critId can not be null
     * @throws FileNotFoundException
     * @throws JAXBException
     * @throws IOException
     */
	private void updateCriteriaList(Text critId) throws FileNotFoundException, JAXBException, IOException {
		criteriaList.set(Integer.parseInt(critId.getData().toString())-1, critId.getText());
    	logger.info(" criteriaList : " + criteriaList.toString());
		marshall();
	}
	
	/**
	 * Update the performance matrix with the performance value contained by the text.
     * Then call the marshaler to get a MCProblem.
	 * 
	 * @param altId can not be null
	 * @param critId can not be null
	 * @param perf can not be null
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 * @throws IOException
	 */
	private void updatePerformanceMat(int altId, int critId, Text perf) throws FileNotFoundException, JAXBException, IOException {
		setPerformanceMat(altId-1, critId-1, Float.parseFloat(perf.getText()));
		marshall();
	}
	
	/**
	 * Set the performance matrix. 
	 * Put the performance value for the corresponding alternative and criteria.
	 * 
	 * @param alt can not be null
	 * @param crit can not be null
	 * @param perf can not be null
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 * @throws IOException
	 */
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
	
	/**
	 * Create a MCProblem with the current association of alternative, criteria and performance values.
	 * Then the MCProblem is marshaled and output in an XML file.
	 * 
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 * @throws IOException
	 */
    private void marshall() throws FileNotFoundException, JAXBException, IOException {
    	mcp = new MCProblem();
    	marshaller = new MCProblemMarshaller(mcp);
    	
    	String path="MCPFile.xml";
    	
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
    	
    	
    	try (final FileOutputStream fos = new FileOutputStream(path)) {
			marshaller.marshalAndWrite(fos);
			System.out.println("Marshalled");
		}
    }
    
}