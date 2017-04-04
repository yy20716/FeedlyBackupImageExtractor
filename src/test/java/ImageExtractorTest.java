import edu.ncsu.csc.util.ImageExtractor;
import org.junit.jupiter.api.DisplayName;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This unit-test class will test whether our image extractor can properly extract
 * and download images as expected. For this purpose, we include a dummy webpage
 * generated from Feedly in src/test/resources. The unit-test codes will then run
 * our image extractor and check the size of downloaded file is the same as the
 * size of the image specified in the dummy webpage.
 *
 * Note that the current Intellij has some issues with using JUnit5
 * http://leakfromjavaheap.blogspot.com/2016/12/playing-with-junit-5-and-intellij-idea.html
 *
 * @author HyeongSik Kim (hkim22@ncsu.edu, yy20716@gmail.com)
 */
class ImageExtractorTest {

    @DisplayName("Should extract and download the image file specified in a test html file")
    @org.junit.jupiter.api.Test
    void testExtractLink() {
        String inputPath = "src/test/resources";
        String outputPath = "testoutput";
        String downloadFilename = "tumblr_nuawdnEidt1qkyzm3o1_1280.jpg";
        int downloadFilesize = 113916;

        /** Clean up the dummy directory that will contain downloaded image */
        File outputDir = new File(outputPath);
        if (outputDir.exists() && outputDir.delete() != true)
            fail("Cannot remove the output directory for testing");

        /** Create the dummy directory */
        if (!outputDir.mkdirs()) {
            fail("Cannot create the output directory for testing");
        } else {
            ImageExtractor extractor;
            extractor = new ImageExtractor();
            extractor.setOutputPath(outputPath);
            extractor.extractLink(inputPath);

            File imageFile = new File(outputPath + File.separator + downloadFilename);
            if(imageFile.exists() && !imageFile.isDirectory()) {
                assertTrue (imageFile.length() == downloadFilesize);
            } else {
                fail("Downloaded image has a wrong outputDir size.");
            }
        }
    }
}