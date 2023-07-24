import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.layout.BorderPane;

/**
 *  Application class   
 * 
 * Contains panel and menu with instruments
 */
public class Paint extends Application{

    @Override
    public void start(Stage primaryStage){

        //panel - painting space with figures
        Panel panel = new Panel();
        //menu contains all the buttons and inputs used to change figures, colors, save or open files
        PaintMenu menu = new PaintMenu(panel);
        
        //main container
        BorderPane pane = new BorderPane();
        pane.setTop(menu);
        pane.setCenter(panel);

        menu.toFront();

        //opening the window
        Scene scene = new Scene(pane, 1000, 600);
        primaryStage.setTitle("Pavlo Seredokha");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}

//use:
//--module-path "C:/Users/HP/JAVA/openjfx-20.0.1_windows-x64_bin-sdk/javafx-sdk-20.0.1/lib" --add-modules javafx.controls

//to generate documentation:
// javadoc --module-path "C:/Users/HP/JAVA/openjfx-20.0.1_windows-x64_bin-sdk/javafx-sdk-20.0.1/lib" --add-modules javafx.controls  -d "documentation" "src/*"

//to compile:
// javac -d bin --module-path "C:/Users/HP/JAVA/openjfx-20.0.1_windows-x64_bin-sdk/javafx-sdk-20.0.1/lib" --add-modules javafx.controls -cp "src/" src/Paint.java

//to launch: (from bin)
// java --module-path "C:/Users/HP/JAVA/openjfx-20.0.1_windows-x64_bin-sdk/javafx-sdk-20.0.1/lib" --add-modules javafx.controls  Paint