package io.github.oliviercailloux.y2018.xmgui.ressources2;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Launch {

	public static void main(String[] args) throws IOException {

		String path = "performanceTableplot.png";
		Path imagePath = Paths.get(path);

		ImageFrame plot = new ImageFrame("Image Frame of a plot", imagePath);
		plot.activeLoop();

	}

}