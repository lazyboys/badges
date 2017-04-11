import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Ирек on 06.03.2017.
 */

public class Badge {
    Font namesf = new Font("Museo Sans Cyrl 900", Font.PLAIN, 40);
    Font regionf = new Font("Museo Sans Cyrl 900", Font.PLAIN, 36);
    Font typef = new Font("Museo Sans Cyrl 900", Font.PLAIN, 50);
    int PW = 250, PH = 330;

    BufferedImage badge;
    public Badge(String name1, String name2, String type, String region, BufferedImage photo, BufferedImage background) throws IOException {
        badge = background;
        Graphics2D graphs = badge.createGraphics();
        graphs.setFont(namesf);
        graphs.drawString(name1, 570-(graphs.getFontMetrics().stringWidth(name1)), 670);
        graphs.drawString(name2, 570-(graphs.getFontMetrics().stringWidth(name2)), 720);
        graphs.setFont(regionf);
        while(graphs.getFontMetrics().stringWidth(region) > 350){
            graphs.setFont(graphs.getFont().deriveFont((float) (graphs.getFont().getSize()-1)));
        }
        graphs.drawString(region, 570-(graphs.getFontMetrics().stringWidth(region)), 770);
        graphs.setFont(typef);
        if(type.equals("Руководитель \nделегации")) {
            graphs.setFont(graphs.getFont().deriveFont((float) (36)));
            graphs.drawString(type.split("\n")[0], 50, 900);
            graphs.drawString(type.split("\n")[1], 50, 940);
        } else {
            while(graphs.getFontMetrics().stringWidth(type) > 350){
                graphs.setFont(graphs.getFont().deriveFont((float) (graphs.getFont().getSize()-1)));
            }
            graphs.drawString(type, 50, 930);
        }
        if(photo.getWidth()/PW < photo.getHeight()/PH){
        }
        graphs.drawImage(photo, 65, 200, PW, PH, null);
        graphs.dispose();
    }

    public BufferedImage getBadge() {
        return badge;
    }
}
