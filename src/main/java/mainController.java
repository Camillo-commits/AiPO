import com.sun.javafx.stage.PopupWindowHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.PopupWindow;

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
    }

    @FXML
    private void saveImage() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showSaveDialog(App.getStage());
        File image = new File(modifiedImage.getImage().getUrl());

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
        TextInputDialog rMin = new TextInputDialog("R min");
        rMin.setHeaderText("Input value");
        rMin.setContentText("R min");
        double rLow = verifyMin(Double.parseDouble(rMin.showAndWait().orElse("0")));

        TextInputDialog rMax = new TextInputDialog("R high");
        rMax.setHeaderText("Input value");
        rMax.setContentText("R high");
        double rHigh = verifyMax(Double.parseDouble(rMax.showAndWait().orElse("255")));

        TextInputDialog gMin = new TextInputDialog("G min");
        gMin.setHeaderText("Input value");
        gMin.setContentText("G min");
        double gLow = verifyMin(Double.parseDouble(gMin.showAndWait().orElse("0")));

        TextInputDialog gMax = new TextInputDialog("G high");
        gMax.setContentText("G high");
        gMax.setHeaderText("Input value");
        double gHigh = verifyMax(Double.parseDouble(gMax.showAndWait().orElse("255")));

        TextInputDialog bMin = new TextInputDialog("B min");
        bMin.setHeaderText("Input value");
        bMin.setContentText("B min");
        double bLow = verifyMin(Double.parseDouble(bMin.showAndWait().orElse("0")));

        TextInputDialog bMax = new TextInputDialog("B high");
        bMax.setContentText("B high");
        bMax.setHeaderText("Input value");
        double bHigh = verifyMax(Double.parseDouble(bMax.showAndWait().orElse("255")));

        List<List<Color>> rgb = ImageConverter.image2RGB(original);

        originalImage.setFitWidth(App.getStage().getWidth() / 2);
        modifiedImage.setFitWidth(App.getStage().getWidth() / 2);
        modified = ImageConverter.rgb2Image(rgb).get();
        modifiedImage.setImage(modified);
    }

    @FXML
    private void modifyHSV() {

    }

    @FXML
    private void modifyCieLAB() {

    }

    private double verifyMin(double min) {
        return min < 0 ? 0: min;
    }

    private double verifyMax(double max) {
        return max > 255 ? 255 : max;
    }
}
