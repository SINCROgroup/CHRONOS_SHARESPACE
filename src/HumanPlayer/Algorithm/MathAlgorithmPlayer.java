/*
 * 
 */
package HumanPlayer.Algorithm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.apache.commons.math3.stat.Frequency;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
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
 * The Class MathAlgorithmPlayer.
 */
public class MathAlgorithmPlayer {
	
	/** The number player. */
	private int numberPlayer = 2;
	
	/** The settings. */
	private Settings settings;
	
	/** The min vel. */
	private int minVel[];
	
	/** The max vel. */
	private int maxVel[];
	
	/**
	 * Instantiates a new math algorithm player.
	 */
	public MathAlgorithmPlayer(){	
		this.settings = Settings.getSettings();
		this.minVel = new int[numberPlayer];
		this.maxVel = new int[numberPlayer];
	}
	
	/**
	 * Gets the velocity from position.
	 *
	 * @param pos the pos
	 * @return the velocity from position
	 */
	public ArrayList<Float> getVelocityFromPosition(ArrayList<Float> pos){
		// per addSignature time = 60 secondi
		
		int samples = settings.getTime()*1000/settings.getDelay(); //numero di campioni in 60 secondi
		ArrayList<Float> vel = new ArrayList<Float>();
		
		vel.add(0, pos.get(0));		
		for(int i = 1; i < samples; i++){
			vel.add(i, (pos.get(i)-pos.get(i-1))*1000/settings.getDelay());
		}
		
		vel.add(samples, (pos.get(samples)-pos.get(samples-1))*1000/settings.getDelay());
		
		//la velocità è calcolata con posizione compresa fra -5 e 5 (database)
		return vel;
	}
	
	/**
	 * Spline position.
	 *
	 * @param pos the pos
	 * @return the array list
	 */
	public ArrayList<Float> splinePosition(ArrayList<Float> pos){
		
		int samples = settings.getTime()*1000/settings.getDelay(); //numero di campioni in 60 secondi
		ArrayList<Float> newPos = new ArrayList<Float>();
				
		//interpolazione della firma se i campioni sono meno di Samples = 6000;
		if(pos.size()<samples){		
			// vettore double della posizione
			double y[] = new double[pos.size()];
			double x[] = new double[pos.size()];
			for(int i = 0; i < pos.size(); i++){
				y[i] = pos.get(i);
				x[i] = i*(samples*settings.getDelay())/pos.size();
			}	
			
			x[pos.size()-1] = samples*settings.getDelay();
			
			//vettore double del tempo
			UnivariateInterpolator interpolator = new SplineInterpolator();
			UnivariateFunction function = interpolator.interpolate(x, y);
			
			double interpolationX[] = new double[samples];
			double interpolatedY[] = new double[samples];
			
			for(int i = 0; i < samples; i++)
				interpolationX[i] = i*settings.getDelay();
			
			for(int i = 0; i < samples; i++)
				interpolatedY[i] = function.value(interpolationX[i]);
			
			for(int i = 0; i < samples; i++){
				newPos.add(i, (float) interpolatedY[i]);
			}
		}else{
			// mapping fra [0, 799] a [-500, 500]
			for(int i = 0; i < samples; i++){
		    	newPos.add(i, pos.get(i));
			}
		}
		
		return newPos;
	}
	
	/**
	 * Gets the PD ffrom velocity.
	 *
	 * @param vel the vel
	 * @param index the index
	 * @return the PD ffrom velocity
	 */
	public ArrayList<Float> getPDFfromVelocity(ArrayList<Float> vel, int index){
		
		Frequency f = new Frequency();
		ArrayList<Float> pdf = new ArrayList<Float>();
		float pdfMax = 0, maxVelF = 0, minVelF = 0;
		
		// la velocità è divisa per 10 come se la posizione fosse fra -0.5 e 0.5
		for(int i = 0; i < vel.size(); i++)
			f.addValue((int)(vel.get(i)*0.1f));
		
		//mi serve il range di valori di vel, prendo il massimo ed il minimo
		maxVelF = vel.get(0);
		for (int i = 1; i <vel.size(); i++)
			if (vel.get(i) > maxVelF) 
				maxVelF = vel.get(i);

		this.maxVel[index] = (int)maxVelF;
		
		if(this.maxVel[index] >= 4)
			this.maxVel[index] = 5;
		
		minVelF = vel.get(0);
		for (int i = 1; i <vel.size(); i++)
			if (vel.get(i) < minVelF) 
				minVelF = vel.get(i); 
			
		this.minVel[index] = (int)minVelF;
		
		if(this.minVel[index] <= -4)
			this.minVel[index] = -5;
		
		for(int i = this.minVel[index]; i <= this.maxVel[index]; i++){
			pdf.add((float) f.getPct(i));
			
		}
		
		pdfMax = pdf.get(0);
		for (int i = 1; i < pdf.size(); i++)
			if (pdf.get(i) > pdfMax) 
				pdfMax = pdf.get(i);
		
		for (int i = 0; i < pdf.size(); i++)
			pdf.set(i, pdf.get(i)*(1/pdfMax));	
	
		return pdf;
	}
	
	/**
	 * Gets the PD ffrom position.
	 *
	 * @param pos the pos
	 * @param index the index
	 * @return the PD ffrom position
	 */
	public ArrayList<Float> getPDFfromPosition(ArrayList<Float> pos, int index){
		
		Frequency f = new Frequency();
		ArrayList<Float> pdf = new ArrayList<Float>();
		float pdfMax = 0, maxVelF = 0, minVelF = 0;
		
		// la velocità è divisa per 100 come se la posizione fosse fra -5 e 5
		for(int i = 0; i < pos.size(); i++)
			f.addValue((int)(pos.get(i)*1f));
		
		//mi serve il range di valori di vel, prendo il massimo ed il minimo
		maxVelF = pos.get(0);
		for (int i = 1; i < pos.size(); i++)
			if (pos.get(i) > maxVelF) 
				maxVelF = pos.get(i);
		
		this.maxVel[index] = (int) maxVelF;
		
		if(this.maxVel[index] >= 4)
			this.maxVel[index] = 5;
		
		minVelF = pos.get(0);
		for (int i = 1; i < pos.size(); i++)
			if (pos.get(i) < minVelF) 
				minVelF = pos.get(i);
		
		this.minVel[index] = (int) minVelF;
		
		if(this.minVel[index] <= -4)
			this.minVel[index] = -5;
		
		for(int i = this.minVel[index]; i <= this.maxVel[index]; i++){
			pdf.add((float) f.getPct(i));
		}
		
		pdfMax = pdf.get(0);
		for (int i = 1; i < pdf.size(); i++)
			if (pdf.get(i) > pdfMax) 
				pdfMax = pdf.get(i);
		
		for (int i = 0; i < pdf.size(); i++)
			pdf.set(i, pdf.get(i)*(1/pdfMax));	
		
	
		return pdf;
	}

	/**
	 * Plot signature velocity.
	 *
	 * @param vel the vel
	 * @param vel2 the vel2
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void plotSignatureVelocity(ArrayList<Float> vel, ArrayList<Float> vel2, String typeTrial) throws IOException{
		
		float time[] = new float[vel.size()];
		
		// setto l'asse delle x (tempo)
		for(int i = 0; i < vel.size(); i++)
			time[i] = (float)i*settings.getDelay();
		
		XYchart plot = null;
		if(typeTrial.equals("HP-VP"))
			plot = new XYchart("plot Signature", time, time, vel, vel2, "seriesVelocityHP_VP", 
				"Velocity time series", "time [ms]", "velocity[dm/s]");
		else
			plot = new XYchart("plot Signature", time, time, vel, vel2, "seriesVelocity2HP", 
					"Velocity time series", "time [ms]", "velocity[dm/s]");
        plot.pack();
        RefineryUtilities.positionFrameRandomly(plot);
        plot.setVisible(true);        
	}
	
	/**
	 * Plot pdf.
	 *
	 * @param pdf1 the pdf1
	 * @param pdf2 the pdf2
	 * @param index the index
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// index scinde fra posizione dalla velocità
	public void plotPDF(ArrayList<Float> pdf1, ArrayList<Float> pdf2, int index, String typeTrial) throws IOException{
		
		//i valori della pdf sono troppo pochi...facciamo la spline per aumentare la granularità
		double y1[] = new double[this.maxVel[0] - this.minVel[0] + 1];
		double y2[] = new double[this.maxVel[1] - this.minVel[1] + 1];
		double x1[] = new double[this.maxVel[0] - this.minVel[0] + 1];
		double x2[] = new double[this.maxVel[1] - this.minVel[1] + 1];
		
		for(int i = this.minVel[0]; i < this.maxVel[0]+1; i++){
			y1[i - this.minVel[0]] = pdf1.get(i-this.minVel[0]);
			x1[i - this.minVel[0]] = i;
		}
		
		for(int i = this.minVel[1]; i < this.maxVel[1]+1; i++){
			y2[i - this.minVel[1]] = pdf2.get(i-this.minVel[1]);
			x2[i - this.minVel[1]] = i;
		}
		
		UnivariateInterpolator interpolator = new SplineInterpolator();
		UnivariateFunction function1 = interpolator.interpolate(x1, y1);
		UnivariateFunction function2 = interpolator.interpolate(x2, y2);
		
		double interpolationX1[] = new double[(this.maxVel[0] - this.minVel[0])*10+1];
		double interpolationX2[] = new double[(this.maxVel[1] - this.minVel[1])*10+1];
		double interpolatedY1[] = new double[(this.maxVel[0] - this.minVel[0])*10+1];
		double interpolatedY2[] = new double[(this.maxVel[1] - this.minVel[1])*10+1];
		
		for(int i = 0; i < (this.maxVel[0] - this.minVel[0])*10+1 ; i++)
			interpolationX1[i] = this.minVel[0]+(0.1*i);
		
		for(int i = 0; i < (this.maxVel[1] - this.minVel[1])*10+1 ; i++)
			interpolationX2[i] = this.minVel[1]+(0.1*i);
		
		for(int i = 0; i < (this.maxVel[0] - this.minVel[0])*10+1 ; i++){
			if(function1.value(interpolationX1[i]) >= 0 && function1.value(interpolationX1[i]) <= 1)
				interpolatedY1[i] = function1.value(interpolationX1[i]);
			else if (function1.value(interpolationX1[i]) < 0)
				interpolatedY1[i] = -interpolatedY1[i];
			else if (function1.value(interpolationX1[i]) > 1)
				interpolatedY1[i] = 1;
		}
		
		for(int i = 0; i < (this.maxVel[1] - this.minVel[1])*10+1 ; i++){
			if(function2.value(interpolationX2[i]) > 0 && function2.value(interpolationX2[i]) <= 1)
				interpolatedY2[i] = function2.value(interpolationX2[i]);
			else if (function2.value(interpolationX2[i]) <= 0)
				interpolatedY2[i] = -interpolatedY2[i];
			else if (function2.value(interpolationX2[i]) > 1)
				interpolatedY2[i] = 1;
		}
		
		// riempio value e pdf con i nuovi valori
		float value1[] = new float[(this.maxVel[0] - this.minVel[0])*10+1];
		for(int i = 0; i < (this.maxVel[0] - this.minVel[0])*10+1; i++)
			value1[i] = (float)interpolationX1[i];
		
		float value2[] = new float[(this.maxVel[1] - this.minVel[1])*10+1];
		for(int i = 0; i < (this.maxVel[1] - this.minVel[1])*10+1; i++)
			value2[i] = (float)interpolationX2[i];
		
		ArrayList<Float> newPdf1 = new ArrayList<Float>();
		ArrayList<Float> newPdf2 = new ArrayList<Float>();
		for(int i = 0; i < (this.maxVel[0] - this.minVel[0])*10+1; i++){
			newPdf1.add((float)interpolatedY1[i]);
		}
		
		for(int i = 0; i < (this.maxVel[1] - this.minVel[1])*10+1; i++){
			newPdf2.add((float)interpolatedY2[i]);
		}
		
		XYchart plot = null;
		
		if(typeTrial.equals("HP-VP"))
			if(index == 0){
				 plot = new XYchart("plot normalised values PDF of Position", value1, value2, newPdf1, newPdf2, "seriesPDFPosHP_VP", "Position PDF", "value position [dm]",
					"position PDF");	        
			} else {
				plot = new XYchart("plot normalised values PDF of Velocity", value1, value2, newPdf1, newPdf2, "seriesPDFVelHP_VP", "Velocity PDF", "value velocity [m/s]",
						"velocity PDF");
			}
		else
			if(index == 0){
				 plot = new XYchart("plot normalised values PDF of Position", value1, value2, newPdf1, newPdf2, "seriesPDFPos2HP", "Position PDF", "value position [dm]",
					"position PDF");	        
			} else {
				plot = new XYchart("plot normalised values PDF of Velocity", value1, value2, newPdf1, newPdf2, "seriesPDFVel2HP", "Velocity PDF", "value velocity [m/s]",
						"velocity PDF");
			}
		
		plot.pack();
        RefineryUtilities.positionFrameRandomly(plot);
        plot.setVisible(true);
	}
	
	
	/**
	 * The Class XYchart.
	 */
	@SuppressWarnings("serial")
	private class XYchart extends JFrame{

		/**
		 * Instantiates a new x ychart.
		 *
		 * @param title the title
		 * @param axisX1 the axis x1
		 * @param axisX2 the axis x2
		 * @param axisY1 the axis y1
		 * @param axisY2 the axis y2
		 * @param nameSeries the name series
		 * @param namePlot the name plot
		 * @param xLabel the x label
		 * @param yLabel the y label
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		public XYchart(String title, float[] axisX1, float[] axisX2, ArrayList<Float> axisY1, ArrayList<Float> axisY2, String nameSeries,
				String namePlot, String xLabel, String yLabel) throws IOException{
			super(title);
			
	        final XYDataset dataset = createDataset(axisX1, axisX2, axisY1, axisY2, nameSeries);
	        
	        final JFreeChart chart = createChart(dataset, namePlot, xLabel, yLabel);
	        final ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(600, 370));
	        setContentPane(chartPanel);        
		}
		
		/**
		 * Creates the dataset.
		 *
		 * @param x1 the x1
		 * @param x2 the x2
		 * @param y1 the y1
		 * @param y2 the y2
		 * @param name the name
		 * @return the XY dataset
		 */
		private XYDataset createDataset(float[] x1, float[] x2, ArrayList<Float> y1, ArrayList<Float> y2, String name){
			
			final XYSeries series1, series2;
			XYSeriesCollection dataset = new XYSeriesCollection();
						
			// plot dell'errore
			if(name == "seriesErrorPos" || name == "seriesErrorVel"){
				series1 = new XYSeries("error");
				
				for(int i = 0; i < x1.length; i++){
					series1.add(x1[i], y1.get(i));					
				}
					
				dataset.addSeries(series1);			
			}else{
				if(name == "seriesPositionHP_VP" || name == "seriesVelocityHP_VP" ||
						name == "seriesPDFPosHP_VP" || name == "seriesPDFVelHP_VP"){
					//series1 = new XYSeries("Human Player");
					//series2 = new XYSeries("Virtual Player");
					series1 = new XYSeries("You");
					series2 = new XYSeries("Your partner");
				}else{
					series1 = new XYSeries("You");
					series2 = new XYSeries("Your partner");
				}
				
				for(int i = 0; i < x1.length; i++)
					series1.add(x1[i], y1.get(i));
			
				
				for(int i = 0; i < x2.length; i++)
					series2.add(x2[i], y2.get(i));
								
				dataset.addSeries(series1);
				dataset.addSeries(series2);
			}
						
			return dataset;			
		}
		
		/**
		 * Creates the chart.
		 *
		 * @param dataset the dataset
		 * @param namePlot the name plot
		 * @param xLabel the x label
		 * @param yLabel the y label
		 * @return the j free chart
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		private JFreeChart createChart(final XYDataset dataset, String namePlot, String xLabel, 
				String yLabel) throws IOException{
			JFreeChart chart = ChartFactory.createXYLineChart(namePlot, xLabel, 
					yLabel, dataset, PlotOrientation.VERTICAL, true, true, false);
			
			chart.setBackgroundPaint(Color.white);

			final XYPlot plot = chart.getXYPlot();
	        plot.setBackgroundPaint(Color.white);
	        plot.setDomainGridlinePaint(Color.black);
	        plot.setRangeGridlinePaint(Color.black);
	        
	        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	        
	        if(namePlot == "Velocity error" || namePlot == "Position error"){
	        	renderer.setSeriesLinesVisible(0, true);
	        	renderer.setSeriesShapesVisible(0, false);
	        	plot.setRenderer(renderer);
	        	plot.getRenderer().setSeriesPaint(0, Color.black);
	        	plot.getRenderer().setSeriesStroke(0, new BasicStroke(2f));
	        }else{
	        	renderer.setSeriesLinesVisible(0, true);
	        	renderer.setSeriesShapesVisible(0, false);
	        	renderer.setSeriesLinesVisible(1, true);
	        	renderer.setSeriesShapesVisible(1, false);
	        	plot.setRenderer(renderer);
	        	plot.getRenderer().setSeriesStroke(0, new BasicStroke(2f));
	        	plot.getRenderer().setSeriesStroke(1, new BasicStroke(2f));
	        	plot.getRenderer().setSeriesPaint(0, Color.blue);
	        	plot.getRenderer().setSeriesPaint(1, Color.red);
	        }

	        //domain restituisce l'asse x	        
	        //range restituisce l'asse y
	        	        
	        if(namePlot == "Velocity PDF" || namePlot == "Position PDF"){
		        final NumberAxis range = (NumberAxis) plot.getRangeAxis();
		        range.setRange(0.0, 1.0);
		        range.setTickUnit(new NumberTickUnit(0.1));	        
	        }
	        
			return chart;
			
		}
	}
	
	
	/**
	 * Plot signature position.
	 *
	 * @param pos1 the pos1
	 * @param pos2 the pos2
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void plotSignaturePosition(ArrayList<Float> pos1, ArrayList<Float> pos2, String typeTrial) throws IOException{
		
		float time[] = new float[pos1.size()];
		
		// setto l'asse delle x (tempo)
		for(int i = 0; i < pos1.size(); i++)
			time[i] = (float)i*settings.getDelay();
		
		XYchart plot = null;
		if(typeTrial.equals("HP-VP"))
			plot = new XYchart("plot Position", time, time, pos1, pos2, "seriesPositionHP_VP", 
				"Position time series", "time [ms]", "position [dm]");
		else
			plot = new XYchart("plot Position", time, time, pos1, pos2, "seriesPosition2HP", 
					"Position time series", "time [ms]", "position [dm]");
		
        plot.pack();
        RefineryUtilities.positionFrameRandomly(plot);
        plot.setVisible(true);  
	}
	
	/**
	 * Plot error.
	 *
	 * @param x1 the x1
	 * @param x2 the x2
	 * @param index the index
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void plotError(ArrayList<Float> x1, ArrayList<Float> x2, int index) throws IOException{
		ArrayList<Float> error = new ArrayList<Float>();
		ArrayList<Float> percentageError = new ArrayList<Float>();
		float time[] = new float[x1.size()];
		
		//index = 0 è la posizione, index = 1 è la velocità
		// la posizione e la velocità sono considerate fra -5 e 5 dm
		for(int i = 0; i < x1.size(); i++){
			if(index == 0)
				error.add((x1.get(i)-x2.get(i))*0.1f);
			else{
				error.add((x1.get(i)-x2.get(i)));
			}
			time[i] = (float)i*settings.getDelay();
		}
		
		for(int i = 0; i < error.size(); i++){
			if(index == 0){
				percentageError.add((Math.abs(error.get(i))/5)*100);
			}else{
				percentageError.add((Math.abs(error.get(i))/50)*100);
			}
		}		
		
		XYchart plot = null;
		if(index == 0)
			plot = new XYchart("plot Error of Position", time, null, percentageError, null, "seriesErrorPosHP_VP", 
				"Position error", "time [ms]", "error [%]");
		else{
			plot = new XYchart("plot Error of Velocity", time, null, percentageError, null, "seriesErrorVelHP_VP", 
					"Velocity error", "time [ms]", "error [%]");
		}
		
        plot.pack();
        RefineryUtilities.positionFrameRandomly(plot);
        plot.setVisible(true); 
	}
}
