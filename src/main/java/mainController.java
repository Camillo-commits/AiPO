import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class mainController implements Initializable {

    @FXML
    ImageView originalImage;

    @FXML
    ImageView modifiedImage;

    @FXML
    AnchorPane originalAnchor;

    @FXML
    AnchorPane modifiedAnchor;

    Image original = null;
    Image modified = null;
    ImageSegmentator imageSegmentatorHsv;
    ImageSegmentator imageSegmentatorRgb;
    ImageSegmentator imageSegmentatorCIELab;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assert originalImage != null : "Fail to inject";
        assert modifiedImage != null : "Fail to inject";
        assert originalAnchor != null : "Fail to inject";
        assert modifiedAnchor != null : "Fail to inject";

        originalImage.setFitWidth(App.getStage().getWidth() / 2);
        modifiedImage.setFitWidth(App.getStage().getWidth() / 2);
        originalImage.setPreserveRatio(true);
        modifiedImage.setPreserveRatio(true);

        imageSegmentatorHsv = new HSVSegmentation();
        imageSegmentatorRgb = new RGBSegmentation();
        imageSegmentatorCIELab = new CIELabSegmentation();
    }

    @FXML
    private void importImage() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image", "*.png", "*.jpg");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showOpenDialog(App.getStage());
        if (file != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file.toString());
                original = new Image(fileInputStream);
                originalImage.setFitWidth(originalAnchor.getWidth());
                originalImage.setImage(original);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Histogram.showHistogram(SwingFXUtils.fromFXImage(original, null));
    }

    @FXML
    private void saveImage() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showSaveDialog(App.getStage());
        File image = new File("tmp.png");

        final String path = file != null ? file.toString() : "modifiedImage";
        FileOutputStream fileOutputStream = null;
        try {
            byte[] imageBytes = image.toURI().toURL().openStream().readAllBytes();
            fileOutputStream = new FileOutputStream(path);
            fileOutputStream.write(imageBytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modifyRGB() {
        TextInputDialog redValueDialog = new TextInputDialog("RED VALUE");
        redValueDialog.setHeaderText("Input value");
        redValueDialog.setContentText("RED VALUE");
        double redValue = verifyMin(Double.parseDouble(redValueDialog.showAndWait().orElse("1")));

        TextInputDialog greenValueDialog = new TextInputDialog("GREEN VALUE");
        greenValueDialog.setHeaderText("Input value");
        greenValueDialog.setContentText("GREEN VALUE");
        double greenValue = verifyMax(Double.parseDouble(greenValueDialog.showAndWait().orElse("1")));

        TextInputDialog blueValueDialog = new TextInputDialog("BLUE VALUE");
        blueValueDialog.setHeaderText("Input value");
        blueValueDialog.setContentText("BLUE VALUE");
        double blueValue = verifyMin(Double.parseDouble(blueValueDialog.showAndWait().orElse("1")));

        TextInputDialog euclidDistDialog = new TextInputDialog("Pixel distance");
        euclidDistDialog.setContentText("Input value");
        euclidDistDialog.setHeaderText("Pixel distance");
        double euclidDist = verifyMax(Double.parseDouble(euclidDistDialog.showAndWait().orElse("1")));

        List<List<Color>> rgb = ImageConverter.image2RGB(original);

        modifiedImage.setImage(modified);
        originalImage.setFitWidth(App.getStage().getWidth() / 2);
        modifiedImage.setFitWidth(App.getStage().getWidth() / 2);
        modified = imageSegmentatorRgb.segmentate(original,(float) redValue,(float) greenValue, (float)blueValue, (float)euclidDist);
        modifiedImage.setImage(modified);
    }

    @FXML
    private void modifyHSV() {
        TextInputDialog hueValueDialog = new TextInputDialog("Lum VALUE");
        hueValueDialog.setHeaderText("Input value");
        hueValueDialog.setContentText("hue VALUE");
        double hueValue = verifyMin(Double.parseDouble(hueValueDialog.showAndWait().orElse("1")));

        TextInputDialog satValueDialog = new TextInputDialog("Saturation VALUE");
        satValueDialog.setHeaderText("Input value");
        satValueDialog.setContentText("Saturation VALUE");
        double satValue = verifyMax(Double.parseDouble(satValueDialog.showAndWait().orElse("1")));

        TextInputDialog euclidDistDialog = new TextInputDialog("Pixel distance");
        euclidDistDialog.setContentText("Input value");
        euclidDistDialog.setHeaderText("Pixel distance");
        double euclidDist = verifyMax(Double.parseDouble(euclidDistDialog.showAndWait().orElse("1")));

        List<List<Color>> rgb = ImageConverter.image2RGB(original);
        originalImage.setFitWidth(App.getStage().getWidth() / 2);
        modifiedImage.setFitWidth(App.getStage().getWidth() / 2);
        modified = imageSegmentatorHsv.segmentate(original,(float) hueValue,(float) satValue,0,(float) euclidDist);
        modifiedImage.setImage(modified);
    }

    @FXML
    private void modifyCieLAB() {
        TextInputDialog lValueDialog = new TextInputDialog("L VALUE");
        lValueDialog.setHeaderText("Input value");
        lValueDialog.setContentText("L VALUE");
        double lValue = verifyMin(Double.parseDouble(lValueDialog.showAndWait().orElse("1")));

        TextInputDialog aValueDialog = new TextInputDialog("A VALUE");
        aValueDialog.setHeaderText("Input value");
        aValueDialog.setContentText("A VALUE");
        double aValue = verifyMax(Double.parseDouble(aValueDialog.showAndWait().orElse("1")));

        TextInputDialog bValueDialog = new TextInputDialog("B VALUE");
        bValueDialog.setHeaderText("Input value");
        bValueDialog.setContentText("B VALUE");
        double bValue = verifyMax(Double.parseDouble(aValueDialog.showAndWait().orElse("1")));

        TextInputDialog euclidDistDialog = new TextInputDialog("Pixel distance");
        euclidDistDialog.setContentText("Input value");
        euclidDistDialog.setHeaderText("Pixel distance");
        double euclidDist = verifyMax(Double.parseDouble(euclidDistDialog.showAndWait().orElse("1")));

        originalImage.setFitWidth(App.getStage().getWidth() / 2);
        modifiedImage.setFitWidth(App.getStage().getWidth() / 2);
        modified = imageSegmentatorCIELab.segmentate(original,(float) lValue,(float) aValue,(float) bValue,(float) euclidDist);
        modifiedImage.setImage(modified);
    }

    private double verifyMin(double min) {
        return min < 0 ? 0: min;
    }

    private double verifyMax(double max) {
        return max > 255 ? 255 : max;
    }
}
