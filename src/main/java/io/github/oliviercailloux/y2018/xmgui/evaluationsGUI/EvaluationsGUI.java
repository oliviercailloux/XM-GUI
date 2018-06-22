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
	
	private final Logger LOGGER = LoggerFactory.getLogger(EvaluationsGUI.class);
	
    private final Display display = Display.getDefault();
    private final Shell shell = new Shell(display);
	
    private String path;
    
	private MCProblem mcp;
	private MCProblemMarshaller marshaller;
	private ArrayList<String> alternativesList = new ArrayList<>();
	private ArrayList<String> criteriaList = new ArrayList<>();
	private ArrayList<ArrayList<Float>> performanceMat = new ArrayList<>();
	
	private int marginTop = 25;
	private int marginBottom = 25;
	private int marginLeft = 25;
	private int marginRight = 25;
	private int itemHeight = 18;
	private int columnWidth = 50;

    private Label label;
    
	private Table table;
    private TableEditor editor;
	
	private Point clickPoint;
	private Text textEdit;
	private TableItem activeItem;
	private int activeIndex;
	private int activeColumn;
	
	private Button addAlternative;
	private Button addCriteria;

    private SelectionListener adderListener;
    
	private Listener alterTableListener;
	private Listener putTableValueListener;
	
	private ModifyListener textListener;
	
	/**
	 * Create a window showing an evaluation table to create a MCProblem.
	 * You can define your alternatives, criteria and performance values.
	 * The MCProblem obtained is marshal in a XML file.
	 */
    public EvaluationsGUI() {
    	
    	// Listener
        createAlterTableListener();
        createAdderListener();
        
        // Window
    	createTableInstruction();
        createTable();
        createEditor();
        createAdder();
        initShell();
        
        // Output file path (default)
        setFilePath("MCPFile.xml");
        
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
 	 * Get the current width of the label instruction.
 	 * 
 	 * @return label instruction width.
 	 */
     private int getLabelWidth(){
     	return label.getBounds().width;
     }
     
 	/** 
 	 * Get the current height of the label instruction.
 	 * 
 	 * @return label instruction height.
 	 */
     private int getLabelHeight(){
     	return label.getBounds().height;
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
 	 * Get the current X axis position of the table.
 	 * 
 	 * @return table X axis position.
 	 */
     private int getTableXAxis(){
     	return table.getBounds().x;
     }
     
 	/** 
 	 * Get the current Y axis position of the table.
 	 * 
 	 * @return table Y axis position.
 	 */
     private int getTableYAxis(){
     	return table.getBounds().y;
     }
     
 	/** 
 	 * Get the current width of the addAlternative button.
 	 * 
 	 * @return addAlternative width.
 	 */
     private int getAddAltWidth(){
     	return addAlternative.getBounds().width;
     }
     
 	/** 
 	 * Get the current width of the addAlternative button.
 	 * 
 	 * @return addAlternative width.
 	 */
     private int getAddAltHeight(){
     	return addAlternative.getBounds().height;
     }
 	
 	/** 
 	 * Get the current X axis position of the addAlternative button.
 	 * 
 	 * @return addAlternative X axis position.
 	 */
     @SuppressWarnings("unused")
     private int getAddAltXAxis(){
     	return addAlternative.getBounds().x;
     }
 	
 	/** 
 	 * Get the current Y axis position of the addAlternative button.
 	 * 
 	 * @return addAlternative Y axis position.
 	 */
     private int getAddAltYAxis(){
     	return addAlternative.getBounds().y;
     }
     
 	/** 
 	 * Get the current X axis position of the addCriteria button.
 	 * 
 	 * @return addCriteria X axis position.
 	 */
     private int getAddCritXAxis(){
     	return addCriteria.getBounds().x;
     }
     
 	/** 
 	 * Get the current Y axis position of the addCriteria button.
 	 * 
 	 * @return addCriteria Y axis position.
 	 */
     private int getAddCritYAxis(){
     	return addCriteria.getBounds().y;
     }
     
    // EVALUATIONS GUI PUBLIC METHODS //
    
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
	
    /**
     * Set the output path for the MCProblem file (XML).
     * 
     * @param filePath can not be null, it's the output path wanted
     */
    public void setFilePath(String filePath){
    	path = filePath;
    }
	
	/**
	 * Get the path of the MCProblem file.
	 * 
	 * @return file path
	 */
    public String getFile(){
    	return path;
    }
    
	// CREATOR AND INITIALIZER FOR ITEMS //
	
    /**
	 * Create the label containing the instructions for the user.
	 */
	private void createTableInstruction() {
		
		label = new Label(shell, SWT.NULL);
	    
		label.setLocation(marginLeft, marginTop);
		label.setText("You need to enter your criterias names in the first row of the table.");
		label.pack();
		
	}

    /**
	 * Create the table and initialize it with one row and one column.
	 * The first row will contains the criteria header.
	 * The first column will contains the alternatives header.
	 */
	private void createTable() {
		
		int width;
		int height;
		
		table = new Table(shell,SWT.NULL);
		
	    table.setLinesVisible(true);
	    
	    TableColumn columnAlt = new TableColumn(table, SWT.NULL);
	    TableColumn column = new TableColumn(table, SWT.NULL);

	    columnAlt.setWidth(100);
	    column.setWidth(columnWidth);
	    width = 100 + columnWidth;
	    
	    TableItem itemHeader = new TableItem(table, SWT.NULL);
		new TableItem(table, SWT.NULL);

		height = 2 * itemHeight;
		
		itemHeader.setText(0,"AlternativeID");
	    
	    table.addListener(SWT.MouseDown, alterTableListener);
	    
	    
	    table.setBounds(marginLeft, marginTop + getLabelHeight() + 5, width, height); 
	    
		alternativesList.add(0, null);
		criteriaList.add(0, null);
	    initPerformanceMat();
	    
	}
	
	/**
	 * Create the table's performance matrix.
	 * This matrix keep actualized the criteria performances of 
	 * the different alternatives in the table.
	 */
    private void initPerformanceMat(){
    	
    	ArrayList<Float> performanceList = new ArrayList<>();
    	
    	for(int i=0; i < alternativesList.size();i++){
	    	for(int j=0; j<criteriaList.size();j++){
	    		performanceList.add(j,null);
	    	}
	    	performanceMat.add(i,performanceList);
    	}
    	
    	LOGGER.info("create : " + performanceMat.toString());
    	
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
	 * Create a button to add a new column on the table.
	 * Adding a row means adding an alternative slot.
	 * Adding a column means adding a criteria slot.
	 */
	private void createAdder() {
		
		int YAxis = getTableYAxis() + getTableHeight() + 5;
	
	    addAlternative = new Button(shell, SWT.PUSH);
	    addAlternative.setText("Add Alternative");
	    addAlternative.setLocation(getTableXAxis(), YAxis);
	    addAlternative.pack();
        
        int XAxis = getTableXAxis() + getAddAltWidth() + 5;
        
	    addCriteria = new Button(shell, SWT.PUSH);
	    addCriteria.setText("Add Criteria");
	    addCriteria.setLocation(XAxis, YAxis);
	    addCriteria.pack();
	    
        addAlternative.addSelectionListener(adderListener);
        addCriteria.addSelectionListener(adderListener);
		
	}
	
    /**
	 * Initialize the dimension of the shell and the inner margin.
	 */
	private void initShell() {
		
		int width = getLabelWidth() + marginLeft + marginRight;
		int height = getLabelHeight() + getTableHeight() + getAddAltHeight() + 5 + marginTop + marginBottom;
		
	    shell.setSize(width, height);
		shell.setMinimumSize(width, height);
	    shell.setText("Alternatives and criteria evaluations");
	    
	}

    // MODIFIER FOR ITEMS DIMENSION // 
	
    /**
	 * Increase the height of both shell and table, depending on their current height.
	 * Used when a row is added (new alternative).
	 * Increase the width of both shell and table, depending on their current width.
	 * Used when a column is added (new criterion).
	 * 
	 * @param event can not be null
	 */
	private void updateGUISize(Object adder) {
		
		if(adder == addAlternative){
			shell.setSize(getShellWidth(), getShellHeight() + itemHeight);
		    table.setSize(getTableWidth(), getTableHeight() + itemHeight);
		}
		
		if(adder == addCriteria){

		    table.setSize(getTableWidth() + columnWidth, getTableHeight());
		    
			int widthNeeded = getTableWidth() + marginLeft + marginRight;
			
			if( widthNeeded >= getShellWidth()){
				shell.setSize(widthNeeded, getShellHeight());
			}
			
		}
		
	}
	
    /**
	 * Update the position of both addAlternative button and addCriterion button.
	 * Used when a row is added (new alternative).
	 */
	private void updateAdderPosition() {
		
			addAlternative.setLocation(getTableXAxis(), getAddAltYAxis() + itemHeight);
	        addAlternative.pack();
	        
			addCriteria.setLocation(getAddCritXAxis(), getAddCritYAxis() + itemHeight);
			addCriteria.pack();
		
	}
	
	// LISTENER FOR THE TABLE STRUCTURE ALTERATION //

    // Creator for the listener //

	/**
	 * Create a listener for the addAlternative and addCriteria buttons.
	 * Handle the creation of a new row or a new column in the table.
	 */ 
    private void createAdderListener(){
    	
        adderListener = new SelectionListener() {
        	
        	@Override
			public void widgetSelected(SelectionEvent event) {
        		Object adder = event.getSource();
        		
        		if(adder == addAlternative){
	        		new TableItem(table, SWT.NULL);
	        		
	        		alternativesList.add(null);
	        		addPerformanceMat(adder);
	        		updateGUISize(adder);
	        		updateAdderPosition();
        		}
        		
        		if(adder == addCriteria){
            		TableColumn column = new TableColumn(table, SWT.NULL);
                	column.setWidth(columnWidth);
            		
            		criteriaList.add(null);
            		addPerformanceMat(adder);
            		updateGUISize(adder);
        		}
        	}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
        }; 

    }
    
    // Methods used by the listener //
    
    /**
     * CASE ADD ALTERNATIVE :
     * Add to the performance matrix a new row.
     * This row is an array and represents a new alternative.
     * Every value of the array is set to null, representing the current value of the criteria.
     * 
     * CASE ADD CRITERIA :
     * Add to the performance matrix a new column.
     * This column represents a new criterion.
     * Every alternative array have a null value added, representing the current value of the new criterion.
     */
    private void addPerformanceMat(Object adder){
    	
    	ArrayList<Float> performanceList = new ArrayList<>();
    	
    	if(adder == addAlternative){
	    	for(int i=0; i < criteriaList.size();i++){
	    		performanceList.add(i,null);
	    	}
			performanceMat.add(performanceList);
	    	LOGGER.info("Update alternative in performance matrix : " + performanceMat.toString());
    	}
    	
    	if(adder == addCriteria){
    		for(int i=0; i < alternativesList.size();i++){
        		performanceList = performanceMat.get(i);
        		performanceList.add(null);
        		performanceMat.set(i, performanceList);
        	}
        	LOGGER.info("Update criteria in performance matrix : " + performanceMat.toString());
    	}
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
			
		        clickPoint = new Point(event.x, event.y);
		    	textEdit = new Text(table, SWT.NULL);

		        setActiveItem(clickPoint);
		        
	            // Can't choose the header's cell "AlternativeID"
	            if(activeColumn == 0 && activeIndex ==0){
	            	return;
	            }

		        createPutTableValueListener(textEdit, activeItem, activeColumn);
		        createTextListener();
		        
		        setTextEdit();
	            
		        return;

			}
        };

    }
	
	// Methods used by the listener //
    
    /**
     * Create a listener to update a table item value according to the user input.
     * 
     * @param textEdit can not be null, the new item value
     * @param item can not be null, the table item
     * @param column can not be null, the table item's column
     */
    private void createPutTableValueListener(Text text, TableItem item, int column){
    	
    	putTableValueListener = new Listener() {
        	@Override
            public void handleEvent(Event event) {
        		putTableValue(event, text, item, column);
            }
        };
        
    }
    
    /**
     * Create a listener to update the MCProblem according to the user input. 
     */
    private void createTextListener(){
    	
    	textListener = new ModifyListener() {
    		@Override
            public void modifyText(ModifyEvent event) {
	    		putMCPValue((Text) event.widget);
            }
        };
        
    }
    
    /**
     * 
     * 
     * @param pt can not be null
     */
    private void setActiveItem(Point pt){
    	
    	activeIndex = getTableIndex(pt);
    	activeColumn = getTableColumn(pt);
    	activeItem = table.getItem(activeIndex);
    	
    }
    
    /**
     * Set the text editor to the table cell selected by the user.
     */
    private void setTextEdit(){
    	
        textEdit.addListener(SWT.FocusOut, putTableValueListener);
        textEdit.addListener(SWT.Traverse, putTableValueListener);
        textEdit.addModifyListener(textListener);
        editor.setEditor(textEdit, activeItem, activeColumn);
        textEdit.setText(activeItem.getText(activeColumn));
        textEdit.selectAll();
        textEdit.setFocus();
        
    }
	
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
     * Update the table item with the new input value.
     * 
     * @param event can not be null
     * @param text can not be null
     * @param item can not be null
     * @param column can not be null
     */
    private void putTableValue(Event event, Text text, TableItem item, int column){
    	
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
	                	
	                case SWT.DEFAULT:
	                	break;
	            }
	            break;
	            
	        case SWT.DEFAULT:
	        	break;
    	}
    	
    }
    
    /**
     * Update the MCProblem with the new input on the table.
     * Relatively to the index and the column of the item, 
     * the value updated is different. It can be an alternative Id, 
     * a criterion Id or a performance value. 
     * 
     * @param item can not be null
     * @param index can not be null
     * @param column can not be null 
     * @param text can not be null
     */
    private void putMCPValue(Text text){
    	
        if(activeColumn == 0){
        	text.setData(activeIndex);
        }
        if(activeIndex == 0){
        	text.setData(activeColumn);
        }
        
        if(activeColumn==0 && activeIndex != 0){
	    	try {
				Integer.parseInt(text.getText());
				updateAlternativeList(text);
			}
			catch(Exception isNull)
			{	
			}
        }
        
        if(activeColumn!=0 && activeIndex == 0){
	    	try {
				Integer.parseInt(text.getText());
				updateCriteriaList(text);
			}
			catch(Exception isNull)
			{
			}
        }
        
        if(activeColumn!=0 && activeIndex != 0){
	    	try {
				Float.parseFloat(text.getText());
				updatePerformanceMat(activeIndex,activeColumn,text);
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
    	LOGGER.info("Update alternativesList : " + alternativesList.toString());
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
    	LOGGER.info("Update criteriaList : " + criteriaList.toString());
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
    	LOGGER.info("Set performance matrix : " + performanceMat.toString());
    	
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
    	Alternative alt = null;
    	Criterion crit = null;

    	for (int i =0; i < alternativesList.size(); i++) {
    		
			if (alternativesList.get(i) != null) {
	    		alt = new Alternative(Integer.parseInt(alternativesList.get(i)));
	    		mcp.addAlt(alt);
			}
				
	    	for(int j=0; j < criteriaList.size(); j++){
	    			
	    		if (criteriaList.get(j) != null) {
		    		crit = new Criterion(Integer.parseInt(criteriaList.get(j)));
		    		try{
			    		mcp.putEvaluation(alt,crit,Float.parseFloat(performanceMat.get(i).get(j).toString()));
		    		}catch(Exception wrongInput){
		   				mcp.addCrit(crit);
		   			}
	    		}
			}
    	}

		LOGGER.info("Alternatives in the MCP: {}." + mcp.getAlternatives());
		LOGGER.info("Criteria in the MCP: {}." + mcp.getCriteria());
		LOGGER.info("Performance in the MCP: {}." + mcp.getTableEval());
    	
    	try (final FileOutputStream fos = new FileOutputStream(path)) {
			marshaller.marshalAndWrite(fos);
			System.out.println("Marshalled");
		}
    }
    
}