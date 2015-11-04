import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements MouseListener
{

    private final Dimension SIZE = new Dimension(800, 600);
    private final double SCALE = 0.5;

    private Interactive interactive;

    private BufferedImage splashImage, newGameButton, howToButton;

    private RenderingHints renderingHints;

    private String cmd;


    public GamePanel()
    {
        setSize(SIZE);
        setPreferredSize(SIZE);
        addMouseListener(this);
        //renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        setBackground(Color.BLACK);
        interactive = new MainMenu();
    }

    @Override
    public void paintComponent(Graphics g)
    {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //g2d.setRenderingHints(renderingHints); // Enable anti-aliasing\
        if (cmd != null)
        {
            int c = Integer.parseInt(cmd);
            cmd = null;
            interactive = new Game(c);
        }
        int command = Interactive.NO_CHANGE;
        if (interactive != null)
            command = interactive.render(g2d);
        if (command == Interactive.MAIN_MENU)
            interactive = new MainMenu();
        else if (command == Interactive.PLAY_MENU)
        {

            EventQueue.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    cmd = JOptionPane.showInputDialog(null, "Please enter your buy-in amount: $", "New Game", JOptionPane.NO_OPTION);
                }
            });

        }
        repaint();
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("Blackjack");
        GamePanel panel = new GamePanel();
        frame.add(panel);
        frame.setContentPane(panel);
        frame.pack();
        if(frame.getContentPane().getWidth() != 800){
            frame.pack();
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        Mouse.pushClickBuffer(e.getX(), e.getY());
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e)
    {

    }
}
