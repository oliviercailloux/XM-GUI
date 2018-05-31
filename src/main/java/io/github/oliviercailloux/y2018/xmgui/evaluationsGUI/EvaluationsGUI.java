package io.github.oliviercailloux.y2018.xmgui.evaluationsGUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.TableEditor;

public class EvaluationsGUI {
	
  public static void main(String[] args) {
	  
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setSize(500, 500);
    shell.setText("Grille d'évaluation des alternatives");
    
    final Text text = new Text(shell, SWT.BORDER);
    text.setBounds(25, 450, 220, 25);

    Table table = new Table(shell, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL
        | SWT.H_SCROLL);
    table.setHeaderVisible(true);
    String[] titles = { 
    		"Col 1", "Col 2", "Col 3", "Col 4" };

    for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
      TableColumn column = new TableColumn(table, SWT.NULL);
      column.setWidth(50);
      column.setText(titles[loopIndex]);
    }

    for (int loopIndex = 0; loopIndex < 24; loopIndex++) {
      TableItem item = new TableItem(table, SWT.NULL);
      item.setText("Item " + loopIndex);
      item.setText(0, "Item " + loopIndex);
      item.setText(1, "Yes");
      item.setText(2, "No");
      item.setText(3, "A table item");
    }

    for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
      table.getColumn(loopIndex).pack();
    }

    table.setBounds(25, 25, 400, 400);

    table.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        if (event.detail == SWT.CHECK) {
          text.setText("You checked " + event.item);
        } else {
          text.setText("You selected " + event.item);
        }
      }
    });
    
    
    
    
    final TableEditor editor = new TableEditor(table);
    //The editor must have the same size as the cell and must
    //not be any smaller than 50 pixels.
    editor.horizontalAlignment = SWT.LEFT;
    editor.grabHorizontal = true;
    editor.minimumWidth = 50;
    // editing the second column
	final int EDITABLECOLUMN = 3;

    table.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	
                    // Clean up any previous editor control
                    Control oldEditor = editor.getEditor();
                    if (oldEditor != null) oldEditor.dispose();

                    // Identify the selected row
                    TableItem item = (TableItem) e.item;
                    if (item == null) return;

                    // The control that will be the editor must be a child of the Table
                    Text newEditor = new Text(table, SWT.NONE);
                    newEditor.setText(item.getText(EDITABLECOLUMN));
                    newEditor.addModifyListener(new ModifyListener() {
                            public void modifyText(ModifyEvent e) {
                                    Text text = (Text)editor.getEditor();
                                    editor.getItem().setText(EDITABLECOLUMN, text.getText());
                            }
                    });
                    newEditor.selectAll();
                    newEditor.setFocus();
                    editor.setEditor(newEditor, item, EDITABLECOLUMN);
            }
    });
    
    /* BOUTON COLONNE */
    
    final Button buttonCol = new Button(shell, SWT.PUSH);
    buttonCol.setText("Click Me");
    buttonCol.setBounds(425, 25, 25, 25);
    buttonCol.pack();
   
    buttonCol.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent event) {
    	TableColumn column = new TableColumn(table, SWT.NULL);
    	column.setWidth(50);
        column.setText("new col");
        table.setHeaderVisible(true);
        text.setText("done");
      }

      public void widgetDefaultSelected(SelectionEvent event) {
        text.setText("No worries!");
      }
    });
    
    /* BOUTON LIGNE */
    
    final Button buttonRow = new Button(shell, SWT.PUSH);
    buttonRow.setText("Click Me");
    buttonRow.setBounds(25, 425, 25, 25);
    buttonRow.pack();
   
    buttonRow.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent event) {
    	  TableItem item = new TableItem(table, SWT.NULL);
    	  System.out.println(table.getItemCount());
          item.setText("Item");
          item.setText(0, "Item");
          item.setText(1, "Yes");
          item.setText(2, "No");
          item.setText(3, "A table item");
        

        for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
          table.getColumn(loopIndex).pack();
        }
      }

      public void widgetDefaultSelected(SelectionEvent event) {
        text.setText("No worries!");
      }
    });

    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  	}
}