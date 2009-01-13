/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package compositeimage;

/**
 *
 * @author liamks
 */
public class Square {
    public int red;
    public int green;
    public int blue;
    public int[] rgb;

    public Square(int hexRGB){
        red = calculateRed(hexRGB);
        green = calculateGreen(hexRGB);
        blue = calculateBlue(hexRGB);
        rgb = calculateRGB(hexRGB);
    }


    public int[] calculateRGB(int hexRGB) {
        int[] temp = {red,green,blue};
        return temp;
    }
    public int calculateRed(int hexRGB){
         return (hexRGB>>16 &0xff);
    }

    public int calculateGreen(int hexRGB){
        return (hexRGB>>8 &0xff);
    }

    public int calculateBlue(int hexRGB){
        return (hexRGB >>0 &0xff);
    }

    @Override
    public String toString(){
        String output = "";
        for(int i=0;i<3;i++){
            if(i > 0){
                output = output + ", ";
            }
            output = output + rgb[i];
        }
        return output;
    }
}
