import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by APCS4 on 5/12/2015.
 */
public class Game implements Interactive
{
    private boolean pause;

    // Width and height for both buttons
    private final Dimension buttonSize = new Dimension(170, 50);

    // Hit Button
    private BufferedImage hitButton;
    private boolean hBActive;
    private final int hBX = 20, hBY = 40;

    // Stand Button
    private BufferedImage standButton;
    private boolean stBActive;
    private final int stBX = 20, stBY = 100;

    // Double Button
    private BufferedImage doubleButton;
    private boolean dBActive;
    private final int dBX = 20, dBY = 160;

    // Split Button
    private BufferedImage splitButton;
    private boolean spBActive;
    private final int spBX = 20, spBY = 220;

    // Exit Button
    private BufferedImage exitButton;
    private boolean eBActive;
    private final int eBX = 20, eBY = 280;

    private final Point dealerCardPosStart = new Point(475, 20);
    private final Point playerCardPosStart = new Point(54, 360);
    private final int spaceBtwnCards = 40;
    private final int vSpaceBtwnCards = 5;
    private final Point dealerUncoveredCardStart = new Point(420, 20);

    private Deck deck;
    private Card hiddenCard;
    private ArrayList<Card> visibleCards;
    private boolean showHiddenCard;
    private boolean outOfCashNotify;



    private Player player;

    // Background image
    private BufferedImage background;

    /**
     * Initialize a new Game
     * @param buyIn the player's starting balance
     */
    public Game(int buyIn)
    {

        Card.initHiddenCardImage(); // Initializes the static Card class's BufferedImage of a flipped over card

        // Initialize the background and button images
        try
        {
            background = ImageIO.read(new File("img/table.png"));
            hitButton = ImageIO.read(new File("img/hit-button.png"));
            standButton = ImageIO.read(new File("img/stand-button.png"));
            doubleButton = ImageIO.read(new File("img/double-button.png"));
            splitButton = ImageIO.read(new File("img/split-button.png"));
            exitButton = ImageIO.read(new File("img/exit-button.png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        player = new Player(buyIn);

        newRound();

        pause = true;
        outOfCashNotify = false;
    }

    private void newRound()
    {
        // Initialize the dealer's deck of cards and deal two
        visibleCards = new ArrayList<Card>();
        deck = new Deck();
        deck.shuffle();
        hiddenCard = deck.deal();
        visibleCards.add(deck.deal());
        showHiddenCard = false;

        player.newRound();
        player.dealTo(deck.deal());
        player.dealTo(deck.deal());
    }

    private void updateButtons()
    {
        if (!pause)
        {
            hBActive = true;
            stBActive = true;
            eBActive = true;
            if (player.getBalance() >= player.getCurrentBet() * 2)
                dBActive = true;
            if (player.cardsSplittable())
                spBActive = true;
        }
        else
        {
            hBActive = false;
            stBActive = false;
            dBActive = false;
            spBActive = false;
            eBActive = false;
        }
    }

    private int updateGame(Point point)
    {
        if (player.isBust())
        {
            pause = true;
        }
        else if (outOfCashNotify)
        {
            pause = true;
            return MAIN_MENU;
        }
        else if (deck.isEmpty())
        {
            EventQueue.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    JOptionPane.showMessageDialog(null, "The deck is out of cards", "Game over", JOptionPane.NO_OPTION);
                }
            });
            return MAIN_MENU;
        }
        else if (player.isNewRoundReq())
        {
            if (player.getBalance() == 0)
            {
                EventQueue.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        JOptionPane.showMessageDialog(null, "You have run out of cash ):", "You suck", JOptionPane.NO_OPTION);
                    }
                });
                return MAIN_MENU;
            }
            else
                newRound();
        }
        // Process click of "Exit"
        else if (point.getX() >= eBX && point.getX() <= eBX + buttonSize.getWidth()
                && point.getY() >= eBY && point.getY() <= eBY * + buttonSize.getHeight())
        {
            return MAIN_MENU;
        }

        // Process click of "Hit"
        else if (point.getX() >= hBX && point.getX() <= hBX + buttonSize.getWidth()
                && point.getY() >= hBY && point.getY() <= hBY  + buttonSize.getHeight())
        {
            hit();

        }

        // Process click of "Stand"
        else if (point.getX() >= stBX && point.getX() <= stBX + buttonSize.getWidth()
                && point.getY() >= stBY && point.getY() <= stBY + buttonSize.getHeight())
        {
            stand();
        }

        // Process click of "Double"
        else if (point.getX() >= dBX && point.getX() <= dBX + buttonSize.getWidth()
                && point.getY() >= dBY && point.getY() <= dBY  + buttonSize.getHeight())
        {
            player.doubleBet();
            hit();
            stand();
        }

        return NO_CHANGE;
    }

    private void hit()
    {
        player.dealTo(deck.deal());
    }

    private void stand()
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                showHiddenCard = true;
                while (dealerCardSum() < 17 && !dealerHasSoft17()) // Dealer continues to take hits while his total is below 17 (or if he has a "soft 17")
                {
                    pause = true;
                    try
                    {
                        Thread.sleep(1000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    visibleCards.add(deck.deal());
                }
                if (dealerCardSum() > 21 || player.getCardTotal() > dealerCardSum())
                {
                    player.win();
                    JOptionPane.showMessageDialog(null, "You have won!", "(:", JOptionPane.INFORMATION_MESSAGE);
                } else if (dealerCardSum() == player.getCardTotal())
                {
                    JOptionPane.showMessageDialog(null, "Your total is the same as the dealer's. You win or lose nothing.", "Push", JOptionPane.INFORMATION_MESSAGE);
                } else
                {
                    player.lose();
                    JOptionPane.showMessageDialog(null, "You have lost.", "You suck", JOptionPane.INFORMATION_MESSAGE);
                }
                if (player.getBalance() != 0)
                    newRound();
                else
                {
                    pause = true;
                    EventQueue.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                Thread.sleep(1);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                            JOptionPane.showMessageDialog(null, "You have run out of cash ):", "You Suck", JOptionPane.NO_OPTION);
                        }
                    });
                    outOfCashNotify = true;
                }
            }
        });
    }

    private int dealerCardSum()
    {
        int sum = 0;
        if (hiddenCard.getPointValue() == 11)
            sum += 1;
        else
            sum += hiddenCard.getPointValue();
        for (Card c : visibleCards)
        {
            if (c.getPointValue() == 11)
                sum += 1;
            else
                sum += c.getPointValue();
        }
        return sum;
    }

    private boolean dealerHasSoft17()
    {
        boolean hasAce = false;
        if (hiddenCard.getPointValue() == 11)
            hasAce = true;
        for (Card card : visibleCards)
            if (card.getPointValue() == 11)
                hasAce = true;
        return hasAce && dealerCardSum() == 17;
    }

    @Override
    public int render(Graphics2D g2d)
    {
        updateButtons();

        // Render the background image of a blackjack table
        AffineTransform universalScale = new AffineTransform();
        universalScale.scale(SCALE, SCALE);
        g2d.drawImage(background, universalScale, null);

        // Render the hit button if it is active
        if (hBActive)
        {
            AffineTransform hBTransform = new AffineTransform();
            hBTransform.setToTranslation(hBX, hBY);
            hBTransform.scale(buttonSize.getWidth() / hitButton.getWidth(),
                    buttonSize.getHeight() / hitButton.getHeight());
            g2d.drawImage(hitButton, hBTransform, null);
        }

        // Render the stand button if it is active
        if (stBActive)
        {
            AffineTransform stBTransform = new AffineTransform();
            stBTransform.setToTranslation(stBX, stBY);
            stBTransform.scale(buttonSize.getWidth() / hitButton.getWidth(),
                    buttonSize.getHeight() / hitButton.getHeight());
            g2d.drawImage(standButton, stBTransform, null);
        }

        // Render the double button if it is active
        if (dBActive)
        {
            AffineTransform dBTransform = new AffineTransform();
            dBTransform.setToTranslation(dBX, dBY);
            dBTransform.scale(buttonSize.getWidth() / hitButton.getWidth(),
                    buttonSize.getHeight() / hitButton.getHeight());
            g2d.drawImage(doubleButton, dBTransform, null);
        }

        // Render the split button if it is active
        if (spBActive)
        {
            AffineTransform spBTransform = new AffineTransform();
            spBTransform.setToTranslation(spBX, spBY);
            spBTransform.scale(buttonSize.getWidth() / hitButton.getWidth(),
                    buttonSize.getHeight() / hitButton.getHeight());
            g2d.drawImage(splitButton, spBTransform, null);
        }

        // Render the exit button if it is active
        if (eBActive)
        {
            AffineTransform eBTransform = new AffineTransform();
            eBTransform.setToTranslation(eBX, eBY);
            eBTransform.scale(buttonSize.getWidth() / hitButton.getWidth(),
                    buttonSize.getHeight() / hitButton.getHeight());
            g2d.drawImage(exitButton, eBTransform, null);
        }

        // Render a string that indicates the current balance
        g2d.setColor(Color.WHITE);
        g2d.drawString("Balance: $" + player.getBalance(), 10, 20);

        // When the player has chosen a bet, indicates that bet, and activates the buttons
        if (player.readyToStart())
        {
            pause = false;
        }

        if (player.getCurrentBet() != -1)
        {
            g2d.setColor(Color.WHITE);
            g2d.drawString("Current Bet: $" + player.getCurrentBet(), 10, 35);
        }


        AffineTransform cardTransform = new AffineTransform();
        // Render the hidden card
        if (showHiddenCard)
        {
            cardTransform.setToTranslation(dealerUncoveredCardStart.getX(), dealerUncoveredCardStart.getY());
            cardTransform.scale(SCALE * ((double) Card.SIZE.getWidth() / hiddenCard.getImage().getWidth()),
                    SCALE * ((double) Card.SIZE.getHeight() / hiddenCard.getImage().getHeight()));
            g2d.drawImage(hiddenCard.getImage(), cardTransform, null);
        }
        else
        {
            cardTransform.setToTranslation(dealerCardPosStart.getX(), dealerCardPosStart.getY());
            cardTransform.scale(SCALE * ((double) Card.SIZE.getWidth() / Card.getHiddenCardImage().getWidth()),
                    SCALE * ((double) Card.SIZE.getHeight() / Card.getHiddenCardImage().getHeight()));
            g2d.drawImage(Card.getHiddenCardImage(), cardTransform, null);
        }

        // Render all the other cards
        for (int i=0; i<visibleCards.size(); i++)
        {
            cardTransform.setToTranslation(dealerCardPosStart.getX() + spaceBtwnCards * (i+1), dealerCardPosStart.getY() + vSpaceBtwnCards * (i+1));
            cardTransform.scale(SCALE * ((double) Card.SIZE.getWidth() / visibleCards.get(i).getImage().getWidth()),
                    SCALE * ((double) Card.SIZE.getHeight() / visibleCards.get(i).getImage().getHeight()));
            g2d.drawImage(visibleCards.get(i).getImage(), cardTransform, null);
        }

        // Render the player's cards
        for (int i= 0; i<player.getCards().size(); i++)
        {
            cardTransform.setToTranslation(playerCardPosStart.getX() + spaceBtwnCards * (i), playerCardPosStart.getY() + vSpaceBtwnCards * (i));
            cardTransform.scale(SCALE * ((double) Card.SIZE.getWidth() / player.getCards().get(i).getImage().getWidth()),
                    SCALE * ((double) Card.SIZE.getHeight() / player.getCards().get(i).getImage().getHeight()));
            g2d.drawImage(player.getCards().get(i).getImage(), cardTransform, null);
        }

        Point mouseClick = Mouse.getClickBuffer();


        return updateGame(mouseClick);
    }


}
