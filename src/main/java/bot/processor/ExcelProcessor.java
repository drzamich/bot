package bot.processor;

import bot.Settings;
import bot.schema.Platform;
import bot.schema.Station;
import lombok.Data;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

public class ExcelProcessor {
    private List<Station> stationList;

    private List<String> headersPlatforms = Arrays.asList("StationName", "PlatformNO", "MainDirection", "Directions");
    private List<String> headersStations = Arrays.asList("MainName", "AcceptedNames");

    private Map<String, List<String>> customAcceptedNames;
    private Map<String, TransferPlatform> platformDirections;

    public ExcelProcessor(List<Station> stationList) {
        this.stationList = stationList;
        doWork();
    }

    private void doWork() {
//        exportPlatformListToExcel();
//        exportStationsListToExcel();
        this.customAcceptedNames = loadCustomAcceptedNames();
        this.platformDirections = loadDirections();
        integrateStationList();
    }

    protected List<Station> getIntegratedList() {
        return this.stationList;
    }

    private void integrateStationList() {
        for (int i = 0; i < this.stationList.size(); i++) {
            Station s = this.stationList.get(i);
            if (this.customAcceptedNames.containsKey(s.getMainName())) {
                List<String> accNames = s.getAcceptedNames();
                accNames.addAll(this.customAcceptedNames.get(s.getMainName()));
                s.setAcceptedNames(accNames);
            }

            for (int j = 0; j < s.getPlatforms().size(); j++) {
                Platform p = s.getPlatforms().get(j);
                String identifier = s.getMainName() + " " + p.getNumber();
                if (this.platformDirections.containsKey(identifier)) {
                    TransferPlatform tp = this.platformDirections.get(identifier);
                    p.setMainDirection(tp.getMainDir());
                    List<String> dirs = p.getDirections();
                    dirs.addAll(tp.getOtherDirs());
                    p.setDirections(dirs);
                }
            }
        }
    }

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
                String dirs = String.join(", ", platform.getDirections());
                cell3.setCellValue(dirs);

            }
        }
        saveWorkbook(workbook, Settings.PATH_EXCEL_PLATFORMS_RAW);
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
            String names = String.join(", ", station.getAcceptedNames());
            cell3.setCellValue(names);
        }

        saveWorkbook(workbook, Settings.PATH_EXCEL_STATIONS_RAw);
    }

    protected Map<String, List<String>> loadCustomAcceptedNames() {
        Map<String, List<String>> res = new HashMap<>();
        Iterator<Row> rowIterator = getRowsOfExcelFile(Settings.PATH_ACCEPTED_NAMES_CUSTOM);

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            String stationName = row.getCell(0).getStringCellValue();
            Optional<Cell> cell = Optional.ofNullable(row.getCell(2));

            if (cell.isPresent()) {
                String accNames = cell.get().getStringCellValue();
                List<String> accNamesList = Arrays.asList(accNames.split(", "));
                res.put(stationName, accNamesList);
            }

        }
        return res;
    }

    private Map<String, TransferPlatform> loadDirections() {
        Map<String, TransferPlatform> res = new HashMap<>();
        Iterator<Row> rowIterator = getRowsOfExcelFile(Settings.PATH_DIRECTIONS_CUSTOM);

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            String stationName = row.getCell(0).getStringCellValue();
            String platformNum = row.getCell(1).getStringCellValue();
            String platformIdentifier = stationName + " " + platformNum;

            String mainDirOld;
            String mainDirNew = "";
            List<String> otherDirNew = new ArrayList<>();

            Optional<Cell> mainDirOldCell = Optional.ofNullable(row.getCell(2));
            Optional<Cell> mainDirNewCell = Optional.ofNullable(row.getCell(3));
            Optional<Cell> otherDirNewCell = Optional.ofNullable(row.getCell(4));


            if (mainDirNewCell.isPresent()) {
                mainDirNew = mainDirNewCell.get().getStringCellValue();

                if (mainDirOldCell.isPresent()) {
                    mainDirOld = mainDirOldCell.get().getStringCellValue();
                    otherDirNew.add(mainDirNew);
                }
            }

            if (otherDirNewCell.isPresent()) {
                otherDirNew.addAll(Arrays.asList(otherDirNewCell.get().getStringCellValue().split((", "))));
            }

            if (!otherDirNew.isEmpty()) {
                res.put(platformIdentifier, new TransferPlatform(mainDirNew, otherDirNew));
            }

        }
        return res;
    }

    private Iterator<Row> getRowsOfExcelFile(String filepath) {
        try {
            File file = new File(filepath);
            FileInputStream fis = new FileInputStream(file);

            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            return sheet.iterator();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not import the fileExists");
            return null;
        }
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

    @Data
    private class TransferPlatform {
        private String mainDir;
        private List<String> otherDirs;

        public TransferPlatform(String mainDir, List<String> otherDirs) {
            this.mainDir = mainDir;
            this.otherDirs = otherDirs;
        }
    }
}

