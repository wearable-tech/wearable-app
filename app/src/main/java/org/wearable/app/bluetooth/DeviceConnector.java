package org.wearable.app.bluetooth;

public interface DeviceConnector {
    void connect();
    void disconnect();
    void sendMessage(CharSequence chars);
}
