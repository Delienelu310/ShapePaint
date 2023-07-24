import javafx.scene.control.Spinner;

/**
 * Input spinner for the main menu, containing information
 * about number of sides in new polygons
 */
public class PaintMenuInput extends Spinner<Integer>{
    PaintMenu menu = null;

    PaintMenuInput(PaintMenu menu){
        super(3, 100, 3);
        this.menu = menu;
        this.valueProperty().addListener((obs, oldValue, newValue) -> {
            this.menu.panel.setPolygonSides(newValue);
            System.out.print(newValue);
        });
    }
}
