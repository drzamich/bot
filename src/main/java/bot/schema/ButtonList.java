package bot.schema;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ButtonList {
    private List<Button> buttonList;
    private String hintMessage;

    public ButtonList(List<Station> stations, String hintMessage) {
        this.hintMessage = hintMessage;
        this.buttonList = stations
                .stream()
                .map(s -> new Button(s.getMainName(), s.getMainName()))
                .collect(Collectors.toList());
    }

    public ButtonList(Station s, String hintMessage) {
        this.hintMessage = hintMessage;
        this.buttonList = s.getPlatforms().stream()
                .map(p -> new Button(p.getNumber() + " (" + p.getMainDirection()+")",
                        s.getMainName() + " " + p.getNumber()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return buttonList
                .stream()
                .map(Button::getTextVisible)
                .collect(Collectors.joining());
    }

    //
//
//    private void createButtonsMsg(Station s) {
//        List<Button> buttons = prepareButtons(s);
//        for (Button button : buttons) {
//            messages.add(button.toString());
//        }
//    }
//
//    private void createButtonsMsg(List<Station> stations) {
//        List<Button> buttons = prepareButtons(stations);
//        for (Button button : buttons) {
//            messages.add(button.toString());
//        }
//    }

}