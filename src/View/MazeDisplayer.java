package View;

import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sample.Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {

    private char[][] maze;
    private ArrayList<int[]> solutionAsIntegersList;

    private int characterPositionRow;
    private int characterPositionColumn;
    private String[] gameParams;
    private int goalPositionRow;
    private int goalPositionColumn;
    private boolean showSolution;






    /* region Properties */
    private StringProperty ImageFileNameWall;
    private StringProperty ImageFileNameCharacter1;
    private StringProperty ImageFileNameCharacter2;
    private StringProperty ImageFileNameCharacter3;
    private StringProperty ImageFileNameCharacter4;
    private StringProperty ImageFileNameStartAndGoal;
    private StringProperty ImageFileNameSolution;
    private MyViewModel viewModel;


    /**
     * constructor
     */
    public MazeDisplayer(){
        showSolution = true;
        ImageFileNameWall = new SimpleStringProperty();
        ImageFileNameCharacter1 = new SimpleStringProperty();
        ImageFileNameCharacter2 = new SimpleStringProperty();
        ImageFileNameCharacter3 = new SimpleStringProperty();
        ImageFileNameCharacter4 = new SimpleStringProperty();
        ImageFileNameStartAndGoal = new SimpleStringProperty();
        ImageFileNameSolution = new SimpleStringProperty();


        viewModel = Main.getViewModel();
        gameParams = viewModel.getParams();

    }
    public void setMaze(char[][] maze, int row, int col) throws FileNotFoundException {
        this.maze = maze;
        goalPositionRow = row;
        goalPositionColumn = col;
        Image characterImage = chooseCharacter();
        redraw(characterImage);

    }

    public void setSolutionAsIntegersList(ArrayList<int[]> solutionAsIntegersList) throws FileNotFoundException {
        this.solutionAsIntegersList = solutionAsIntegersList;
        Image characterImage = chooseCharacter();
        redraw(characterImage);
    }

    public void setCharacterPosition(int row, int column) throws FileNotFoundException {
        characterPositionRow = row;
        characterPositionColumn = column;
        Image characterImage =  chooseCharacter();
        redraw(characterImage);
    }

    private Image chooseCharacter() throws FileNotFoundException {

        System.out.println("merge");
        switch (gameParams[3]){
            case "Netta":
                return new Image(new FileInputStream(ImageFileNameCharacter1.get()));
            case "Dana":
                return  new Image(new FileInputStream(ImageFileNameCharacter2.get()));
            case "Gali":
                return  new Image(new FileInputStream(ImageFileNameCharacter3.get()));
            case "Izhar":
                return  new Image(new FileInputStream(ImageFileNameCharacter4.get()));
        }
        return new Image(new FileInputStream(ImageFileNameCharacter1.get()));
    }


    public void redraw(Image characterImage) {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image startAndGoalImage = new Image(new FileInputStream(ImageFileNameStartAndGoal.get()));
                Image solutionImage = new Image (new FileInputStream(ImageFileNameSolution.get()));

                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Maze
                for (int i = 0; i < maze[0].length; i++) {
                    for (int j = 0; j < maze.length; j++) {
                        if (maze[j][i] == '1') {
                            gc.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                            gc.drawImage(wallImage, i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                        }
                    }
                }

               //draw solution
               if(solutionAsIntegersList!=null && showSolution){
                   for (int i = 0; i < solutionAsIntegersList.size(); i++) {
                       gc.fillRect(solutionAsIntegersList.get(i)[1] * cellHeight, solutionAsIntegersList.get(i)[0] * cellWidth, cellHeight, cellWidth);
                       gc.drawImage(solutionImage, solutionAsIntegersList.get(i)[1] * cellHeight, solutionAsIntegersList.get(i)[0] * cellWidth, cellHeight, cellWidth);

                   }
               }

                // Draw Start and Goal
                gc.drawImage(startAndGoalImage, goalPositionColumn * cellHeight, goalPositionRow  * cellWidth, cellHeight, cellWidth);
                // Draw character
                gc.drawImage(characterImage, characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void redrawWithoutSolution() throws FileNotFoundException {
        Image characterImage =  chooseCharacter();
        showSolution = false;
        redraw(characterImage);
    }

    public void redrawWithSolution() throws FileNotFoundException {
        Image characterImage =  chooseCharacter();
        showSolution = true;
        redraw(characterImage);
    }

    /* Getters & Setters */

    public String[] getGameParams() {
        return gameParams;
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }



    /* region Properties Getters & Setters */

    public String getImageFileNameWall() { return ImageFileNameWall.get(); }
    public void setImageFileNameWall(String imageFileNameWall) { this.ImageFileNameWall.set(imageFileNameWall); }

    public String getImageFileNameCharacter1() { return ImageFileNameCharacter1.get();}
    public StringProperty imageFileNameCharacter1Property() { return ImageFileNameCharacter1;}
    public void setImageFileNameCharacter1(String imageFileNameCharacter1) {
        this.ImageFileNameCharacter1.set(imageFileNameCharacter1);
    }

    public String getImageFileNameCharacter2() { return ImageFileNameCharacter2.get(); }
    public StringProperty imageFileNameCharacter2Property() { return ImageFileNameCharacter2; }
    public void setImageFileNameCharacter2(String imageFileNameCharacter2) {
        this.ImageFileNameCharacter2.set(imageFileNameCharacter2);
    }

    public String getImageFileNameCharacter3() { return ImageFileNameCharacter3.get(); }
    public StringProperty imageFileNameCharacter3Property() { return ImageFileNameCharacter3; }
    public void setImageFileNameCharacter3(String imageFileNameCharacter3) {
        this.ImageFileNameCharacter3.set(imageFileNameCharacter3);
    }

    public String getImageFileNameCharacter4() { return ImageFileNameCharacter4.get(); }
    public StringProperty imageFileNameCharacter4Property() { return ImageFileNameCharacter4; }
    public void setImageFileNameCharacter4(String imageFileNameCharacter4) {
        this.ImageFileNameCharacter4.set(imageFileNameCharacter4);
    }

    public String getImageFileNameStartAndGoal() { return ImageFileNameStartAndGoal.get(); }
    public void setImageFileNameStartAndGoal(String imageFileNameStartAndGoal) {
        this.ImageFileNameStartAndGoal.set(imageFileNameStartAndGoal);
    }

    public String getImageFileNameSolution() { return ImageFileNameSolution.get(); }
    public StringProperty imageFileNameSolutionProperty() {  return ImageFileNameSolution; }
    public void setImageFileNameSolution(String imageFileNameSolution) {
        this.ImageFileNameSolution.set(imageFileNameSolution);
    }



}

