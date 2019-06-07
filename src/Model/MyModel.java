package Model;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyCode;


public class MyModel extends Observable implements IModel {

    Server mazeGeneratingServer;
    Server solveSearchProblemServer;
    private int currentPositionRow;
    private int currentPositionColumn;
    private Maze maze;
    private ExecutorService threadPool = Executors.newCachedThreadPool();



    public MyModel() {
        //Raise the servers

        mazeGeneratingServer =new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 5000, new ServerStrategySolveSearchProblem());
    }

    public void startServers() {
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
    }


    @Override
    public void stopServers() {
        try {
            mazeGeneratingServer.stop();
            solveSearchProblemServer.stop();
            threadPool.shutdown();
            threadPool.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public void generateMaze(int row, int col) {

        //Generate maze
        threadPool.execute(() -> {
            generateMazeWithServers(row,col);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentPositionColumn = maze.getStartPosition().getColumnIndex();
            currentPositionRow = maze.getStartPosition().getRowIndex();
            setChanged();
            notifyObservers();
        });
    }

    private void generateMazeWithServers(int row,int col){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])((byte[])fromServer.readObject());
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[20018];
                        is.read(decompressedMaze);
                        maze = new Maze(decompressedMaze);
                        if(maze != null){
                            maze.print();
                        }

                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
    }


    public void moveCharacter(KeyCode movement, char[][] array) {
        switch (movement) {
            case T://UP
                if(checkPassability(currentPositionRow -1, currentPositionColumn, array)){
                    currentPositionRow--;
                }
                break;


            case B://DOWN
                if(checkPassability(currentPositionRow +1, currentPositionColumn, array)){
                    currentPositionRow++;
                }
                break;

            case F://LEFT
                if(checkPassability(currentPositionRow, currentPositionColumn -1, array)){
                    currentPositionColumn--;
                }
                break;

            case H://RIGHT
                if(checkPassability(currentPositionRow, currentPositionColumn +1, array)){
                    currentPositionColumn++;
                }
                break;

            case R://UP-LEFT
                if(checkPassability(currentPositionRow -1, currentPositionColumn -1, array)){
                    currentPositionRow--;
                    currentPositionColumn--;
                }
                break;

            case Y://UP-RIGHT
                if(checkPassability(currentPositionRow -1, currentPositionColumn +1, array)){
                    currentPositionRow--;
                    currentPositionColumn++;
                }
                break;

            case N://DOWN-RIGHT
                if(checkPassability(currentPositionRow +1, currentPositionColumn +1, array)){
                    currentPositionRow++;
                    currentPositionColumn++;
                }
                break;

            case V://DOWN-LEFT
                if(checkPassability(currentPositionRow +1, currentPositionColumn -1, array)){
                    currentPositionRow++;
                    currentPositionColumn--;
                }
                break;
        }
        setChanged();
        notifyObservers(false);
    }

    private boolean checkPassability(int row, int col, char[][] array) {
        if(row<0 ||
                col<0 ||
                row>= array.length||
                col>= array[0].length||
                array[row][col]=='1')
        {
            return false;
        }
        return true;
    }


    @Override
    public Maze getMaze() {
        return maze;
    }

    public int getCurrentPositionRow() {
        return currentPositionRow;
    }

    public int getCurrentPositionColumn() {
        return currentPositionColumn;
    }

    @Override
    public int[][] solveMaze() {
        return new int[0][];
    }

}
