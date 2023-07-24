import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.io.*;

enum WindowBtnType {
    SAVE, OPEN
}

/**
 * Window, which appers when save and open buttons are clicked
 * 
 * It contains input, to get name of the file, and button, to initiate the process
 */
public class MenuHelpWindow extends Stage {
    /**
     * Link to the main menu
     */
    PaintMenu menu = null;
    /**
     * Input textfield, which gets name of the file
     */
    TextField input = null;
    /**
     * Button, click on which initiates the process
     */
    WindowButton btn = null;

    /**
     * Constructor of the help window: initialises and positions the elements and the window
     * @param menu link to the main menu
     * @param type type of the window: save or open
     */
    MenuHelpWindow(PaintMenu menu, WindowBtnType type){
        super();
        this.menu = menu;
        VBox pane = new VBox();
        input = new TextField();
        btn = new WindowButton(this, type);
        
        pane.getChildren().addAll(input, btn);

        Scene scene = new Scene(pane, 500, 200);
        this.setScene(scene);
    }   
}

/**
 * Class for button used in help window
 */
class WindowButton extends Button{
    /**
     * Link to help window
     */
    MenuHelpWindow window = null;
    /**
     * Type of the button: save or open - determines the functionality
     */
    WindowBtnType type = null;
    /**
     * Constructor of the button
     * @param window link to the window
     * @param type type of the button
     */
    WindowButton(MenuHelpWindow window, WindowBtnType type){
        super();
        this.window = window;
        this.type = type;
        switch(type){
            case OPEN:
                this.setText("Open");
                break;
            case SAVE:
                this.setText("Save");
                break;
            default:
                break;

        }
        this.setOnAction(new WindowButtonHandler());
    }
}

/**
 * Handler of the button in the help window
 */
class WindowButtonHandler implements EventHandler<ActionEvent>{
    @Override
    public void handle(ActionEvent event){
        WindowButton btn = (WindowButton)event.getSource();
        switch(btn.type){
            case SAVE:
                try{
                    //create the writing streams
                    FileOutputStream fileOutputStream = new FileOutputStream("../db/" + btn.window.input.getText() + ".txt");
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    
                    //each figure is translated into serializable form to easily write them down into files
                    Panel panel = btn.window.menu.panel; 
                    for(int i = 0; i < panel.getChildren().size(); i++){
                        PanelFigureSerializable obj;
                        if(panel.getChildren().get(i) instanceof MyCircle)
                            obj = new PanelFigureSerializable( (( MyCircle)panel.getChildren().get(i)).panelFigure);
                        else if(panel.getChildren().get(i) instanceof MyRectangle){
                            obj = new PanelFigureSerializable( (( MyRectangle)panel.getChildren().get(i)).panelFigure);
                        }else if(panel.getChildren().get(i) instanceof MyPolygon){
                            obj = new PanelFigureSerializable( (( MyPolygon)panel.getChildren().get(i)).panelFigure);
                        }else continue;

                        objectOutputStream.writeObject(obj);
                    }
                    //closing of writing streams
                    objectOutputStream.close();
                    fileOutputStream.close();
                    
                    btn.window.close();
                }catch(Exception e){
                    btn.window.input.setText("Error");
                    System.out.print(e);
                }
                break;
            case OPEN:
                
                try{
                    //opening of the reading streams
                    FileInputStream fileInputStream = new FileInputStream("../db/" + btn.window.input.getText() + ".txt");
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    
                    //reading figures as serializable objects
                    ArrayList<PanelFigureSerializable> figuresRead = new ArrayList<PanelFigureSerializable>();
                    try{
                        while(true){
                            figuresRead.add((PanelFigureSerializable)objectInputStream.readObject());
                        }
                    }catch (EOFException e){

                    }

                    //translating them into figures
                    
                    Panel newPanel = new Panel();

                    for(int i = 0; i < figuresRead.size(); i++){
                        PanelFigureSerializable figureRead = figuresRead.get(i);
                        PanelFigure figure = figureRead.getOriginalFigure();

                        newPanel.getChildren().add(figure.figure);
                    }

                    //closing reading streams
                    objectInputStream.close();
                    fileInputStream.close();

                    
                    //creating new panel and replacing old one with it
                    BorderPane container = (BorderPane)btn.window.menu.getParent();
                    container.getChildren().remove(btn.window.menu.panel);
                    container.setCenter(newPanel);
                    btn.window.menu.panel = newPanel;

                    btn.window.close();
                    //set menu to front on z-index coordinate
                    btn.window.menu.toFront();
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            
                break;
            default:
                break;
        }
    }
}


