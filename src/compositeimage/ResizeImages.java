/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package compositeimage;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author liamks
 */
public class ResizeImages {
    public String directory;
    public String smallDirectory;

    public ResizeImages(String dir){
        directory = dir;
        smallDirectory = dir + "small";
        boolean success = (new File(smallDirectory)).mkdir();
        if (success) {
            System.out.println("Directory: " + smallDirectory + " created");
        }
    }
    
    public BufferedImage[] getScaledFiles(int width,int height){
        File actual = new File(directory);
        int i = 0;

        BufferedImage[] temp = new BufferedImage[actual.listFiles().length];

        for( File f : actual.listFiles()){
            String ext = getFileExtension(f.getName());
            if(ext.equals("jpg") || ext.equals("png")){
                temp[i] = getScaledFile(f.getName(),width,height);
                System.out.println(f.getName() + " has been scaled!");
                i++;
            }
        }

        BufferedImage[] tinyImages = new BufferedImage[i];
        int j = 0;
        for(BufferedImage im: temp){
            if(im != null){
                tinyImages[j] = im;
                j++;
            }
        }
        return tinyImages;
    }

    public BufferedImage getScaledFile(String name, int width, int height) {

        BufferedImage img = null;

        try {
            img = ImageIO.read(new File(directory + name));

        } catch (IOException e) {}

        double wfactor = ((double) width) / ((double) img.getWidth());
        double hfactor = ((double) height) / ((double) img.getHeight());

        System.out.printf("%f %f \n", hfactor, wfactor);


        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D temp = output.createGraphics();
        temp.drawImage(img, AffineTransform.getScaleInstance(wfactor, hfactor), null);

        File outputfile = new File(directory + "/small/" + name + "_small.png");

        try {
            ImageIO.write(output, "PNG", outputfile);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return output;
    }


    public static String getFileExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');
        if (i > 0 && i < f.length() - 1) {
            ext = f.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
