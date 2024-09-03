package Administrator.Algorithm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.stat.Frequency;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

// TODO: Auto-generated Javadoc
/**
 * @version
 * <ul>
 * <li> 1.0: March 8, 2016 created by Maria Lombardi </li>
 * </ul>
 * <hr>
 * @author Maria Lombardi
 * <p>
 * <br>
 * The Class MathAlgorithmAdmin.
 * <p>
 * This class contains all methods for utility mathematical functions as spline, estimating the velocity
 * from the position, building several plots and so on.
 * <br>
 */
@SuppressWarnings("serial")
public class MathAlgorithmAdmin{

	/** The resolution. */
	@SuppressWarnings("unused")
	private int resolution;
	
	/** The delay. */
	private int delay; // delay di campionamento in millisecondi
	
	/** The time. */
	private int time; //durata del gioco = 60 secondi
	
	/** The min vel. */
	private int minVel;
	
	/** The max vel. */
	private int maxVel;
	
	/** The vel. */
	@SuppressWarnings("unused")
	private ArrayList<Float> vel;
	
	/** The color. */
	private Color color[] = new Color[7];
	
	/**
	 * Instantiates a new math object for the administrator. Each player has an unique color
	 * for plotting his/her signature.
	 */
	public MathAlgorithmAdmin(){	
		this.vel = new ArrayList<Float>();
		this.resolution = 800;
		this.delay = Settings.getSettings().getDelay();
		this.time = Settings.getSettings().getTime();
		
		
		this.color[0] = Color.black;
		this.color[1] = Color.red;
		this.color[2] = Color.blue;
		this.color[3] = Color.orange;
		this.color[4] = Color.gray;
		this.color[5] = Color.green;
		this.color[6] = Color.magenta;
	}

	//plot della pallina va da -5 a 5
	//il mouse da 0 a 799
	//delay in millisecondi = 10 -> prendo un campione ogni 10 millisecondi
	//periodo di campionamento dt in secondi => delay/1000
	//campioni => 60/periodo dt
	//la posizione è in millimetri
	//la velocità in millimetri al secondo
	//la posizione deve andare da -5 a 5 dm
	
	/**
	 * Gets the velocity from position.
	 *
	 * @param pos  arrayList of the values of positions saved during the trial. The position is always 
	 * a value inside the range [-5, 5] because the plot of sphere on the screen requires a value in
	 * that range
	 * @return the arrayList of values of estimated velocity
	 */
	public ArrayList<Float> getVelocityFromPosition(ArrayList<Float> pos){
		// per addSignature time = 60 secondi
		int samples = time*1000/delay; //numero di campioni in 60 secondi
		ArrayList<Float> vel = new ArrayList<Float>();
		
		vel.add(0, pos.get(0));		
		for(int i = 1; i < samples; i++){
			vel.add(i, (pos.get(i)-pos.get(i-1))*1000/delay);
		}
		//la velocità è calcolata con posizione compresa fra -5 e 5
		return vel;
	}
	
	/**
	 * Spline of values of position.
	 *
	 * @param pos  arrayList of the values of positions saved during the trial. The position is always 
	 * a value inside the range [-5, 5] because the plot of sphere on the screen requires a value in
	 * that range
	 * @return the spline of the arrayList of positions in input. The spline is applied if the number
	 * of the samples is less than that expected (samples frequency too high).
	 */
	public ArrayList<Float> splinePosition(ArrayList<Float> pos){
		int samples = time*1000/delay; //numero di campioni in 60 secondi
		ArrayList<Float> newPos = new ArrayList<Float>();
				
		//interpolazione della firma se i campioni sono meno di Samples = 6000;
		if(pos.size()<samples){		
			// vettore double della posizione
			double y[] = new double[pos.size()];
			double x[] = new double[pos.size()];
			for(int i = 0; i < pos.size(); i++){
				y[i] = pos.get(i);
				x[i] = i*(samples*delay)/pos.size();
			}	
			
			x[pos.size()-1] = samples*delay;
			
			//vettore double del tempo
			UnivariateInterpolator interpolator = new SplineInterpolator();
			UnivariateFunction function = interpolator.interpolate(x, y);
			
			double interpolationX[] = new double[samples];
			double interpolatedY[] = new double[samples];
			
			for(int i = 0; i < samples; i++)
				interpolationX[i] = i*delay;
			
			for(int i = 0; i < samples; i++)
				interpolatedY[i] = function.value(interpolationX[i]);
			
			for(int i = 0; i < samples; i++){
				newPos.add(i, (float) interpolatedY[i]);
			}
		}else{
			for(int i = 0; i < samples; i++){
		    	newPos.add(i, pos.get(i));
			}
		}
		
		return newPos;
	}
	
	/**
	 * Gets the PDF from velocity.
	 *
	 * @param vel arrayList of estimated velocity gotten during the trial
	 * @return the probability distribution of the values of velocity
	 */
	public ArrayList<Float> getPDFfromVelocity(ArrayList<Float> vel){
		this.vel = vel;
		Frequency f = new Frequency();
		ArrayList<Float> pdf = new ArrayList<Float>();
		float pdfMax = 0, maxVelF = 0, minVelF = 0;
		
		// diviso per 10 come se la posizione fosse fra -0.5 e 0.5 (in m)
		// le velocità le prendo int o non posso usarle come indici del vettore
		for(int i = 0; i < vel.size(); i++){
			f.addValue((int)(vel.get(i)*0.1f));
		}
		
		// la velocità mi viene fra -50 e 50
		//mi serve il range di valori di vel, prendo il massimo ed il minimo
		maxVelF = vel.get(0);
		for (int i = 1; i <vel.size(); i++)
			if (vel.get(i) > maxVelF) 
				maxVelF = vel.get(i);

		if(maxVelF >= 4)
			maxVelF = 5;
		
		this.maxVel = (int)maxVelF;
		
		minVelF = vel.get(0);
		for (int i = 1; i <vel.size(); i++)
			if (vel.get(i) < minVelF) 
				minVelF = vel.get(i); 
		
		if(minVelF <= -4)
			minVelF = -5;
		
		this.minVel = (int)minVelF;
			
		for(int i = this.minVel; i <= this.maxVel; i++){
			pdf.add((float) f.getPct(i));
		}
		
						
		pdfMax = pdf.get(0);
		for (int i = 1; i < pdf.size(); i++)
			if (pdf.get(i) > pdfMax) 
				pdfMax = pdf.get(i);
		
				
		for (int i = 0; i < pdf.size(); i++){
			pdf.set(i, pdf.get(i)*(1/pdfMax));	
		}
		
		return pdf;
	}
	
	/**
	 * Plot signature velocity.
	 *
	 * @param vel  arrayList of estimated velocity gotten during the trial
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void plotSignatureVelocity(ArrayList<Float> vel) throws IOException{

		ArrayList<Float> temp = new ArrayList<Float>();
		
		this.vel = vel;
		
		float time[] = new float[vel.size()];
		
		// setto l'asse delle x (tempo)
		for(int i = 0; i < vel.size(); i++)
			time[i] = (float)i*delay;
		
		for(int i = 0; i < vel.size(); i++)
			temp.add((float)vel.get(i)*0.1f);
		
		XYchart plot = new XYchart("plot signature", time, temp, "seriesVelocity", 
				"Velocity time series", "time [ms]", "velocity [m/s]");
        plot.pack();
        RefineryUtilities.positionFrameRandomly(plot);
        plot.setVisible(true);        
	}
	
	public Complex exponential(double im) {
		//double length = Math.exp(re);
		return new Complex(Math.cos(im), Math.sin(im) );
	}

	public Complex add_complex(Complex a, Complex b) {
		double real_a = a.getReal();
		double real_b = b.getReal();
		double imag_a = a.getImaginary();
		double imag_b = b.getImaginary();
		
		return new Complex(real_a + real_b, imag_a + imag_b);
	}
	
	/**
	 * Plot signature position of a multi-players game.
	 *
	 * @param pos arrayList of sampled position gotten during the trial on the network
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void plotSignaturePositionNetwork(ArrayList<ArrayList<Float>> pos) throws IOException{
		
		float time[] = new float[pos.get(0).size()];
		
		// setto l'asse delle x (tempo)
		for(int i = 0; i < pos.get(0).size(); i++)
			time[i] = (float)i*delay;
		
		XYchartNetwork plotNetwork = new XYchartNetwork("plot positions", time, pos, "seriesPositions",
				"Position time series", "time [ms]", "position [dm]");
		plotNetwork.pack();
		RefineryUtilities.positionFrameRandomly(plotNetwork);
	    plotNetwork.setVisible(true);
	}
	
	/**
	 * Plot signature velocity of a multi-players game.
	 *
	 * @param vel arrayList of estimated velocity gotten during the trial on the network
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void plotSignatureVelocityNetwork(ArrayList<ArrayList<Float>> vel) throws IOException{
		float time[] = new float[vel.get(0).size()];
		
		// setto l'asse delle x (tempo)
		for(int i = 0; i < vel.get(0).size(); i++)
			time[i] = (float)i*delay;
		
		System.out.println("size di vel " + vel.size());
		
		XYchartNetwork plotNetwork = new XYchartNetwork("plot velocities", time, vel, "seriesVelocity",
				"Velocity time series", "time [ms]", "velocity [dm/s]");
		plotNetwork.pack();
		RefineryUtilities.positionFrameRandomly(plotNetwork);
	    plotNetwork.setVisible(true);
	}
	
	/**
	 * Plot velocity pdf. The pdf is normalized in order that the maximum value of velocity has 
	 * a value of pdf equals to 1.
	 *
	 * @param pdf normalized velocity PDF evaluated by "getPDFfromVelocity"
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void plotPDF(ArrayList<Float> pdf) throws IOException{
		
		//i valori della pdf sono troppo pochi...facciamo la spline per aumentare la granularità
		double y[] = new double[this.maxVel - this.minVel + 1];
		double x[] = new double[this.maxVel - this.minVel + 1];
		
		for(int i = this.minVel; i < this.maxVel+1; i++){
			y[i - minVel] = pdf.get(i-minVel);
			x[i - minVel] = i;
		}
		
		UnivariateInterpolator interpolator = new SplineInterpolator();
		UnivariateFunction function = interpolator.interpolate(x, y);
		
		double interpolationX[] = new double[(this.maxVel - this.minVel)*10+1];
		double interpolatedY[] = new double[(this.maxVel - this.minVel)*10+1];
		
		for(int i = 0; i < (this.maxVel - this.minVel)*10+1 ; i++)
			interpolationX[i] = this.minVel+(0.1*i);
		
		for(int i = 0; i < (this.maxVel - this.minVel)*10+1 ; i++)
			if(function.value(interpolationX[i]) > 0 && function.value(interpolationX[i]) <= 1)
				interpolatedY[i] = function.value(interpolationX[i]);
			else if (function.value(interpolationX[i]) <= 0)
				interpolatedY[i] = -interpolatedY[i];
			else if (function.value(interpolationX[i]) > 1)
				interpolatedY[i] = 1;		

		
		// riempio value e pdf con i nuovi valori
		float value[] = new float[(this.maxVel - this.minVel)*10+1];
		for(int i = 0; i < (this.maxVel - this.minVel)*10+1; i++)
			value[i] = (float)interpolationX[i];
		
		ArrayList<Float> newPdf = new ArrayList<Float>();
		for(int i = 0; i < (this.maxVel - this.minVel)*10+1; i++)
			newPdf.add((float)interpolatedY[i]);
		
		XYchart plot = new XYchart("plot normalised values PDF", value, newPdf, "seriesPDF", "Normalized velocity PDF", "velocity [m/s]",
				"normalized PDF");
        plot.pack();
        RefineryUtilities.positionFrameRandomly(plot);
        plot.setVisible(true); 
	}
	
	/**
	 * The Class XYchart.
	 * <p>
	 * This class is necessary to draw the several plots. First, a dataset is created with 
	 * all x and y values, then the dataset is used by createChart that creates the plot settings
	 * several options such as legend, colors and size of lines. Finally, chartPanel create the panel
	 * for the chart with selected dimensions.
	 */
	private class XYchart extends JFrame{

		/**
		 * Instantiates a new XYchart.
		 *
		 * @param title string used as handle of the plot
		 * @param axisX array of values plotted on the axis x
		 * @param axisY array of values plotted on the axis y
		 * @param nameSeries string used as handle of the dataset
		 * @param namePlot title of the chart
		 * @param xLabel label of axis x
		 * @param yLabel label of axis y
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		public XYchart(String title, float[] axisX, ArrayList<Float> axisY, String nameSeries,
				String namePlot, String xLabel, String yLabel) throws IOException{
			super(title);
			
	        final XYDataset dataset = createDataset(axisX, axisY, nameSeries);
	        final JFreeChart chart = createChart(dataset, namePlot, xLabel, yLabel);
	        final ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(600, 370));
	        setContentPane(chartPanel);        
		}
		
		/**
		 * Creates the dataset.
		 *
		 * @param x array of values plotted on the axis x
		 * @param y array of values plotted on the axis y
		 * @param name string used ad handle of the dataset
		 * @return the XY dataset where for each value x corresponded a value y
		 */
		XYDataset createDataset(float[] x, ArrayList<Float> y, String name){
			XYSeries series = new XYSeries(name);
			
			for(int i = 0; i < x.length; i++)
				series.add(x[i], y.get(i));
			
			XYSeriesCollection dataset = new XYSeriesCollection();
			dataset.addSeries(series);
			
			return dataset;			
		}
		
		/**
		 * Creates the chart.
		 *
		 * @param dataset set of data where for each value x corresponded a value y
		 * @param namePlot title of the chart
		 * @param xLabel label of axis x
		 * @param yLabel label of axis y
		 * @return the chart with specified options such as orientation, colors and so on
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		JFreeChart createChart(XYDataset dataset, String namePlot, String xLabel, 
				String yLabel) throws IOException{
			JFreeChart chart = ChartFactory.createXYLineChart(namePlot, xLabel, 
					yLabel, dataset, PlotOrientation.VERTICAL, true, true, false);
			
			chart.setBackgroundPaint(Color.white);

			final XYPlot plot = chart.getXYPlot();
	        plot.setBackgroundPaint(Color.white);
	        plot.setDomainGridlinePaint(Color.black);
	        plot.setRangeGridlinePaint(Color.black);
	        
	        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	        renderer.setSeriesLinesVisible(0, true);
	        renderer.setSeriesShapesVisible(0, false);
	        plot.setRenderer(renderer);
	        plot.getRenderer().setSeriesPaint(0, Color.black);
	        plot.getRenderer().setSeriesStroke(0, new BasicStroke(2f));

	        //domain restituisce l'asse x	        
	        //range restituisce l'asse y
	        	        
	        if(namePlot == "Normalized velocity PDF"){
		        NumberAxis range = (NumberAxis) plot.getRangeAxis();
		        range.setRange(0.0, 1.0);
		        range.setTickUnit(new NumberTickUnit(0.1));	        
	        }
	        
			return chart;
			
		}
	}
	
	/**
	 * The Class XYchartNetwork.
	 * <p>
	 * This class is necessary to draw the several plots when the game is a multiplayers game. 
	 * First, a dataset is created with all x and y values, then the dataset is used by createChart 
	 * that creates the plot settings
	 * several options such as legend, colors and size of lines. Finally, chartPanel create the panel
	 * for the chart with selected dimensions.
	 */
	private class XYchartNetwork extends JFrame{

		/**
		 * Instantiates a new XYchart of the network.
		 *
		 * @param title string used as handle of the plot
		 * @param axisX array of values plotted on the axis x
		 * @param axisY array of array of values plotted on the axis y. Each array corresponds to a 
		 * player's motion gotten during the trial, so we have an array of player's motion
		 * @param nameSeries string used as handle of the dataset
		 * @param namePlot title of the chart
		 * @param xLabel label of axis x
		 * @param yLabel label of axis y
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		public XYchartNetwork(String title, float[] axisX, ArrayList<ArrayList<Float>> axisY, String nameSeries,
				String namePlot, String xLabel, String yLabel) throws IOException{
			super(title);
			
	        final XYDataset dataset = createDatasetNetwork(axisX, axisY, nameSeries);
	        final JFreeChart chart = createChartNetwork(dataset, namePlot, xLabel, yLabel);
	        final ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(600, 370));
	        setContentPane(chartPanel);        
		}
		

		/**
		 * Creates the dataset of the network.
		 *
		 * @param x array of values plotted on the axis x
		 * @param y array of array of values plotted on the axis y
		 * @param name string used ad handle of the dataset
		 * @return the XY dataset where for each value x corresponded a value y
		 */
		XYDataset createDatasetNetwork(float[] x, ArrayList<ArrayList<Float>> y, String name){
			
			final XYSeries series[] = new XYSeries[y.size()];
			XYSeriesCollection dataset = new XYSeriesCollection();
			
			for(int i = 0; i < y.size(); i++){
				//series[i] = new XYSeries("Player " + y.get(i).get(0));
				series[i] = new XYSeries("Player " + (i + 1));
				//System.out.println("Player " + y.get(i).get(0));
				for(int z = 1; z < x.length; z++){
					series[i].add(x[z-1], y.get(i).get(z));
				}
			}

			for(int i = 0; i < y.size(); i++)					
				dataset.addSeries(series[i]);
			
			System.out.println("Mean synch index " + y.get(1).get(0));
			
			return dataset;			
		}
		
		/**
		 * Creates the chart of the network.
		 *
		 * @param dataset set of data where for each value x corresponded a value y
		 * @param namePlot title of the chart
		 * @param xLabel label of axis x
		 * @param yLabel label of axis y
		 * @return the chart with specified options such as orientation, colors and so on. The chart
		 * shows a line with different color for each player.
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		JFreeChart createChartNetwork(XYDataset dataset, String namePlot, String xLabel, 
				String yLabel) throws IOException{
			JFreeChart chart = ChartFactory.createXYLineChart(namePlot, xLabel, 
					yLabel, dataset, PlotOrientation.VERTICAL, true, true, false);
			
			chart.setBackgroundPaint(Color.white);

			final XYPlot plot = chart.getXYPlot();
	        plot.setBackgroundPaint(Color.white);
	        plot.setDomainGridlinePaint(Color.black);
	        plot.setRangeGridlinePaint(Color.black);
	        
	        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	        
	        for(int i = 0; i < dataset.getSeriesCount(); i++){
	        	renderer.setSeriesLinesVisible(i, true);
	        	renderer.setSeriesShapesVisible(i, false);
	        	plot.setRenderer(renderer);
	        	plot.getRenderer().setSeriesStroke(i, new BasicStroke(2f));
	        	plot.getRenderer().setSeriesPaint(i, color[i]);
	        }
	        

	        //domain restituisce l'asse x	        
	        //range restituisce l'asse y
	        	        
	        if(namePlot == "Normalised values PDF"){
		        NumberAxis range = (NumberAxis) plot.getRangeAxis();
		        range.setRange(0.0, 1.0);
		        range.setTickUnit(new NumberTickUnit(0.1));	        
	        }
	        
			return chart;
			
		}
	}
	
}