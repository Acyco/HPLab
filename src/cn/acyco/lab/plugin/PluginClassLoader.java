package cn.acyco.lab.plugin;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Acyco
 * @create 2020-01-29 04:46
 */
public class PluginClassLoader extends URLClassLoader {
    public PluginClassLoader(URL[] urls) {
        super(urls);
    }
}
