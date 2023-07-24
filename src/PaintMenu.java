import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Menu of the application
 * 
 * Used to change mode, figure created, color of the figure, to save and open files
 */
public class PaintMenu extends HBox{
    Panel panel = null;

    //menu elements:

    /**
     * Label demonstrating which mode is active: cursor, polygon, rectangle or circle
     */
    Label modeIndicator;
    /**
     * Buttons used in the menu:
     * mode buttons
     * cancel button - cancels the creationg of polygon and deletes the red points on the panel
     * delete button - deletes the active figure
     * save and open buttons - used to save current panel as a file or open file as a panel
     * info button - provides instructions and basic informaiton about program
     * 
     */
    PaintMenuButton 
        cursorButton, circleButton, rectangleButton, 
        polygon1Button, polygonCancelButton, 
        deleteButton, saveButton, openButton, infoButton;
        //polygonButton2 - additional functionality
    /**
     * Color picker - used to change the color of new figures created
     */
    PaintMenuColorPicker colorPicker;
    /**
     * Number of sides of new polygons
     */
    PaintMenuInput polygonSidesInput;

    /**
     * Windows appearinng when save or open button are clicked in order to type in the name of the file 
     */
    MenuHelpWindow saveWindow = null;
    MenuHelpWindow openWindow = null;


    /**
     * Constructor
     * Initialises the elements
     * 
     * @param panel link to the panel in order to immidiately tell about changes made by user to the panel
     */
    public PaintMenu(Panel panel){
        super();

        this.setWidth(1000);
        this.setHeight(50);
        this.panel = panel;

        this.modeIndicator = new Label("Chosen mode: Cursor");
        this.cursorButton = new PaintMenuButton(this, PanelFigureType.CURSOR);
        this.circleButton = new PaintMenuButton(this, PanelFigureType.CIRCLE);
        this.rectangleButton = new PaintMenuButton(this, PanelFigureType.RECTANGLE);
        this.polygon1Button = new PaintMenuButton(this, PanelFigureType.POLYGON1);
        // this.polygon2Button = new PaintMenuButton(this, PanelFigureType.POLYGON2);
        this.polygonCancelButton = new PaintMenuButton(this, AdditionalButtonType.POLYGON_CANCEL);
        
        this.colorPicker = new PaintMenuColorPicker(this);
        this.polygonSidesInput = new PaintMenuInput(this);

        this.deleteButton = new PaintMenuButton(this, AdditionalButtonType.DELETE);
        this.saveButton = new PaintMenuButton(this, AdditionalButtonType.SAVE);
        this.openButton = new PaintMenuButton(this, AdditionalButtonType.OPEN);
        this.infoButton = new PaintMenuButton(this, AdditionalButtonType.INFO);

        this.getChildren().addAll(this.modeIndicator, this.cursorButton, this.circleButton, this.rectangleButton,
            this.polygon1Button, this.polygonCancelButton, this.polygonSidesInput, this.colorPicker, this.deleteButton,
            this.saveButton, this.openButton, this.infoButton);
        //this.polygon2Button

        this.saveWindow = new MenuHelpWindow(this, WindowBtnType.SAVE);
        this.openWindow = new MenuHelpWindow(this, WindowBtnType.OPEN);
    }

}
