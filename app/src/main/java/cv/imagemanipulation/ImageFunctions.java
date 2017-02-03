package cv.imagemanipulation;

import android.graphics.Bitmap;

/**
 * Created by krant on 22-04-2016.
 */


public class ImageFunctions
{
    Bitmap ActiveImage;
    Bitmap BackupImage;
    boolean verbose = false;
    public float minDataRange = Float.MAX_VALUE;
    public float maxDataRange = Float.MIN_VALUE;
    int data[];
    int rows;
    int cols;

    protected void createPixels(Bitmap image)
    {
        System.out.println("In function createPixels");
        int pixel, red, green, blue, r,c;
        rows = image.getHeight();
        cols = image.getWidth();
        data = new int[rows*cols];
        try
        {
            image.getPixels(data, 0, cols, 0, 0, cols, rows);
        }
        catch(Exception e)
        {
            System.out.println("Error: "+e);
            e.printStackTrace(System.out);
            return;
        }

        System.out.println("Rows: "+rows+" Columns: "+cols);
        for(r=0; r<rows; r++)
        {
            for(c=0; c<cols; c++)
            {   pixel = data[r*cols + c];
                red   = (pixel >> 16) & 0xff;
                green = (pixel >>  8) & 0xff;
                blue  = (pixel      ) & 0xff;
                if(verbose)
                    System.out.println("RGB: " + red + "," + green +"," +blue);
                data[r*cols+c] = (int)((red+green+blue)/3);  //SPECIAL NOTE: This sample code converts RGB image to a greyscale one
                //System.out.print(data[r*cols+c]+"\t");
                if(verbose)
                    System.out.println("Pixel: " + (int)((red+green+blue)/3));
                minDataRange = Math.min(minDataRange, data[r*cols+c]);
                maxDataRange = Math.max(maxDataRange, data[r*cols+c]);
            }
            //System.out.println();
        }
        System.out.println("Returning from function createPixels");
    }

    public Bitmap brighten(int x)
    {
        int pixel, alpha, red, green,blue;
        int i =0;
        Bitmap retImage = Bitmap.createBitmap(cols,rows,ActiveImage.getConfig());

        System.out.println("In ImageData.brighten Function  "+ rows*cols+"    "+ActiveImage.getConfig());

        /*for(i=0; i<rows*cols; i++)
        {
            pixel = data[i];
            pixel = pixel + x;
            if(pixel > 255)
                pixel = 255;
            data[i] = pixel;//(int)((alpha+red+green+blue)/4);
            //System.out.print(data[i] + "\t");


        }

        System.out.println(i);*/
        int counter = 0;
        for(int r=0; r<rows; r++)
        {
            for(int c=0; c<cols; c++)
            {
                pixel = ActiveImage.getPixel(c,r);
                //System.out.println(counter++);
                pixel = pixel + x;
                if(pixel > 255)
                    pixel = 255;
                data[i] = pixel;
                retImage.setPixel(c,r,pixel);
            }
            //System.out.println();
        }

        //retImage = ActiveImage;//.createBitmap(data,cols,rows, Bitmap.Config.RGB_565);

        return  retImage;
    }
}
