/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package compositeimage;

import java.awt.image.BufferedImage;
import java.io.IOException;

/*
The format for the RGB values is an integer with 8 bits each of alpha, red, green,
 and blue color components ordered correspondingly from the most significant byte to
 the least significant byte, as in: 0xAARRGGBB
*/
/**
 *
 * @author liamks
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        String sourceImage = "/Users/liamks/NetBeansProjects/CompositeImage/images/IMG_1598.JPG";
        String folderOfImages = "/Users/liamks/NetBeansProjects/CompositeImage/images/small/";
        //String sImage, String folderOfImages,int x,int y
        //CreateCompositeImage composite = new CreateCompositeImage(sImage,folderOfImages,40,30);

       
        
         int sWidth = 40;
         int sHeight = 30;
         //ResizeImages resizedImages = new ResizeImages(folderOfImages);
         //BufferedImage[] smallImages = resizedImages.getScaledFiles(sWidth,sHeight);
         CreateCompositeImage mosaic = new CreateCompositeImage(sourceImage,folderOfImages);
         mosaic.saveMosaic(null,"mosaic3","png");
         

        /**
        File outputfile = new File("/Users/liamks/NetBeansProjects/CompositeImage/test.png");

        try {
            ImageIO.write(composite.getCompositeImage(),"PNG",outputfile);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        

        }**/
        
    }

    
}
