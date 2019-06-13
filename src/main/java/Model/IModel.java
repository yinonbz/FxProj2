package Model;

import algorithms.search.AState;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface IModel {
    void generateMaze(int width, int height);

    void moveCharacter(KeyCode movement);

    int[][] getMaze();

    int getCharacterPositionRow();

    int getCharacterPositionColumn();
    void solveMaze() throws InterruptedException;
    ArrayList<int[]> getSolution();

    int getEndPositionRow();
    int getEndPositionColumn();
    void save(File save) throws IOException;
    void Load(File load) throws IOException;
    void Exit() throws IOException;

}

