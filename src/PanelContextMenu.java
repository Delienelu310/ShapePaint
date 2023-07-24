import javafx.scene.control.ColorPicker;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/**
 * Context menu used by panel when right click occured on figure
 */
public class PanelContextMenu extends VBox{
    /**
     * Link to the panel in order to access the figure easily 
     */
    Panel panel = null;
    PanelColorPicker colorPicker = null;
    /**
     * Constructor of the context menu
     * @param panel link to the panel to easily access the figure clicked
     */
    PanelContextMenu(Panel panel){
        super();
        this.panel = panel;
        colorPicker = new PanelColorPicker(this);
        this.getChildren().add(colorPicker);
    }
}

/**
 * Color picker used by the context menu 
 */
class PanelColorPicker extends ColorPicker{
    /**
     * Fast link to the contextMenu
     */
    PanelContextMenu contextMenu = null;
    
    /**
     * Constructor of the color picker
     * @param contextMenu link to the context menu
     */
    PanelColorPicker(PanelContextMenu contextMenu){
        super();
        this.contextMenu = contextMenu;
        this.setOnAction(new PanelColorPickerHandler());
    }
}

/**
 * Handler of color picker
 * Changes the color of active figure, which is set as active when right click occurred
 */
class PanelColorPickerHandler implements EventHandler<ActionEvent>{

    @Override
    public void handle(ActionEvent event) {
        PanelColorPicker colorPicker = (PanelColorPicker)event.getSource();
        Color color = colorPicker.getValue();
        colorPicker.contextMenu.panel.activeFigure.setColor(color);
    }
};
