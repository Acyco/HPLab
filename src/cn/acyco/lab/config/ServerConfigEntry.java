package cn.acyco.lab.config;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * @author Acyco
 * @create 2020-01-28 18:02
 */
public class ServerConfigEntry<T> extends ConfigEntry<T> {
    @SerializedName("user_name")
    protected String name;
    protected String jar;
    protected String work_dir;
    protected String run_cmd;
    protected boolean enable;

    public ServerConfigEntry(T value, String name, String jar, String work_dir,String run_cmd,boolean enable) {
        super(value);
        this.name = name==null||name.equals("")?"unknow name":name;
        this.jar = jar==null||jar.equals("")?"unknow jar":jar;
        this.work_dir = work_dir==null||work_dir.equals("")?"unknow work_dir":work_dir;
        this.run_cmd = run_cmd==null||run_cmd.equals("")?"unknow run_cmd":run_cmd;
        this.enable = enable;
        
    }

    protected ServerConfigEntry(T value, JsonObject jsonObject) {
        
        super(value, jsonObject);
       name = jsonObject.has("name")?name:"unknow name";
       jar = jsonObject.has("jar")?jar:"unknow jar";
       work_dir = jsonObject.has("work_dir")?work_dir:"unknow work_dir";
        run_cmd = jsonObject.has("run_cmd")?run_cmd:"unknow run_cmd";
        enable = jsonObject.has("enable")?enable:false;
    }
    public String getName() {
        return name;
    }

    public String getJar() {
        return jar;
    }

    public String getWork_dir() {
        return work_dir;
    }

    public String getRun_cmd() {
        return run_cmd;
    }

    public boolean isEnable() {
        return enable;
    }

    @Override
    protected void onSerialization(JsonObject data) {
        System.out.println("onSerialization");
        data.addProperty("name",this.name);
        data.addProperty("jar", this.jar);
        data.addProperty("work_dir",this.work_dir);;
        data.addProperty("run_cmd",this.run_cmd);
    }
}
