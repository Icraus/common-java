/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plugins.pluginloader;

import com.plugins.exception.PluginErrorLoadingException;
import com.plugins.plugin.PluginIFace;
import com.plugins.plugin.JarPluginBase;
import com.plugins.exception.PluginNotFoundException;
import com.plugins.exception.PluginProperiesNotFound;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
//TODO 1.add Plugin Manager

/**
 * <h2>Used to load Plugins from jar files</h2>
 * it uses a class loader and read class From jar files specified in a property called className
 * and found in JarPluginLoader.PLUGIN_METADATA_FILE
 * clients of this loader should implements the PluginIface 
 * @author Mohamed Khaled(icraus)
 * @version 1.1
 */
public class JarPluginLoader implements PluginLoaderIFace {
    
    /**
     * <h2>The Default Plugin Meta data file which must exists in all plugins Jar and of type <b>.properties</b></h2>
     */
    public static String PLUGIN_METADATA_FILE = "PluginMetadata.properties";
    public static String PLUGIN_PACKAGE_PROPERTY = "PLUGIN_META_INF";
    private final String PLUGIN_PROPERTIES_PATH = PLUGIN_PACKAGE_PROPERTY + "/" + PLUGIN_METADATA_FILE;

    private String filePath;
    private ClassLoader loader;
    private Properties prop;
    public JarPluginLoader() {
    }

    /**
     * <h2>used to init class Loader</h2>
     *  the default is java.net.URLClassLoader
     * @return
     * @throws PluginNotFoundException
     */
    protected ClassLoader initClassLoader() throws PluginNotFoundException{
        try {
            File file = new File(getFilePath());
            if(!file.exists())
                throw new MalformedURLException();
            URL jarPath = file.toURI().toURL();
            ClassLoader _load =  new URLClassLoader(new URL[]{jarPath});
            return _load;
        } catch (MalformedURLException ex) {
            throw new PluginNotFoundException("No such File");
        }
        
    }
    /**
     * used to load properties from jar files
     * @return
     * @throws PluginProperiesNotFound 
     */
    protected Properties loadProperties() throws PluginProperiesNotFound{
        prop = new Properties();
        try (InputStream streamProp = getLoader().getResourceAsStream(PLUGIN_PROPERTIES_PATH)){
            
            prop.load(streamProp);
            return prop;     
        } catch (IOException | NullPointerException ex) {
            throw new PluginProperiesNotFound("Error Loading Properties");
        }
    }
    
    /**
     * Factory method for loading class from jar files
     * @param className
     * @return an instance of the plugin class
     * @throws PluginNotFoundException
     * @throws PluginErrorLoadingException
     */
    protected JarPluginBase loadPluginHelper(String className) throws PluginNotFoundException, PluginErrorLoadingException { 
        try {
            JarPluginBase plugin = new JarPluginBase((PluginIFace) Class.forName(className, true, getLoader()).newInstance());
            return plugin;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            throw new PluginNotFoundException("Can't Find Plugin, Error Class Name" + ex.getMessage());
        }catch(ClassCastException ex){
            throw new PluginErrorLoadingException(); 
        }
    }
    protected void initPluginLoader(String _filePath) throws PluginNotFoundException, PluginProperiesNotFound{
        setFilePath(_filePath);
        ClassLoader _load = initClassLoader();
        setLoader(_load);
        loadProperties();
        
    }
    /**
     * 
     * @param _filePath
     * @return an instance of com.plugins.plugin.JarPluginBase
     * @throws PluginNotFoundException
     * @throws PluginErrorLoadingException
     * @throws PluginProperiesNotFound 
     */
    
    @Override
    public PluginIFace loadPlugin(String _filePath) throws PluginNotFoundException, PluginErrorLoadingException, PluginProperiesNotFound {
        initPluginLoader(_filePath);
        String className = prop.getProperty(JarPluginBase.CLASSNAME_PROPERTY);
        JarPluginBase plugin = loadPluginHelper(className);
        plugin.setPluginMetaData(prop);
        return plugin;
    }
     public String getFilePath() {
        return filePath;
    }

    protected void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ClassLoader getLoader() {
        return loader;
    }

    protected void setLoader(ClassLoader loader) {
        this.loader = loader;
    }

    

    
    
}
