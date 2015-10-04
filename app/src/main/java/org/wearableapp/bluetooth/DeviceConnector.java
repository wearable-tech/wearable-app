package org.wearableapp.bluetooth;

public interface DeviceConnector {
    void connect();
    void disconnect();
    void sendMessage(CharSequence chars);
}
