package com.wiatec.btv_launcher.ws;

import com.px.common.utils.Logger;
import com.px.common.utils.NetUtil;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.F;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by patrick on 08/01/2018.
 * create time : 2:45 PM
 */

public class SocketTask implements Runnable {

    @Override
    public void run() {
        do{
            if(NetUtil.isConnected()) {
                createWebSocket();
            }
        } while (!NetUtil.isConnected());
    }

    private void createWebSocket() {
        try {
            String mac = (String) SPUtil.get(F.sp.ethernet_mac, "default");
            final WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://172.27.0.106:8080/panel/socket/" + mac)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Logger.d("ws: onOpen");
                }

                @Override
                public void onMessage(String message) {
                    Logger.d("ws: " + message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Logger.d("ws: onClose");
                    do {
                        if(NetUtil.isConnected()) {
                            createWebSocket();
                        }
                    }while (!NetUtil.isConnected());
                }

                @Override
                public void onError(Exception ex) {
                    Logger.e("ws: " + ex.getMessage());
                }
            };
            webSocketClient.connect();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }
}
