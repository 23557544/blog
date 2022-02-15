package cn.codest.pluginboot;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.net.URLClassLoader;

@Component
public class PluginTester {

    @PostConstruct
    public void test() {
        // 打印当前类加载器
        System.out.println("Boot: " + this.getClass().getClassLoader());
        // 获取StringUtils的类全路径
        System.out.println("Boot: " + StringUtils.class.getResource("").getPath());
        // 模拟调用插件包
        testPlugin();
    }

    public void testPlugin() {
        try {
            // 加载插件包
            ClassPathResource resource = new ClassPathResource("plugin/plugin-provider.jar");
            // 打印插件包路径
            System.out.println(resource.getURL().getPath());

//            URLClassLoader classLoader = new URLClassLoader(new URL[]{resource.getURL()});
            // 初始化自己的ClassLoader
            PluginClassLoader pluginClassLoader = new PluginClassLoader(new URL[]{resource.getURL()});
            // 这里需要临时更改当前线程的 ContextClassLoader
            // 避免中间件代码中存在Thread.currentThread().getContextClassLoader()获取类加载器
            // 因为它们会获取当前线程的 ClassLoader 来加载 class，而当前线程的ClassLoader极可能是App ClassLoader而非自定义的ClassLoader, 也许是为了安全起见，但是这会导致它可能加载到启动项目中的class（如果有），或者发生其它的异常，所以我们在执行时需要临时的将当前线程的ClassLoader设置为自定义的ClassLoader，以实现绝对的隔离执行
            ClassLoader originClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(pluginClassLoader);

            // 加载插件包中的类
            Class<?> clazz = pluginClassLoader.loadClass("cn.codest.PluginProvider");
            // 反射执行
            clazz.getDeclaredMethod("test", null).invoke(clazz.newInstance(), null);

            Thread.currentThread().setContextClassLoader(originClassLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
