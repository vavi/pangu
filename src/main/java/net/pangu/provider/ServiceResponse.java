package net.pangu.provider;

public class ServiceResponse implements Response {

    private static final long serialVersionUID = -8551517393493336551L;
    private Long id;
    private Object result;

    @Override
    public Long getId() {
	return this.id;
    }

    @Override
    public void setId(Long id) {
	this.id = id;
    }

    @Override
    public Object getResult() {
	return result;
    }

    @Override
    public void setResult(Object result) {
	this.result = result;
    }

}
