package twodplots;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.jme3.math.Vector3f;

import plotting.CustomChartPanel2;
import precreatedObjects.SpaceObject;

public class PositionGraph extends JFrame{
	
	private boolean COMFrame = false;
	private ArrayList<SpaceObject> spaceObjectsReference;
	
	public PositionGraph(ArrayList<SpaceObject> spaceObjects, boolean centerOfMassFrame) {
        super("Position Graph");
 
        spaceObjectsReference = spaceObjects;
        JPanel chartPanel = createChartPanel();
        add(chartPanel, BorderLayout.CENTER);
 
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        COMFrame = centerOfMassFrame;
      
    }
 
	XYItemRenderer renderer2;
	XYItemRenderer renderer;
    private JPanel createChartPanel() {
    	String chartTitle = "Positions of Space Objects Over Planar Space (Relative to CM)";
        String categoryAxisLabel = "X (km)";
        String valueAxisLabel = " Y (km)";
     
        XYDataset dataset = createDataset();
       
        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
                categoryAxisLabel, valueAxisLabel, dataset);
        

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        renderer = plot.getRenderer();
        
        renderer2 = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES);
       
        plot.setDataset(1, datasetLatestPos);
        plot.setRenderer(1,renderer2);
        
        return new CustomChartPanel2(chart);
    }
 
    private HashMap<String, XYSeries> objectSeries = new HashMap<String, XYSeries>();
    private HashMap<String, XYSeries> objectLatestPos = new HashMap<String, XYSeries>();
    
    private XYSeriesCollection dataset;
    private XYSeriesCollection datasetLatestPos;
    //int index = 0;
    private XYDataset createDataset() {
    	dataset = new XYSeriesCollection();
    	datasetLatestPos = new XYSeriesCollection();
    	for (SpaceObject s : spaceObjectsReference) {
    		String name = s.getName();
    		if(objectSeries.get(name) == null) {
    		XYSeries seriesTemp = new XYSeries(name, false, true);
			XYSeries seriesPoint = new XYSeries(name, false, true);
    		dataset.addSeries(seriesTemp);
    		datasetLatestPos.addSeries(seriesPoint);
    		objectSeries.put(name, seriesTemp);
    		objectLatestPos.put(name, seriesPoint);
    		}
    		
    	}
        return dataset;
    }
    
    public void update() {
    	Vector3f com = new Vector3f(0,0,0);
    	double total_mass = 0;
    	
    	for (SpaceObject s : spaceObjectsReference) {
    		String name = s.getName();
    		if(objectSeries.get(name) == null) {
    			XYSeries seriesTemp = new XYSeries(name, false, true);
    			XYSeries seriesPoint = new XYSeries(name, false, true);
        		dataset.addSeries(seriesTemp);
        		datasetLatestPos.addSeries(seriesPoint);
        		objectSeries.put(name, seriesTemp);
        		objectLatestPos.put(name, seriesPoint);
    		}
    		total_mass += s.getMass();		
    		
    	}
    
    if(COMFrame) {
    	for(SpaceObject s : spaceObjectsReference) {
    		com = com.add(s.getToScalePositionFloat().mult((float) s.getMass()).divide((float) total_mass));
    	}
    }
    
    int index = 0;
    for(SpaceObject s : spaceObjectsReference) {
    	String name = s.getName();
		objectSeries.get(name).add(s.getToScalePosition().x-com.x, s.getToScalePosition().z-com.z);
		objectLatestPos.get(name).clear();
		objectLatestPos.get(name).add(s.getToScalePosition().x-com.x, s.getToScalePosition().z-com.z);
		Shape shape  = new Ellipse2D.Double(-5,-5,10,10);
		renderer2.setSeriesShape(index,shape);
		renderer2.setSeriesPaint(index, renderer.getSeriesPaint(index));
		renderer2.setSeriesVisibleInLegend(index, Boolean.FALSE, true);
		index++;
    }
    }
}
