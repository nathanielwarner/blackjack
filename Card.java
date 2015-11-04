import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by APCS4 on 5/14/2015.
 */
public class Card
{
    private static final String hiddenUrl = "cards/hidden.png";
    private static BufferedImage hiddenImage;

    public static final Dimension SIZE = new Dimension(250, 363);
    private BufferedImage image;

    public static final String[] suits =
            {
                    "clubs", "diamonds", "hearts", "spades"
            };

    public static final String[] ranks =
            {
                    "2", "3", "4", "5", "6", "7", "8", "9", "10",
                    "ace", "jack", "queen", "king"
            };

    private String suit, rank;

    public Card(String cardSuit, String cardRank)
    {
        suit = cardSuit;
        rank = cardRank;
        try
        {
            image = ImageIO.read(new File("cards/" + rank + "_of_" + suit + ".png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean matches(Card card)
    {
        return suit == card.suit && rank == card.rank;
    }

    public String toString()
    {
        return rank + " " + suit;
    }

    public int getPointValue()
    {
        if (rank.equals("ace"))
            return 11;
        else if (rank.equals("jack") || rank.equals("queen") || rank.equals("king"))
            return 10;
        else
            return Integer.parseInt(rank);
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public static void initHiddenCardImage()
    {
        try
        {
            hiddenImage = ImageIO.read(new File(hiddenUrl));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static BufferedImage getHiddenCardImage()
    {
        return hiddenImage;
    }
}
