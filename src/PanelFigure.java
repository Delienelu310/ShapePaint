import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

import java.util.ArrayList;


enum PanelFigureType{
    CIRCLE, RECTANGLE, POLYGON1, POLYGON2, CURSOR
}

class MyCircle extends Circle{
    PanelFigure panelFigure = null;
    public MyCircle(PanelFigure panelFigure){
        super();
        this.panelFigure = panelFigure;
    }
}

class MyRectangle extends Rectangle{
    PanelFigure panelFigure = null;
    public MyRectangle(PanelFigure panelFigure){
        super();
        this.panelFigure = panelFigure;
    }
}

class MyPolygon extends Polygon{
    PanelFigure panelFigure = null;
    public MyPolygon(PanelFigure panelFigure){
        super();
        this.panelFigure = panelFigure;
    }
}

/**
 * Manager of figures drawn on the panel
 * 
 * It performs all operations on the figure
 */
public class PanelFigure{

    /**
     * type of the figure
     */
    PanelFigureType type = null;
    /**
     * References to the javafx shape object, which will be on the plane physically
     */
    Shape figure = null;
    MyCircle circle = null;
    MyRectangle rectangle = null;
    MyPolygon polygon = null;

    /**
     * Current color of the figure
     */
    Color color = Color.BLACK;


    public void setColor(Color color){
        this.color = color;
        this.figure.setFill(color);
    }

    /**
     * Coordinates of two main points, that decide all qualities of the figure
     */
    double mainPointX, mainPointY, movingPointX, movingPointY;
    /**
     * What is current angle of rotation of the figure from starting position
     */
    double rotation = 0;

    /**
     * Changing mainPoint 
     * 
     * The change of main point provokes change in qualities of the figure
     * @param x x coordinate of main point
     * @param y y coordinate of main point
     */
    public void setMainPoint(double x, double y){
        mainPointX = x;
        mainPointY = y;

        switch(this.type){
            //in c
            case CIRCLE:
                this.circle.setCenterX(x);
                this.circle.setCenterY(y);
                this.circle.setRadius(0);
                this.circle.setFill(this.color);
                break;
            //in rectangle main point is top left point
            case RECTANGLE:
                this.rectangle.setX(x);
                this.rectangle.setY(y);
                this.rectangle.setWidth(0);
                this.rectangle.setHeight(0);
                this.rectangle.setFill(this.color);
                break;
            //in polygon main point is top left point of surrounding rectangle
            //when main point is changed, that means that surrounding rectangle and it`s whole content
            //moves to a new place
            case POLYGON1:
                for(int i = 0; i < this.polygonMovingPoints.size(); i++){
                    double newX =  this.mainPointX + polygonMovingPoints.get(i)[0];
                    double newY = this.mainPointY + polygonMovingPoints.get(i)[1];
                    //we change each point coords by adding to the new left top surrounding rectangle point
                    //distance of each polygon point from it
                    this.polygon.getPoints().set(i * 2, newX);
                    this.polygon.getPoints().set(i * 2 + 1, newY);
                }
                break;
            default: 
                break;
        }
    }

    /**
     * Method used to set moving point of the figure
     * 
     * Moving point is used to decide qualities of the figure
     * 
     * @param x x moving point coordinate
     * @param y y moving point coordinate
     */
    public void setMovingPoint(double x, double y){
        movingPointX = x;
        movingPointY = y;

        switch(this.type){
            //In circle, center is in the middle between main and moving points, and radius is half of bigger side of surrounding rectangle
            case CIRCLE:
                double deltaX = Math.abs(this.mainPointX - x), deltaY =  Math.abs(this.mainPointY - y);
                if(deltaX > deltaY){
                    this.circle.setCenterX((this.mainPointX + x) / 2);
                    this.circle.setCenterY(this.mainPointY + (y >= this.mainPointY ? (deltaX / 2) : (-deltaX / 2)));
                    this.circle.setRadius(deltaX / 2);

                    this.movingPointY = this.mainPointY + (this.circle.getCenterY() - this.mainPointY) * 2;
                }else{
                    this.circle.setCenterY((this.mainPointY + y) / 2);
                    this.circle.setCenterX(this.mainPointX + (x >= this.mainPointX ? (deltaY / 2) : (-deltaY / 2)));
                    this.circle.setRadius(deltaY / 2);

                    this.movingPointX = this.mainPointX + (this.circle.getCenterX() - this.mainPointX) * 2;
                }
                break;
            //main point and moving point are angles of the rectangle and based on this rule, the 
            //left top and down right points are deduced
            case RECTANGLE:
                this.rectangle.setX(Math.min(x, this.mainPointX));
                this.rectangle.setY(Math.min(y, this.mainPointY));
                this.rectangle.setWidth(Math.abs(x - this.mainPointX));
                this.rectangle.setHeight(Math.abs(y - this.mainPointY));
                break;
            //in polygon moving point is not used, instead distances from main point to vertices is used
            case POLYGON1:
                break;
            default:
                break;
        }
    }

    public double[] getMainPoint(){
        double[] point = {mainPointX, mainPointY};
        return point;
    }

    public double[] getMovingPoint(){
        double[] point = {movingPointX, movingPointY};
        return point;
    }

    /**
     * Method used to change size of the figure when scrolling
     * 
     *  
     * @param deltaX how the width of the figure will be changed
     */
    public void changeSize(double deltaX){
        
        //finding left top and right down points of surrounding rectangle and moving them from the center or to the center 
        double x1 = Math.min(this.getMainPoint()[0], this.getMovingPoint()[0]);
        double x2 = Math.max(this.getMainPoint()[0], this.getMovingPoint()[0]);
        double y1 = Math.min(this.getMainPoint()[1], this.getMovingPoint()[1]);
        double y2 = Math.max(this.getMainPoint()[1], this.getMovingPoint()[1]);
        //how the height will be changed (calculating in a way, that proportiong are remained the same)
        double deltaY = deltaX * (y2 - y1) / (x2 - x1);
        this.setMainPoint(x1 - deltaX / 2 , y1 - deltaY / 2);
        this.setMovingPoint(x2 + deltaX / 2, y2 + deltaY / 2);
        
        //if the figure is polygon, we change the distance of vertices from left top point proportionallly
        if(this.type == PanelFigureType.POLYGON1){
            double coefX = (this.movingPointX - this.mainPointX) / (x2 - x1);
            double coefY = (this.movingPointY - this.mainPointY) / (y2 - y1);
            for(int i = 0; i < this.polygonMovingPoints.size(); i++){
                double newX = this.polygonMovingPoints.get(i)[0] * coefX,
                    newY = this.polygonMovingPoints.get(i)[1] * coefY;
                this.polygonMovingPoints.set(i, new Double[]{newX, newY});
                this.polygon.getPoints().set(i * 2, this.mainPointX + newX);
                this.polygon.getPoints().set(i*2 + 1, this.mainPointY + newY);
            }

        }
        
        
    }
    /**
     * Method used to rotate the figures
     * @param delta angle of rotation
     */
    public void rotate(double delta){
        //seeking for the center of surrounding rectangle
        double x1 = Math.min(this.getMainPoint()[0], this.getMovingPoint()[0]);
        double x2 = Math.max(this.getMainPoint()[0], this.getMovingPoint()[0]);
        double y1 = Math.min(this.getMainPoint()[1], this.getMovingPoint()[1]);
        double y2 = Math.max(this.getMainPoint()[1], this.getMovingPoint()[1]);
        //rotating using the found center as pivot point
        Rotate rotate = new Rotate(delta, (x1 + x2) / 2, (y1 + y2) / 2);
        this.figure.getTransforms().add(rotate);
        //writing the rotation down, to keep track of how is figure rotated
        this.rotation += delta;
    }

    /**
     * Physical red points set by user when creating the figure
     */
    ArrayList<Circle> polygonPoints;

    /**
     * Distance of vertices of polygon from left top angle of surrounding rectangle
     */
    ArrayList<Double[]> polygonMovingPoints; 
    
    /**
     * Constructor of the figure manager
     * 
     * Creates physical shape, that will later be modified with manager`s method and added to the panel
     * 
     * @param type type of the figure
     */
    public PanelFigure(PanelFigureType type){
        super();
        this.type = type;
        switch(type){
            case CIRCLE:
                this.circle = new MyCircle(this);
                this.figure = this.circle;
                break;
            case RECTANGLE:
                this.rectangle = new MyRectangle(this);
                this.figure = this.rectangle;
                break;
            case POLYGON1:
                this.polygonPoints = new ArrayList<Circle>();
                this.polygonMovingPoints = new ArrayList<>();
                this.polygon = new MyPolygon(this);
                this.figure = this.polygon;
                break;
            case POLYGON2:
                break;
            default:
                break;
        }
        
    }
}
