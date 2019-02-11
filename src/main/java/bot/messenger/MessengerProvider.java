package bot.messenger;

import bot.configuration.MessengerConfiguration;
import com.github.messenger4j.Messenger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessengerProvider {

    private MessengerConfiguration messengerConfiguration;

    @Autowired
    public MessengerProvider(MessengerConfiguration messengerConfiguration) {
        this.messengerConfiguration = messengerConfiguration;
    }

    @Bean
    public Messenger messenger() {
        return Messenger.create(messengerConfiguration.getAppSecret(), messengerConfiguration.getPageAccessToken(), messengerConfiguration.getVerifyToken());
    }
}
