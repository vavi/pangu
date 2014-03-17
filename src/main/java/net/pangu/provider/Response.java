package net.pangu.provider;

import java.io.Serializable;

public interface Response extends Serializable {
    public long getId();

    public void setId(long id);

    public Object getResult();

    public void setResult(Object result);
}
