import net.minidev.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculations {
    private static String exmp = "https://i.imgur.com/S1pTI7I.png";

    public static void main(String[] args) {

    }

    public static BufferedImage loadImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }



    private JSONObject getBWHistogramImage(int id) {
        Map<Integer, BufferedImage> map = new HashMap<>();
        JSONObject histrogramData = new JSONObject();
        int blackColour = 0;
        int whiteColour = 0;
        for (int i = 0; i < map.get(id).getWidth(); i++) {
            for (int j = 0; j < map.get(id).getHeight(); j++) {
                if (map.get(id).getRGB(i, j) == -1) {
                    whiteColour++;
                } else {
                    blackColour++;
                }
            }
        }
        histrogramData.put("Black", blackColour);
        histrogramData.put("White", whiteColour);
        return histrogramData;
    }

    private JSONObject getRGBHistogramImage(int id) {
        Map<Integer, BufferedImage> map = new HashMap<>();
        JSONObject histrogramData = new JSONObject();
        int redColour = 0;
        int greenColour = 0;
        int blueColour = 0;
        for (int i = 0; i < map.get(id).getWidth(); i++) {
            for (int j = 0; j < map.get(id).getHeight(); j++) {
                Color color = new Color(map.get(id).getRGB(i, j));
                redColour = color.getRed();
                greenColour = color.getGreen();
                blueColour = color.getBlue();
            }
        }
        histrogramData.put("Red", redColour);
        histrogramData.put("Green", greenColour);
        histrogramData.put("Blue", blueColour);
        return histrogramData;
    }

}
