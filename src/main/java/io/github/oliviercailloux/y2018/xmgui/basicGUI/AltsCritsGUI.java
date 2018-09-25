package io.github.oliviercailloux.y2018.xmgui.basicGUI;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.y2018.xmgui.contract1.Alternative;
import io.github.oliviercailloux.y2018.xmgui.contract1.Criterion;
import io.github.oliviercailloux.y2018.xmgui.contract1.MCProblem;
import io.github.oliviercailloux.y2018.xmgui.file1.MCProblemMarshaller;

/*
 * This class is a basic graphical user interface for Alternatives and Criteria creation and edition (id).
 */
public class AltsCritsGUI {

	private final Logger logger = LoggerFactory.getLogger(AltsCritsGUI.class);

	private final Display display = Display.getDefault();
	private final Shell shell = new Shell(display);

	private MCProblem mcp;
	private MCProblemMarshaller marshaller;

	// List used to keep an up-to-date collection of the created and/or edited
	// alternatives
	private ArrayList<String> alternativesList = new ArrayList<>();
	// List used to keep an up-to-date collection of the created and/or edited
	// alternatives
	private ArrayList<String> criteriaList = new ArrayList<>();

	private Composite alternatives;
	private Composite criteria;
	private Composite alternativesIdPanel;
	private Composite criteriaIdPanel;

	private ModifyListener altTextListener;
	private ModifyListener critTextListener;
	private SelectionListener validateListener;

	private Label nbAltsDirection;
	private Button validateNbAlts;
	private int initialNbOfAlts;
	private int nbOfAltsIncrementor = 0;
	private int altTextPositionIncrementor = 30;
	private Label altIdInputDirection;
	private Text nbAlts;

	private Text id;

	private Label nbCritDirection;
	private Button validateNbCrit;
	private int initialNbOfCrit;
	private int nbOfCritIncrementor = 0;
	private Label critIdInputDirection;
	private Text nbCrit;
	private int critTextPositionIncrementor = 30;

	/*
	 * This method contains the various listeners used in this GUI to create
	 * Alternatives, Criteria, and validate their entered id value.
	 */
	public AltsCritsGUI() {

		// Listener used to get the value of the desired id entered by the user to
		// create criteria
		altTextListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				Text text = (Text) e.widget;
				try {
					Integer.parseInt(text.getText());
					updateAlternativeList(text);
					text.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
				} catch (Exception wrongInput) {
					text.setBackground(display.getSystemColor(SWT.COLOR_RED));
				}
			}
		};

		// Listener used to get the value of the desired id entered by the user to
		// create criteria
		critTextListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				Text text = (Text) e.widget;
				try {
					Integer.parseInt(text.getText());
					updateCriteriaList(text);
					text.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
				} catch (Exception wrongInput) {
					text.setBackground(display.getSystemColor(SWT.COLOR_RED));
				}
			}
		};

		// Listener used to validate the id entered by the user to create an alternative
		// or a criterion
		validateListener = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.getSource() == validateNbAlts) {
					if (!nbAlts.isDisposed()) {
						try {
							initialNbOfAlts = Integer.parseInt(nbAlts.getText());
						} catch (Exception wrongInput) {
							MessageDialog.openError(shell, "Error", "Please enter an integer");
						}
						nbAlts.dispose();
						nbAltsDirection.setText("Click to add a new alternative");
						for (int i = 0; i < initialNbOfAlts; i++)
							alternativesList.add(i, null);
					}
					createAltsTextBoxes(initialNbOfAlts);
					validateNbAlts.setBounds(10, 35, 150, 26);
					validateNbAlts.setText("Add alternative");
					alternativesList.add(initialNbOfAlts + nbOfAltsIncrementor, null);
					nbOfAltsIncrementor += 1;
				}

				if (e.getSource() == validateNbCrit) {
					if (!nbCrit.isDisposed()) {
						try {
							initialNbOfCrit = Integer.parseInt(nbCrit.getText());
						} catch (Exception wrongInput) {
							MessageDialog.openError(shell, "Error", "Please enter an integer");
						}

						nbCrit.dispose();
						nbCritDirection.setText("Click to add a new criterion");
						for (int i = 0; i < initialNbOfCrit; i++)
							criteriaList.add(i, null);
					}
					createCritTextBoxes(initialNbOfCrit);
					validateNbCrit.setBounds(10, 35, 150, 26);
					validateNbCrit.setText("Add criterion");
					criteriaList.add(initialNbOfCrit + nbOfCritIncrementor, null);
					nbOfCritIncrementor += 1;
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};
	}

	private static void createShell(AltsCritsGUI gui) {
		gui.shell.setSize(500, 500);
		gui.shell.setMinimumSize(500, 500);
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

	/*
	 * This method creates the text field for the the user to enter how many
	 * alternatives he initially wants to create, and listens to it.
	 */
	private static void createInitialAltsFields(AltsCritsGUI gui) {
		gui.nbAltsDirection = new Label(gui.alternatives, SWT.NONE);
		gui.nbAltsDirection.setText("How many alternatives do you need?");
		gui.nbAltsDirection.setBounds(10, 10, 250, 20);
		gui.nbAlts = new Text(gui.alternatives, SWT.BORDER);
		gui.nbAlts.setBounds(10, 35, 100, 26);
		gui.validateNbAlts = new Button(gui.alternatives, SWT.PUSH);
		gui.validateNbAlts.setText("Create");
		gui.validateNbAlts.setBounds(120, 35, 80, 26);
		gui.validateNbAlts.addSelectionListener(gui.validateListener);
	}

	/*
	 * This method creates the text field for the user to enter how many criteria he
	 * initially wants to create, and listens to it.
	 */
	private static void createInitialCritsFields(AltsCritsGUI gui) {
		gui.nbCritDirection = new Label(gui.criteria, SWT.NONE);
		gui.nbCritDirection.setText("How many criteria do you need?");
		gui.nbCritDirection.setBounds(10, 10, 250, 20);
		gui.nbCrit = new Text(gui.criteria, SWT.BORDER);
		gui.nbCrit.setBounds(10, 35, 100, 26);
		gui.validateNbCrit = new Button(gui.criteria, SWT.PUSH);
		gui.validateNbCrit.setText("Create");
		gui.validateNbCrit.setBounds(120, 35, 80, 26);
		gui.validateNbCrit.addSelectionListener(gui.validateListener);
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

	/*
	 * This method creates text areas for the user to enter the id of the
	 * alternatives that are to be created and marshalled.
	 * 
	 * @param initialNbOfCrits the initial number of alternatives to be created as
	 * entered by the user
	 */
	protected void createAltsTextBoxes(int initialNbOfAlt) {
		if (alternativesIdPanel != null) {
			mcp = new MCProblem();
			alternativesIdPanel.dispose();
			altTextPositionIncrementor = 30;
		}
		alternativesIdPanel = new Composite(alternatives, SWT.NONE);
		alternativesIdPanel.setBounds(0, 80, 400, 400);
		altIdInputDirection = new Label(alternativesIdPanel, SWT.NONE);
		altIdInputDirection.setBounds(10, 0, 300, 20);
		altIdInputDirection.setText("Enter your alternatives' id (integer only) :");

		for (int i = 0; i < initialNbOfAlt + nbOfAltsIncrementor; i++) {
			id = new Text(alternativesIdPanel, SWT.BORDER);
			id.setData(i);
			id.setBounds(10, altTextPositionIncrementor, 100, 26);
			altTextPositionIncrementor += 50;
			id.addModifyListener(altTextListener);
			if (alternativesList.get(i) != null) {
				id.setText(alternativesList.get(i));
			}
		}
		alternatives.layout();
	}

	/*
	 * This method creates text areas for the user to enter the id of the criteria
	 * that are to be created and marshalled.
	 * 
	 * @param initialNbOfCrits the initial number of criteria to be created as
	 * entered by the user
	 */
	protected void createCritTextBoxes(int initialNbOfCrits) {
		if (criteriaIdPanel != null) {
			mcp = new MCProblem();
			criteriaIdPanel.dispose();
			critTextPositionIncrementor = 30;
		}
		criteriaIdPanel = new Composite(criteria, SWT.NONE);
		criteriaIdPanel.setBounds(0, 80, 400, 400);
		critIdInputDirection = new Label(criteriaIdPanel, SWT.NONE);
		critIdInputDirection.setBounds(10, 0, 300, 20);
		critIdInputDirection.setText("Enter your criteria' id (integer only) :");

		for (int i = 0; i < initialNbOfCrits + nbOfCritIncrementor; i++) {
			id = new Text(criteriaIdPanel, SWT.BORDER);
			id.setData(i);
			id.setBounds(10, critTextPositionIncrementor, 100, 26);
			critTextPositionIncrementor += 50;
			id.addModifyListener(critTextListener);
			if (criteriaList.get(i) != null) {
				id.setText(criteriaList.get(i));
			}
		}
		criteria.layout();
	}

	/*
	 * This method marshalls Alternatives and Criteria based on the updated
	 * alternativesList and criteriaList.
	 */
	protected void marshall() throws JAXBException, FileNotFoundException, IOException {
		mcp = new MCProblem();
		marshaller = new MCProblemMarshaller(mcp);
		for (int i = 0; i < alternativesList.size(); i++) {
			if (alternativesList.get(i) != null) {
				mcp.addAlt(new Alternative(Integer.parseInt(alternativesList.get(i))));
				logger.info("Alternatives in the MCP: {}." + mcp.getAlternatives());
			}
		}
		for (int i = 0; i < criteriaList.size(); i++) {
			if (criteriaList.get(i) != null) {
				mcp.addCrit(new Criterion(Integer.parseInt(criteriaList.get(i))));
				logger.info("Criteria in the MCP: {}." + mcp.getCriteria());
			}
		}
		try (final FileOutputStream fos = new FileOutputStream("MCPFile.xml")) {
			marshaller.marshalAndWrite(fos);
		}
	}

	protected MCProblem getMCP() {
		return this.mcp;
	}

	protected void updateAlternativeList(Text altId) throws FileNotFoundException, JAXBException, IOException {
		alternativesList.set(Integer.parseInt(altId.getData().toString()), altId.getText());
		marshall();
	}

	protected void updateCriteriaList(Text critId) throws FileNotFoundException, JAXBException, IOException {
		criteriaList.set(Integer.parseInt(critId.getData().toString()), critId.getText());
		marshall();
	}

	public static void main(String[] args) {
		AltsCritsGUI gui = new AltsCritsGUI();

		createShell(gui);
		TabFolder tabfolder = createTabFolder(gui);
		createTabItemAlternatives(gui, tabfolder);
		createTabItemCriteria(gui, tabfolder);
		createInitialAltsFields(gui);
		createInitialCritsFields(gui);
		displayLoop(gui);
	}
}
