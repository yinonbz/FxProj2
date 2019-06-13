package View;

import Model.MyModel;
import Server.Configurations;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.ASearchingAlgorithm;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;

public class NewGameController implements Observer, Initializable, IView {

    boolean solve = false;
    boolean lock = true;
    boolean end = false;
    ObservableList listGenerate = FXCollections.observableArrayList();
    ObservableList listSolution = FXCollections.observableArrayList();
    private boolean finish = false;
    private MyViewModel viewModel;
    @FXML
    private ComboBox<String> comboSolve;
    @FXML
    private ChoiceBox<String> choiceSolver;
    //@FXML
  //  private TextField screen;
    @FXML
    public TextField txtfld_rowsNum;
    @FXML
    public TextField txtfld_columnsNum;
    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public SolDisplayer solDisplayer;
    @FXML
    private ComboBox<String> combo;
    @FXML
    public CharacterDisplayer characterDisplayer;
    @FXML
    private Pane pane;
    @FXML
    private BorderPane border;
    @FXML
    private Button B_Stop;

    //private static String ssound = "./resources/maritheme.mp3";
    //private static Media  sound = new Media(new File(ssound).toURI().toString());
    //private static MediaPlayer a = new MediaPlayer(sound);

    /* the code from the lecture*/
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void displayMaze(int[][] maze) {

        mazeDisplayer.setMaze(maze);
        characterDisplayer.setMaze(maze);
        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionColumn = viewModel.getCharacterPositionColumn();
        int endPositionRow = viewModel.getEndPositionRow();
        int endPositionCol = viewModel.getEndPositionColumn();
        characterDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn, endPositionRow, endPositionCol);
        if (solve == true) {
            solDisplayer.setMaze(maze);
            solDisplayer.cleanCell(characterPositionRow, characterPositionColumn);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            displayMaze(viewModel.getMaze());
        }
    }


    @FXML
    private void generate(ActionEvent event) {
        solve =false;
        if (end == true) {
           // a.play();
        }
        MyViewController.writeToLog("user generated a maze", "info");
        solDisplayer.cleansol();
        lock = false;

//        screen.setText("");
        String maze = combo.getValue();

        try {
            Properties prop = getConfigFile();

            FileOutputStream out = new FileOutputStream("resources/config.properties");
            if(maze == null){
                showAlert("please enter maze difficulty");
                MyViewController.writeToLog("user didn't enter ", "alert");
                return;
            }
            if (maze != null) {
                if (maze == "Empty Maze") {
                    prop.setProperty("Generate", "Empty");
           //         screen.setText("Empty Maze");
                    System.out.println("Empty Maze");

                }
                if (maze == "Simple Maze") {
                    prop.setProperty("Generate", "Simple");
              //      screen.setText("Simple Maze");
                    System.out.println("Simple Maze");
                }
                if (maze == "Complex Maze") {
                    prop.setProperty("Generate", "Complex");
                //    screen.setText("Complex Maze");
                    System.out.println("Complex Maze");
                }
                prop.store(out, null);
                out.close();

                String heigth = txtfld_rowsNum.getText();
                String width = txtfld_columnsNum.getText();
                boolean correctVal = true;
                for (int i = 0; i < heigth.length(); i++)
                    if (Character.isDigit(heigth.charAt(i)) == false) {
                        correctVal = false;
                    }
                for (int i = 0; i < width.length(); i++)
                    if (Character.isDigit(width.charAt(i)) == false) {
                        correctVal = false;
                    }
                if (correctVal) {
                    int intWidth = Integer.parseInt(width);
                    int intHeigth = Integer.parseInt(heigth);
                    if (intWidth > 3 && intHeigth > 3) {
                        viewModel.generateMaze(intWidth, intHeigth);
                        displayMaze(viewModel.getMaze());
                        finish = false;
                    } else {
                        showAlert("Please enter correct values");
                  //      screen.setText("Please enter correct values");
                        System.out.println("Please enter correct values");
                    }
                } else {
                    showAlert("Please enter correct values");
                 //   screen.setText("Please enter correct values");
                    System.out.println("Please enter correct values");
                }
                lock = false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void solve(ActionEvent event) {

        if (lock == false) {

            try {
                Properties prop= getConfigFile();

                String massege = comboSolve.getValue();
                if(massege==null){
                    showAlert("please choose way to solve maze");
                }
                if (massege != null) {
                    if (massege == "BFS") {
                        prop.setProperty("Solve", "BFS");
                //        screen.setText("BFS is selected");
                        System.out.println("BFS");
                    }

                    if (massege == "DFS") {
                        prop.setProperty("Solve", "DFS");
                    //    screen.setText("DFS is selected");
                        System.out.println("DFS");

                    }
                    if (massege == "Best First Search") {
                        prop.setProperty("Solve", "Best First Search");
                   //     screen.setText("Best first search is selected");
                        System.out.println("Best first search");
                    }
                    solve = true;
                    viewModel.solveMaze();
                    solDisplayer.setMaze(viewModel.getMaze());
                    solDisplayer.setSol(viewModel.getsolution());
                    MyViewController.writeToLog("user solved a maze", "info");

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("you can't get a solution without a maze");
       //     screen.setText("you can't do it");
            System.out.println("you can't do it");
        }
    }

    private Properties getConfigFile() throws IOException {
        InputStream in = new FileInputStream("resources/config.properties");
        Throwable var1 = null;

        SimpleMazeGenerator var3;
        Properties prop = new Properties();
        prop.load(in);
        in.close();
        return prop;
    }


    @FXML
    public void New(ActionEvent event) throws IOException {
        MyViewController MVC = new MyViewController();
        MVC.getNewGameView(event);
        MyViewController.writeToLog("user started a new maze", "info");
        finish = false;
    }

    public void KeyPressed(KeyEvent keyEvent) throws IOException {
        if (!finish) {
            viewModel.moveCharacter(keyEvent.getCode());
            MyViewController.writeToLog("user moved the characted via" +keyEvent.getCode().toString(), "info");
            keyEvent.consume();
        }
        if (viewModel.getCharacterPositionRow() == viewModel.getEndPositionRow() && viewModel.getCharacterPositionColumn() == viewModel.getEndPositionColumn() && lock == false&&finish==false) {
            endGame();
        }

    }
    @FXML
    public void stopMusic(){
        if(B_Stop.getText().equals("pause music")){
            MyViewController.getMediaPlayer().stop();
            B_Stop.setText("play music");
            MyViewController.writeToLog("user stopped the music", "info");
        }
        else{
            MyViewController.getMediaPlayer().play();
            B_Stop.setText("pause music");
            MyViewController.writeToLog("user started the music", "info");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadData();
    }

    private void loadData() {
        listGenerate.removeAll(listGenerate);
        listSolution.removeAll(listSolution);
        String name_G1 = "Empty Maze";
        String name_G2 = "Simple Maze";
        String name_G3 = "Complex Maze";
        listGenerate.addAll(name_G1, name_G2, name_G3);
        String name_S1 = "BFS";
        String name_S2 = "DFS";
        String name_S3 = "Best First Search";
        listSolution.addAll(name_S1, name_S2, name_S3);
        combo.getItems().addAll(listGenerate);
        comboSolve.getItems().addAll(listSolution);
    }

    public void setSize() {

        mazeDisplayer.widthProperty().bind(pane.heightProperty());
        mazeDisplayer.heightProperty().bind(pane.widthProperty());
        mazeDisplayer.widthProperty().addListener(event -> mazeDisplayer.redraw());
        mazeDisplayer.heightProperty().addListener(event -> mazeDisplayer.redraw());

        solDisplayer.widthProperty().bind(pane.heightProperty());
        solDisplayer.heightProperty().bind(pane.widthProperty());
        solDisplayer.widthProperty().addListener(event -> solDisplayer.presentSol());
        solDisplayer.heightProperty().addListener(event -> solDisplayer.presentSol());

        characterDisplayer.widthProperty().bind(pane.heightProperty());
        characterDisplayer.heightProperty().bind(pane.widthProperty());
        characterDisplayer.widthProperty().addListener(event -> characterDisplayer.displayCharcter());
        characterDisplayer.heightProperty().addListener(event -> characterDisplayer.displayCharcter());

            }


    public void help(ActionEvent event) throws IOException{

        MyViewController MVC = new MyViewController();
        MVC.help(event);
        MyViewController.writeToLog("user used the help window", "info");

    }

    public void about(ActionEvent event) throws IOException{
        MyModel model = new MyModel();
        //model.startServers();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about.fxml"));

        MyViewController MVC = new MyViewController();
        Stage window = MVC.getPopUpWindow(fxmlLoader,"About");

        window.setTitle("About");


        aboutController view = fxmlLoader.getController();
//       view.setResizeEvent(scene);
        view.setViewModel(viewModel);
        //viewModel.addObserver(view);
        MyViewController.writeToLog("user used the about window", "info");

        window.show();
    }


    public void save(ActionEvent event) throws IOException{
        FileChooser fc = new FileChooser();
        fc.setTitle("Save as");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("maze files (*.maze)", "*.maze");
        fc.getExtensionFilters().add(extFilter);

        Stage window = new Stage();
        window.setTitle("Save as");
     //   window.setScene(scene);

        File save = fc.showSaveDialog(window);

        if (save != null) {
            viewModel.save(save);
        }

        MyViewController.writeToLog("user saved an existing game", "info");

    }

    public void Load(ActionEvent event) throws IOException {
        File Load = GetLoadFile();
        if(Load != null){
            viewModel.Load(Load);
            characterDisplayer.displayCharcter();

            displayMaze(viewModel.getMaze());
        }
        MyViewController.writeToLog("user loaded an existing game", "info");

    }

    protected File GetLoadFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Load File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("maze files (*.maze)", "*.maze");
        fc.getExtensionFilters().add(extFilter);

        Stage window = new Stage();
        window.setTitle("Load");


        return fc.showOpenDialog(window);
    }

    public void Exit(ActionEvent event) throws IOException {
        viewModel.Exit();
        Platform.exit();
        MyViewController.writeToLog("user quit the game", "info");
        System.exit(0);

    }

    public void setMusic() {
        //URL resource = getClass().getResource("maritheme.mp3");
       // MyViewController.getMediaPlayer().setCycleCount(MediaPlayer.INDEFINITE);
       // MyViewController.getMediaPlayer().play();
    }

    public void preferences(ActionEvent event) throws IOException{
        MyViewController MVC = new MyViewController();
        MVC.preferences(event);
        MyViewController.writeToLog("user used the preferences window", "info");

    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void zoom(){
        pane.setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event){
                        double zoomFactor = 1.05;
                        double deltaY = event.getDeltaY();
                        if(deltaY<0){
                            zoomFactor =0.95;
                        }
                        pane.setScaleX(pane.getScaleX()*zoomFactor);
                        pane.setScaleY(pane.getScaleY()*zoomFactor);
                        event.consume();
                    }

                });
    }

    public void mouseDrag(MouseEvent event) throws IOException {
        if(viewModel.getMaze() != null) {
            double mouseXPos = event.getX() / (mazeDisplayer.getWidth() / viewModel.getMaze()[0].length);
            double mouseYPos = event.getY() / (mazeDisplayer.getHeight() / viewModel.getMaze().length);

            if (!(viewModel.getCharacterPositionRow() == viewModel.getEndPositionRow() && viewModel.getCharacterPositionColumn() == viewModel.getEndPositionColumn() && lock == false && finish == false)) {
                if (mouseXPos > viewModel.getCharacterPositionColumn() + 1) {
                    viewModel.moveCharacter(KeyCode.NUMPAD6);
                } else if (mouseXPos < viewModel.getCharacterPositionColumn() - 1) {
                    viewModel.moveCharacter(KeyCode.NUMPAD4);
                } else if (mouseYPos > viewModel.getCharacterPositionRow() + 1) {
                    viewModel.moveCharacter(KeyCode.NUMPAD2);
                } else if (mouseYPos < viewModel.getCharacterPositionRow() - 1) {
                    viewModel.moveCharacter(KeyCode.NUMPAD8);
                }

            }
            if (viewModel.getCharacterPositionRow() == viewModel.getEndPositionRow() && viewModel.getCharacterPositionColumn() == viewModel.getEndPositionColumn() && lock == false && finish == false) {
                endGame();
            }
        }
    }

    private void endGame() throws IOException {
        finish = true;
        end = true;
        //a.stop();
        String url = "./resources/CourseClear.mp3";
        Media usound = new Media(new File(url).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(usound);
        mediaPlayer.play();
        // screen.setText("Congratulations!");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EndGame.fxml"));
        Parent tableParent = (Parent) fxmlLoader.load();
        Scene scene = new Scene(tableParent, 800, 700);
        scene.getStylesheets().add(getClass().getResource("EndGameStyle.css").toExternalForm());
        Stage window = new Stage();
        window.setScene(scene);
        window.setTitle("congratulations you found princess peach!!!!!");
        MyViewController.writeToLog("user finished the game successfully", "info");
        window.show();
    }

}

