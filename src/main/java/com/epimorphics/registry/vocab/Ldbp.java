/* CVS $Id: $ */
package com.epimorphics.registry.vocab; 
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.ontology.*;
 
/**
 * Vocabulary definitions from file:/home/der/projects/java-workspace/ukl/ukl-registry-poc/src/main/vocabs/ldbp.rdf 
 * @author Auto-generated by schemagen on 05 Feb 2013 08:35 
 */
public class Ldbp {
    /** <p>The ontology model that holds the vocabulary terms</p> */
    private static OntModel m_model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://www.w3.org/ns/ldp#";
    
    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    /** <p>List of predicates that indicate the ascending order of the members in a page.</p> */
    public static final OntProperty containerSortPredicates = m_model.createOntProperty( "http://www.w3.org/ns/ldp#containerSortPredicates" );
    
    /** <p>Indicates which predicate of the container should be used to determine the 
     *  membership.</p>
     */
    public static final OntProperty membershipPredicate = m_model.createOntProperty( "http://www.w3.org/ns/ldp#membershipPredicate" );
    
    /** <p>Indicates which resource is the subject for the members of the container.</p> */
    public static final OntProperty membershipSubject = m_model.createOntProperty( "http://www.w3.org/ns/ldp#membershipSubject" );
    
    /** <p>From a known page, how to indicate the next or last page as rdf:nil.</p> */
    public static final OntProperty nextPage = m_model.createOntProperty( "http://www.w3.org/ns/ldp#nextPage" );
    
    /** <p>Associated a page with its container.</p> */
    public static final OntProperty pageOf = m_model.createOntProperty( "http://www.w3.org/ns/ldp#pageOf" );
    
    /** <p>A Basic Profile Resource (BPR) that also conforms to additional patterns and 
     *  conventions in this document for managing membership.</p>
     */
    public static final OntClass Container = m_model.createClass( "http://www.w3.org/ns/ldp#Container" );
    
    /** <p>A resource that represents a limited set of members of a Basic Profile Container.</p> */
    public static final OntClass Page = m_model.createClass( "http://www.w3.org/ns/ldp#Page" );
    
}