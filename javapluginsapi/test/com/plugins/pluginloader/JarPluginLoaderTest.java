/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plugins.pluginloader;

import com.Test.PluginLoader.TestInterface;
import com.plugins.exception.PluginErrorLoadingException;
import com.plugins.exception.PluginNotFoundException;
import com.plugins.exception.PluginProperiesNotFound;
import com.plugins.plugin.JarPluginBase;
import com.plugins.plugin.Plugin;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Mohamed Khaled(icraus)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JarPluginLoaderTest {
    
    public JarPluginLoaderTest() {
    }
    private String fileName;
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
        URL resource = this.getClass().getResource("TestingLibrary.jar");
        fileName = resource.getFile();

    }
    
    @After
    public void tearDown() {
//        JarPluginLoader.PLUGIN_METADATA_FILE = "PluginMetadata.properties";
        
    }
    @Test
    public void testFileExists(){
        assertEquals(new File(fileName).exists(), true);    
    }
    @Test
    public void testLoader(){
        PluginLoader loader = new JarPluginLoader();
        try {
//            loader.initPluginLoader(fileName);

            TestInterface plugin= (TestInterface)  loader.loadPlugin(fileName);
            
            assertNotEquals(plugin, null);
            plugin.run();
            System.out.println("Test Succeed");
        } catch (PluginNotFoundException | PluginErrorLoadingException | PluginProperiesNotFound ex) {
            Logger.getLogger(JarPluginLoaderTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Exception was thrown");
        }
    }
    
    @Test(expected = PluginNotFoundException.class)
    public void loaderThrowNotFoundExcption() throws PluginNotFoundException, PluginProperiesNotFound, PluginErrorLoadingException{
        PluginLoader loader = new JarPluginLoader();
            TestInterface plugin = (TestInterface)loader.loadPlugin("Dumb Name");
            assertNotEquals(plugin, null);
            plugin.run();
    }
    
    @Test(expected = PluginProperiesNotFound.class)
    public void loaderThrowPropertyNotFoundExcption() throws PluginNotFoundException, PluginProperiesNotFound, PluginErrorLoadingException{
        PluginLoader loader = new JarPluginLoader();
        TestInterface plugin = (TestInterface)loader.loadPlugin(fileName);
        assertNotEquals(plugin, null);
        plugin.run();
    }
    /* TODO add Test For Error Loading
    @Test(expected = PluginErrorLoadingException.class)
    public void loaderThrowErrorLoadingExcption() throws PluginNotFoundException, PluginProperiesNotFound, PluginErrorLoadingException{
        PluginLoader loader = new JarPluginLoader();
        TestInterface plugin = (TestInterface) loader.loadPlugin(fileName);
        assertNotEquals(plugin, null);
        plugin.run();
    }*/
    @Test
    public void testLoaderMetaData(){
        try {
            JarPluginLoader loader = new JarPluginLoader();
            TestInterface plugin = (TestInterface)loader.loadPlugin(fileName);
            assertNotEquals(plugin, null);
            JarPluginBase metaPlugin = loader.getMetaPlugin();
            Plugin plugin1 = metaPlugin.getPlugin();
            assertNotEquals(plugin1, null);
            TestInterface t = (TestInterface)plugin1;
            t.run();
            assertNotEquals(t , null);
            System.out.println("Success");
        } catch (PluginNotFoundException ex) {
            Logger.getLogger(JarPluginLoaderTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PluginProperiesNotFound ex) {
            Logger.getLogger(JarPluginLoaderTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PluginErrorLoadingException ex) {
            Logger.getLogger(JarPluginLoaderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
   
    }
    
    
}
