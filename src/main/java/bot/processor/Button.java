package bot.processor;

import lombok.Data;

@Data
public class Button {
    private String textVisible;
    private String textHidden;

    public Button(String textVisible, String textHidden) {
        this.textVisible = textVisible;
        this.textHidden = textHidden;
    }

    @Override
    public String toString() {
        return this.textVisible;
    }
}
