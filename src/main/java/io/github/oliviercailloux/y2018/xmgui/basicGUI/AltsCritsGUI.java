package io.github.oliviercailloux.y2018.xmgui.basicGUI;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.*;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.*;

import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;
import io.github.oliviercailloux.y2018.xmgui.file1.MCProblemMarshaller;

import org.eclipse.swt.layout.*;

public class AltsCritsGUI {

    private final Display display = Display.getDefault();
    private final Shell shell = new Shell(display);
    
	private MCProblem mcp;
	private Alternative alt1;
	private MCProblemMarshaller marshaller;
	private ArrayList<String> alternativesList = new ArrayList<>();
	
	private Composite alternatives;
	private Composite criteria;
    private Composite alternativesIdPanel;

    private ModifyListener textListener;
    private SelectionListener validateListener;

    private Label nbAltsDirection;
    private Button validateNbAlts; 
    private int initialNbOfAlts;
    private int nbOfAltsIncrementor = 0;
	private int textPositionIncrementor = 30;
    private Label altIdInputDirection;
    private Text nbAlts;
    private Text id;
    
    
    public AltsCritsGUI() {
    	    	
    	textListener = new ModifyListener() {
    		@Override
            public void modifyText(ModifyEvent e) {
    			Text text = (Text) e.widget;

    			try {
					Integer.parseInt(text.getText());
					updateAlternativeList(text);
				}
				catch(Exception wrongInput)
				{
				MessageDialog.openError(shell, "Error", "Please enter an integer");	
				}
            }
        };
        	
        validateListener = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(e.getSource()==validateNbAlts) {
					if (!nbAlts.isDisposed()) {
						initialNbOfAlts = Integer.parseInt(nbAlts.getText());
						nbAlts.dispose();
						nbAltsDirection.setText("Click to add a new alternative");
						for (int i = 0 ; i < initialNbOfAlts; i ++)
			    	        alternativesList.add(i, null);
					}
					createAltsTextBoxes(initialNbOfAlts);
					validateNbAlts.setBounds(10, 35, 150, 26);
					validateNbAlts.setText("Add alternative");
					alternativesList.add(initialNbOfAlts + nbOfAltsIncrementor, null);
					nbOfAltsIncrementor += 1;
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				}
            };
	}
    
	public static void main(String[] args) {
    	
    	AltsCritsGUI gui = new AltsCritsGUI();
    	createShell(gui);
        TabFolder tabfolder = createTabFolder(gui);
        createTabItemAlternatives(gui, tabfolder);
        createTabItemCriteria(gui, tabfolder);
        createInitialAltsFields(gui);
        displayLoop(gui);
    }

	private static void createShell(AltsCritsGUI gui) {
		gui.shell.setSize(500, 500);
        gui.shell.setText("Multi Criteria Problem Edition");
	}

	private static TabFolder createTabFolder(AltsCritsGUI gui) {
		TabFolder tabfolder = new TabFolder(gui.shell, SWT.NONE);
        tabfolder.setSize(480, 480);
        gui.alternatives = new Composite(tabfolder, SWT.NONE);
        gui.alternatives.setBounds(10, 100, 500, 500);
        gui.criteria = new Composite(tabfolder, SWT.NONE);
        gui.criteria.setSize(450, 450);
		return tabfolder;
	}

	private static void createTabItemAlternatives(AltsCritsGUI gui, TabFolder tabfolder) {
		TabItem altTab = new TabItem(tabfolder, SWT.NONE);
        altTab.setText("ALTERNATIVES");
        altTab.setControl(gui.alternatives);
	}

	private static void createTabItemCriteria(AltsCritsGUI gui, TabFolder tabfolder) {
		TabItem critTab = new TabItem(tabfolder, SWT.NONE);
        critTab.setText("CRITERIA");
        critTab.setControl(gui.criteria);
	}

	private static void createInitialAltsFields(AltsCritsGUI gui) {
		gui.nbAltsDirection = new Label(gui.alternatives, SWT.NONE);
        gui.nbAltsDirection.setText("How many alternatives do you need?");
        gui.nbAltsDirection.setBounds(10, 10, 250, 20);
        gui.nbAlts = new Text(gui.alternatives, SWT.NONE);
        gui.nbAlts.setBounds(10, 35, 100, 26);
        gui.validateNbAlts = new Button(gui.alternatives, SWT.PUSH);
        gui.validateNbAlts.setText("Create");
        gui.validateNbAlts.setBounds(120, 35, 80, 26);
        gui.validateNbAlts.addSelectionListener(gui.validateListener);
	}

	private static void displayLoop(AltsCritsGUI gui) {
		gui.shell.open();
        gui.shell.layout();
        while (!gui.shell.isDisposed()) {
            if (!gui.display.readAndDispatch()) {
                gui.display.sleep();
            }
        }
	}

    protected void createAltsTextBoxes(int initialNbOfAlts) {
        
        if(alternativesIdPanel!=null) {
        	mcp = new MCProblem();
        	alternativesIdPanel.dispose();
        	textPositionIncrementor=30;
        }
    	alternativesIdPanel = new Composite(alternatives, SWT.NONE);
    	alternativesIdPanel.setBounds(0, 80, 400, 400);
    	altIdInputDirection = new Label(alternativesIdPanel, SWT.NONE);
    	altIdInputDirection.setBounds(10, 0, 300, 20);
    	altIdInputDirection.setText("Enter your alternatives' id (integer only) :");
	    		for(int i=0; i < initialNbOfAlts + nbOfAltsIncrementor ; i++) {
	    			id = new Text(alternativesIdPanel, SWT.BORDER);
	    	    	id.setData(i);
	    	    	id.setBounds(10, textPositionIncrementor, 100, 26);
	    	    	textPositionIncrementor += 50;
	    	        id.addModifyListener(textListener);
	    	        if (alternativesList.get(i) != null) {
	    	        	id.setText(alternativesList.get(i)); 
	    	        }
	    		}
	    		
	        alternatives.layout();
    }
    
    protected void marshall() throws JAXBException, FileNotFoundException, IOException {
    	mcp = new MCProblem();
    	marshaller = new MCProblemMarshaller(mcp);
    	for (int i =0; i < alternativesList.size(); i++) {
			if (alternativesList.get(i) != null) {
				mcp.addAlt(new Alternative(Integer.parseInt(alternativesList.get(i))));
				System.out.println(mcp.getAlternatives());
			}
		}
    	try (final FileOutputStream fos = new FileOutputStream("MCPGuiFile.xml")) {
			marshaller.marshalAndWrite(fos);
		}
    }
    
    protected void updateAlternativeList(Text altId) throws FileNotFoundException, JAXBException, IOException {
    	alternativesList.set(Integer.parseInt(altId.getData().toString()), altId.getText());
    	System.out.println(alternativesList);
    	marshall();
    }
    
}

