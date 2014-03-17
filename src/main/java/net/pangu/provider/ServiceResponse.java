package net.pangu.provider;

public class ServiceResponse implements Response {

    private static final long serialVersionUID = -8551517393493336551L;
    private long id;
    private Object result;

    public long getId() {
	return this.id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public Object getResult() {
	return result;
    }

    public void setResult(Object result) {
	this.result = result;
    }

}
