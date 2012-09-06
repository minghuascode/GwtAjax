/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.gwt.sample.hello.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import java.util.Set;

/*
 * JSON Services Example from http://www.gwtapps.com/?p=42
 * GWT Tutorial: How to Read Web Services Client-Side with JSONP
 * 
    http://www.geonames.org/postalCodeLookupJSON?
    postalcode=94566&country=US&callback=abcd    

    abcd({"postalcodes":
            [{"adminName2":"Alameda","adminCode2":"001",
              "postalcode":"94566","adminCode1":"CA",
              "countryCode":"US","lng":-121.8755,
              "placeName":"Pleasanton","lat":37.665804,
              "adminName1":"California"}]}); 
              
 * The JSONP example below is modified from
http://www.gwtsite.com/how-to-access-web-services-with-gwt/ 
 * The same example is also in the book "Google Web Toolkit by Ryan Dewsbury 
 * and published by Prentice Hall 2008", on page 338.
 * 
 * Note abcd function name is used as the callback function, 
 * and abcd_script id tag  as the injected javascript element. 
 * 
 * The function decodeJson is from the JSON sample of gwt sdk.
 */

/**
 *
 * @author gwt user
 */
public class HelloJsonp implements HelloJsonRequestHandler {

    private StringBuilder str = new StringBuilder(); /* response decoding */
    private int           indent = 0;
    private void prefix() { 
        StringBuilder bdr = new StringBuilder();
        for(int i=0; i<indent; i++) bdr.append(" ");
        str.append(bdr.toString());
    }
    
    private void decodeJson(JSONValue jv) {
        JSONArray jsonArray;
        JSONObject jsonObject;
        JSONString jsonString;

        if ((jsonArray = jv.isArray()) != null) {
          str.append(" [[ \n");
          indent += 4;
          prefix();
          for (int i = 0; i < jsonArray.size(); ++i) {
            str.append("["+ Integer.toString(i) + "]=(");
            decodeJson(jsonArray.get(i));
            str.append(")\n");
            prefix();
          }
          str.append("\n");
          indent -= 4;
          prefix();
          str.append(" ]] \n");
          prefix();
        } else if ((jsonObject = jv.isObject()) != null) {
          Set<String> keys = jsonObject.keySet();
          str.append(" {{ \n");
          indent += 4;
          prefix();
          for (String key : keys) {
            str.append("{" + key + "}=(");
            decodeJson(jsonObject.get(key));
            str.append(")\n");
            prefix();
          }
          str.append("\n");
          indent -= 4;
          prefix();
          str.append(" }} \n");
          prefix();
        } else if ((jsonString = jv.isString()) != null) {
          // Use stringValue instead of toString() because we don't want escaping
          str.append("\"" + jsonString.stringValue() + "\"\n");
          prefix();
        } else {
          // JSONBoolean, JSONNumber, and JSONNull work well with toString().
          str.append(jv.toString() + "\n");
          prefix();
        }
    }
    
    @Override
    public void onRequestComplete( JavaScriptObject json ) {
        GWT.log(json.toString(), null);
        str.append("Return:\n");
        
        JSONObject jo = new JSONObject(json);
        decodeJson(jo);
        
        Hello.callback( str.toString() );
        removeScript();
    }
    
    public static void get() {
        String url = "http://www.geonames.org/postalCodeLookupJSON?" +
                     "postalcode=94566&country=US&callback=abcd";
        HelloJsonp.get(url, "abcd", new HelloJsonp() );
    }
    
  public static void get(String url, HelloJsonRequestHandler handler) {
    String callbackName = "JSONCallback"+handler.hashCode();
    get( url+callbackName, callbackName, handler );
  }	
  public static void get(String url, String callbackName, 
                         HelloJsonRequestHandler handler ) {
    createCallbackFunction( handler, callbackName );
    addScript(url);
  }
  public static native void addScript(String url) /*-{
    var scr = document.createElement("script");
    scr.setAttribute("language", "JavaScript");
    scr.setAttribute("src", url);
    scr.setAttribute("id", "abcd_script");
    document.getElementsByTagName("body")[0].appendChild(scr);
  }-*/;
  public static native void removeScript() /*-{
    var scr = document.getElementById("abcd_script");
    document.getElementsByTagName("body")[0].removeChild(scr);
    delete src;
  }-*/;
  private native static void createCallbackFunction( HelloJsonRequestHandler obj, 
                                                     String callbackName)/*-{
    tmpcallback = function(j) {
      obj.@com.google.gwt.sample.hello.client.HelloJsonp::onRequestComplete(Lcom/google/gwt/core/client/JavaScriptObject;)(j);
    };
    eval( "window." + callbackName + "=tmpcallback" );
  }-*/;
  
  /*
obj.@com.google.gwt.sample.hello.client.HelloJsonRequestHandler::onRequestComplete(Lcom/google/gwt/core/client/JavaScriptObject;)(j);
   */
}

/* Further notes: 
 * 
 * --------------------------------------------------------
 * Hello.java: 
 * 
public class Hello implements EntryPoint {

  public void onModuleLoad() {
    Button b = new Button("Click me", new ClickHandler() {
      public void onClick(ClickEvent event) {
        //Window.alert("Hello, AJAX");
          HelloJsonp.get();
      }
    });

    RootPanel.get().add(b);
  }
  
  public static void callback(String m) {
    Window.alert("Hello, AJAX\n" + m);
  }
}
 * 
 * --------------------------------------------------------
 * HelloJsonRequestHandler.java:
 * 
public interface HelloJsonRequestHandler {
    public void onRequestComplete( JavaScriptObject json );
}
 * 
 * --------------------------------------------------------
 * Hello.gwt.xml: add this: 
 * 
        <inherits name='com.google.gwt.json.JSON'/>
 * 
 * --------------------------------------------------------
 * build.xml: modify the location:
 * 
  <property name="gwt.sdk" location="../../gwt-linux-1.7.1" />
 * 
 * --------------------------------------------------------
 * build.xml: add these it will be slow but prints more errors:
 * 
      <!-- Additional arguments like -style PRETTY or -logLevel DEBUG -->
      <arg value="-style"/>
      <arg value="DETAILED"/>
      <arg value="-logLevel"/>
      <arg value="DEBUG"/>
 * 
 */
