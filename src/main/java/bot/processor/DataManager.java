package bot.processor;

import bot.externalservice.apium.ZtmDataScraper;
import bot.externalservice.siptw.schema.PlatformSipTw;
import bot.schema.Platform;
import bot.schema.Station;
import bot.externalservice.siptw.SipTwDataCollector;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Getter
@Setter
@Service
public class DataManager extends Settings {

    private Map<String, Station> stationMap;
    private boolean overwriteWhenPresent = true;
    private boolean fetchNewListOnEveryRun = false;
    private List<Station> ztmStationList;
    private Map<String, PlatformSipTw> sipTwPlatformMap;
    private List<Station> integratedList = new ArrayList<>();
    private List<Station> finalList;
    private Map<String, Station> finalStationMap;
    private Set<String> acceptedNamesBase;

    private boolean loadNewData = false;
    private boolean reloadExistingData = true;

    @Autowired
    private SipTwDataCollector sipTwDataCollector;

    @Autowired
    private ZtmDataScraper ztmDataScraper;

    public DataManager() {
    }

    Map<String, Station> getFinalMap() {
        if (!Utilities.objectExists(PATH_FINAL_MAP)) {
            prepareData();
        }
        return Utilities.deserializeObject(PATH_FINAL_MAP);
    }

    public void prepareData() {
        if (reloadExistingData) {
            fetchLists();
            integrateLists();
            generateAcceptedNames();
            saveIntegratedList();
        }
        loadIntegratedList();
        processInExcel();
        convertStationListToMap();
    }

    private void fetchLists() {
        if (loadNewData || !Utilities.objectExists(PATH_LIST_ZTM)) {
            this.ztmStationList = ztmDataScraper.getZtmStationList();
            Utilities.serializeObject(ztmStationList, PATH_LIST_ZTM);
        } else {
            this.ztmStationList = Utilities.deserializeObject(PATH_LIST_ZTM);
        }

        if (loadNewData || !Utilities.objectExists(PATH_MAP_SIPTW)) {
            sipTwPlatformMap = sipTwDataCollector.fetchPlatformMap();
            Utilities.serializeObject(sipTwPlatformMap, PATH_MAP_SIPTW);
        } else {
            this.sipTwPlatformMap = Utilities.deserializeObject(PATH_MAP_SIPTW);
        }

        if (overwriteWhenPresent) {
            Utilities.serializeObject(ztmStationList, PATH_LIST_ZTM);
            Utilities.serializeObject(sipTwPlatformMap, PATH_MAP_SIPTW);
        }
    }

    private void integrateLists() {
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

    private void generateAcceptedNames() {
        acceptedNamesBase = new TreeSet<>();
        for (Station station : integratedList) {
            String name = station.getMainName();
            NameProcessor nameProcessor = new NameProcessor(name);
            List<String> acceptedNames = nameProcessor.getAcceptedNames();
            if (!Collections.disjoint(acceptedNamesBase, acceptedNames)) {
                System.out.println("Repeated names for station: " + name);
            }
            station.setAcceptedNames(acceptedNames);
            acceptedNamesBase.addAll(acceptedNames);
        }
    }

    private void saveIntegratedList() {
        Utilities.serializeObject(integratedList, PATH_INTEGRATED_LIST);
    }

    private void loadIntegratedList() {
        if (integratedList.size() <= 1) {
            integratedList = Utilities.deserializeObject(PATH_INTEGRATED_LIST);
        }
    }

    private void processInExcel() {
        ExcelProcessor excelProcessor = new ExcelProcessor(integratedList);
        this.finalList = excelProcessor.getIntegratedList();
        Utilities.serializeObject(this.finalList, PATH_FINAL_LIST);
    }

    private void convertStationListToMap() {
        Map<String, Station> res = new HashMap<>();
        for (Station s : this.finalList) {
            for (String accName : s.getAcceptedNames()) {
                res.put(accName, s);
            }
        }
        Utilities.serializeObject(res, PATH_FINAL_MAP);
    }
}
