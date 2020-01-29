package cn.acyco.lab.config;

import com.google.gson.JsonObject;

/**
 * @author Acyco
 * @create 2020-01-28 17:49
 */
public class ConfigEntry<T> {
    private final T value;
    


    public ConfigEntry(T value) {
        this.value = value;
    }
    protected ConfigEntry(T value,JsonObject jsonObject) {
        this.value = value;
    }

    T getValue() {
        return this.value;
    }

    protected void onSerialization(JsonObject data)
    {
    }
}
