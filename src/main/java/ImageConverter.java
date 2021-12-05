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
                int rgb = 1;
                rgb = (int) ((rgb << 8) + color.getRed() * 255);
                rgb = (int) ((rgb << 8) + color.getGreen() * 255);
                rgb = (int) ((rgb << 8) + color.getBlue() * 255);

                image.setRGB(j, i, rgb);
            }
        }
        try {
            ImageIO.write(image, "png", new File("tmp.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fileInputStream = new FileInputStream("tmp.png");
            Image result = new Image(fileInputStream);
            return Optional.of(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<Image> rgb2ImageAwt(List<List<java.awt.Color>> pixels) {
        int width = pixels.size();
        int height = pixels.get(0).size();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                java.awt.Color color = pixels.get(j).get(i);
                int rgb = 1;
                rgb = (rgb << 8) + color.getRed();
                rgb = (rgb << 8) + color.getGreen();
                rgb = (rgb << 8) + color.getBlue();

                image.setRGB(j, i, rgb);
            }
        }
        try {
            ImageIO.write(image, "png", new File("tmp.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fileInputStream = new FileInputStream("tmp.png");
            Image result = new Image(fileInputStream);
            return Optional.of(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
