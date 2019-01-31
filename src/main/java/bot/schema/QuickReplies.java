package bot.schema;

import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.quickreply.TextQuickReply;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class QuickReplies {
    private List<QuickReply> quickReplyList;
    private String hintMessage;

    public QuickReplies(List<Station> stations, String hintMessage) {
        this.hintMessage = hintMessage;
        this.quickReplyList = stations
                .stream()
                .map(s-> TextQuickReply.create(s.getMainName(), s.getMainName()))
                .collect(Collectors.toList());
    }

    public QuickReplies(Station s, String hintMessage) {
        this.hintMessage = hintMessage;
        this.quickReplyList = s.getPlatforms().stream()
                .map(p -> TextQuickReply.create(p.getNumber() + " (" + p.getMainDirection()+")",
                        s.getMainName() + " " + p.getNumber()))
                .collect(Collectors.toList());
    }

    public QuickReplies(String hintMessage, String stationName, String platformNumber) {
        this.hintMessage = hintMessage;
        this.quickReplyList = Arrays.asList(TextQuickReply.create("Refresh",stationName+" "+platformNumber));
    }

}