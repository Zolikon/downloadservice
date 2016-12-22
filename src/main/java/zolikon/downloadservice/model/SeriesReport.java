package zolikon.downloadservice.model;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static zolikon.downloadservice.model.ReportColouring.*;

public class SeriesReport {

    private static final String REPLACEMENT = "*";
    private Integer length;
    private String seriesName;
    private List<String> reports = new ArrayList<>();

    public SeriesReport(String seriesName) {
        this.seriesName = BLUE.getPlaceHolder() + seriesName + RESET.getPlaceHolder();
    }

    public void addReport(ReportColouring colouring, String report, Object... toInsert) {
        reports.add(colouring.getPlaceHolder() + String.format(report, toInsert) + RESET.getPlaceHolder());
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        determineLength();
        builder.append(addColouring(createClosureLine()));
        builder.append(addColouring(createFormattedLine(seriesName)));
        builder.append(addColouring(createClosureLine()));
        for (String str : reports) {
            builder.append(addColouring(createFormattedLine(str)));
        }
        builder.append(addColouring(createClosureLine()));
        return builder.toString();
    }

    private void determineLength() {
        int max = seriesName.length();
        for (String str : reports) {
            if (str.length() > max) {
                max = str.length();
            }
        }
        length = max + 4;
    }

    private String addColouring(String line){
        String result = line;
        for(ReportColouring reportColouring:ReportColouring.values()){
            result = result.replace(reportColouring.getPlaceHolder(),reportColouring.getColourCode());
        }
        return result;
    }

    private String createClosureLine() {
        return String.format("%" + length + "s%n", "").replace(" ", REPLACEMENT);
    }

    private String createFormattedLine(String str) {
        String center = StringUtils.center(str, length);
        return REPLACEMENT + center + REPLACEMENT + "\n";
    }

    public static void main(String[] args) throws Exception {
        SeriesReport report = new SeriesReport("test");
        report.addReport(GREEN,"test report fghdfghdfghsdfghdfghdfghdfgh");
        report.addReport(RED,"test report");
        System.out.println(report);
    }
}
