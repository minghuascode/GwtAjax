/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.example3.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * HelloWorld application.
 * Package name changed from  com.google.gwt.sample.hello. to  com.example3.
 */
public class Hello implements EntryPoint {
    
  public void onModuleLoad() {
    thisHelloObj = this;
    Button b = new Button("Click me", new ClickHandler() {
      public void onClick(ClickEvent event) {
        Window.alert("Hello, AJAX");
        //  HelloJsonp.get();
      }
    });

    RootPanel.get().add(b);
    
    Button b2 = new Button("Click me b2", new ClickHandler() {
      public void onClick(ClickEvent event) {
        HelloFront front = new HelloFront();
        front.init();
      }
    });
    RootPanel.get().add(b2);
    thisHelloRoot = RootPanel.get();
  }
  
  public static void callback(String m) {
    Window.alert("Hello, AJAX\n" + m);
  }
  
  private static Hello thisHelloObj = null;
  private static RootPanel thisHelloRoot = null;
  public static void hookNewRoot(Panel pnl) {
    if (thisHelloObj == null) {
        GWT.log("Null thisHelloObj. Return", null);
        return;
    }
    if (thisHelloRoot == null) {
        GWT.log("Null thisHelloRoot. Return.", null);
        return;
    }
    if (pnl == null) {
        GWT.log("Null pnl. Return.",null);
        return;
    }
    thisHelloRoot.clear();
    thisHelloRoot.add(pnl);
  }
}
