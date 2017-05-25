package CIS6905;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public final class PlotUtility extends JFrame {

	private static final long serialVersionUID = 1L;

	public PlotUtility(final String title, double[] array) {
		super("");
		final XYSeries series = new XYSeries(title);
		for (int i = 0; i < array.length; i++) {
			series.add(i, array[i]);
		}
		final XYSeriesCollection data = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart("", "", "", data);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(720, 480));
		setContentPane(chartPanel);
		pack();
		setDefaultCloseOperation(3); // exit on close
		setLocationRelativeTo(null); // center on screen
		setVisible(true);
	}

}