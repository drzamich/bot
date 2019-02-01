package bot.schema;

import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.quickreply.TextQuickReply;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class QuickReplies {
    private List<QuickReply> quickReplyList;
    private String hintMessage;
    private List<String> payloadList = new ArrayList<>(); //this is to be saved separately in order to create console info

    public QuickReplies(List<Station> stations, String hintMessage) {
        this.hintMessage = hintMessage;
        this.quickReplyList = stations
                .stream()
                .peek(s-> payloadList.add(s.getMainName()))
                .map(s -> TextQuickReply.create(s.getMainName(), s.getMainName()))
                .collect(Collectors.toList());
    }

    public QuickReplies(Station s, String hintMessage) {
        this.hintMessage = hintMessage;
        this.quickReplyList = s.getPlatforms()
                .stream()
                .peek(p -> payloadList.add(p.getNumber() + " (" + p.getMainDirection() + ")"))
                .map(p -> TextQuickReply.create(p.getNumber() + " (" + p.getMainDirection() + ")",
                        s.getMainName() + " " + p.getNumber()))
                .collect(Collectors.toList());
    }

    public QuickReplies(String hintMessage, String stationName, String platformNumber) {
        this.hintMessage = hintMessage;
        this.quickReplyList = Collections.singletonList(TextQuickReply.create("Refresh", stationName +
                                                                                    " " + platformNumber));
        this.payloadList.add("Refresh");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.hintMessage);
        payloadList
                .forEach(q -> sb.append((System.getProperty("line.separator") + q)));
        return sb.toString();
    }
}