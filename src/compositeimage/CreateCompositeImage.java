/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package compositeimage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author liamks
 * help from: http://java.sun.com/docs/books/tutorial/2d/images/index.html
 */
public class CreateCompositeImage {
    public BufferedImage sourceImage;
    public BufferedImage[] tinyImages;
    public int smallWidth;
    public int smallHeight;
    public int numXSquares;
    public int numYSquares;
    public int totalSquares;
    public int[][] tinyImagesMeanRGB;
    public int[][] sourceImageMeanRGB;
    public BufferedImage[] arrayOfTinyImagesForOutput;


    //public CreateCompositeImage(String sImage, String folderOfImages,int x,int y){
    public CreateCompositeImage(String sImage, String smallImages){
        tinyImages = toBufferedImages(smallImages);
        System.out.println("Small images Buffered");
        
        smallWidth = tinyImages[0].getWidth(); //Width of small images
        smallHeight = tinyImages[0].getHeight(); //Height of small images
        
        
        //scaleFiles(folderOfImages);
       
        try {
            sourceImage = ImageIO.read(new File(sImage));
        } catch (IOException e) {}
       
        numXSquares = sourceImage.getWidth()/smallWidth;
        numYSquares = sourceImage.getHeight()/smallHeight;
        totalSquares = numXSquares*numYSquares;

        System.out.printf("numXSquares: %d, numYSquares: %d, image size(width: %d, height: %d)",
                numXSquares, numYSquares, sourceImage.getWidth(), sourceImage.getHeight());

        sourceImageMeanRGB = rgbPerSquare(sourceImage);
        tinyImagesMeanRGB = rgbPerTinyImage(); //populates tinyImagesMeanRGB

        //pictures for output, should return an array of arrays, each element is a row
        //while each row has many tinyBuffered images
    }

    public BufferedImage[] toBufferedImages(String path) {
        File pics = new File(path);
        int i = 0;

        BufferedImage[] temp = new BufferedImage[pics.listFiles().length];

        for (File f : pics.listFiles()) {
           
            String ext = ResizeImages.getFileExtension(f.getName());
            if (ext.equals("jpg") || ext.equals("png")) {

                BufferedImage img = null;
                try {
                    img = ImageIO.read(f);
                } catch (IOException e) {
                }
                temp[i] = img;
                i++;
            }
        }

        BufferedImage[] smallImages = new BufferedImage[i];
        int j = 0;
        for(BufferedImage im: temp){
            if(im != null){
                smallImages[j] = im;
                j++;
            }
        }
        return smallImages;
    }

    public boolean saveMosaic(String path, String picName, String ext){
        boolean save = false;
        if(path == null){
            File currentDir = new File(".");
            try {
                path = currentDir.getCanonicalPath() + '/';
            } catch (IOException ex) {
                Logger.getLogger(CreateCompositeImage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        File outputfile = new File(path + picName + '.' + ext);

        try {
            ImageIO.write(getCompositeImage(),ext.toUpperCase(),outputfile);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

        }
        save = true;
        return save;
    }
    
    public int[][] rgbPerSquare(BufferedImage pic){

        //int[] rgbs = new int[totalSquares];
        int startX = 0, startY = 0;
        int[][] squares = new int[totalSquares][3];
        int i = 0;
        for(int[] s: squares){
            int tempx = smallWidth;
            if((startX + smallWidth)>pic.getWidth()){ 
                smallWidth = pic.getWidth() - startX;
            }
            int[] temp = pic.getRGB(startX, startY, smallWidth, smallHeight, null, 0, smallWidth);
            
            squares[i] = getAverage(temp);
            smallWidth = tempx;
            startX += smallWidth;
            if (startX >= pic.getWidth()){
                startX = 0;
                startY += smallHeight;
            }
            i++;
        }
        return squares;
    }

     public int[][] rgbPerTinyImage(){
        
        int[][] tempMeanRGB = new int[tinyImages.length][3];
        int[] rgbs = new int[tinyImages.length];
        int index = 0;
       
        int last = tinyImages.length - 1;
        for(BufferedImage i: tinyImages){
            if (i != null) {
                int[] temp = i.getRGB(0, 0, i.getWidth(), i.getHeight(), null, 0, i.getWidth());
                tempMeanRGB[index] = getAverage(temp);
                index++;
            }
        }
        return tempMeanRGB;
    }

    public int[] getAverage(int[] p){
        int[] sum = {0,0,0};
        for(int pixel: p){
            Square square = new Square(pixel);
            sum[0] += square.red;
            sum[1] += square.green;
            sum[2] += square.blue;
        }
        
        int[] outputAverage = {sum[0]/p.length,sum[1]/p.length,sum[2]/p.length};
        return outputAverage;
    }

    public void getTinyImagePerSquare(){
        //tinyImagesMeanRGB
        //sourceImageMeanRGB
        //BufferedImage[] arrayOfTinyImagesForOutput
        //the difference in mean RGB between the tiny image and the square in sourceImage
        //should be minimized
        int variation;
        int red,green,blue;
        int i = 0, x = 0;
       
        
        for(int[] sourceRGB: sourceImageMeanRGB){
            variation = 255*3;
            i = 0;
            for(int[] tinyRGB: tinyImagesMeanRGB){
                red = Math.abs(sourceRGB[0] - tinyRGB[0]);
                green = Math.abs(sourceRGB[1] - tinyRGB[1]);
                blue = Math.abs(sourceRGB[2] - tinyRGB[2]);
                if((red + green + blue) < variation){
                    variation = red+green+blue;
                    arrayOfTinyImagesForOutput[x] = tinyImages[i];
                }
                i++;
            }
           x++;
        }
    }

    public BufferedImage getCompositeImage(){
        BufferedImage composite = new BufferedImage(sourceImage.getWidth(),sourceImage.getHeight(),BufferedImage.TYPE_INT_ARGB);
        arrayOfTinyImagesForOutput = new BufferedImage[totalSquares];
        getTinyImagePerSquare();
        int x=0,y=0,i = 0;

        for (BufferedImage p : arrayOfTinyImagesForOutput) {

            Graphics2D temp = composite.createGraphics();
            temp.drawImage(p, x, y, null);
            x += p.getWidth();
            
            if(x >= sourceImage.getWidth()){
                y+= p.getHeight();
                x = 0;
            }
            i++;
        }
        return composite;
    }
}
