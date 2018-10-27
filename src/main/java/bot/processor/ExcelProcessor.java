package bot.processor;

import bot.schema.Platform;
import bot.schema.Station;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

public class ExcelProcessor extends Settings {
    private List<Station> stationList;

    private List<String> headersPlatforms = Arrays.asList("StationName", "PlatformNO", "MainDirection", "Directions");
    private List<String> headersStations = Arrays.asList("MainName", "AcceptedNames");

    public ExcelProcessor(List<Station> stationList) {
        this.stationList = stationList;
    }


    //    protected void exportStationListToExcel
    protected void exportPlatformListToExcel() {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Platforms");

        generateHeaders(workbook, sheet, headersPlatforms);
        int rows = 1;
        for (Station station : stationList) {
            for (Platform platform : station.getPlatforms()) {
                Row row = sheet.createRow(rows);
                rows += 1;

                Cell cell0 = row.createCell(0);
                cell0.setCellValue(station.getMainName());

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(platform.getNumber());

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(platform.getMainDirection());

                Cell cell3 = row.createCell(3);
                String dirs = Utilities.listToString(platform.getDirections(), ", ");
                cell3.setCellValue(dirs);

            }
        }
        saveWorkbook(workbook, PATH_SAVE_PLATFORMS_RAW);
    }

    protected void exportStationsListToExcel() {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Stations");

        generateHeaders(workbook, sheet, headersStations);
        int rows = 1;

        for (Station station : stationList) {
            Row row = sheet.createRow(rows);
            rows += 1;

            Cell cell0 = row.createCell(0);
            cell0.setCellValue(station.getMainName());

            Cell cell3 = row.createCell(1);
            String names = Utilities.listToString(station.getAcceptedNames(), ", ");
            cell3.setCellValue(names);
        }

        saveWorkbook(workbook, PATH_SAVE_STATIONS_RAw);
    }


    private void saveWorkbook(Workbook workbook, String path) {
        try {
            FileOutputStream outputStream = new FileOutputStream(path);
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateHeaders(Workbook workbook, Sheet sheet, List<String> headers) {
        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            sheet.setColumnWidth(i, 4000);
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers.get(i));
            headerCell.setCellStyle(getHeaderStyle(workbook));
        }
    }

    private CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        return headerStyle;
    }

}

