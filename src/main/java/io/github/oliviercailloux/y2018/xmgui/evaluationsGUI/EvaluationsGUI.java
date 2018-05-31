package io.github.oliviercailloux.y2018.xmgui.evaluationsGUI;




import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.swt.custom.TableEditor;

public class EvaluationsGUI {

  private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationsGUI.class);
	
  public static void main(String[] args) {
	  
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setSize(500, 500);
    shell.setText("Grille d'évaluation des alternatives");
    
    /* INITIALISATION TABLE */
    Table table = new Table(shell,SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
    table.setHeaderVisible(true);
    table.setLinesVisible(true);
    
    String[] titles = {"","AlternativeID"};

    for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
      TableColumn column = new TableColumn(table, SWT.NULL);
      column.setWidth(50);
      column.setText(titles[loopIndex]);
    }

    table.setBounds(25, 25, 400, 400);
    
    
   
    
    /* BOUTON COLONNE */
    
    final Button buttonCol = new Button(shell, SWT.PUSH);
    buttonCol.setText("Ajouter un critère");
    buttonCol.setBounds(425, 25, 25, 25);
    buttonCol.pack();
   
    buttonCol.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent event) {
    	TableColumn column = new TableColumn(table, SWT.NULL);
    	column.setWidth(50);
        column.setText("Critère" + (table.getColumnCount() - 1));
      }

      public void widgetDefaultSelected(SelectionEvent event) {
      }
    });
    
    
    
    /* BOUTON LIGNE */
    
    final Button buttonRow = new Button(shell, SWT.PUSH);
    buttonRow.setText("Ajouter une alternative");
    buttonRow.setBounds(25, 425, 25, 25);
    buttonRow.pack();
   
    buttonRow.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent event) {
    	  TableItem item = new TableItem(table, SWT.NULL);
    	  System.out.println(table.getItemCount());
    	  item.setText(0,"" + table.getItemCount());
    	  final TableEditor editor = new TableEditor(table);
      }

      public void widgetDefaultSelected(SelectionEvent event) {
    	  
      }
    });
    
    /* MODIF CELLULE */
    
    /* A PROG : mise à jour du MCProblem au fur et à mesure des insertions de l'U */
    /*
    final TableEditor editor = new TableEditor(table);
    editor.horizontalAlignment = SWT.LEFT;
    editor.grabHorizontal = true;
    editor.minimumWidth = 50;
    
    final int EDITABLECOLUMN = 1;

    table.addSelectionListener(new SelectionAdapter() {
    	
            public void widgetSelected(SelectionEvent e) {
            		
                    // suppression ancien control
                    Control oldEditor = editor.getEditor();
                    if (oldEditor != null) oldEditor.dispose();

                    // Identification Ligne
                    TableItem item = (TableItem) e.item;
                    if (item == null) return;
                    
                   
                  
                    LOGGER.info("x: {}.", e.x);
                    LOGGER.info("y: {}.", e.y);
                    LOGGER.info("data: {}.", e.data);   
                    LOGGER.info("getSource: {}.", e.getSource());     
                    LOGGER.info("widget: {}.", e.widget);
                    
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
    });*/
    
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