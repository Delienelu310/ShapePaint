import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

enum AdditionalButtonType{
    SAVE, OPEN, DELETE, POLYGON_CANCEL, INFO
}

/**
 * Button class used for buttons in the menu
 */
public class PaintMenuButton extends Button{
    /**
     * Link to the menu to tell changes to panel when click on button occured
     */
    PaintMenu menu = null;
    /**
     * Type of the button - which type of button it is?
     * PanelFigureType for cursor, circle, rectangle and polygon buttons
     * AdditionalButtonType - for others
     */
    PanelFigureType type = null;
    AdditionalButtonType btnType = null;

    /**
     * Constructor of the button for mode buttons
     * @param menu link to the menu of the button
     * @param type type of the mode button, which determines it`s functionality
     */
    public PaintMenuButton(PaintMenu menu, PanelFigureType type){
        this.menu = menu;
        this.type = type;
        
        //the text of button depends on it`s type
        switch(type){
            case CIRCLE:
                this.setText("Circle");
                break;
            case RECTANGLE:
                this.setText("Rectangle");
                break;
            case CURSOR:
                this.setText("Cursor");
                break;
            case POLYGON1:
                this.setText("Polygon 1");
                break;
            case POLYGON2:
                this.setText("Polygon 2");
                break;
            default: 
                break;
        }
        this.setOnAction(new MenuButtonHandler());
    }

    /**
     * Constructor for additional buttons
     * @param menu link to the menu
     * @param btnType type of the button
     */
    public PaintMenuButton(PaintMenu menu, AdditionalButtonType btnType){
        this.menu = menu;
        this.btnType = btnType;
        //setting text of the button based on its type
        switch(btnType){
            case OPEN:
                this.setText("Open");
                break;
            case SAVE:
                this.setText("Save");
                break;
            case DELETE:
                this.setText("Delete");
                break;
            case POLYGON_CANCEL:
                this.setText("Cancel");
                break;
            case INFO:
                this.setText("Info");
            default:
                break;
        }
        this.setOnAction(new MenuButtonHandler());
    }
}

/**
 * Handler, that handles click on the menu buttons based on its type
 */
class MenuButtonHandler implements EventHandler<ActionEvent>{
    @Override
    public void handle(ActionEvent event){
        PaintMenuButton btn = (PaintMenuButton) event.getSource();
        if(btn.type != null)
            //for mode buttons: tell the panel, that mode was changed
            switch(btn.type){
                case CURSOR:
                case CIRCLE:
                case RECTANGLE:
                case POLYGON1:
                case POLYGON2:
                    btn.menu.panel.setChosenFigure(btn.type);
                    btn.menu.modeIndicator.setText("Chosen mode: " + btn.getText());
                    return;
                default:
                    break;
            }
        else
            switch(btn.btnType){
                case SAVE:
                    btn.menu.saveWindow.show();
                    break;
                case OPEN:
                    btn.menu.openWindow.show();
                    break;
                //cancel button uses setChosenFigure, which clears the red temporary points
                case POLYGON_CANCEL:
                    btn.menu.panel.setChosenFigure(PanelFigureType.CURSOR);
                    btn.menu.panel.setChosenFigure(PanelFigureType.POLYGON1);
                    break;
                //deletes active figures
                case DELETE:
                    PanelFigure target = btn.menu.panel.getActiveFigure();
                    if(target == null) return;
                    btn.menu.panel.getChildren().remove(target.figure);
                    btn.menu.panel.deleteActiveFigure();
                    break;
                //creates alert window with information:
                case INFO:
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("Paint");
                    alert.setContentText("\nPrzeznaczenie: to create shapes and modify them\n" + 
                    "Author: Pavlo Seredokha\nInstructions:\n" + 
                    "- to create circle or rectangle: choose figure in menu, press button on the panel and drag the button\n" + 
                    "- to create polygon: set sides number in input, and click point where the points of the polygon. Press cancel to delete points." + 
                    "\n- to change color:" + 
                        "\n\t- change in menu before creating the figure" +
                        "\n\t- or change color of existing figure by clicking right button on figure and changing with color picker\n" + 
                    "\n To move, change size or rotate existing figure, click on cursor on menu and:" + 
                        "\n\t -click on figure to make it active" + 
                        "\n\t - drag it to move it" + 
                        "\n\t - scroll to change size" + 
                        "\n\t - z and x for rotation" + 
                    "\n - you can save and open files");
                    
                    alert.showAndWait();
                    break;
                default:
                    break;
            }
    }

}
