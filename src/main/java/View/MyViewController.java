package View;


import java.io.File;
import java.io.IOException;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.rmi.runtime.Log;

public class MyViewController implements IView {

    private static final Logger LOG = LogManager.getLogger(); //Log4j2

    private static String ssound = "./resources/maritheme.mp3";
    private static Media sound = new Media(new File(ssound).toURI().toString());
    private static MediaPlayer mediaPlayer = new MediaPlayer(sound);

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }


    @FXML
    public void New(ActionEvent event) throws IOException {

        getNewGameView(event);
        LOG.info("The user started a new game");
    }

    @Override
    public void displayMaze(int[][] maze) {
    }

    public void help(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("help.fxml"));

        Stage window = getHelpWindow(fxmlLoader);

        helpController view = fxmlLoader.getController();

        LOG.info("The user opened the help window");

        window.show();
    }

    public Stage getHelpWindow(FXMLLoader fxml) throws IOException {
        Stage window = getPopUpWindow(fxml,"Help");
        window.setTitle("Help");
        return window;
    }

    public Stage getPopUpWindow(FXMLLoader fxml,String type) throws IOException {
        Parent tableParent = fxml.load();
        Scene scene=null;
        if(type.equals("Help")){
            scene = new Scene(tableParent, 800, 700);
        }
        else{
            scene = new Scene(tableParent, 400, 350);
        }
        scene.getStylesheets().add(getClass().getResource("helpStyle.css").toExternalForm());

        Stage window = new Stage();
        window.setScene(scene);

        return window;
    }


        public void about(ActionEvent event) throws IOException{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about.fxml"));

        Stage window = getPopUpWindow(fxmlLoader,"About");
        window.setTitle("About");

        aboutController view = fxmlLoader.getController();

        LOG.info("The user opened the about window");

        window.show();
    }

    public void Load(ActionEvent event) throws IOException {

        NewGameController NGC = new NewGameController();
        File Load = NGC.GetLoadFile();
        if(Load != null){
            getNewGameView(event).Load(Load);
            LOG.info("the user loaded existing game");

        }
    }

    public MyViewModel getNewGameView(ActionEvent event) throws IOException {
        MyModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("newGame.fxml"));

        Parent tableParent = fxmlLoader.load();
        Scene scene = new Scene(tableParent, 800, 700);
        scene.getStylesheets().add(getClass().getResource("GenerateStyle.css").toExternalForm());

        Stage window = null;
        if (event.getTarget().toString().substring(0, event.getTarget().toString().indexOf('@')).equals("MenuItem")) {
            MenuItem menu = ((MenuItem) event.getSource());
            while (menu.getParentPopup() == null) {
                menu = menu.getParentMenu();
            }

            window = (Stage) menu.getParentPopup().getOwnerWindow();
        } else {
            window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        }

        window.setScene(scene);

        NewGameController view = fxmlLoader.getController();
        view.setSize();
        view.setMusic();
        view.setViewModel(viewModel);
        viewModel.addObserver(view);

        window.show();

        return viewModel;
    }

    public void Exit(ActionEvent event) throws IOException {
        Platform.exit();
        LOG.info("The user quit the game");
        System.exit(0);

    }

    public void preferences(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("preference.fxml"));

        Parent tableParent = fxmlLoader.load();
        Scene scene = new Scene(tableParent, 500, 400);
        scene.getStylesheets().add(getClass().getResource("helpStyle.css").toExternalForm());

        Stage window = new Stage();
        window.setTitle("Properties");
        window.setScene(scene);

        preferencesController view = fxmlLoader.getController();
        view.setButtonsName();

        window.show();
    }



    }


