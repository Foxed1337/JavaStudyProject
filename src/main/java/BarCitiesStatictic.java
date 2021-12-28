import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.ApplicationFrame;

public class BarCitiesStatictic extends ApplicationFrame {
    Map<String, Integer> data;

    public BarCitiesStatictic(String title, Map<String, Integer> data) {
        super(title);
        this.data = data;
        setContentPane(createPanel());
    }

    public JPanel createPanel() {
        CategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new Dimension(1000, 600));
        return chartPanel;
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (var i : data.keySet()) {
            dataset.addValue(data.get(i), i, i);
        }
        return dataset;
    }

    private JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Статистика городов студентов записанных на курс",
                null,                    // x-axis label
                "Кол-во студентов",                 // y-axis label
                dataset);
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        chart.getLegend().setFrame(BlockBorder.NONE);
        return chart;
    }

    public static void draw(String title, Map<String, Integer> data) {
        BarCitiesStatictic statistics = new BarCitiesStatictic(title, data);
        statistics.pack();
        statistics.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        RefineryUtilities.centerFrameOnScreen(statistics);
        statistics.setVisible(true);

    }

}
