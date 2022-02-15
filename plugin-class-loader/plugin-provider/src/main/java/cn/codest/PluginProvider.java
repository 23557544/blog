package cn.codest;

import org.apache.commons.lang3.StringUtils;

public class PluginProvider {

    public void test() {
        // 获取当前的类加载器
        System.out.println("Plugin: " + this.getClass().getClassLoader());
        // 获取类全路径
        System.out.println("Plugin: " + StringUtils.class.getResource("").getPath());
    }

}
