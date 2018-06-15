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
import org.eclipse.swt.graphics.Rectangle;
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

    private Label label;
    
	private Table table;
    private TableEditor editor;
	
	private Button addAlternative;
	private Button addCriteria;

	private SelectionListener addCriteriaListener;
	private SelectionListener addAlternativeListener;
    
	private Listener alterTableListener;
	private Listener putPerformanceListener;
	private ModifyListener perfTextListener;
	private ModifyListener altTextListener;
	private ModifyListener critTextListener;
	
    public EvaluationsGUI() {
    		
		alterTableListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
			
				Rectangle clientArea = table.getClientArea();
		        Point pt = new Point(event.x, event.y);
		        int index = table.getTopIndex();
		        
		        while (index < table.getItemCount()) {
		        	  
		            boolean visible = false;
		            final TableItem item = table.getItem(index);
		            
		            for (int i = 0; i < table.getColumnCount(); i++) {
		            	
		            	Rectangle rect = item.getBounds(i);
		              
		            	if (rect.contains(pt)) {

	                        final int column = i;
		            		final int row = index;
		            		final Text text = new Text(table, SWT.BORDER);  
		            		
		            		putPerformanceListener = new Listener() {

		            			@Override
		            			public void handleEvent(Event event) {
		            				
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
		            		};
		            		
		            		altTextListener = new ModifyListener() {
		            			
		            			@Override
		            		    public void modifyText(ModifyEvent e) {
		            				Text input = (Text) e.widget;
		            				try {
				            			Integer.parseInt(input.getText());
				            			if(row != 0 && column == 0){
				            				updateAlternativeList(input.getText());
				            			}
				            			if(row != 0 && column == 0){
				            				updateAlternativeList(input.getText());
				            			}
									}
	            					catch(Exception wrongInput)
	            					{
	            					}
		            		    }
		            		};
		            		
		            		critTextListener = new ModifyListener() {
		            			@Override
		            		    public void modifyText(ModifyEvent e) {
		            				Text input = (Text) e.widget;
		            				try {
			            				Integer.parseInt(input.getText());
				            			if(row == 0 && column != 0){
				            				updateCriteriaList(input.getText());
				            			}
									}
		            				catch(Exception wrongInput)
		            				{	
		            				}
		            			}
		            		};
		            		
		            		perfTextListener = new ModifyListener() {
		            			@Override
		            		    public void modifyText(ModifyEvent e) {
		            				Text input = (Text) e.widget;
		            				try {
			            				Integer.parseInt(input.getText());
				            			if(row != 0 && column != 0){
				            				updatePerformanceList(item.getText(column), table.getItem(0).getText(column), input);
				            			}
									}
		            				catch(Exception wrongInput)
		            				{	
		            				}
		            			}
		            		};

		            		text.addListener(SWT.FocusOut, putPerformanceListener);
		            		text.addListener(SWT.Traverse, putPerformanceListener);
		            		text.addModifyListener(altTextListener);
		            		text.addModifyListener(critTextListener);
		            		text.addModifyListener(perfTextListener);
		            		editor.setEditor(text, item, i);
		            		text.setText(item.getText(i));
		            		text.selectAll();
		            		text.setFocus();
		            		return;
		            	}
		              
		            	if (!visible && rect.intersects(clientArea)) {
		            		visible = true;
		            	}
		              
		            }
		            
		            if (!visible) return;
		            index++;
		            
		          }    
		            
		    }

			
        };

        
        addAlternativeListener = new SelectionListener() {
        	
        	@Override
			public void widgetSelected(SelectionEvent event) {
        		
          	  	TableItem item = new TableItem(table, SWT.NULL);
        		setTableSize(event.getSource());
        		setButtonPosition(event.getSource());
        		setShellSize(event.getSource());
        	}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
        }; 
            
        addCriteriaListener = new SelectionListener() {
        	
        	@Override
			public void widgetSelected(SelectionEvent event) {
        		
        		TableColumn column = new TableColumn(table, SWT.NULL);
            	column.setWidth(50);
		    	setTableSize(event.getSource());
		    	setShellSize(event.getSource());
		          
        	}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
        }; 
        
   
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
	    table.setBounds(25, 50, colAlt.getWidth() + column.getWidth(), 36); 
	    
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
	    addAlternative.setBounds(25, table.getBounds().y + table.getBounds().height + 5, 25, 25);
	    addAlternative.pack();
        addAlternative.addSelectionListener(addAlternativeListener);
		
	}
	
	private void createCriteriaAdder() {
		
	    addCriteria = new Button(shell, SWT.PUSH);
	    addCriteria.setText("Add Criteria");
	    addCriteria.setBounds(addAlternative.getBounds().x + addAlternative.getBounds().width + 5,
	    				 		  table.getBounds().y + table.getBounds().height + 5, 25, 25);
	    addCriteria.pack();
        addCriteria.addSelectionListener(addCriteriaListener);
		
	}
	
	private void setShellSize(Object object) {
		
		int width = shell.getSize().x;
		int height = shell.getSize().y;
		int widthNeeded = table.getSize().x + 25;
		
		if(object == addAlternative){
	        shell.setSize(width,height + 18);
		}
		if(object == addCriteria){
			if( widthNeeded >= width){
				shell.setSize(width + 50,height);
			}
		}
		
	}
	
	private void setTableSize(Object object) {
		
		if(object == addAlternative){
	        table.setSize(table.getSize().x, table.getBounds().height + 18);
		}
		if(object == addCriteria){
	        table.setSize(table.getSize().x + 50, table.getSize().y);
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
	
    protected void marshall() throws FileNotFoundException, JAXBException, IOException {
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
    	try (final FileOutputStream fos = new FileOutputStream("MCPEvaluationsGUI.xml")) {
			marshaller.marshalAndWrite(fos);
			System.out.println("Marshalled");
		}
    }
	
    protected void updateAlternativeList(String altId) throws FileNotFoundException, JAXBException, IOException{
    	System.out.println(alternativesList.get(Integer.parseInt(altId)));
    	alternativesList.set(Integer.parseInt(altId), altId);
    	marshall();
    }
    
    protected void updateCriteriaList(String critId) throws FileNotFoundException, JAXBException, IOException{
    	System.out.println(criteriaList.get(Integer.parseInt(critId)));
    	criteriaList.set(Integer.parseInt(critId), critId);
    	marshall();
    }
    
    protected void marshall(int altId, int critId, float perf) throws JAXBException, FileNotFoundException, IOException {
    	mcp = new MCProblem();
    	marshaller = new MCProblemMarshaller(mcp);
    	
    	Alternative alt = new Alternative(Integer.parseInt(alternativesList.get(altId)));
    	Criterion crit = new Criterion(Integer.parseInt(criteriaList.get(critId)));
    	
		mcp.putEvaluation(alt,crit,perf);
		
    	try (final FileOutputStream fos = new FileOutputStream("MCPEvaluationsGUI.xml")) {
			marshaller.marshalAndWrite(fos);
			System.out.println("Marshalled");
		}
    	
    }
    
    protected void updatePerformanceList(String alt, String crit, Text value) throws FileNotFoundException, JAXBException, IOException {
    	int altId = Integer.parseInt(alt);
    	int critId = Integer.parseInt(crit);
    	float perf = Float.parseFloat(value.getData().toString());
    
    	marshall(altId, critId, perf);
    }
    
}