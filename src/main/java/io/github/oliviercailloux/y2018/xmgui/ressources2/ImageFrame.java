package io.github.oliviercailloux.y2018.xmgui.ressources2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageFrame {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageFrame.class);
	
	private final Display display = Display.getDefault();
	private final Shell shell = new Shell(display);
	
	private Image image;
	private int height;
	private int width;
    
    public ImageFrame(String title, Path path) throws IOException{
    	
    	importImage(path);
    	setImage();
    	
    	shell.setText(title);
    	
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
    
    private void importImage(Path path) throws IOException{    	
    	try (InputStream input = java.nio.file.Files.newInputStream(path)) {
    		image = new Image(display, input);
    		height = image.getBounds().height;
        	width = image.getBounds().width;
    		LOGGER.debug("Image imported");
    	}
    }
    
    private void setImage(){
    	shell.setBackgroundImage(image);
    	shell.setSize(width, height);
    }
    
}