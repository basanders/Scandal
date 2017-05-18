package CIS6905;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class PlotUtility extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	public PlotUtility(final String title, double[] array) {
		super("");
		final XYSeries series = new XYSeries(title);
		for (int i = 0; i < array.length; i++) {
			series.add(i, array[i]);
		}
		final XYSeriesCollection data = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart("", "", "", data, PlotOrientation.VERTICAL, true, true, false);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(720, 480));
		setContentPane(chartPanel);
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);
	}

}