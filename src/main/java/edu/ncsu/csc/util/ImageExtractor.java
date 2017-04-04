package edu.ncsu.csc.util;

import org.ini4j.Wini;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

/**
 * @author HyeongSik Kim (hkim22@ncsu.edu, yy20716@gmail.com)
 */
public class ImageExtractor {
	private static final Logger log = LoggerFactory.getLogger(ImageExtractor.class);

	/**
	 * For now, we assume that we only consider three common image types
	 * when we extract images from pages */
	private String[] imageType = {"gif", "png", "jpg"};
	private String outputPath = null;
	
	/** Let us assume that we overwrite if the same file already exists */
	static CopyOption[] options = new CopyOption[]{
			StandardCopyOption.REPLACE_EXISTING,
	};

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	/**
	 * download and copy images into the directory specified as output
	 * @param link an element that represents an image link 
	 */
	public void downloadImage(Element link) {
		String linkSrc = link.attr("src");

		/** We skip downloading process if the link contains nothing */
		if (linkSrc.isEmpty())
			return;

		String[] linkSrcArr = linkSrc.split(File.separator);
		try(InputStream in = new URL(linkSrc).openStream()){
			log.info("Downloading image: " + linkSrc);
			Files.copy(in, Paths.get(outputPath + File.separator + linkSrcArr[linkSrcArr.length - 1]), options);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * This method literally extracts all image links from files in the given directory.
	 * @param path input path that contains Feedly's backup html files. 
	 */
	public void extractLink(String path){
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			/** Make sure that we process only files, not dirs */
			if (listOfFiles[i].isFile()) {
				String filename = path + File.separator + listOfFiles[i].getName();
				File input = new File(filename);
				Document doc = null;
				try {
					doc = Jsoup.parse(input, "UTF-8");
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
				
				for (String eachImageType : imageType) {
					Elements imageLinks = doc.select("img[src$=." + eachImageType + "]");
					for (Element imageLink : imageLinks) {
						downloadImage(imageLink);
						
						/**
						 * We wait for a second before downloading the next image.
						 *
						 * TODO: Parameterize this waiting period so that
						 * users can set time in settings.ini file
						 */
						try {
							TimeUnit.SECONDS.sleep(1);
						} catch (InterruptedException e) {
							log.error(e.getMessage(), e);
						}
					}
				}
			} 
		}
	}

	public static void main(String[] args) {
		Wini ini = null;
				
		try {
			/** Read path information from settings.ini file */
			ini = new Wini(new File("settings.ini"));
			String inputPath = ini.get("main", "inputPath", String.class);
			String outputPath = ini.get("main", "outputPath", String.class);
			
			/** Launch the extractor instance **/
			ImageExtractor extractor = new ImageExtractor();
			extractor.setOutputPath(outputPath);
			extractor.extractLink(inputPath);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}