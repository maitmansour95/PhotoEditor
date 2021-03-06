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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Mohamed AIT MANSOUR <contact@numidea.com>
 */
public class PhotoEditor extends Application {

    private static String selectedPath;
    private static FindCertainExtension extentionAndFileFounder = new FindCertainExtension();
    private static Map < String, ArrayList > MapOfKeywords;
    private static final Alert alert = new Alert(Alert.AlertType.NONE);
    public static final String FILE_TEXT_EXT = ".jpg";
    public static Locale locale;
    public static NodeOrientation nodeOrientation;

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        setSelectedPath("");
        unFreezeKeywordsMap();
        unFreezeConfiguration();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLMain.fxml"), getBundleByLocal());

        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        scene.setNodeOrientation(nodeOrientation);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Stop stage and Freeze keywords and configuration
     * @throws Exception 
     */
    @Override
    public void stop() throws Exception {
        freezeKeywordsMap();
        freezeConfiguration();

    }
    
    /**
     * Load scene on language switch
     * @param orientation
     * @throws IOException 
     */
    public void reload(Boolean orientation) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLHome.fxml"), getBundleByLocal());
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        if (orientation) {
            nodeOrientation = NodeOrientation.RIGHT_TO_LEFT;
        } else {
            nodeOrientation = NodeOrientation.LEFT_TO_RIGHT;
        }
        scene.setNodeOrientation(nodeOrientation);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    /**
     * SelectedPath Getter
     * @return String Selected Path
     */
    public static String getSelectedPath() {
        return PhotoEditor.selectedPath;
    }

    /**
     * ExtentionAndFileFounder Getter / Singleton
     * @return FindCertainExtension Instance
     */
    public static FindCertainExtension getExtentionAndFileFounder() {
        if (PhotoEditor.extentionAndFileFounder == null) {
            PhotoEditor.extentionAndFileFounder = new FindCertainExtension();
        }
        return PhotoEditor.extentionAndFileFounder;
    }

    /**
     * SelectedPath Setter
     * @param selectedPath 
     */
    public static void setSelectedPath(String selectedPath) {
        PhotoEditor.selectedPath = selectedPath;
    }

    /**
     * Prepare image Path 
     * @param imageName 
     * @return  
     */
    public static String prepareImagePath(String imageName) {
        return "file:///" + PhotoEditor.getSelectedPath() + "\\" + imageName;
    }

    /**
     * ExtentionAndFileFounder Getter / Singleton
     * @return FindCertainExtension Instance
     * @throws java.lang.Exception
     */
    public static Map < String, ArrayList > getMapOfKeywords() throws Exception {
        if (PhotoEditor.MapOfKeywords == null) {
            PhotoEditor.MapOfKeywords = new HashMap < > ();
        }
        return PhotoEditor.MapOfKeywords;
    }

    /**
     * freeze keywords Map
     * @throws Exception 
     */
    public static void freezeKeywordsMap() throws Exception {
        FileOutputStream keywordsFile = new FileOutputStream("src/data/keywords.dat");
        try (ObjectOutputStream writer = new ObjectOutputStream(keywordsFile)) {
            writer.writeObject(getMapOfKeywords());
        }
    }

    /**
     * freeze keywords Map
     * @throws Exception 
     */
    public static void freezeConfiguration() throws Exception {
        try (PrintWriter writer = new PrintWriter("src/data/configuration.dat", "UTF-8")) {
            writer.print(locale.toString());
        }
    }

    /**
     * unfreeze keywords and fill MapOfKeywords
     * @return
     * @throws Exception 
     */
    public static boolean unFreezeKeywordsMap() throws Exception {
        File f = new File("src/data/keywords.dat");
        if (f.exists() && !f.isDirectory()) {
            FileInputStream keywordsFile = new FileInputStream("src/data/keywords.dat");
            try (ObjectInputStream reader = new ObjectInputStream(keywordsFile)) {
                MapOfKeywords = (Map < String, ArrayList > ) reader.readObject();
            }
            return true;
        }
        f.getParentFile().mkdirs();
        f.createNewFile();
        return false;
    }

    /**
     * unfreeze keywords and fill MapOfKeywords
     * @return
     * @throws Exception 
     */
    public static boolean unFreezeConfiguration() throws Exception {
        File f = new File("src/data/configuration.dat");
        if (f.exists() && !f.isDirectory()) {
            String savedLocal;
            try (Scanner in = new Scanner(new FileReader("src/data/configuration.dat"))) {
                savedLocal = in .next();
            }
            setLocal(savedLocal);
            if (savedLocal.contains("ar")) {
                nodeOrientation = NodeOrientation.RIGHT_TO_LEFT;
            } else {
                nodeOrientation = NodeOrientation.LEFT_TO_RIGHT;
            }
            return true;
        } else {
            f.getParentFile().mkdirs();
            locale = Locale.getDefault();
            nodeOrientation = NodeOrientation.LEFT_TO_RIGHT;
            freezeConfiguration();
        }

        return false;
    }

    /**
     * built Alert using messages and type
     * @param message
     * @param type
     * @return 
     */
    public static Optional < ButtonType > alertBuilder(int message, AlertType type) {
        alert.setAlertType(type);
        alert.setTitle("PhotoEditor");
        alert.setHeaderText(null);
        ResourceBundle bundle =getBundleByLocal();

      
        if (message == 1) {
            alert.setContentText(bundle.getString("alert1"));
        } else if (message == 2) {
            alert.setContentText(bundle.getString("alert2"));
        } else if (message == 3) {
            alert.setContentText(bundle.getString("alert3"));
        } else if (message == 4) {
            alert.setContentText(bundle.getString("alert4"));
        } else if (message == 5) {
            alert.setContentText(bundle.getString("alert5"));
        }  else if (message == 6) {
            alert.setContentText(bundle.getString("alert6"));
        } else if (message == 7) {
            alert.setContentText(bundle.getString("alert7"));
        } else if (message == 8) {
            alert.setContentText(bundle.getString("alert8"));
        } else if (message == 9) {
            alert.setContentText(bundle.getString("alert9"));
        } else if (message == 10) {
            alert.setContentText(bundle.getString("alert10"));
        } 
        if (type==AlertType.CONFIRMATION) {
             Alert alertConfirmation =new Alert(AlertType.CONFIRMATION);
              alertConfirmation.setAlertType(type);
            alertConfirmation.setTitle("PhotoEditor");
            alertConfirmation.setHeaderText(null);
            alertConfirmation.setContentText(alert.getContentText());
            ButtonType buttonYes = new ButtonType(bundle.getString("buttonYes"), ButtonBar.ButtonData.YES);
            ButtonType buttonNo = new ButtonType(bundle.getString("buttonNo") , ButtonBar.ButtonData.NO);
            alertConfirmation.getButtonTypes().setAll(buttonYes,buttonNo);
        return alertConfirmation.showAndWait();
                }
        return alert.showAndWait();
    }

    /**
     * set Local by string
     * @param loc 
     */
    public static void setLocal(String loc) {
        if (loc == null) {
            locale = Locale.getDefault();
        } else {
            locale = new Locale(loc);
        }
    }
    /**
     * get used Local
     * @return local String
     */
    public static String getLocal() {
        return locale.toString();
    }
    
    /**
     * Get bundle by used locale
     * @return 
     */
    public static ResourceBundle getBundleByLocal() {
        return ResourceBundle.getBundle("bundles.lang", locale);
    }



}