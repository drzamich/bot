package bot.schema;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Button {
    private String textVisible;
    private String textHidden;

    @Override
    public String toString() {
        return this.textVisible;
    }
}
