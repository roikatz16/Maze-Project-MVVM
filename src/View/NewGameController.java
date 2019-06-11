package View;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Main;


import java.io.IOException;
import java.net.URL;
import java.util.*;

public class NewGameController extends Controller implements Initializable{


    String[] gameParams = new String[11];

    public TextField size;
    public Text rowLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = Main.getViewModel();
        gameParams[0] = "easy";
        gameParams[3] = "Netta";


        Stage s = Main.getStage();
        s.setOnCloseRequest(e->{
            e.consume();
            closeProgram();
        });
    }



    public void exitGame() {
        closeProgram();


    }



    public void backToLanding(ActionEvent actionEvent) throws IOException {
        Stage s = Main.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("../View/Landing.fxml"));
        s.setScene(new Scene(root, 600, 400));
        Main.setStage(s);
        s.show();

    }



    public void StartGame() throws IOException {
        FXMLLoader fxmlLoader= new FXMLLoader();
        sendParamsToViewModel();
        generateMaze();
        Stage s = Main.getStage();
        Parent root = fxmlLoader.load(getClass().getResource("../View/MyView.fxml").openStream());
        s.setScene(new Scene(root, 1000, 800));
        MyViewController vc = fxmlLoader.getController();

        vc.setViewModel(viewModel);
        viewModel.addObserver(vc);
        Main.setStage(s);

        s.show();
    }

    private void sendParamsToViewModel() {
        viewModel.setParams(gameParams);
    }

    /*
        ///////////////MAZE PARAMS/////////////
        0 - DIFFICULTY
        1 - DIFFICULTY (CUSTOM CASE) ROWS
        2 - DIFFICULTY (CUSTOM CASE) COLUMNS
        3 - CHARACTER
        4 - THEME
        4 - TIME (OLD GAME CASE)
        5 - CHARACTER ROW POSITION
        6 - CHARACTER COL POSITION
        7 - CHARACTER START  ROW POSITION
        8 - CHARACTER START COL POSITION
        9 - CHARACTER END  ROW POSITION
        10 - CHARACTER END COL POSITION


        */
    private void generateMaze() {
        //get maze params from scene
        System.out.println(gameParams[0]);
        int row = 0;
        int col = 0;

        switch (gameParams[0]){
            case "easy":
                System.out.println(gameParams[0]);
                row = 25;
                col = 25;
                break;
            case "medium":
                row = 37;
                col = 37;
                break;
            case "hard":
                row = 51;
                col = 51;
                break;
            case "custom":
                getCustomMazeSize();
                row = Integer.parseInt(gameParams[1]);
                col = Integer.parseInt(gameParams[2]);
        }
        viewModel.generateMaze(row, col,gameParams[3]);
    }

    @Override
    public void update(Observable o, Object arg) {
        boolean bool = false;
    }

    public void pickEasy() {

        gameParams[0]="easy";
        setRowsCol(true);

    }
    public void pickMedium() {

        gameParams[0]="medium";
        setRowsCol(true);
    }
    public void PickHard() {

        gameParams[0]="hard";
        setRowsCol(true);
    }

    public void pressedCustom() {

        gameParams[0]="custom";
        setRowsCol(false);
    }

    private void setRowsCol(boolean val){
        size.setDisable(val);

    }


    public void getCustomMazeSize() {
        if (isInt(size,size.getText()) && isInt(size,size.getText()) ){
            gameParams[1] = size.getText();
            gameParams[2] = size.getText();
        }


    }

    //Validate age
    private boolean isInt(TextField input, String message){
        try{
            int age = Integer.parseInt(input.getText());
            System.out.println("row/col is: " + age);
            return true;
        }catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Ooops, there was an error! " + message + " is not a number");

            alert.showAndWait();
            return false;
        }
    }




    public void pickTreeTheme(ActionEvent actionEvent) {
        gameParams[2]="tree";
    }

    public void pickNetta(MouseEvent mouseEvent) {
        gameParams[3]="Netta";
    }


    public void pickDana(ActionEvent actionEvent) {
        gameParams[3]="Dana";
    }

    public void pickIzhar(ActionEvent actionEvent) {gameParams[3]="Izhar";
    }

    public void pickGali(ActionEvent actionEvent) {gameParams[3]="Gali";
    }

    public void pickEuroVisionTheme(ActionEvent actionEvent) {
        gameParams[4] = "EuroVision";
    }

    public void pickHomeTheme(ActionEvent actionEvent) {
        gameParams[4] = "Home";
    }
}

