/*
 * The MIT License
 *
 * Copyright 2018 Mohamed AIT MANSOUR <contact@numidea.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package photoeditor;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

/**
 *
 * @author Mohamed AIT MANSOUR <contact@numidea.com>
 */
public class FXMLMainController implements Initializable {

    @FXML
    private void selectDirectoryHandler(MouseEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory == null) {
            PhotoEditor.alertBuilder(1,Alert.AlertType.WARNING);
        } else {
            String path = selectedDirectory.getAbsolutePath();
            PhotoEditor.setSelectedPath(path);
            if (PhotoEditor.getExtentionAndFileFounder().checkFileExistence(path, PhotoEditor.FILE_TEXT_EXT)) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLHome.fxml"), PhotoEditor.getBundleByLocal());
                Parent rootWindow = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                 Scene scene = new Scene(rootWindow);
                scene.setNodeOrientation(PhotoEditor.nodeOrientation);
                        stage.setScene(scene);

                Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                app_stage.hide();
                stage.setResizable(false);
                stage.show();
            } else {
            PhotoEditor.alertBuilder(2,Alert.AlertType.WARNING);
            }

        }
    }

    @FXML
    private void closeHandler(MouseEvent event) {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialize
    }

}