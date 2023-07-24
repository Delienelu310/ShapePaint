import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Color picker used by main menu
 */
public class PaintMenuColorPicker extends ColorPicker{
    /**
     * link to the main menu
     */
    PaintMenu menu;

    PaintMenuColorPicker(PaintMenu menu){
        super();
        this.setValue(Color.BLACK);
        this.menu = menu;
        setOnAction(new ColorPickerEventHandler());
    }
}

/**
 * Handler, that immediately tells panel about color change
 */
class ColorPickerEventHandler implements EventHandler<ActionEvent>{

    @Override
    public void handle(ActionEvent event) {
        PaintMenuColorPicker colorPicker = (PaintMenuColorPicker)event.getSource();
        Color color = colorPicker.getValue();
        colorPicker.menu.panel.setPaintColor(color);
    }
};


