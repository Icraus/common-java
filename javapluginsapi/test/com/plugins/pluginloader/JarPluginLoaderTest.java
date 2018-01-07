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
import com.plugins.plugin.PluginIFace;
import java.io.File;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mohamed Khaled(icraus)
 */
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
        URL resource = getClass().getResource("TestingLibrary.jar");
        fileName = resource.getFile();

    }
    
    @After
    public void tearDown() {
    }
    @Test
    public void testFileExists(){
        assertEquals(new File(fileName).exists(), true);    
    }
    @Test
    public void testLoader(){
        PluginLoaderIFace loader = new JarPluginLoader();
        try {
            TestInterface plugin = (TestInterface)loader.loadPlugin(fileName);
            assertNotEquals(plugin, null);
            plugin.run();
            System.out.println("Test Successfully");
        } catch (PluginNotFoundException ex) {
            Logger.getLogger(JarPluginLoaderTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PluginProperiesNotFound ex) {
            Logger.getLogger(JarPluginLoaderTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PluginErrorLoadingException ex) {
            Logger.getLogger(JarPluginLoaderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test(expected = PluginNotFoundException.class)
    public void loaderThrowNotFoundExcption() throws PluginNotFoundException, PluginProperiesNotFound, PluginErrorLoadingException{
        PluginLoaderIFace loader = new JarPluginLoader();
            TestInterface plugin = (TestInterface)loader.loadPlugin("Dumb Name");
            assertNotEquals(plugin, null);
            plugin.run();
    }
    
    @Test(expected = PluginProperiesNotFound.class)
    public void loaderThrowPropertyNotFoundExcption() throws PluginNotFoundException, PluginProperiesNotFound, PluginErrorLoadingException{
        PluginLoaderIFace loader = new JarPluginLoader();
        JarPluginLoader.PLUGIN_METADATA_FILE = "Hello World";
        TestInterface plugin = (TestInterface)loader.loadPlugin(fileName);
        assertNotEquals(plugin, null);
        plugin.run();
    }
    /* TODO add Test For Error Loading
    @Test(expected = PluginErrorLoadingException.class)
    public void loaderThrowErrorLoadingExcption() throws PluginNotFoundException, PluginProperiesNotFound, PluginErrorLoadingException{
        PluginLoaderIFace loader = new JarPluginLoader();
        TestInterface plugin = (TestInterface) loader.loadPlugin(fileName);
        assertNotEquals(plugin, null);
        plugin.run();
    }*/
}
