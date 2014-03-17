package net.pangu.consumer;

public class ServiceRequest implements Request {

    private static final long serialVersionUID = -9082069612614937789L;

    private Long id;
    private String interfaceName;

    private Class<?>[] parameterTypes;
    private Class<?> returnType;
    private String methodName;
    private Object[] args;

    @Override
    public Long getId() {
	return id;
    }

    @Override
    public void setId(Long id) {
	this.id = id;
    }

    @Override
    public Class<?>[] getParameterTypes() {
	return parameterTypes;
    }

    @Override
    public void setParameterTypes(Class<?>[] parameterTypes) {
	this.parameterTypes = parameterTypes;
    }

    @Override
    public Class<?> getReturnType() {
	return returnType;
    }

    @Override
    public void setReturnType(Class<?> returnType) {
	this.returnType = returnType;
    }

    @Override
    public String getMethodName() {
	return methodName;
    }

    @Override
    public void setMethodName(String methodName) {
	this.methodName = methodName;
    }

    @Override
    public Object[] getArgs() {
	return args;
    }

    @Override
    public void setArgs(Object[] args) {
	this.args = args;
    }

    @Override
    public String getInterfaceName() {
	return interfaceName;
    }

    @Override
    public void setInterfaceName(String interfaceName) {
	this.interfaceName = interfaceName;
    }
}
