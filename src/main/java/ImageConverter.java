import com.sun.javafx.image.impl.BaseIntToByteConverter;
import com.sun.javafx.image.impl.ByteRgb;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImageConverter {
    public static List<List<Color>> image2RGB(Image image) {
        List<List<Color>> result = new ArrayList<>();
        PixelReader pixelReader = image.getPixelReader();

        for (int i = 0; i < image.getWidth(); ++i) {
            List<Color> rowList = new ArrayList<>();
            for (int j = 0; j < image.getHeight(); ++j) {
                Color color = pixelReader.getColor(i, j);
                rowList.add(color);
            }
            result.add(rowList);
        }
        return result;
    }

    public static Optional<Image> rgb2Image(List<List<Color>> pixels) {
        int width = pixels.size();
        int height = pixels.get(0).size();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                Color color = pixels.get(j).get(i);
                int r = (int) color.getRed() * 255;
                int g = (int) color.getGreen() * 255;
                int b = (int) color.getBlue() * 255;
                int rgb = 1;
                rgb = (int) ((rgb << 8) + color.getRed() * 255);
                rgb = (int) ((rgb << 8) + color.getGreen() * 255);
                rgb = (int) ((rgb << 8) + color.getBlue() * 255);
                /*java.awt.Color color1 = new java.awt.Color(r, g, b);
                int rgb = color1.getRGB();*/
                //int rgb = 65536 * r + 256 * g + b;
                /*int rgb = ((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);*/
                image.setRGB(j, i, rgb);
            }
        }
        try {
            ImageIO.write(image,"png",new File("tmp.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("tmp.png");
            Image result = new Image(fileInputStream);
            return Optional.of(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
