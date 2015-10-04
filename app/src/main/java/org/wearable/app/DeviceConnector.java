package org.wearable.app;

public interface DeviceConnector {
    void connect();
    void disconnect();
    void sendMessage(CharSequence chars);
}
