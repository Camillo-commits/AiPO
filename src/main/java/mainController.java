import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.imageio.stream.ImageInputStream;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assert originalImage != null : "Fail to inject";
        assert modifiedImage != null : "Fail to inject";
        assert originalAnchor != null : "Fail to inject";
        assert modifiedAnchor != null : "Fail to inject";
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
                Image image = new Image(fileInputStream);
                originalImage.setFitWidth(originalAnchor.getWidth());
                originalImage.setImage(image);
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
        modifiedImage.setFitWidth(modifiedAnchor.getWidth());
        List<List<Color>> rgb = ImageConverter.image2RGB(originalImage.getImage());

        modifiedImage.setImage(ImageConverter.rgb2Image(rgb).get());


    }

    @FXML
    private void modifyHSV() {

    }

    @FXML
    private void modifyCieLAB() {

    }
}
