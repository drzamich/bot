package bot.processor;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Data
public class QueryProcessor {


//    @Autowired
//    SipTwProcessor sipTwProcessor;
//
//    @Autowired
//    ApiUmProcessor apiUmProcessor = new ApiUmProcessor();

    private Query query;
    private Response response;

    //@Autowired
    TimetableProcessor timetableProcessor = new TimetableProcessor();


    public void processQuery(String msg){
        msg = Utilities.parseInput(msg);
        this.query = new Query(msg);

        if(query.isSettingsQuery()){

        }
        else{
            timetableProcessor.processQuery(this.query);
        }
//
//        if(query.isToSipTw()){
//            sipTwProcessor.processQuery(this.query);
//        }
//
//        if(query.isToApiUm()){
//            response = apiUmProcessor.processQuery(this.query);
//        }
//
//        response.prepareMsg();
//        for(String s : response.getMessages()){
//            System.out.println(s);
//        }
    }
}
