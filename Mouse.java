import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

/**
 * Created by APCS4 on 5/21/2015.
 */
public class Mouse
{
    private Mouse() {}
    private static int xClickBuffer = -1, yClickBuffer = -1;

    public static void pushClickBuffer(int x, int y)
    {
        xClickBuffer = x;
        yClickBuffer = y;
    }

    public static Point getClickBuffer()
    {
        Point rtn = new Point(xClickBuffer, yClickBuffer);
        xClickBuffer = -1;
        yClickBuffer = -1;
        return rtn;
    }
}
