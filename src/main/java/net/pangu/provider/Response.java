package net.pangu.provider;

import java.io.Serializable;

public interface Response extends Serializable {
    public Long getId();

    public void setId(Long id);

    public Object getResult();

    public void setResult(Object result);
}
