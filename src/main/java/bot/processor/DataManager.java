package bot.processor;

import bot.externalservice.apium.ZtmDataScraper;
import bot.externalservice.general.NameProcessor;
import bot.schema.Platform;
import bot.schema.Station;
import bot.externalservice.siptw.SipTwDataCollector;
import bot.externalservice.siptw.data.PlatformRaw;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.*;

@Service
@Data
public class DataManager extends Settings {

    private Map<String, Station> stationMap;
    boolean overwriteWhenPresent = true;
    boolean fetchNewListOnEveryRun = false;
    private List<Station> ztmStationList;
    private Map<String, PlatformRaw> sipTwPlatformMap;
    private List<Station> integratedList;
    private Set<String> acceptedNamesBase;


    @Autowired
    SipTwDataCollector sipTwDataCollector = new SipTwDataCollector();

    @Autowired
    ZtmDataScraper ztmDataScraper = new ZtmDataScraper();


    public DataManager() {
//        prepareData();
//        printMap(this.stationMap);
    }

    public void prepareData() {
        fetchLists();
        integrateLists();
        generateAcceptedNames();
        exportToExcel();
        loadFromExcel();
    }


    public void fetchLists() {
        if (fetchNewListOnEveryRun || !Utilities.objectExists(PATH_LIST_ZTM)) {
            this.ztmStationList = ztmDataScraper.getZtmStationList();

        } else {
            this.ztmStationList = Utilities.deserializeObject(PATH_LIST_ZTM);
        }

        if (fetchNewListOnEveryRun || !Utilities.objectExists(PATH_MAP_SIPTW)) {
            sipTwPlatformMap = sipTwDataCollector.fetchPlatformMap();
        } else {
            this.sipTwPlatformMap = Utilities.deserializeObject(PATH_MAP_SIPTW);
        }

        if (overwriteWhenPresent) {
            Utilities.serializeObject(ztmStationList, PATH_LIST_ZTM);
            Utilities.serializeObject(sipTwPlatformMap, PATH_MAP_SIPTW);
        }
    }


    public void integrateLists() {
        integratedList = new ArrayList<>();
        for (Station station : ztmStationList) {
            String stationName = station.getMainName();
            for (Platform platform : station.getPlatforms()) {
                String number = platform.getNumber();
                String validator = stationName + " " + number;
                if (sipTwPlatformMap.containsKey(validator)) {
                    int SipTwId = Integer.valueOf(sipTwPlatformMap.get(validator).getInnerId());
                    platform.setAtSipTw(true);
                    platform.setSipTwID(SipTwId);
                }
            }
            integratedList.add(station);
        }
    }

    public void generateAcceptedNames(){
        acceptedNamesBase = new TreeSet<>();
        for(Station station: integratedList){
            String name = station.getMainName();
            NameProcessor nameProcessor = new NameProcessor(name);
            List<String> acceptedNames = nameProcessor.getAcceptedNames();
            if(!Collections.disjoint(acceptedNamesBase,acceptedNames)){
                System.out.println("Repeated names for station: "+name);
            }
            station.setAcceptedNames(acceptedNames);
            acceptedNamesBase.addAll(acceptedNames);
        }
    }

    public void exportToExcel(){
        ExcelProcessor excelProcessor = new ExcelProcessor(integratedList);
        excelProcessor.exportPlatformListToExcel();
        excelProcessor.exportStationsListToExcel();
    }
//
//        Map<String,Station> stationMap = new HashMap<>();
//        for(Station station: umStationList){
//            for(String accName : station.getAcceptedNames()){
//                stationMap.put(accName,station);
//            }
//        }
//        return stationMap;

    public void loadFromExcel(){

    }

    public static void printMap(Map<String, Station> mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String key = (String) pair.getKey();
            Station station = (Station) pair.getValue();
            String name = station.getMainName();
            System.out.println(name);

            it.remove(); // avoids a ConcurrentModificationException
        }
    }
}
