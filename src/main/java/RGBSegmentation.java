import boofcv.gui.image.ImagePanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.Planar;
import georegression.metric.UtilAngle;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class RGBSegmentation implements ImageSegmentator {
    @Override
    public Image segmentate(Image image, float v1, float v2, float v3, float v4) {
        BufferedImage res = SwingFXUtils.fromFXImage(image,null);
        BufferedImage img = segmentation( res, v1, v2,v3, v4);
        return SwingFXUtils.toFXImage(img, null);
    }

    public static BufferedImage segmentation( BufferedImage image, float red, float green, float blue, float dest) {
        Planar<GrayF32> input = ConvertBufferedImage.convertFromPlanar(image, null, true, GrayF32.class);
        Planar<GrayF32> rgb =ConvertBufferedImage.convertFromPlanar(image, null, true, GrayF32.class);
        ImageConverter.processRgbChannels(input, rgb);

        // odległosc euklidesowa do decydowania, które piksele należą do wybranego zbioru
        float maxDist2 = dest * dest;

        GrayF32 R = rgb.getBand(0);
        GrayF32 G = rgb.getBand(1);
        GrayF32 B = rgb.getBand(1);

        // przejchodzi przez każdy piksel i zaznacza, jak blisko jest do wybranego koloru
        BufferedImage output = new BufferedImage(rgb.width, rgb.height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < rgb.height; y++) {
            for (int x = 0; x < rgb.width; x++) {
                float dr = UtilAngle.dist(R.unsafe_get(x, y), red);
                float dg = UtilAngle.dist(G.unsafe_get(x, y), green);
                float db = UtilAngle.dist(B.unsafe_get(x, y), blue);

                float dist2 = dr * dr + dg * dg * db * db;
                if (dist2 <= maxDist2) {
                    output.setRGB(x, y, image.getRGB(x, y));
                }
            }
        }

        return output;
    }


}
