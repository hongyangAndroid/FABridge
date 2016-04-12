package com.zhy.fabridge.lib;

/**
 * Created by zhy on 16/2/21.
 */
public interface FabridgeProxy<T>
{
    void call(T source, String id, Object... params);
}
