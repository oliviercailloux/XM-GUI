package io.github.oliviercailloux.y2018.xmgui.evaluationsGUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

import org.eclipse.swt.custom.TableEditor;

public class EvaluationsGUI {
	
  public static void main(String[] args) {
	  
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setSize(700, 700);
    shell.setText("Alternatives and criteria evaluations");
    
    /* LABEL EN TETE */
    
    Label label = new Label(shell, SWT.NULL);
	label.setSize(400,25);
	label.setLocation(25, 25);
	label.setText("Saisir vos en-têtes de colonne dans la première ligne du tableau.");
    
    /* INITIALISATION TABLE */
    
    Table table = new Table(shell,SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
    table.setLinesVisible(true);
    
    TableColumn colAlt = new TableColumn(table, SWT.NULL);
    colAlt.setWidth(100);
    TableColumn column = new TableColumn(table, SWT.NULL);
    column.setWidth(50);
    
    TableItem itemHeader = new TableItem(table, SWT.NULL);
	itemHeader.setText(0,"AlternativeID");
	TableItem item = new TableItem(table, SWT.NULL);
	  
    table.setBounds(25, 50, colAlt.getWidth() + column.getWidth(), item.getBounds(0).height*2);
    
    /* BUTTON ROW - Add Alternative */
    
    final Button buttonRow = new Button(shell, SWT.PUSH);
    buttonRow.setText("Add Alternative");
    buttonRow.setBounds(25, table.getBounds().y + table.getBounds().height + 5, 25, 25);
    buttonRow.pack();
    
    /* BUTTON COLUMN - Add Criteria */
    
    final Button buttonCol = new Button(shell, SWT.PUSH);
    buttonCol.setText("Add Criteria");
    buttonCol.setBounds(buttonRow.getBounds().x + buttonRow.getBounds().width + 5, table.getBounds().y + table.getBounds().height + 5, 25, 25);
    buttonCol.pack();
   
    /* Listener button row */
    buttonRow.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent event) {
    	  TableItem item = new TableItem(table, SWT.NULL);
    	  int hRow = item.getBounds(0).height;
          table.setSize(table.getSize().x, table.getSize().y + hRow);
          buttonRow.setBounds(25, buttonRow.getBounds().y + hRow, 25, 25);
          buttonRow.pack();
          buttonCol.setBounds(buttonRow.getBounds().x + buttonRow.getBounds().width + 5, buttonCol.getBounds().y + hRow, 25, 25);
          buttonCol.pack();
      }

      public void widgetDefaultSelected(SelectionEvent event) {
      }
    });
    
    /* Listener button column */
    buttonCol.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent event) {
    	TableColumn column = new TableColumn(table, SWT.NULL);
    	column.setWidth(50);
        table.setSize(table.getSize().x + column.getWidth(), table.getSize().y);
      }

      public void widgetDefaultSelected(SelectionEvent event) {
      }
    });
    
    /* MODIF CELLULE */
    
    final TableEditor editor = new TableEditor(table);
    editor.horizontalAlignment = SWT.LEFT;
    editor.grabHorizontal = true;
    
    table.addListener(SWT.MouseDown, new Listener() {
    	
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
                final Text text = new Text(table, SWT.NONE);
                
                Listener textListener = new Listener() {
                	
                  public void handleEvent(final Event e) {
                	  
                    switch (e.type) {
                    
                    case SWT.FocusOut:
                      item.setText(column, text.getText());
                      text.dispose();
                      break;
                      
                    case SWT.Traverse:
                      switch (e.detail) {
                      
                      case SWT.TRAVERSE_RETURN:
                        item.setText(column, text.getText());
                        
                      case SWT.TRAVERSE_ESCAPE:
                        text.dispose();
                        e.doit = false;
                      }
                      
                      break;
                      
                    }
                  }
                };
                
                text.addListener(SWT.FocusOut, textListener);
                text.addListener(SWT.Traverse, textListener);
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
            
            if (!visible)
              return;
            index++;
          }
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