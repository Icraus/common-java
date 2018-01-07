/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Test.PluginLoader;

import com.plugins.plugin.Plugin;

/**
 *
 * @author Shoka
 */
public class TestPlugin implements Plugin, TestInterface{

    @Override
    public void run() {
        System.out.println("From Run");
    }
    
}
