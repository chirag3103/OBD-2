package com.util;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

/**
 * This demo shows a bar chart with time based data where the time periods are slightly
 * irregular.
 *
 */
public class LineChart2D extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public LineChart2D(final String title) {

        super(title);

        final XYDataset data1 = createDataset();
        final XYItemRenderer renderer1 = new XYBarRenderer();
        
        final DateAxis domainAxis = new DateAxis("Date");
        final ValueAxis rangeAxis = new NumberAxis("Value");
        
        final XYPlot plot = new XYPlot(data1, domainAxis, rangeAxis, renderer1);

        final JFreeChart chart = new JFreeChart("Time Period Values Demo 3", plot);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        chartPanel.setMouseZoomable(true, false);
        setContentPane(chartPanel);

    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    
    /**
     * Creates a dataset, consisting of two series of monthly data.
     *
     * @return the dataset.
     */
    public XYDataset createDataset() {

        final TimePeriodValues s1 = new TimePeriodValues("Series 1");
        
        final DateFormat df = DateFormat.getInstance();
        try {
            final Date d0 = df.parse("11/5/2003 0:00:00.000");
            final Date d1 = df.parse("11/5/2003 0:15:00.000");
            final Date d2 = df.parse("11/5/2003 0:30:00.000");
            final Date d3 = df.parse("11/5/2003 0:45:00.000");
            final Date d4 = df.parse("11/5/2003 1:00:00.001");
            final Date d5 = df.parse("11/5/2003 1:14:59.999");
            final Date d6 = df.parse("11/5/2003 1:30:00.000");
            final Date d7 = df.parse("11/5/2003 1:45:00.000");
            final Date d8 = df.parse("11/5/2003 2:00:00.000");
            final Date d9 = df.parse("11/5/2003 2:15:00.000");
                
            s1.add(new SimpleTimePeriod(d0, d1), 0.39);
            //s1.add(new SimpleTimePeriod(d1, d2), 0.338);
            s1.add(new SimpleTimePeriod(d2, d3), 0.225);
            s1.add(new SimpleTimePeriod(d3, d4), 0.235);
            s1.add(new SimpleTimePeriod(d4, d5), 0.238);
            s1.add(new SimpleTimePeriod(d5, d6), 0.236);
            s1.add(new SimpleTimePeriod(d6, d7), 0.25);
            s1.add(new SimpleTimePeriod(d7, d8), 0.238);
            s1.add(new SimpleTimePeriod(d8, d9), 0.215);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

        final TimePeriodValuesCollection dataset = new TimePeriodValuesCollection();
        dataset.addSeries(s1);
        dataset.setDomainIsPointsInTime(false);

        return dataset;

    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     * @throws ParseException 
     */
    public static void main(final String[] args) throws ParseException {

//        final TimePeriodValuesDemo3 demo = new TimePeriodValuesDemo3("Time Period Values Demo 3");
//        demo.pack();
//        RefineryUtilities.centerFrameOnScreen(demo);
//        demo.setVisible(true);
//    	 DefaultCategoryDataset de=new DefaultCategoryDataset();
//    	 de.setValue(10,"Vehicle Speed",new Date().toLocaleString()) ;
//    	 de.setValue(11,"Vehicle Speed","Shahadeo") ;
//    	 de.setValue(12,"Vehicle Speed","Ashok") ;
//    	 
//    	  JFreeChart chart = ChartFactory.createBarChart(
//    	  "Driver Performance Graph",
//    	  "Date",
//    	  "VechicleSpeed",
//    	  de,
//    	  PlotOrientation.VERTICAL,
//    	  true,
//    	  true,   
//    	  false);
//    	 
//    	  try {
//    	  ChartUtilities.saveChartAsJPEG(new File("D:\\chart.jpg"), chart, 500, 300);
//    	  } catch (IOException e) {   
//    	  System.err.println("Problem occurred creating chart.");
//    	  }
    	List l=null;
    	XYDataset pop = createDataset2(l);
//    	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-dd-MM kk:mm:ss"); 
//		Date d=sdf.parse("2013-04-05 15:40:42");
//    	pop.add(new Day(d), 100);
//    	d=sdf.parse("2013-04-05 16:40:42");
//    	pop.add(new Day(d), 150);
//    	d=sdf.parse("2013-04-05 17:40:42");
//    	pop.add(new Day(d), 250);
    	
    
    	JFreeChart chart = ChartFactory.createTimeSeriesChart(
    	"Population of CSC408 Town",
    	"Date",
    	"Population",
    	pop,
    	true,
    	true,
    	false);
    	try {
    	ChartUtilities.saveChartAsJPEG(new File("D:\\chart.jpg"), chart, 500, 300);
    	} catch (IOException e) {
    	System.err.println("Problem occurred creating chart.");
    	}
    	  
    }
    public static XYDataset createDataset1() {

        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.setDomainIsPointsInTime(true);
                
        final TimeSeries s1 = new TimeSeries("Series 1", Second.class);
        s1.add(new Second(0, 0, 0, 7, 7, 2012), 12);
        s1.add(new Second(0, 30, 2, 7, 7, 2012), 31);
        s1.add(new Second(0, 15, 12, 7, 7, 2012), 80);
        dataset.addSeries(s1);
        return dataset;
}
    public static XYDataset createDataset2(List list) {
//    	  List list = null;
    	
//    	  String sql = "";
//    	  
//    	 
//    	 		sql = "Select *,date_format(o.`time`, '%d-%b-%y %h:%i:%s %p') as `time`,o.time as udate from  obdserver o,vehicles v where v.vehicleId=o.vehicleId Limit 0,100";
//    	 	
//    	  
//    	  list = DBUtils.getBeanList(OBDServerModel.class,sql);
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.setDomainIsPointsInTime(true);
                
        final TimeSeries s1 = new TimeSeries("Driver (Date vs Vehicle Speed) Graph", Second.class);
        final TimeSeries s2 = new TimeSeries("Driver (Date vs RPM) Graph", Second.class);
        final TimeSeries s3 = new TimeSeries("Driver (Date vs Throttle Pos) Graph", Second.class);
        int i=0;
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			try {
				OBDServerModel vehicle = (OBDServerModel) iterator.next();
				String udate = vehicle.getUdate();
				if (udate.lastIndexOf(".0") != -1) {
					udate = udate.substring(0, udate.length() - 2);
				}
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-dd-MM kk:mm:ss");
				Date d = sdf.parse(udate);
//				s1.add(new Second(d.getSeconds(), d.getMinutes(), d.getHours(),
//						d.getDate(), d.getMonth() + 1, d.getYear()),
//						StringHelper.nullObjectToIntegerEmpty(vehicle.getVss()));
				s1.add(new Second(d),StringHelper.nullObjectToFloatEmpty(vehicle.getVss()));
				s2.add(new Second(d),StringHelper.nullObjectToFloatEmpty(vehicle.getRpm()));
				s3.add(new Second(d),StringHelper.nullObjectToFloatEmpty(vehicle.getThrottlepos()));
				i++;
				if(i>100){
					break;
				}
			} catch (Exception e) {
					e.printStackTrace();
			}
    	}
//        s1.add(new Second(0, 0, 0, 7, 7, 2012), 12);
//        s1.add(new Second(0, 30, 2, 7, 7, 2012), 31);
//        s1.add(new Second(0, 15, 12, 7, 7, 2012), 80);
        dataset.addSeries(s1);
        dataset.addSeries(s2);
        dataset.addSeries(s3);
        return dataset;
}
        
}
