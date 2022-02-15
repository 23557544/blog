package cn.codest.pluginboot;

import java.net.URL;
import java.net.URLClassLoader;

public class PluginClassLoader extends URLClassLoader {
    public PluginClassLoader(URL[] urls) {
        // 类加载器的双亲委派机制
        // 先使用父加载器加载class，加载不到时再调用findClass方法
        super(urls, null);
    }
}
