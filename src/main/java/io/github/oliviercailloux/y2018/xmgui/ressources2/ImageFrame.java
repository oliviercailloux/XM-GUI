package io.github.oliviercailloux.y2018.xmgui.ressources2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageFrame {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageFrame.class);

	private final Display display = Display.getDefault();
	private final Shell shell = new Shell(display);

	private Image image;
	private int height;
	private int width;

	/**
	 * Create a window showing an image.
	 * 
	 * @param title of the window
	 * @param path  of the image
	 * @throws IOException
	 */
	public ImageFrame(String title, Path path) throws IOException {

		importImage(path);
		setImage();

		shell.setText(title);

	}

	/**
	 * Open the window of an imageFrame instance and keep it open while it is not
	 * closed.
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
	 * Import the image located at the path given. Set the image, its width and
	 * height.
	 * 
	 * @param path can not be null
	 * @throws IOException
	 */
	private void importImage(Path path) throws IOException {
		try (InputStream input = java.nio.file.Files.newInputStream(path)) {
			image = new Image(display, input);
			height = image.getBounds().height;
			width = image.getBounds().width;
			LOGGER.debug("Image imported");
		}
	}

	/**
	 * Set the image as the shell's background. Set the shell's dimensions with
	 * those of the image.
	 */
	private void setImage() {
		shell.setBackgroundImage(image);
		shell.setSize(width, height);
	}

}