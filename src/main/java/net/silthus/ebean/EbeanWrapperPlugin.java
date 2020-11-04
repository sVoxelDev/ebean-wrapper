package net.silthus.ebean;

import kr.entree.spigradle.annotations.PluginMain;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

@PluginMain
public class EbeanWrapperPlugin extends JavaPlugin {

    public EbeanWrapperPlugin() {
    }

    public EbeanWrapperPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }
}
