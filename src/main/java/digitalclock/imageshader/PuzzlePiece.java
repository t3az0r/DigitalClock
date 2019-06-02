/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitalclock.imageshader;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author cthies
 */
public class PuzzlePiece extends BufferedImage {
    

    public PuzzlePiece(int width, int height, int maxRasterX, int maxRasterY, int rasterX, int rasterY) {
        this(width, height);
        this.paintOneRaster(maxRasterX, maxRasterY, rasterX, rasterY);
    }
    
    public PuzzlePiece(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_ARGB);
    }
        
    public final void paintOneRaster(int maxRasterX, int maxRasterY, int rasterX, int rasterY) {
        int w = getWidth();
        int h = getHeight();
                
        int dx = w / maxRasterX;
        int dy = h / maxRasterY;
        
        int startX = rasterX * dx;
        int startY = rasterY * dy;
        
        Graphics2D g = (Graphics2D) this.getGraphics();
        
        g.setColor(Color.LIGHT_GRAY);
        
        g.fillRect(startX, startY, dx, dy);

        
        if(rasterY < maxRasterY-1) {
            g.fillRect(startX+(2*dx/5), startY+dy, (dx/5), (dy/5));
        }
        
        if(rasterX < maxRasterX-1) {
            g.fillRect(startX+dx, startY+(2*dy/5), (dx/5), (dy/5));
        }
        
        if(rasterY > 0) {
            g.setBackground(new Color(0x00FFFFFF, true));
            g.clearRect(startX+(2*dx/5), startY, (dx/5), (dy/5));
        }
        
        if(rasterX > 0) {
            g.setBackground(new Color(0x00FFFFFF, true));
            g.clearRect(startX, startY+(2*dy/5), (dx/5), (dy/5));
        }
        
    }

    public static void main(String args[]) throws IOException {
        PuzzlePiece puzzlePiece = new PuzzlePiece(400, 300);
        ImageIO.write(puzzlePiece, "PNG", new File("p400x300.png"));
        
        puzzlePiece.paintOneRaster(4, 3, 1, 1);
        ImageIO.write(puzzlePiece, "PNG", new File("p400x300_1_1.png"));

        puzzlePiece.paintOneRaster(4, 3, 2, 2);
        ImageIO.write(puzzlePiece, "PNG", new File("p400x300_2_2.png"));
        
    }
}
