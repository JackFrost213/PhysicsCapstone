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
import precreatedObjects.JamesWebbSpaceTelescope;
import precreatedObjects.SpaceObject;

public class ScatterPlotGraph extends JFrame{
	
	private ArrayList<SpaceObject> spaceObjectsReference;
	
	public ScatterPlotGraph(ArrayList<SpaceObject> spaceObjects) {
        super("Position Graph");
 
        spaceObjectsReference = spaceObjects;
        JPanel chartPanel = createChartPanel();
        add(chartPanel, BorderLayout.CENTER);
 
        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
      
    }
 
	XYItemRenderer renderer2;
	XYItemRenderer renderer;
    private JPanel createChartPanel() {
    	String chartTitle = "Positions of Space Objects Over Planar Space (Relative to CM)";
        String categoryAxisLabel = "X (km)";
        String valueAxisLabel = " Y (km)";
     
        XYDataset dataset = createDataset();
       
        JFreeChart chart = ChartFactory.createScatterPlot(chartTitle,
                categoryAxisLabel, valueAxisLabel, dataset);
        

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        
        renderer2 = new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
        
        plot.setDataset(1, datasetLatestPos);
        plot.setRenderer(1,renderer2);
        
        chart.removeLegend();
        
        return new CustomChartPanel2(chart);
    }
 
    HashMap<String, XYSeries> objectSeries = new HashMap<String, XYSeries>();
    HashMap<String, XYSeries> objectLatestPos = new HashMap<String, XYSeries>();
    
    private XYSeriesCollection dataset;
    private XYSeriesCollection datasetLatestPos;
    int index = 0;
    private XYDataset createDataset() {
    	dataset = new XYSeriesCollection();
    	datasetLatestPos = new XYSeriesCollection();
    	for (SpaceObject s : spaceObjectsReference) {
    		String name = s.getName() + " " + String.valueOf(index);
    		((JamesWebbSpaceTelescope)s).dataTakingName = name;
    		XYSeries seriesTemp = new XYSeries(name, false, true);
    		index++;
    		dataset.addSeries(seriesTemp);
    		objectSeries.put(name, seriesTemp);
    		
    	}
    	
    	XYSeries seriesTemp = new XYSeries(String.valueOf(indexTest), false, true);
    	datasetLatestPos.addSeries(seriesTemp);
    	
        return dataset;
    }
    
    int indexTest = 0;
    public void update() {
    	Vector3f com = new Vector3f();
    	 SpaceObject probe = spaceObjectsReference.get(spaceObjectsReference.size()-1);
    	 com = probe.getToScalePositionFloat();
    	for(SpaceObject s : spaceObjectsReference) {
    		String name = ((JamesWebbSpaceTelescope)s).dataTakingName;
    		objectSeries.get(name).clear();
    		objectSeries.get(name).add(s.getToScalePosition().x-com.x, s.getToScalePosition().z-com.z);
    	}
    	
    	XYSeries seriesTemp = datasetLatestPos.getSeries(0);
    	seriesTemp.clear();
    	for(int x = 0; x<spaceObjectsReference.size()-1; x++) {
    		seriesTemp.add(spaceObjectsReference.get(x).getToScalePosition().x-com.x,spaceObjectsReference.get(x).getToScalePosition().z-com.z);
    	}
    	seriesTemp.add(spaceObjectsReference.get(0).getToScalePosition().x-com.x,spaceObjectsReference.get(0).getToScalePosition().z-com.z);

    	indexTest++;
    }
}
