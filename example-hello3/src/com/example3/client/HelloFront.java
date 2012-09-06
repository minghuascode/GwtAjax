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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * HelloWorld application.
 */
public class HelloFront {
    
  public void init() {
    
    /* panel */
    VerticalPanel pnl = new VerticalPanel();
    pnl.setWidth("100%"); pnl.setBorderWidth(3); pnl.setSpacing(3);
    
    /* table */
    FlexTable tbl = new FlexTable();
    tbl.setWidth("100%"); tbl.setBorderWidth(3);
    tbl.getColumnFormatter().setWidth(0, "100%");
    
    /* row */
    int row = 0;
    {
        TextArea txt = new TextArea();
        txt.setWidth("100%");
        txt.setText("HelloJsonp Test");
        Button btn = new Button("Click to test", new ClickHandler() {
                     public void onClick(ClickEvent event) { HelloJsonp.get(); }
                     });
        tbl.setWidget(row, 0, txt);
        tbl.setWidget(row, 1, btn);
    }

    /* add table to panel, panel to root */
    pnl.add(tbl);
    Hello.hookNewRoot(pnl);
    thisPanel = pnl;
  }
  
  private static HelloFront thisHelloFrontObj = null;
  private static VerticalPanel thisPanel = null;
  public static void hookToRoot() {
    if (thisHelloFrontObj == null) {
        GWT.log("Null thisHelloFrontObj. Hook nothing!",null);
    }
    Hello.hookNewRoot(thisPanel);
  }
}
