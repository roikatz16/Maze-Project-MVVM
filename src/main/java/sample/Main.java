package sample;

import Model.MyModel;
import View.LandingController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;
    private static MyViewModel viewModel;



    public static MyViewModel getViewModel(){return viewModel;}
    public static void setStage(Stage stage) {
        Main.primaryStage = stage;
    }

    public static Stage getStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        //---------------


        MyModel model = new MyModel();
        viewModel = new MyViewModel(model);
        model.startServers();

        setStage(primaryStage);
        model.addObserver(viewModel);

        //---------------
       FXMLLoader fxmlLoader = new FXMLLoader();




        primaryStage.setTitle("EUROVISION CONTEST MAZE GAME 2019");
        Parent root = fxmlLoader.load(getClass().getResource("../View/Landing.fxml").openStream());
        Scene scene = new Scene(root, 758, 454);
        scene.getStylesheets().add(getClass().getResource("../View/Landing.css").toExternalForm());
        primaryStage.setScene(scene);
        LandingController lc = fxmlLoader.getController();
        lc.setViewModel(viewModel);
        primaryStage.show();

       // fxmlLoader.load(getClass().getResource("../View/MyView.fxml").openStream());
       //MyViewController view = fxmlLoader.getController();
        //MyViewController view = new MyViewController();
        //view.setViewModel(viewModel);
       // viewModel.addObserver(view);

        //fxmlLoader.load(getClass().getResource("../View/NewGame.fxml").openStream());
        //NewGameController view1 = fxmlLoader.getController();
        //NewGameController view1 = new NewGameController();
        //view1.setViewModel(viewModel);
        //viewModel.addObserver(view1);

        //MyViewController myViewController = new MyViewController(viewModel);
        //NewGameController newGameController = new NewGameController(viewModel);

        //viewModel.addObserver(newGameController);
        //model.startServers();


    }



}
