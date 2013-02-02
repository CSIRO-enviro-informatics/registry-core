/******************************************************************
 * File:        TestAPI.java
 * Created by:  Dave Reynolds
 * Created on:  1 Feb 2013
 * 
 * (c) Copyright 2013, Epimorphics Limited
 *
 *****************************************************************/

package com.epimorphics.registry.webapi;

import static org.junit.Assert.*;

import org.junit.Test;

import com.epimorphics.registry.vocab.RegistryVocab;
import com.epimorphics.util.TestUtil;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;
import com.sun.jersey.api.client.ClientResponse;


/**
 * Test harness for testing access to the API - launches a registry using an 
 * embedded tomcat and a memory-backed store then talks to the API using http.
 * 
 * @author <a href="mailto:dave@epimorphics.com">Dave Reynolds</a>
 */
public class TestAPI extends TomcatTestBase {

    static final String EXT_BLACK = "http://example.com/colours/black";
    static final String REG1 = BASE_URL + "reg1";
    static final String REG1_ITEM = ROOT_REGISTER + "_reg1";
    
    @Test
    public void testBasics() {
        // Registration
        ClientResponse response = postFile("test/reg1.ttl", BASE_URL, "text/turtle");
        assertEquals("Register a register", 204, response.getStatus());
        assertEquals(REG1_ITEM, response.getLocation().toString());
        assertEquals("Register in non-existant location", 404, postFileStatus("test/reg1.ttl", BASE_URL+"foo"));
        assertEquals("Register the same again", 403, postFileStatus("test/reg1.ttl", BASE_URL));
        assertEquals(204, postFileStatus("test/red.ttl", REG1));
        
        // Entity and item access
        checkModelResponse(REG1 + "/red", ROOT_REGISTER + "reg1/red", "test/expected/red.ttl");
        Model m = getModelResponse(REG1 + "/red?_view=with_metadata");
        checkModelResponse(m, ROOT_REGISTER + "reg1/_red", "test/expected/red_item.ttl");
        checkModelResponse(m, ROOT_REGISTER + "reg1/red", "test/expected/red.ttl");
        checkEntity(m, ROOT_REGISTER + "reg1/_red",  ROOT_REGISTER + "reg1/red");
        assertEquals(404, getResponse(REG1 + "/notred").getStatus());
        m = getModelResponse(REG1 + "/_red");
        checkModelResponse(m, ROOT_REGISTER + "reg1/_red", "test/expected/red_item.ttl");
        checkModelResponse(m, ROOT_REGISTER + "reg1/red", "test/expected/red.ttl");
        checkEntity(m, ROOT_REGISTER + "reg1/_red",  ROOT_REGISTER + "reg1/red");
        
        // External (not managed) entities
        assertEquals(204, postFileStatus("test/absolute-black.ttl", REG1));
        m = checkModelResponse(REG1 + "/_black", EXT_BLACK, "test/expected/absolute-black.ttl");
        checkEntity(m, ROOT_REGISTER + "reg1/_black", EXT_BLACK);
        
        // ?entity not yet implemented
//        checkModelResponse(REG1 + "?entity=" + EXT_BLACK, EXT_BLACK, "test/expected/absolute-black.ttl");
//        checkModelResponse(BASE_URL + "?entity=" + EXT_BLACK, EXT_BLACK, "test/expected/absolute-black.ttl");
        
        // Updates, change properties on red
        response = invoke("PUT", "test/red1.ttl", REG1 + "/red");
        assertEquals(204,  response.getStatus());
        String red1Version = response.getLocation().toString();
        assertTrue(red1Version.startsWith(ROOT_REGISTER + "reg1/_red:"));
        String versionSuffix = red1Version.substring( red1Version.length()-2 );
        checkModelResponse(REG1 + "/red", ROOT_REGISTER + "reg1/red", "test/expected/red1.ttl");

        response = invoke("PATCH", "test/red1b.ttl", REG1 + "/red");
        assertEquals(204,  response.getStatus());
        checkModelResponse(REG1 + "/red", ROOT_REGISTER + "reg1/red", "test/expected/red1b.ttl");

        checkModelResponse(REG1 + "/_red" + versionSuffix, ROOT_REGISTER + "reg1/red", "test/expected/red1.ttl");
        
        // Register read
        checkModelResponse(REG1, ROOT_REGISTER + "reg1", "test/expected/reg1-empty.ttl");
        // TODO set status to visible and list with members
        
//        m.write(System.out, "Turtle");
    }
    
    private Model checkModelResponse(String fetch, String rooturi, String file, Property...omit) {
        Model m = getModelResponse(fetch);
        Resource actual = m.getResource(rooturi);
        Resource expected = FileManager.get().loadModel(file).getResource(rooturi);
        assertTrue(actual.listProperties().hasNext());  // guard against wrong rooturi in config
        TestUtil.testResourcesMatch(expected, actual, omit);
        return m;
    }
    
    private Model checkModelResponse(Model m, String rooturi, String file, Property...omit) {
        Resource actual = m.getResource(rooturi);
        Resource expected = FileManager.get().loadModel(file).getResource(rooturi);
        assertTrue(actual.listProperties().hasNext());  // guard against wrong rooturi in config
        TestUtil.testResourcesMatch(expected, actual, omit);
        return m;
    }
    
    private void checkEntity(Model m, String itemURI, String entityURI) {
        Resource entity = m.getResource(itemURI).getPropertyResourceValue(RegistryVocab.definition).getPropertyResourceValue(RegistryVocab.entity);
        assertEquals(entityURI, entity.getURI());
    }
}
