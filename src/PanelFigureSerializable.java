import java.io.*;
import java.util.ArrayList;

import javafx.scene.paint.Color;

/**
 * Class used for writing figures into files
 * 
 * It contains all data contained in the PanelFigure in an easy way
 * 
 */
public class PanelFigureSerializable implements Serializable{
    PanelFigureType type;
    String color; 
    double mainPointX, mainPointY, movingPointX, movingPointY;    
    double rotation;
    ArrayList<Double[]> polygonMovingPoints; 
    
    /**
     * Constructor: rewrites the properties of panelFigure into its own properties
     * @param panelFigure panel figure to translate
     */
    PanelFigureSerializable(PanelFigure panelFigure){
        this.type = panelFigure.type;
        this.color = panelFigure.color.toString();

        this.mainPointX = panelFigure.mainPointX;
        this.mainPointY = panelFigure.mainPointY;
        this.movingPointX = panelFigure.movingPointX;
        this.movingPointY = panelFigure.movingPointY;

        this.rotation = panelFigure.rotation;

        //for polygon there`s array of points required to be written
        if(panelFigure.polygonMovingPoints == null) return;
        this.polygonMovingPoints = new ArrayList<>();
        for(int i = 0; i < panelFigure.polygonMovingPoints.size(); i++){
            this.polygonMovingPoints.add(new Double[]{panelFigure.polygonMovingPoints.get(i)[0],
                panelFigure.polygonMovingPoints.get(i)[1]});
        }
    }

    /**
     * Method to translate serializable figure into normal figure
     * @return figure for panel
     */
    PanelFigure getOriginalFigure(){
        PanelFigure figure = new PanelFigure(this.type);
        figure.setColor(Color.web(this.color));
        figure.setMainPoint(this.mainPointX, this.mainPointY);
        figure.setMovingPoint(this.movingPointX, this.movingPointY);
        figure.rotate(this.rotation);
        //if polygon, then we need to set each point
        if(this.type == PanelFigureType.POLYGON1){
            for(int i = 0; i < this.polygonMovingPoints.size(); i++){
                figure.polygonMovingPoints.add(new Double[]{this.polygonMovingPoints.get(i)[0],
                    this.polygonMovingPoints.get(i)[1]});
                figure.polygon.getPoints().add(this.mainPointX +  this.polygonMovingPoints.get(i)[0]);
                figure.polygon.getPoints().add(this.mainPointY + this.polygonMovingPoints.get(i)[1]);
            }
        }

        return figure;
    }
}
