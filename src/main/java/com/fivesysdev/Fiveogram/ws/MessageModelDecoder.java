package com.fivesysdev.Fiveogram.ws;

import com.fivesysdev.Fiveogram.models.MessageModel;
import com.google.gson.Gson;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageModelDecoder implements Decoder.Text<MessageModel> {
    
    Gson gson = new Gson();

    @Override
    public MessageModel decode(String s) {
        return gson.fromJson(s, MessageModel.class);
    }

    @Override
    public boolean willDecode(String s) {
        return s != null;
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

}
