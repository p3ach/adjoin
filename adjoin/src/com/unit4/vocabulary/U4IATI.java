package com.unit4.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class U4IATI extends U4Vocabulary {
	
//	Class.
	public static final String PREFIX = "iati";
	public static final String NS = "http://iatistandard.org/def/standard#";
	
	public static final Resource EntityIdentifier = createResource(getURI("EntityIdentifier"));
	
	public static final Resource Activities = createResource(getURI("Activities"));
		public static final Resource Activity = createResource(getURI("Activity"));
			public static final Resource ContainerElements = createResource(getURI("ContainerElements"));
				public static final Resource FileHeader = createResource(getURI("FileHeader"));
				public static final Resource RecordHeader = createResource(getURI("RecordHeader"));
			public static final Resource Identification = createResource(getURI("Identification"));
			public static final Resource ReportingOrganisation = createResource(getURI("ReportingOrganisation"));
			public static final Resource ParticipatingOrganisation = createResource(getURI("ParticipatingOrganisation"));
			public static final Resource BasicActivityInformation = createResource(getURI("BasicActivityInformation"));
				public static final Resource ActivityDescription = createResource(getURI("ActivityDescription"));
				public static final Resource ActivityStatus = createResource(getURI("ActivityStatus"));
				public static final Resource ActivityDate = createResource(getURI("ActivityDate"));
				public static final Resource ActivityContact = createResource(getURI("ActivityContact"));
					public static final Resource Organisation = createResource(getURI("Organisation"));
			public static final Resource TiedStatus = createResource(getURI("TiedStatus"));

	public static final Resource Category = createResource(getURI("Category"));
	public static final Resource Email = createResource(getURI("Email"));
	public static final Resource Telephone = createResource(getURI("Telephone"));
	
	public static final Resource PolicyMarker = createResource(getURI("PolicyMarker"));
	public static final Property policyMarker = createProperty(getURI("policyMarker"));
	public static final Resource Vocabulary = createResource(getURI("Vocabulary"));
	public static final Property vocabulary = createProperty(getURI("vocabulary"));
	public static final Resource Significance = createResource(getURI("Significance"));
	public static final Property policySignificance = createProperty(getURI("policySignificance"));

	public static final Resource TransactionType = createResource(getURI("TransactionType"));
	public static final Property transactionType = createProperty(getURI("transactionType"));

	public static final Resource TransactionDate = createResource(getURI("TransactionDate"));
	public static final Property transactionDate = createProperty(getURI("transactionDate"));
	
	public static final Resource Value = createResource(getURI("Value"));
	
	public static final Resource MailingAddress = createResource(getURI("MailingAddress"));
	
	public static final Resource Title = createResource(getURI("Title"));
			
	public static final Resource RelatedActivity = createResource(getURI("RelatedActivity"));
			
	public static final Resource DocumentLink = createResource(getURI("DocumentLink"));

    public static final Property currency = createProperty(getURI("currency"));

	
    public static final Property entityIdentifier = createProperty(getURI("entityIdentifier"));
		
    public static final Property version = createProperty(getURI("version"));
    
    public static final Property activityDateType = createProperty(getURI("activityDateType"));
    public static final Property activityStatus = createProperty(getURI("activityStatus"));
    
    public static final Property categoryCode = createProperty(getURI("categoryCode"));
    
    public static final Property defaultLanguage= createProperty(getURI("defaultLanguage"));
    public static final Property defaultCurrency = createProperty(getURI("defaultCurrency"));
    
    public static final Property hierachy = createProperty(getURI("hierachy"));
    
    public static final Property organisationIdentifier = createProperty(getURI("organisationIdentifier"));
    public static final Property organisationRole = createProperty(getURI("organisationRole"));
    public static final Property organisationType = createProperty(getURI("organisationType"));
    
    public static final Property relatedActivityType = createProperty(getURI("relatedActivityType"));

	public static final Resource CodeList = createResource(getURI("CodeList"));
		public static final Resource OrganisationalIdentifier = createResource(getURI("OrganisationalIdentifier"));
			public static final Resource BilateralAgency = createResource(getURI("BilateralAgency"));
			public static final Resource Multilateral = createResource(getURI("Multilateral"));
			public static final Resource InternationalNGO = createResource(getURI("InternationalNGO"));
		public static final Resource OrganisationalRole = createResource(getURI("OrganisationalRole"));

	public static final Resource tiedStatus = createResource(getURI("tiedStatus"));
		
    public static String getPrefix() {
		return PREFIX;
	}
	
	public static String getNS() {
		return NS;
	}

	public static void setNsPrefix(Model model) {
		model.setNsPrefix(getPrefix(), getNS());
	}
	
	public static String getURI(String fragment) {
		return getNS() + fragment;
	}
	
	//	Instance.
	
	public U4IATI() {
		super();
	}
	
	public U4IATI(String uri) {
		super(uri);
	}
	
	public U4IATI(Resource subject) {
		super(subject);
	}

	public U4IATI(U4Vocabulary vocabulary) {
		super(vocabulary);
	}
	
	public String toString() {
		return String.format("U4IATI [%s]", getSubject().toString());
	}
}
