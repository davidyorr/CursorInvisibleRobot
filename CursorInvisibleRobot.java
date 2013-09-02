import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.KeyboardFocusManager;
import java.awt.KeyEventDispatcher;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CursorInvisibleRobot {
  public static void main(String[] args) throws AWTException {
    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
      int clicks = 0;
      Robot robot = new Robot();
      Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
      BufferedImage screen;
      int color, r, g, b;
      boolean prevBlack = false;
      boolean clicked = false;

      @Override
      public void run() {
        if (clicks == 1000) {
          System.exit(1);
        }

        pressed = false;
        screen = robot.createScreenCapture(screenRect);
        for (int y = 0; y < screen.getHeight(); y++) {
          for (int x = 0; x < screen.getWidth(); x++) {
            color = screen.getRGB(x,y);
            r = (color & 0x00ff0000) >> 16;
            g = (color & 0x0000ff00) >> 8;
            b = color & 0x000000ff;

            if (prevBlack && !isBlack(r,g,b)) {
              if (r > 80 && g > 80 && b > 80 &&
                  r < 100 && g < 100 && b < 100 &&
                  !clicked) {
                robot.mouseMove(x,y);
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                clicked = true;
                clicks++;
              }
            }

            prevBlack = isBlack(r,g,b);
          }
        }
      }
    }, 3000, 350, TimeUnit.MILLISECONDS);
  }

  static boolean isBlack(int r, int g, int b) {
    return r == 0 && g == 0 && b == 0; 
  }
}