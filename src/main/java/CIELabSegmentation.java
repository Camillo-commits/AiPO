import boofcv.alg.color.ColorLab;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.Planar;
import georegression.metric.UtilAngle;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class CIELabSegmentation implements ImageSegmentator {
    @Override
    public Image segmentate(Image image, float v1, float v2, float v3, float v4) {
        BufferedImage res = SwingFXUtils.fromFXImage(image, null);
        BufferedImage img = segmentation(res, v1, v2, v3, v4);
        return SwingFXUtils.toFXImage(img, null);
    }

    public static BufferedImage segmentation(BufferedImage image, float lum, float aVal, float bVal, float dest) {
        Planar<GrayF32> input = ConvertBufferedImage.convertFromPlanar(image, null, true, GrayF32.class);
        Planar<GrayF32> rgb = ConvertBufferedImage.convertFromPlanar(image, null, true, GrayF32.class);
        ImageConverter.processRgbChannels(input, rgb);
        ColorLab.rgbToLab(rgb, input);

        // odległosc euklidesowa do decydowania, które piksele należą do wybranego zbioru
        float maxDist2 = dest * dest;
        GrayF32 L = rgb.getBand(0);
        GrayF32 A = rgb.getBand(1);
        GrayF32 B = rgb.getBand(1);

        // przechodzi przez każdy piksel i zaznacza, jak blisko jest do wybranego koloru
        BufferedImage output = new BufferedImage(rgb.width, rgb.height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < rgb.height; y++) {
            for (int x = 0; x < rgb.width; x++) {
                float dl = UtilAngle.dist(L.unsafe_get(x, y), lum);
                float da = UtilAngle.dist(A.unsafe_get(x, y), aVal);
                float db = UtilAngle.dist(B.unsafe_get(x, y), bVal);

                float dist2 = dl * dl + da * da * db * db;
                if (dist2 <= maxDist2) {
                    output.setRGB(x, y, image.getRGB(x, y));
                }
            }
        }
        return output;
    }
}
