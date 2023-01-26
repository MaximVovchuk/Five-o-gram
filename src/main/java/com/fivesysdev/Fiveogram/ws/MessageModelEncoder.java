
package com.fivesysdev.Fiveogram.ws;

import com.fivesysdev.Fiveogram.models.MessageModel;
import com.google.gson.Gson;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;


public class MessageModelEncoder implements Encoder.Text<MessageModel> {

    Gson gson = new Gson();

    @Override
    public String encode(MessageModel message) {
        return gson.toJson(message);
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

}
