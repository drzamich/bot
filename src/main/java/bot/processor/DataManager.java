package bot.processor;

import bot.Settings;
import bot.externalservice.siptw.response.SipTwPlatform;
import bot.externalservice.ztm.ZtmScraper;
import bot.schema.Platform;
import bot.schema.Station;
import bot.externalservice.siptw.SipTwDataCollector;
import bot.utils.FileHelper;
import bot.utils.NameProcessor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Getter
@Setter
@Service
@Slf4j
public class DataManager {

    private Map<String, Station> stationMap;
    private boolean overwriteWhenPresent = true;
    private boolean fetchNewListOnEveryRun = false;
    private List<Station> ztmStationList;
    private Map<String, SipTwPlatform> sipTwPlatformMap; // powinno tutaj być już generyczne Platform, nie SipTwPlatform - TODO
    private List<Station> integratedList = new ArrayList<>();
    private List<Station> finalList;
    private Map<String, Station> finalStationMap;
    private Set<String> acceptedNamesBase;

    private boolean loadNewData = false;
    private boolean reloadExistingData = true;

    private SipTwDataCollector sipTwDataCollector;

    private ZtmScraper ztmDataScraper;

    @Autowired
    public DataManager(SipTwDataCollector sipTwDataCollector, @Qualifier("productionZtm") ZtmScraper ztmDataScraper) {
        this.sipTwDataCollector = sipTwDataCollector;
        this.ztmDataScraper = ztmDataScraper;
    }

    Map<String, Station> getFinalMap() {
        if (!FileHelper.fileExists(Settings.PATH_FINAL_MAP)) {
            prepareData();
        }
        return FileHelper.deserializeObject(Settings.PATH_FINAL_MAP);
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
        if (loadNewData || !FileHelper.fileExists(Settings.PATH_LIST_ZTM)) {
//            this.ztmStationList = ztmDataScraper.getZtmStationList();
            FileHelper.serializeObject(ztmStationList, Settings.PATH_LIST_ZTM);
        } else {
            this.ztmStationList = FileHelper.deserializeObject(Settings.PATH_LIST_ZTM);
        }
        if (loadNewData || !FileHelper.fileExists(Settings.PATH_MAP_SIPTW)) {
            sipTwPlatformMap = sipTwDataCollector.fetchPlatformMap();
            FileHelper.serializeObject(sipTwPlatformMap, Settings.PATH_MAP_SIPTW);
        } else {
            this.sipTwPlatformMap = FileHelper.deserializeObject(Settings.PATH_MAP_SIPTW);
        }
        if (overwriteWhenPresent) {
            FileHelper.serializeObject(ztmStationList, Settings.PATH_LIST_ZTM);
            FileHelper.serializeObject(sipTwPlatformMap, Settings.PATH_MAP_SIPTW);
        }
    }

    private void integrateLists() {
        for (Station station : ztmStationList) {
            String stationName = station.getMainName();
            try {
                for (Platform platform : station.getPlatforms()) {
                    String number = platform.getNumber();
                    String validator = stationName + " " + number;
                    if (sipTwPlatformMap.containsKey(validator)) {
                        int sipTwId = sipTwPlatformMap.get(validator).getPlatformId(); //TODO czy innerId nie było jednak krytyczne?
                        platform.setAtSipTw(true);
                        platform.setSipTwID(sipTwId);
                    }
                }
                integratedList.add(station);
            } catch (Exception e) {
                System.out.println("No platforms for station:" + stationName);
            }
        }
    }

    private void generateAcceptedNames() {
        acceptedNamesBase = new TreeSet<>();
        for (Station station : integratedList) {
            String name = station.getMainName();
            Set<String> acceptedNames = NameProcessor.generateAcceptedNames(name);
            if (!Collections.disjoint(acceptedNamesBase, acceptedNames)) {
                log.debug("Repeated names for station: " + name);
            }
            station.setAcceptedNames(acceptedNames);
            acceptedNamesBase.addAll(acceptedNames);
        }
    }

    private void saveIntegratedList() {
        FileHelper.serializeObject(integratedList, Settings.PATH_INTEGRATED_LIST);
    }

    private void loadIntegratedList() {
        if (integratedList.isEmpty()) {
            integratedList = FileHelper.deserializeObject(Settings.PATH_INTEGRATED_LIST);
        }
    }

    private void processInExcel() {
        ExcelProcessor excelProcessor = new ExcelProcessor(integratedList);
        this.finalList = excelProcessor.getIntegratedList();
        FileHelper.serializeObject(this.finalList, Settings.PATH_FINAL_LIST);
    }

    private void convertStationListToMap() {
        Map<String, Station> res = new HashMap<>();
        for (Station s : this.finalList) {
            for (String accName : s.getAcceptedNames()) {
                res.put(accName, s);
            }
        }
        FileHelper.serializeObject(res, Settings.PATH_FINAL_MAP);
    }
}
