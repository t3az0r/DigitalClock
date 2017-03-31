/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitalclock.imageshader;

import digitalclock.DigitalClock;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author cthies
 */
public class TestOverlay {

    public TestOverlay() throws IOException {
        BufferedImage large = null;
        large = ImageIO.read(DigitalClock.class.getResourceAsStream("intermission/banana.jpg"));

        BufferedImage small = null;

        //small = ImageIO.read(DigitalClock.class.getResourceAsStream("intermission/ballon.jpeg"));
        //small = ImageIO.read(new File("p400x300_1_1.png"));

        small = new PuzzlePiece(large.getWidth(), large.getHeight());
        ((PuzzlePiece)small).paintOneRaster(4, 3, 1, 1);

        int w = Math.max(large.getWidth(), small.getWidth());
        int h = Math.max(large.getHeight(), small.getHeight());
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();
        g.drawImage(large, 0, 0, null);
        g.drawImage(small, 0, 0, null);

        ImageIO.write(combined, "PNG", new File("twoInOne.png"));

    }

    public static void main(String... args) throws IOException {
        new TestOverlay();
    }

}
