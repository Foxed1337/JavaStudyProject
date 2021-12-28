import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Charts {

    public static void drawCitiesStatistics() throws SQLException {
        var cities = DBManager.getCities();
        if (cities == null) {
            System.out.println("Не удалось получить данные");
            return;
        }
        var citiesStat = new HashMap<String, Integer>();
        for (var city : cities) {
            if (citiesStat.containsKey(city)) {
                citiesStat.put(city, citiesStat.get(city) + 1);
            } else {
                citiesStat.put(city, 1);
            }
        }
        BarCitiesStatictic.draw("ГОРОДА", citiesStat);
        System.out.println();
    }

    public static void drawAgeStatistics() throws SQLException {
        var birthdays = DBManager.getAge();
        if (birthdays == null) {
            System.out.println("Не удалось получить данные");
            return;
        }
        var agesStat = new HashMap<Integer, Integer>();
        var ages = birthdays.stream()
                .map(x -> LocalDate.now().getYear() - Integer.parseInt(x.split("\\.")[2]))
                .collect(Collectors.toList());

        for (var age : ages) {
            if (agesStat.containsKey(age)) {
                agesStat.put(age, agesStat.get(age) + 1);
            } else {
                agesStat.put(age, 1);
            }
        }
        PieAgesStatictic.draw("Статистика по возрастам", agesStat);
        System.out.println();
    }

    public static void drawTopicStatistic(String topicName) throws SQLException {

        var topicsAndMarks = DBManager.getTasksAndMarksOfTopic(topicName);
        if (topicsAndMarks == null) {
            System.out.println("Не удалось получить данные");
            return;
        }
        var topicStat = new HashMap<String, Integer>();

        for (var i : topicsAndMarks) {
            if (i.y != 0) {
                if (topicStat.containsKey(i.x)) {
                    topicStat.put(i.x, topicStat.get(i.x) + 1);
                } else {
                    topicStat.put(i.x, i.y);
                }
            }
        }
        BarTopicStatistic.draw(topicName, topicStat);
        System.out.println();
    }

    public static void drawGenderStatistic() throws SQLException {
        var genders = DBManager.getGenders();
        if (genders == null) {
            System.out.println("Не удалось получить данные");
            return;
        }
        var gendersStat = new HashMap<String, Integer>();

        for (var gender : genders) {
            if (gendersStat.containsKey(gender)) {
                gendersStat.put(gender, gendersStat.get(gender) + 1);
            } else {
                gendersStat.put(gender, 1);
            }
        }
        PieGendersStatictic.draw("Половая принадлежность студентов курса", gendersStat);
        System.out.println();
    }

//    public static  void drawCitiesAndTaskMarksStatistic(String topicName) throws SQLException {
//
//        var townsAndTasks = DBManager.getCitiesAndTasksMarks(topicName);
//        System.out.println();
//        //ScatterCitiesAndTasksMarksStatictic.draw();
//    }

}

class BarCitiesStatictic extends ApplicationFrame {
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

class BarTopicStatistic extends ApplicationFrame {
    Map<String, Integer> data;

    public BarTopicStatistic(String title, Map<String, Integer> data) {
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
                String.format("Выполнение заданий в теме %s", this.getTitle()),
                null,                                    // x-axis label
                "Кол-во студентов, выполнивших задание",   // y-axis label
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
        BarTopicStatistic statistics = new BarTopicStatistic(title, data);
        statistics.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        statistics.pack();
        RefineryUtilities.centerFrameOnScreen(statistics);
        statistics.setVisible(true);
    }
}

class PieAgesStatictic extends ApplicationFrame {

    Map<Integer, Integer> data;

    public PieAgesStatictic(String title, Map<Integer, Integer> data) {
        super(title);
        this.data = data;
        setContentPane(createPanel());
    }

    public JPanel createPanel() {
        JFreeChart chart = createChart(createDataset());
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(1000, 600));
        return panel;
    }

    private PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (var i : data.keySet()) {
            dataset.setValue(String.format("Возраст: %s", i), data.get(i));
        }
        return dataset;
    }

    private JFreeChart createChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                this.getTitle(),  // chart title
                dataset,             // data
                false,               // no legend
                true,                // tooltips
                false                // no URL generation
        );

        // Определение фона графического изображения
        chart.setBackgroundPaint(new GradientPaint(new Point(0, 0), new Color(20, 20, 20),
                new Point(400, 200), Color.DARK_GRAY));
        // Определение заголовка
        TextTitle t = chart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.LEFT);
        t.setPaint(new Color(240, 240, 240));
        t.setFont(new Font("Arial", Font.BOLD, 26));

        // Определение подзаголовка
        TextTitle source = new TextTitle(this.getTitle(),
                new Font("Courier New", Font.PLAIN, 12));
        source.setPaint(Color.WHITE);
        source.setPosition(RectangleEdge.BOTTOM);
        source.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        chart.addSubtitle(source);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setInteriorGap(0.04);
        plot.setOutlineVisible(false);


        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(2.0f));

        // Настройка меток названий секций
        plot.setLabelFont(new Font("Courier New", Font.BOLD, 20));
        plot.setLabelLinkPaint(Color.WHITE);
        plot.setLabelLinkStroke(new BasicStroke(2.0f));
        plot.setLabelOutlineStroke(null);
        plot.setLabelPaint(Color.WHITE);
        plot.setLabelBackgroundPaint(null);

        return chart;
    }

    public static void draw(String title, Map<Integer, Integer> data) {
        PieAgesStatictic statistics = new PieAgesStatictic(title, data);
        statistics.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        statistics.pack();
        RefineryUtilities.centerFrameOnScreen(statistics);
        statistics.setVisible(true);
    }
}

class PieGendersStatictic extends ApplicationFrame {
    Map<String, Integer> data;

    public PieGendersStatictic(String title, Map<String, Integer> data) {
        super(title);
        this.data = data;
        setContentPane(createPanel());
    }

    public JPanel createPanel() {
        JFreeChart chart = createChart(createDataset());
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(1000, 600));
        return panel;
    }

    private PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (var i : data.keySet()) {
            String gender = i.equals("1") ? "Женщины" : i.equals("2") ? "Мужчины" : i.equals("none") ? "Пол не указан" : i;
            dataset.setValue(String.format("%s", gender), data.get(i));
        }
        return dataset;
    }

    private JFreeChart createChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                this.getTitle(),  // chart title
                dataset,             // data
                false,               // no legend
                true,                // tooltips
                false                // no URL generation
        );

        // Определение фона графического изображения
        chart.setBackgroundPaint(new GradientPaint(new Point(0, 0), new Color(20, 20, 20),
                new Point(400, 200), Color.DARK_GRAY));
        // Определение заголовка
        TextTitle t = chart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.LEFT);
        t.setPaint(new Color(240, 240, 240));
        t.setFont(new Font("Arial", Font.BOLD, 26));

        // Определение подзаголовка
        TextTitle source = new TextTitle(this.getTitle(),
                new Font("Courier New", Font.PLAIN, 12));
        source.setPaint(Color.WHITE);
        source.setPosition(RectangleEdge.BOTTOM);
        source.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        chart.addSubtitle(source);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setInteriorGap(0.04);
        plot.setOutlineVisible(false);


        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(2.0f));

        // Настройка меток названий секций
        plot.setLabelFont(new Font("Courier New", Font.BOLD, 20));
        plot.setLabelLinkPaint(Color.WHITE);
        plot.setLabelLinkStroke(new BasicStroke(2.0f));
        plot.setLabelOutlineStroke(null);
        plot.setLabelPaint(Color.WHITE);
        plot.setLabelBackgroundPaint(null);

        return chart;
    }

    public static void draw(String title, Map<String, Integer> data) {
        PieGendersStatictic statistics = new PieGendersStatictic(title, data);
        statistics.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        statistics.pack();
        RefineryUtilities.centerFrameOnScreen(statistics);
        statistics.setVisible(true);
    }

}


