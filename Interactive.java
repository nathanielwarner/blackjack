import java.awt.*;

/**
 * Created by APCS4 on 5/22/2015.
 */
public interface Interactive
{
    int NO_CHANGE = -1;
    int MAIN_MENU = -2;
    int HOW_TO_PLAY = -3;
    int PLAY_MENU = -4;

    /**
     * The universal rendering scale that allows for compatibility with high-PPI displays ("Retina")
     */
    double SCALE = 0.5;

    int render(Graphics2D g2d);
}
