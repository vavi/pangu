package net.pangu.consumer;

import java.io.Serializable;

public interface Request extends Serializable {
    public Long getId();

    public void setId(Long id);

    public String getInterfaceName();

    public void setInterfaceName(String interfaceName);

    public Class<?>[] getParameterTypes();

    public void setParameterTypes(Class<?>[] parameterTypes);

    public Class<?> getReturnType();

    public void setReturnType(Class<?> returnType);

    public String getMethodName();

    public void setMethodName(String methodName);

    public Object[] getArgs();

    public void setArgs(Object[] args);
}
