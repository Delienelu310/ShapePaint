import javafx.scene.layout.Pane;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import javafx.scene.shape.Shape;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

/**
 * Panel class - canvas of the program and container of all figures
 */
public class Panel extends Pane{

    /**
     * Variable containing active figure
     * 
     * Active figure is the figure user is trying to move/resize/rotate
     */
    PanelFigure activeFigure = null;

    /**
     * Variable containing figure in process of creation\
     * 
     * It allows change the size of the figure during creating process
     * 
     */
    PanelFigure creatingFigure = null;
    
    /**
     * Used for drag&drop
     */
    boolean mouseDown = false;

    /**
     * Chosen mode in menu
     * If cursor, moving/resizing/rotating of active figures is allow
     * If rectangle/circle/polygon - creationg of figures is allowed
     */
    PanelFigureType chosenFigure = PanelFigureType.CURSOR;
    /**
     * Color, which is used when creating new figures
     */
    Color paintColor = Color.BLACK;
    /**
     * To create a polygon, user sets points
     * When number of points comes up to the number of polygon sides, the polygon is fully created and drawn
     */
    int polygonSides = 3;
    /**
     * cursor coordinates just before the drag
     * used for drag&drop
     */
    double dragX, dragY;  

    /**
     * Context menu containing color picker
     * It is used to change the color of existing figures by clicking right click on them
     */
    PanelContextMenu contextMenu = null;

    public void setMouseDown(boolean isMouseDown){
        this.mouseDown = true;
    }

    public boolean isMouseDown(){
        return this.mouseDown;
    }

    public Color getPaintColor(){
        return this.paintColor;
    }

    public void setPaintColor(Color color){
        this.paintColor = color;
    }

    public void setPolygonSides(int sides){
        this.polygonSides = sides;
    }

    public int getPolygonSides(){
        return this.polygonSides;
    }

    /**
     * setter of chosenFigure member
     * 
     * If previous mode was polygon creation mode, then the points of unfinished polygon are deleted 
     * 
     * @param chosenFigure current mode of a program, chosen in the menu
     */
    public void setChosenFigure(PanelFigureType chosenFigure){
        if(this.chosenFigure == PanelFigureType.POLYGON1){
            if(this.creatingFigure != null && this.creatingFigure.polygonPoints != null){
                for(int i = 0; i < this.creatingFigure.polygonPoints.size(); i++){
                    this.getChildren().remove(this.creatingFigure.polygonPoints.get(i));
                }
            }
            this.creatingFigure = null;
        }
        this.chosenFigure = chosenFigure;
    }

    public PanelFigure getActiveFigure(){
        return this.activeFigure;
    }

    public void deleteActiveFigure(){
        this.activeFigure = null;
    }

    /**
     * Panel constructor
     * 
     * bounding the handlers to itself and initializating the context menu
     */
    Panel(){
        super();
        this.setOnMousePressed(new PanelEventHandler());
        this.setOnMouseReleased(new PanelEventHandler());
        this.setOnMouseDragged(new PanelEventHandler());
        this.setOnScroll(new PanelScrollHandler());
        this.setOnKeyPressed(new PanelKeyHandler());

        this.contextMenu = new PanelContextMenu(this);
    }
}

/**
 * Event handler for all mouse operations
 * 
 * 1. Creating figures and changing their size during creation process
 * 2. Activating the figure
 * 3. Moving the activated figure
 * 4. Opening and closing of the context menu
 */
class PanelEventHandler implements EventHandler<MouseEvent>{
    @Override
    public void handle(MouseEvent event){
        Panel panel = (Panel)event.getSource();
        Object target = event.getTarget();
        

        //Opening context menu
        if(event.getButton() == MouseButton.SECONDARY){
            
            //filter all events except click
            if(event.getEventType() != MouseEvent.MOUSE_PRESSED) return;
            
            //if context menu is opened and click is made NOT on the menu, 
            //the context menu closes
            if(panel.getChildren().contains(panel.contextMenu)){
                if(target != panel.contextMenu && target != panel.contextMenu.colorPicker){
                    panel.getChildren().remove(panel.contextMenu);
                    panel.activeFigure = null;
                }
            //othervise, context menu opens, if the right click was made on cshape
            }else{
                if(target instanceof Shape){
                    if(target instanceof MyCircle) panel.activeFigure = ((MyCircle)target).panelFigure;
                    if(target instanceof MyRectangle) panel.activeFigure = ((MyRectangle)target).panelFigure;
                    if(target instanceof MyPolygon) panel.activeFigure = ((MyPolygon)target).panelFigure;
                    
                    //setting position of context menu
                    panel.contextMenu.setLayoutX(event.getX());
                    panel.contextMenu.setLayoutY(event.getY());
                    //setting figure`s color as default value
                    panel.contextMenu.colorPicker.setValue((Color)((Shape)target).getFill());
                    //opening context menu
                    panel.getChildren().add(panel.contextMenu); 
                }
                
            }
            //exiting the event handler
            return;
        }

        //closing the context menu, if the click was not made on the context menu
        if(panel.getChildren().contains(panel.contextMenu)) 
            if(target != panel.contextMenu && target != panel.contextMenu.colorPicker) panel.getChildren().remove(panel.contextMenu);


        //other events
        if(event.getEventType() == MouseEvent.MOUSE_PRESSED){
            panel.setMouseDown(true);
            panel.requestFocus();

            //Handling left click in Cursor mode - mode of moving/resizing/rotating figures
            if(panel.chosenFigure == PanelFigureType.CURSOR){
                //if click was not made on figure - ignore
                if(target == panel){
                    panel.activeFigure = null;
                    return;
                //if click was made on shape - make this shape active
                }else if(target != panel.activeFigure && target instanceof Shape){
                    if(target instanceof MyCircle) panel.activeFigure =  ((MyCircle)target).panelFigure;
                    else if(target instanceof MyRectangle) panel.activeFigure = ((MyRectangle)target).panelFigure;
                    else if(target instanceof MyPolygon) panel.activeFigure = ((MyPolygon)target).panelFigure;

                    //setting temporary variables for future drag and drop
                    panel.dragX = event.getX();
                    panel.dragY = event.getY();
                    return;
                //if click was made on active figure, then ignore
                }else if(target == panel.activeFigure){
                    panel.dragX = event.getX();
                    panel.dragY = event.getY();
                }
            //Handling left click for mode of creation Circle or Rectangle
            }else if(panel.chosenFigure == PanelFigureType.CIRCLE ||
            panel.chosenFigure == PanelFigureType.RECTANGLE){
                
                PanelFigure currentFigure = new PanelFigure(panel.chosenFigure);
                panel.creatingFigure = currentFigure;
                
                //setting the color, chosen in menu
                currentFigure.setColor(panel.getPaintColor());
                //setting the foundation point - point which won`t move for the time of creation
                currentFigure.setMainPoint(event.getX(), event.getY());

                panel.getChildren().add(currentFigure.figure);


            //Handling left click for mode of creation Polygon
            }else if(panel.chosenFigure == PanelFigureType.POLYGON1){
                PanelFigure currentFigure;
                
                //if this is the first point, we create the polygon object
                if(panel.creatingFigure == null) panel.creatingFigure = new PanelFigure(PanelFigureType.POLYGON1);
                currentFigure = panel.creatingFigure;
                
                //seeting the color chosen in menu
                currentFigure.setColor(panel.getPaintColor());

                //in each click we add point, which will be the vertice of the polygon
                //points are displayed as red circles
                Circle point = new Circle(event.getX(), event.getY(), 5, Color.RED);
                currentFigure.polygonPoints.add(point);
                panel.getChildren().add(point);


                //when number of points reaches maximum set in input in menu, we finally create a polygon
                if(currentFigure.polygonPoints.size() >= panel.getPolygonSides()){

                    //adding points to the polygon
                    for(int i = 0 ; i < currentFigure.polygonPoints.size(); i++){
                        Circle curPoint = currentFigure.polygonPoints.get(i);
                        currentFigure.polygon.getPoints().add(curPoint.getCenterX());
                        currentFigure.polygon.getPoints().add(curPoint.getCenterY());
                        panel.getChildren().remove(curPoint);
                    }

                    //making an rectangle surrounding the polygon
                    //it is defined with two points: top left and down right
                    Circle zero = currentFigure.polygonPoints.get(0);
                    Double minX = zero.getCenterX(), maxX = zero.getCenterX(), minY = zero.getCenterY(), maxY = zero.getCenterY();
                    for(int i = 0; i < currentFigure.polygonPoints.size(); i++){
                        Circle curP = currentFigure.polygonPoints.get(i);
                        minX = Math.min(minX, curP.getCenterX());
                        maxX = Math.max(maxX, curP.getCenterX());
                        minY = Math.min(minY, curP.getCenterY());
                        maxY = Math.max(maxY, curP.getCenterY());

                    }

                    //in polygon managing object, the distance of vertices from top left point of surrounding
                    //rectangle are saved
                    for(int i = 0; i < currentFigure.polygonPoints.size(); i++){
                        Circle curP = currentFigure.polygonPoints.get(i);
                        currentFigure.polygonMovingPoints.add(new Double[]{curP.getCenterX() - minX, curP.getCenterY() - minY});
                    }

                    currentFigure.polygonPoints = null;
                    //main point is top left point of surrounding rectangle,
                    //moving point - is down right poitn
                    currentFigure.setMainPoint(minX, minY);
                    currentFigure.setMovingPoint(maxX, maxY);
                    panel.getChildren().add(currentFigure.polygon);
                    panel.creatingFigure = null;

                }

            }
            
        //clearing up when mouse is released
        }else if(event.getEventType() == MouseEvent.MOUSE_RELEASED){

            panel.setMouseDown(false);

            //usually we want to delete creating figure
            //however we can`t do it, the polygon is creating, because we need to set multiple points
            if(panel.chosenFigure != PanelFigureType.POLYGON1) panel.creatingFigure = null;

        //dragging - used for drag and drop and changing size during creation process
        }else if(event.getEventType() == MouseEvent.MOUSE_DRAGGED){
            
            //if drags are performed with mouse button released - ignore
            if(!panel.isMouseDown()){
                return;
            }
            
            //When cursor mode is turned on
            if(panel.chosenFigure == PanelFigureType.CURSOR){

                //ignore if no figure is active
                if(panel.activeFigure == null) return;

                //getting difference between current and previous cursor coordinates
                double deltaX = event.getX() - panel.dragX, deltaY = event.getY() - panel.dragY;
                
                //temporary turning rotation off (it makes changing position more complex)
                double rotation = panel.activeFigure.rotation;
                panel.activeFigure.rotate(-rotation);

                //changing position by changing to points of the figure (the change of shape position
                //is described is setter of main and moving points)
                panel.activeFigure.setMainPoint(panel.activeFigure.getMainPoint()[0] + deltaX , 
                    panel.activeFigure.getMainPoint()[1] + deltaY);
                panel.activeFigure.setMovingPoint(panel.activeFigure.getMovingPoint()[0] + deltaX , 
                    panel.activeFigure.getMovingPoint()[1] + deltaY);

                //returning the rotation
                panel.activeFigure.rotate(rotation);

                //setting new previous cursor coordinates
                panel.dragX = event.getX();
                panel.dragY = event.getY();

            //when creating the figure, we change the position of moving point and this way changes the radius/diagonal of a figure
            }else if(panel.chosenFigure == PanelFigureType.CIRCLE ||
                panel.chosenFigure == PanelFigureType.RECTANGLE){
                panel.creatingFigure.setMovingPoint(event.getX(), event.getY());
            }

        }

    }
}

/**
 * Handler used to chande size of figures
 * 
 * The size is changed with scrolling
 * To change size, the figure`s method "changeSize" is used
 */
class PanelScrollHandler implements EventHandler<ScrollEvent>{
    @Override
    public void handle(ScrollEvent event){
        Panel panel = (Panel)event.getSource();
        if(panel.activeFigure == null) return;

        if (event.getEventType()==ScrollEvent.SCROLL){
            panel.activeFigure.changeSize(event.getDeltaY()*0.2);
            panel.activeFigure.changeSize(event.getDeltaY()*0.2);
        }
    }
}

/**
 * Handler used to rotate the figures
 * 
 * The figures are rotated using "z" and "x" keys
 * To rotate the figures, the figure`s rotate method is used
 */
class PanelKeyHandler implements EventHandler<KeyEvent>{
    @Override
    public void handle(KeyEvent event){
        Panel panel = (Panel)event.getSource();
        if(panel.activeFigure == null) return;

        if(event.getCode() == KeyCode.Z){
            panel.activeFigure.rotate(8);
        }else if(event.getCode() == KeyCode.X){
            panel.activeFigure.rotate(-8);
        }
    }
}
