package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.util.StringTokenizer;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Group root = new Group();
        /*
        Pane pane = new Pane();

        root.getChildren().add();
        */

        primaryStage.setTitle("Hello World");
        final Scene scene = new Scene(root, 551, 400);


        final Server server = new Server();
        server.start();
        //final Server server = new Server();

        scene.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                    if(server.isConnected()){
                        scene.setFill(Color.GREEN);
                    } else {
                        scene.setFill(Color.RED);
                    }
                } else {
                    event.consume();
                }
            }
        });

        // Dropping over surface
        scene.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    String filePath = null;
                    for (File file:db.getFiles()) {
                        if(server.isConnected()){
                            filePath = file.getAbsolutePath();
                            System.out.println(filePath);
                            server.transfer(filePath);
                        } else {
                            System.out.println("Couldn't transfer file.");
                        }
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });

        scene.setOnDragExited(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                scene.setFill(Color.WHITE);
            }
        });


        primaryStage.setScene(scene);
        primaryStage.show();




    }

    private String extractFileName(String filePath){
        String[] tokens = filePath.split("\\\\");
        return tokens[tokens.length-1];
    }



}
