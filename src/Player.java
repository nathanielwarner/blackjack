import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by APCS4 on 5/28/2015.
 */
public class Player
{
    private ArrayList<Card> cards;

    private int balance, currentBet;

    private boolean bust, newRoundReq, readyToStart;

    public Player(int buyIn)
    {
        balance = buyIn;
        currentBet = -1;
    }

    public ArrayList<Card> getCards()
    {
        return cards;
    }

    public int getBalance()
    {
        return balance;
    }

    public int getCurrentBet()
    {
        return currentBet;
    }

    public void dealTo(Card card)
    {
        cards.add(card);

        if (getCardTotal() > 21)
        {
            lose();
            bust = true;
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run()
                {
                    JOptionPane.showMessageDialog(null, "You have gone bust ):", "Bust", JOptionPane.NO_OPTION);
                    newRoundReq = true;
                }
            });
        }
    }

    public int getCardTotal()
    {
        int sum = 0;
        int aceCount = 0;
        for (Card c : cards)
        {
            if (c.getPointValue() == 11)
            {
                aceCount++;
                sum += 1;
            }

            else
                sum += c.getPointValue();
        }
        for (int i=0; i<aceCount; i++)
            if (sum + 10 <= 21)
                sum += 10;
        return sum;
    }

    public boolean cardsSplittable()
    {
        return getCards().size() == 2 && getCards().get(0).matches(getCards().get(1));
    }

    public boolean isNewRoundReq()
    {
        return newRoundReq;
    }

    public boolean isBust()
    {
        if (bust)
        {
            bust = false;
            return true;
        }
        else return false;
    }

    public void newRound()
    {
        newRoundReq = false;
        cards = new ArrayList<Card>();
        currentBet = -1;
        bust = false;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                boolean proceedFlag = false;
                while (!proceedFlag)
                {
                    //int attempt = Integer.parseInt(JOptionPane.showInputDialog(null, "Please enter your bet:", "New Round", JOptionPane.NO_OPTION));
                    String[] options = {"OK"};
                    JPanel panel = new JPanel();
                    JLabel lbl = new JLabel("Please enter your bet: $");
                    JTextField txt = new JTextField(10) {
                        @Override
                        public void addNotify()
                        {
                            super.addNotify();
                            requestFocus();
                        }
                    };
                    panel.add(lbl);
                    panel.add(txt);
                    int attempt = JOptionPane.showOptionDialog(null, panel, "New Round", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options , options[0]);

                    if(attempt == 0)
                    {
                        String text = txt.getText();
                        int input = Integer.parseInt(text);
                        if (input < 1 || input > balance)
                        {
                            JOptionPane.showMessageDialog(null, "Please enter a bet between $1 and your current balance.", "Error", JOptionPane.NO_OPTION);

                        }
                        else
                        {
                            currentBet = input;
                            proceedFlag = true;
                        }
                    }

                }
                readyToStart = true;

            }
        });

    }

    public boolean readyToStart()
    {
        if (readyToStart)
        {
            readyToStart = false;
            return true;
        }
        else
            return false;
    }

    public void doubleBet()
    {
        currentBet *= 2;
    }

    public void win()
    {
        balance += currentBet;
    }

    public void lose()
    {
        balance -= currentBet;
    }
}
