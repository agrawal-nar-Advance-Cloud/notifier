package com.webapp.notifier.controller;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.webapp.notifier.config.DoublyLinkedList;
import com.webapp.notifier.model.Notification;
import com.webapp.notifier.service.NotificationService;
import io.prometheus.client.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;



import java.util.*;


@Controller
public class ConsumerController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    DoublyLinkedList info;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    static final Counter consumedCount = Counter.build().name("message_consumed_total_weather").labelNames("topic").help("Total message consumed on weather topic.").register();

    @KafkaListener(topics = "weather", groupId = "notifier", containerFactory = "weatherKafkaListenerFactory" )
    public void consumeJson(String weatherInfo) {
        consumedCount.labels("weather").inc();
        info.addNode(weatherInfo);
        processData(info.getFirstNode());
    }

    public void processData(String data) {

        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        Long watchID = jsonObject.get("watchID").getAsLong();
        JsonArray alertArray = jsonObject.getAsJsonArray("alerts");
        JsonObject weatherElement = jsonObject.getAsJsonObject("main");

        log.info("Watch consumed, TOPIC:weather, WatchID:"+watchID);

        for (int i = 0; i < alertArray.size(); i++) {

            String fieldType = alertArray.get(i).getAsJsonObject().get("fieldType").getAsString();
            String operator = alertArray.get(i).getAsJsonObject().get("operator").getAsString();
            Long alertID = alertArray.get(i).getAsJsonObject().get("alertID").getAsLong();
            int alertValue = alertArray.get(i).getAsJsonObject().get("value").getAsInt();
            int value = weatherElement.get(fieldType).getAsInt();

            Notification notification = notificationService.getNotificationByID(alertID);
            if(notification==null) {
                Notification n = new Notification(alertID,watchID);
                n.setValue(data);
                notification(n,alertValue,value,operator);
            }else if(notification.getTime() < (new Date().getTime())) {
                notification.setValue(data);
                notification(notification,alertValue,value,operator);
            }else if(!(notification.getStatus().equals("ALERT_IGNORED_THRESHOLD_REACHED"))){
                notification.setStatus("ALERT_IGNORED_THRESHOLD_REACHED");
                notification.setValue(data);
                notificationService.save(notification);
            }
        }
    }

    public void notification(Notification notification,  int alertValue,  int value, String operator){

        switch(operator){
            case "gt":
                if(value>alertValue) {
                    notification.setStatus("ALERT_SEND");
                    notification.setTime(new Date().getTime() + 3600000);
                }else{
                    notification.setStatus("ALERT_IGNORED_DUPLICATE");
                    notification.setTime(0l);
                }
                break;
            case "gte":
                if(value>=alertValue) {
                    notification.setStatus("ALERT_SEND");
                    notification.setTime(new Date().getTime() + 3600000);
                }else{
                    notification.setStatus("ALERT_IGNORED_DUPLICATE");
                    notification.setTime(0l);
                }
                break;
            case "eq":
                if(value==alertValue) {
                    notification.setStatus("ALERT_SEND");
                    notification.setTime(new Date().getTime() + 3600000);
                }else{
                    notification.setStatus("ALERT_IGNORED_DUPLICATE");
                    notification.setTime(0l);
                }
                break;
            case "lt":
                if(value<alertValue) {
                    notification.setStatus("ALERT_SEND");
                    notification.setTime(new Date().getTime() + 3600000);
                }else{
                    notification.setStatus("ALERT_IGNORED_DUPLICATE");
                    notification.setTime(0l);
                }
                break;
            case "lte":
                if(value<=alertValue) {
                    notification.setStatus("ALERT_SEND");
                    notification.setTime(new Date().getTime() + 3600000);
                }else{
                    notification.setStatus("ALERT_IGNORED_DUPLICATE");
                    notification.setTime(0l);
                }
                break;
        }

        notificationService.save(notification);

    }


}
