package main.runnable;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.BlockFrame;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.VerticalAlignment;

import com.jme3.app.SimpleApplication;

import main.ChaosGenerator;
import main.ChaosNumber;
import main.FileIO;
import net.wcomohundro.jme3.math.Vector3d;
import plotting.CustomChartPanel;
import plotting.CustomNumberTickUnit;
import plotting.CustomTickUnitSource;
import precreatedObjects.Earth;
import precreatedObjects.JamesWebbSpaceTelescope;
import precreatedObjects.Moon;
import precreatedObjects.SpaceObject;
import precreatedObjects.Sun;
import precreatedScenarios.ChaoticSystemSimulation;
import precreatedScenarios.ChaoticSystemSimulation2;
import precreatedScenarios.EqualMassSimulation;
import precreatedScenarios.Simulation;
import precreatedScenarios.SunEarthJamesWebbL1Simulation;
import precreatedScenarios.SunEarthJamesWebbL4Simulation;
import precreatedScenarios.SunEarthJamesWebbSimulation;
import shapes3D.SpaceObjectFast;

public class ChaosGraph extends ApplicationFrame {
	
	
	public static boolean generatingChaos = false;
	
	static double au = 147120163;
	
	static double xMin = -2000000;
	static double yMin = -500000;
	static double xMax = 2000000;
	static double yMax = 500000;
	
	private JFreeChart chart;
    private TextTitle chartinfo;
	
	
	/*static double xMin = 0.5f*au - 5000000;
	static double yMin = Math.sqrt(Math.pow(au, 2) - Math.pow(0.5f*au, 2)) - 5000000;
	static double xMax = 0.5f*au + 5000000;
	static double yMax = Math.sqrt(Math.pow(au, 2) - Math.pow(0.5f*au, 2)) + 5000000;
	*/
	//0.5f*au,0,(double)Math.sqrt(Math.pow(au, 2) - Math.pow(0.5f*au, 2)
	static int numberOfSamples = 500;

    /**
     * Constructs the demo application.
     *
     * @param title  the frame title.
     */
    public ChaosGraph(String title, int viewType) {
        super(title);
        JPanel chartPanel = createDemoPanel(viewType);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        //chartPanel.setDoubleBuffered(true);
        setContentPane(chartPanel);
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return A sample chart.
     */
    private static XYBlockRenderer renderer;
    private JFreeChart createChart(XYZDataset dataset, int viewType) {
        NumberAxis xAxis = new NumberAxis("X (km)");
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        xAxis.setLowerBound(xMin);
        xAxis.setUpperBound(xMax);
        xAxis.setAxisLinePaint(Color.white);
        xAxis.setTickMarkPaint(Color.white);

        NumberAxis yAxis = new NumberAxis("Y (km)");
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        yAxis.setLowerBound(yMin);
        yAxis.setUpperBound(yMax);
        yAxis.setAxisLinePaint(Color.white);
        yAxis.setTickMarkPaint(Color.white);
        renderer = new XYBlockRenderer();
        PaintScale scale = new GrayPaintScale(0, 1.0);
        renderer.setPaintScale(scale);
        int remainder = 0;
        if ((Math.sqrt(numberOfSamples) % 1) != 0) {
        	remainder = (int)Math.ceil((numberOfSamples - Math.pow(Math.floor(Math.sqrt(numberOfSamples)),2)) / Math.floor(Math.sqrt(numberOfSamples)));
		}
        renderer.setBlockWidth((xMax-xMin)/(Math.floor(Math.sqrt(numberOfSamples))+remainder));
        renderer.setBlockHeight((yMax-yMin)/Math.floor(Math.sqrt(numberOfSamples)));
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5, 5, 5, 5));
        plot.setOutlinePaint(Color.blue);
        String title = viewType == 0 ? "Chaos Plot - Lyapunov Exponents" : "Chaos Plot - Sticky Stability";
        JFreeChart chart = new JFreeChart(title, plot);
        chart.removeLegend();
        NumberAxis scaleAxis = new NumberAxis("Chaoticness");
        //scaleAxis.setStandardTickUnits(new CustomTickUnitSource());
        scaleAxis.setTickUnit(new CustomNumberTickUnit(1.0/20.0,new DecimalFormat("0.00")));
        scaleAxis.setAxisLinePaint(Color.white);
        scaleAxis.setTickMarkPaint(Color.white);
        scaleAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 7));
        PaintScaleLegend legend = new PaintScaleLegend(new GrayPaintScale(),
                scaleAxis);
        legend.setStripOutlineVisible(false);
        legend.setSubdivisionCount(20);
        legend.setAxisLocation(AxisLocation.TOP_OR_LEFT);
        scaleAxis.setFixedDimension(100);
        scaleAxis.setLabelInsets(new RectangleInsets(-20, -20, -20, -20));
        legend.setAxisOffset(1.0);
        legend.setMargin(new RectangleInsets(5, 5, 5, 5));
        legend.setFrame(new BlockBorder(Color.red));
        legend.setPadding(new RectangleInsets(10, 10, 10, 10));
        legend.setStripWidth(10);
        legend.setPosition(RectangleEdge.LEFT);
        //legend.setBackgroundPaint(new Color(120, 120, 180));
        chart.addSubtitle(legend);
        //chart.setAntiAlias(true);
        chart.setTextAntiAlias(true);
        //chart.setBackgroundPaint(new Color(180, 180, 250));
        
        /*
         * Probe Mass = 0.0\nProbe Velocity = (0,0,0) \nSaturation = 1
         */
        
        chartinfo = new TextTitle("Probe Mass = 0.0 kg      Probe Velocity = (0,0,0) km/s\nSaturation = 1");
        chartinfo.setPosition(RectangleEdge.TOP); 
        chartinfo.setHorizontalAlignment(HorizontalAlignment.CENTER); 
        chart.addSubtitle(chartinfo); 
        
        ChartUtilities.applyCurrentTheme(chart);
        return chart;
    }

    /**
     * Creates a sample dataset.
     *
     * @return A dataset.
     */
    
    private static ArrayList<ChaosNumber> chaosNumbers;
    
    private static XYZDataset createDataset(int viewType) {
        return new XYZDataset() {
            public int getSeriesCount() {
                return 1;
            }
            public int getItemCount(int series) {
                return numberOfSamples;
            }
            public Number getX(int series, int item) {
                return new Double(getXValue(series, item));
            }
            public double getXValue(int series, int item) {
                return chaosNumbers.get(item).getPosition().x;
            }
            public Number getY(int series, int item) {
                return new Double(getYValue(series, item));
            }
            public double getYValue(int series, int item) {
            	 return chaosNumbers.get(item).getPosition().y;
            }
            public Number getZ(int series, int item) {
                return new Double(getZValue(series, item));
            }
            public double getZValue(int series, int item) {
                return viewType == 0 ? chaosNumbers.get(item).getLyapunovExponentsScaled().x : chaosNumbers.get(item).getLyapunovExponentsScaled().z;
            }
            public void addChangeListener(DatasetChangeListener listener) {
                // ignore - this dataset never changes
            }
            public void removeChangeListener(DatasetChangeListener listener) {
                // ignore
            }
            public DatasetGroup getGroup() {
                return null;
            }
            public void setGroup(DatasetGroup group) {
                // ignore
            }
            public Comparable getSeriesKey(int series) {
                return "LyapunovExponent(x, y))";
            }
            public int indexOf(Comparable seriesKey) {
                return 0;
            }
            public DomainOrder getDomainOrder() {
                return DomainOrder.ASCENDING;
            }
        };
    }

    /**
     * Creates a panel for the demo.
     *
     * @return A panel.
     */
    public JPanel createDemoPanel(int viewType) {
    	chart = createChart(createDataset(viewType), viewType);
        return new CustomChartPanel(chart);
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
   
    static String filePath = "GeneratorFunctionChaos4/Data";
    static SpaceObject probe;
    public static void main(String[] args) {
    	
    	generatingChaos = true;
    	chaosNumbers = null;
    	Simulation sim = new SunEarthJamesWebbSimulation(null);
    	sim.createInitialItems();
    	ArrayList<SpaceObject> spaceObjects = sim.getSpaceObjects();
    	probe = spaceObjects.get(1);
    	
    	Object[] returnedData = loadData("GeneratorFunctionChaos4/Two Body Data");
		//Object[] returnedData = generateData(filePath, spaceObjects, probe, sim.getClass());
		chaosNumbers = (ArrayList<ChaosNumber>) returnedData[0];
		xMin = ((double[])returnedData[2])[0];
		xMax = ((double[])returnedData[2])[1];
		yMin = ((double[])returnedData[2])[2];
		yMax = ((double[])returnedData[2])[3];
		numberOfSamples = ((int[])returnedData[1])[4];
		if (returnedData[3] != null) {
			Class<? extends Simulation> classType = ((Class<? extends Simulation>) returnedData[3]);
			try {
				Class[] cArg = new Class[1]; 
				cArg[0] = SimpleApplication.class;

				SimpleApplication app = null;

				sim = classType.getDeclaredConstructor(cArg).newInstance(app);
				sim.createInitialItems();
				spaceObjects = sim.getSpaceObjects();
				probe = spaceObjects.get(1);
			} catch (Exception e) {
			}
		}else {
			sim = null;
		}

		//Transformation
		
    	//Order: (smallestXPoint, largestXPoint),(smallestYPoint, largestYPoint),(smallestZPoint, largestZPoint)
    	ChaosNumber[][] smallestLargestChaosNumbers = new ChaosNumber[3][2];
    	for(int x = 0; x < smallestLargestChaosNumbers.length; x++) {
    		double maxValue = Double.NEGATIVE_INFINITY;
        	double minValue = Double.POSITIVE_INFINITY;
    		for(ChaosNumber c : chaosNumbers) {
    			double chaosValue = 0;
    			switch(x) {
					case 0 : chaosValue = c.getLyapunovExponents().x; break;
					case 1 : chaosValue = c.getLyapunovExponents().y; break;
					case 2 : chaosValue = c.getLyapunovExponents().z; break;
    			}
    			if(chaosValue > maxValue) {
    				maxValue = chaosValue;
    				smallestLargestChaosNumbers[x][1] = c;
    			}
    			if(chaosValue < minValue) {
    				minValue = chaosValue;
    				smallestLargestChaosNumbers[x][0] = c;
    			}
    		}
    	}
    	
    	double maxXValue = smallestLargestChaosNumbers[0][1].getLyapunovExponents().x;
    	double minXValue = smallestLargestChaosNumbers[0][0].getLyapunovExponents().x;
    	double maxYValue = smallestLargestChaosNumbers[1][1].getLyapunovExponents().y;
    	double minYValue = smallestLargestChaosNumbers[1][0].getLyapunovExponents().y;
    	double maxZValue = smallestLargestChaosNumbers[2][1].getLyapunovExponents().z;
    	double minZValue = smallestLargestChaosNumbers[2][0].getLyapunovExponents().z;
    	ChaosNumber largestZPoint = smallestLargestChaosNumbers[2][1];
    	ChaosNumber smallestZPoint = smallestLargestChaosNumbers[2][0];
    	
		if (sim != null) {
			sim.analyzeChaosData(smallestZPoint, largestZPoint, probe);
		}
		
		//Setting up Window Controls and Visuals
		ChaosGraph demo = new ChaosGraph(
                "Chaos Plot XY", 0);
        ChaosGraph demo2 = new ChaosGraph(
                "Chaos Plot XY", 1);
		
		JFrame window = new JFrame();
		JPanel scalar = new JPanel();
		scalar.setLayout(new BoxLayout(scalar, BoxLayout.Y_AXIS));
		JTextField temp = new JTextField();
		JButton startButton = new JButton("Modify Saturation");
		startButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				double scale = 1;
				
				try {
					scale = Double.valueOf(temp.getText());
				}catch(Exception e){
				}
				
				for(ChaosNumber c : chaosNumbers) {
		    		c.getLyapunovExponentsScaled().x = scale*(c.getLyapunovExponents().x-minXValue)/(maxXValue-minXValue);
		    		c.getLyapunovExponentsScaled().y = scale*(c.getLyapunovExponents().y-minYValue)/(maxYValue-minYValue);
		    		c.getLyapunovExponentsScaled().z = scale*(c.getLyapunovExponents().z-minZValue)/(maxZValue-minZValue);
		    	}
				
				demo.getChart().getXYPlot().setDataset(demo.getChart().getXYPlot().getDataset());
				demo.getChartInfo().setText("Probe Mass = " + probe.getMass() + " kg      Probe Velocity = " + probe.getVelocity() + " km/s\nSaturation = " + scale);
				demo2.getChart().getXYPlot().setDataset(demo2.getChart().getXYPlot().getDataset());
				demo2.getChartInfo().setText("Probe Mass = " + probe.getMass() + " kg      Probe Velocity = " + probe.getVelocity() + " km/s\nSaturation = " + scale);
			}
		});
		temp.setText("1");
		scalar.add(temp);
		scalar.add(startButton);
		scalar.setVisible(true);
		window.setVisible(true);
		window.add(scalar);
		window.pack();
		
        startButton.doClick();
        demo.pack();
        demo2.pack();
        demo.setMaximumSize(demo.getPreferredSize());
        demo2.setMaximumSize(demo2.getMaximumSize());
        RefineryUtilities.centerFrameOnScreen(demo);
        RefineryUtilities.centerFrameOnScreen(demo2);
        demo.setVisible(true);
        demo2.setVisible(true);
        
    }
	
	protected TextTitle getChartInfo() {
		return chartinfo;
	}

	protected JFreeChart getChart() {
		return chart;
	}

	public static int index = 0;
	public static int indexTemp = 0;
	private static boolean threadsCompleted = false;
	public static Object[] generateData(String filePath, ArrayList<SpaceObject> spaceObjects, SpaceObject probe, Class<? extends Simulation> simulationClass) {
    	int[] parameters = {20,500,500,500,numberOfSamples};
    	double[] ranges = {xMin,xMax,yMin,yMax};
 
    	ArrayList<ChaosNumber> chaosNumTemp = ChaosGenerator.generateChaosNumbers(xMin, xMax, yMin, yMax, numberOfSamples);
    	ArrayList<ArrayList<ChaosNumber>> threadedWork = new ArrayList<ArrayList<ChaosNumber>>();
    	int numberOfThreads = 50;
    	int samplesPerThread = numberOfSamples/numberOfThreads;
    	int remainingSamples = numberOfSamples % numberOfThreads;
    	int count = 0;
    	for(int x = 0; x < numberOfThreads; x++) {
    		ArrayList<ChaosNumber> subList = new ArrayList<ChaosNumber>(chaosNumTemp.subList(x*samplesPerThread, (x+1)*samplesPerThread));
    		count += subList.size();
    		threadedWork.add(subList);
    	}
    	ArrayList<ChaosNumber> subList = new ArrayList<ChaosNumber>(chaosNumTemp.subList(numberOfSamples-remainingSamples, numberOfSamples));
    	count += subList.size();
    	threadedWork.add(subList);
    	
    	double notifyAmount = 0;
    	long currentTime = System.nanoTime();
    	ArrayList<Thread> threads = new ArrayList<Thread>();
    	for(ArrayList<ChaosNumber> chaosThreadedList : threadedWork) {
    		ArrayList<SpaceObjectFast> spaceObjectsCopy = new ArrayList<SpaceObjectFast>();
        	for(SpaceObject s : spaceObjects) {
        		spaceObjectsCopy.add(new SpaceObjectFast(s));
        	}
        	SpaceObjectFast probeCopy = new SpaceObjectFast(probe);
    		Thread thread = new Thread(){
    			public void run(){
    				ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
    				for(ChaosNumber c : chaosThreadedList) {
    		    		temp = new ArrayList<Vector3d>();
    		    		temp.add(new Vector3d(c.getPosition().x,0,c.getPosition().y));
    		    		temp.add(probe.getVelocity());
    		    		probeCopy.setInitialConditions(temp);
    		    		c.setLyapunovExponents(ChaosGenerator.getChaosValues(spaceObjectsCopy, probeCopy, parameters[0], parameters[1], parameters[2], parameters[3]));
    		    		index++;
    		    		//System.out.println(index);
    		    	}
    			}
    		};

    		thread.start();
    		threads.add(thread);
    	}
    	
    	
    	Thread indexCounter = new Thread(){
			public void run(){
				while(!threadsCompleted) {
					if(index >= indexTemp) {
						System.out.println(100*((double)index)/numberOfSamples + "%");
						indexTemp += 1000;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}};
    	
		indexCounter.start();
			
    	for (Thread thread : threads) {
    	    try {
				thread.join();
			} catch (InterruptedException e) {
			}
    	}
    	System.out.println(100*((double)index)/numberOfSamples + "%");
    	threadsCompleted = true;
    	long afterTime = System.nanoTime();
    	
    	System.out.println("Time Taken: " + (afterTime - currentTime)/10E9 + " s");
    	
    	FileIO.writeFile(filePath, ChaosGenerator.getChaosSetupAsString(spaceObjects, probe, parameters, ranges, simulationClass), chaosNumTemp);
    	Object[] returnedData = {chaosNumTemp, parameters, ranges, simulationClass};
    	return returnedData;
	}
	
	public static Object[] loadData(String s) {
		//readFile
		Object[] arr = FileIO.readFile(s);
		return arr;
	}
	
}