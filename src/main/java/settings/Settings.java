package settings;

/**
 * Class containing all settings for the program
 */

public class Settings {

    private GraphSettings graphSettings;
    private HardwareCostSettings hardwareCostSettings;
    private SketchesMemorySetting sketchesMemorySetting;
    private StatisticsSettings statisticsSettings;

    // setters
    public void setGraphSettings(GraphSettings graphSettings) {
        this.graphSettings = graphSettings;
    }

    public void setHardwareCostSettings(HardwareCostSettings hardwareCostSettings) {
        this.hardwareCostSettings = hardwareCostSettings;
    }

    public void setSketchesMemorySetting(SketchesMemorySetting sketchesMemorySetting) {
        this.sketchesMemorySetting = sketchesMemorySetting;
    }

    public void setStatisticsSettings(StatisticsSettings statisticsSettings) {
        this.statisticsSettings = statisticsSettings;
    }

    // getters
    public GraphSettings getGraphSettings() {
        return graphSettings;
    }

    public HardwareCostSettings getHardwareCostSettings() {
        return hardwareCostSettings;
    }

    public SketchesMemorySetting getSketchesMemorySetting() {
        return sketchesMemorySetting;
    }

    public StatisticsSettings getStatisticsSettings() {
        return statisticsSettings;
    }

}
