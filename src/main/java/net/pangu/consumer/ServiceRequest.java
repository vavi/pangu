package net.pangu.consumer;

public class ServiceRequest implements Request {

    private static final long serialVersionUID = -9082069612614937789L;

    private long id;
    private String interfaceName;

    private Class<?>[] parameterTypes;
    private Class<?> returnType;
    private String methodName;
    private Object[] args;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Class<?>[] getParameterTypes() {
	return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
	this.parameterTypes = parameterTypes;
    }

    public Class<?> getReturnType() {
	return returnType;
    }

    public void setReturnType(Class<?> returnType) {
	this.returnType = returnType;
    }

    public String getMethodName() {
	return methodName;
    }

    public void setMethodName(String methodName) {
	this.methodName = methodName;
    }

    public Object[] getArgs() {
	return args;
    }

    public void setArgs(Object[] args) {
	this.args = args;
    }

    public String getInterfaceName() {
	return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
	this.interfaceName = interfaceName;
    }
}
