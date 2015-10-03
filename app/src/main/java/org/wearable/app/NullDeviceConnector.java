package org.wearable.app;

public class NullDeviceConnector implements DeviceConnector {

    @Override
    public void connect() {
        // Do nothing
    }

    @Override
    public void disconnect() {
        // Do nothing
    }

    @Override
    public void sendMessage(CharSequence chars) {
        // Do nothing
    }
}
