package bot.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PlatformService {

    public List<String> getExcludedPlatformNames() {
        return Collections.emptyList();
    }

    public List<String> getExcludedStationIDs() {
        return Collections.emptyList();
    }
}
