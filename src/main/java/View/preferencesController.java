package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class preferencesController {
    private MyViewModel viewModel;
    public Label Difficulty = new Label();
    public Label Solve = new Label();
    public Label Threads = new Label();



    public void setViewModel(MyViewModel viewModel) throws IOException {
        this.viewModel = viewModel;
    }

    public String getStr(String str) throws IOException {
        InputStream in = new FileInputStream("resources/config.properties");
        Throwable var1 = null;

        SimpleMazeGenerator var3;
        Properties prop = new Properties();
        prop.load(in);
        in.close();
        String text = null;
        if (str.equals("Generate")){
            text = prop.getProperty("Generate");
        }
        if(str.equals("Solve")){
            text = prop.getProperty("Solve");
        }
        if(str.equals("Threads")){
            text = prop.getProperty("Threads");
        }

        return text;
    }

    public void setButtonsName() throws IOException {
        Difficulty.setText(getStr("Generate"));
        Solve.setText(getStr("Solve"));
        Threads.setText(getStr("Threads"));

    }

    @FXML
    private void Cancel(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
