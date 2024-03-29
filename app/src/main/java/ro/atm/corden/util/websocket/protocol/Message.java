package ro.atm.corden.util.websocket.protocol;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import java.util.HashMap;
import java.util.List;

import ro.atm.corden.model.map.MapItem;
import ro.atm.corden.model.map.Mark;
import ro.atm.corden.model.map.Path;
import ro.atm.corden.model.map.Zone;
import ro.atm.corden.model.user.User;
import ro.atm.corden.util.websocket.AuthenticatedUser;
import ro.atm.corden.util.websocket.protocol.events.MediaEventType;

public class Message {
    private static final String USER = "user";
    private static final String PAYLOAD = "payload";
    private static final String DATE = "date";
    private final static String EVENT = "event";
    private static final String METHOD = "method";
    private final static String METHOD_REQUEST = "request";
    private final static String METHOD_UPDATE = "update";
    private static final String METHOD_MEDIA = "media";
    private static final String METHOD_ACTIVITY = "activity";

    private String message;

    private Message(){

    }
    public static class RequestMessageBuilder{
        private JsonObject message;

        public RequestMessageBuilder(){
            message = new JsonObject();
            message.addProperty(METHOD, METHOD_REQUEST);
            message.addProperty("token", AuthenticatedUser.getInstance().getToken());
        }

        public RequestMessageBuilder addEvent(String event){
            message.addProperty(EVENT, event);
            return this;
        }

        public RequestMessageBuilder addUser(String username){
            message.addProperty(USER, username);
            return this;
        }

        public RequestMessageBuilder addDate(String date){
            message.addProperty(DATE, date);
            return this;
        }

        public Message build(){
            Message message = new Message();
            message.message = this.message.toString();
            return message;
        }

    }

    public static class UpdateMessageBuilder{
        private JsonObject message;

        public UpdateMessageBuilder(){
            message = new JsonObject();
            message.addProperty(METHOD, METHOD_UPDATE);
            message.addProperty("token", AuthenticatedUser.getInstance().getToken());
        }

        public UpdateMessageBuilder addEvent(String event){
            message.addProperty(EVENT, event);
            return this;
        }

        public UpdateMessageBuilder addPayload(User user){
            message.addProperty(PAYLOAD, user.toJson());
            return this;
        }

        public UpdateMessageBuilder addPayload(String username){
            message.addProperty(PAYLOAD, username);
            return this;
        }

        public UpdateMessageBuilder addLocation(double lat, double lng, double accuracy){
            JsonObject location = new JsonObject();
            location.addProperty("lat", lat);
            location.addProperty("lng", lng);
            location.addProperty("accuracy", accuracy);
            message.add("payload", location);
            return this;
        }

        public UpdateMessageBuilder addPayload(HashMap<String, MapItem> mapItems){
            JsonArray marks = new JsonArray();
            JsonArray paths = new JsonArray();
            JsonArray zones = new JsonArray();

            for(String key : mapItems.keySet()){
                MapItem currentItem = mapItems.get(key);
                String json = "";
                if(currentItem instanceof Mark){
                    //json = ((Mark)currentItem).toJson();
                    json = currentItem.toJson();
                    marks.add(json);
                }
                if(currentItem instanceof Zone){
                    json = currentItem.toJson();
                    zones.add(json);
                }
                if(currentItem instanceof Path){
                    json = currentItem.toJson();
                    paths.add(json);
                }
            }
            message.add("marks", marks);
            message.add("paths", paths);
            message.add("zones", zones);
            return this;
        }
        public Message build(){
            Message message = new Message();
            message.message = this.message.toString();
            return message;
        }
    }

    public static class MediaMessageBuilder{
        private JsonObject message;

        public MediaMessageBuilder(){
            message = new JsonObject();
            message.addProperty(METHOD, METHOD_MEDIA);
            message.addProperty("token", AuthenticatedUser.getInstance().getToken());
        }

        public MediaMessageBuilder addEvent(String event){
            message.addProperty(EVENT, event);
            return this;
        }

        public MediaMessageBuilder addIceCandidate(IceCandidate iceCandidate, String iceFor){
            JsonObject candidate = new JsonObject();

            candidate.addProperty("sdpMLineIndex", iceCandidate.sdpMLineIndex);
            candidate.addProperty("sdpMid", iceCandidate.sdpMid);
            candidate.addProperty("candidate", iceCandidate.sdp);
            candidate.addProperty("iceFor", iceFor);

            message.add("candidate", candidate);
            return this;
        }

        public MediaMessageBuilder addSdpOffer(SessionDescription sessionDescription){
            if(sessionDescription.type == SessionDescription.Type.ANSWER){
                //todo
            }
            message.addProperty("sdpOffer", sessionDescription.description);

            return this;
        }

        public MediaMessageBuilder addVideoPath(String videoPath){
            message.addProperty("path", videoPath);
            return this;
        }

        public MediaMessageBuilder addUser(String username){
            message.addProperty("user", username);
            return this;
        }

        public MediaMessageBuilder addVideoPosition (Long position){
            message.addProperty("position", position);
            return this;
        }

        public Message build(){
            Message message = new Message();
            message.message = this.message.toString();
            return message;
        }
    }

    public static class SubscribeMessageBuilder{
        private JsonObject message;

        public SubscribeMessageBuilder(){
            message = new JsonObject();
            message.addProperty(METHOD, "subscribe");
            message.addProperty("token", AuthenticatedUser.getInstance().getToken());
        }

        public SubscribeMessageBuilder addEvent(String event){
            message.addProperty(EVENT, event);
            return this;
        }

        public Message build(){
            Message message = new Message();
            message.message = this.message.toString();
            return message;
        }
    }

    public static class UnsubscribeMessageBuilder{
        private JsonObject message;

        public UnsubscribeMessageBuilder(){
            message = new JsonObject();
            message.addProperty(METHOD, "unsubscribe");
            message.addProperty("token", AuthenticatedUser.getInstance().getToken());
        }

        public UnsubscribeMessageBuilder addEvent(String event){
            message.addProperty(EVENT, event);
            return this;
        }

        public Message build(){
            Message message = new Message();
            message.message = this.message.toString();
            return message;
        }
    }

    public static class ActivityMessageBuilder{
        private JsonObject message;

        public ActivityMessageBuilder(){
            message = new JsonObject();
            message.addProperty(METHOD, METHOD_ACTIVITY);
            message.addProperty("token", AuthenticatedUser.getInstance().getToken());
        }

        public ActivityMessageBuilder addEvent(String event){
            message.addProperty(EVENT, event);
            return this;
        }

        public ActivityMessageBuilder addPrecision(int precision){
            message.addProperty("precision", precision);
            return this;
        }

        public Message build(){
            Message message = new Message();
            message.message = this.message.toString();
            return message;
        }
    }
    @NonNull
    @Override
    public String toString() {
        return message;
    }
}
