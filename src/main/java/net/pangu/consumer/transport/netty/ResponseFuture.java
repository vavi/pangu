package net.pangu.consumer.transport.netty;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.pangu.consumer.Request;
import net.pangu.provider.Response;

public class ResponseFuture implements Serializable {

    private static final long serialVersionUID = 8853860402670011858L;

    private static final Map<Long, ResponseFuture> futures = new ConcurrentHashMap<Long, ResponseFuture>();

    private final Lock lock = new ReentrantLock();

    private final Condition done = lock.newCondition();

    private Response response;

    private final Request request;

    public ResponseFuture(Request request) {
	this.request = request;
	futures.put(request.getId(), this);
    }

    public Response get() {
	lock.lock();
	try {
	    // TODO add timeout
	    while (!isDone()) {
		done.await();
	    }
	} catch (InterruptedException e) {
	    throw new RuntimeException(e);
	} finally {
	    lock.unlock();
	}

	return response;
    }

    public static void doSingalWhenReceivedServerResponse(Response response) {
	ResponseFuture future = futures.remove(response.getId());
	if (future != null) {
	    future.assginResponseAndDoSignal(response);
	}

    }

    private boolean isDone() {
	return this.response != null;
    }

    private void assginResponseAndDoSignal(Response response) {
	lock.lock();
	try {
	    this.response = response;
	    done.signal();
	} finally {
	    lock.unlock();
	}

    }

    public Request getRequest() {
	return request;
    }
}