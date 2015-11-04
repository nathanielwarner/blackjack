import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenu implements Interactive
{
    // Background image
    private BufferedImage splashImage;

    // Width and height for both buttons
    private final int bW = (int) (237 / SCALE), bH = (int) (70 / SCALE);

    // New Game Button
    private BufferedImage newGameButton;
    private final int ngX = (int) (478 / SCALE), ngY = (int) (300 / SCALE);

    // How To Play Button
    private BufferedImage howToButton;
    private final int hpX = (int) (478 / SCALE), hpY = (int) (400 / SCALE);

    public MainMenu()
    {
        try
        {
            splashImage = ImageIO.read(new File("img/splash.png"));
            newGameButton = ImageIO.read(new File("img/newgame-button.png"));
            howToButton = ImageIO.read(new File("img/how-to-play-button.png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int render(Graphics2D g2d)
    {
        AffineTransform universalScale = new AffineTransform();
        universalScale.scale(SCALE, SCALE);
        g2d.drawImage(splashImage, universalScale, null);

        AffineTransform newGameTrans = new AffineTransform();

        newGameTrans.setToTranslation(ngX * SCALE, ngY * SCALE);
        newGameTrans.scale(SCALE, SCALE);
        g2d.drawImage(newGameButton, newGameTrans, null);

        AffineTransform howToTrans = new AffineTransform();
        howToTrans.setToTranslation(hpX * SCALE, hpY * SCALE);
        howToTrans.scale(SCALE, SCALE);
        g2d.drawImage(howToButton, howToTrans, null);

        Point p = Mouse.getClickBuffer();
        // Process click of "New Game"
        if (p.getX() >= ngX * SCALE && p.getX() <= ngX * SCALE + bW * SCALE
                && p.getY() >= ngY * SCALE && p.getY() <= ngY * SCALE + bH * SCALE)
        {
            return PLAY_MENU;
        }

        // Process click of "How To Play"
        else if (p.getX() >= hpX * SCALE && p.getX() <= hpX * SCALE + bW * SCALE
                && p.getY() >= hpY * SCALE && p.getY() <= hpY * SCALE + bH * SCALE)
        {
            return HOW_TO_PLAY;
        }
        return NO_CHANGE;
    }
}
