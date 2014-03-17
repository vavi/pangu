package net.pangu.consumer.transport;

import net.pangu.consumer.Request;
import net.pangu.provider.Response;

public interface NetworkClient {

    void connect(String host, int port);

    Response sendRequestAndGetResponse(Request request);

}
