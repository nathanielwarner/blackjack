import java.util.ArrayList;
import java.util.List;

/**
 * Created by APCS4 on 5/12/2015.
 */
public class Deck
{

    private Card[] cards;
    private int size;

    public Deck()
    {
        cards = new Card[52];
        size = 52;

        int pos = 0;
        for (String suit : Card.suits)
            for (String rank : Card.ranks)
            {
                cards[pos] = new Card(suit, rank);
                pos++;
            }

    }

    public void shuffle()
    {
        for (int k = size - 1; k > 0; k--) {
            int howMany = k + 1;
            int randPos = (int) (Math.random() * howMany);
            Card temp = cards[k];
            cards[k] = cards[randPos];
            cards[randPos] = temp;
        }
    }

    public Card deal()
    {
        Card rtn = cards[size - 1];
        cards[size - 1] = null;
        size--;
        return rtn;
    }

    public String toString()
    {
        String rtn = "";
        for (int i=0; i<size; i++)
            rtn += "\n" + cards[i].toString();
        rtn += "\n\n" + size;
        return rtn;
    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    public int getSize()
    {
        return size;
    }
}
