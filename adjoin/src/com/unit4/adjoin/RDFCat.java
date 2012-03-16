package com.unit4.adjoin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.FieldMethodizer;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.unit4.cli.Argument;
import com.unit4.cli.Declaration;
import com.unit4.cli.Handler;
import com.unit4.cli.CLI;
import com.unit4.exception.Exception;
import com.unit4.input.U4Input;
import com.unit4.input.U4InputCallback;
import com.unit4.input.U4InputFactory;
import com.unit4.input.U4InputXML;
import com.unit4.output.U4Output;
import com.unit4.output.U4OutputRDF;
import com.unit4.tabular.U4Columns;
import com.unit4.tabular.U4Common;
import com.unit4.tabular.U4Row;
import com.unit4.vocabulary.U4AdJoin;

public class RDFCat {

//	Class.
	
	private static Logger logger = LoggerFactory.getLogger(RDFCat.class);
	
	public static final String HEADER_URN = "Header";
	public static final String FOOTER_URN = "Footer";
	public static final String BEFORE_ROW_URN = "BeforeRow";
	public static final String AFTER_ROW_URN = "AfterRow";
	public static final String ROW_URN = "Row";
	public static final String COLUMNS_URN = "Columns";
	
    public static void main(String[] args) {
		new RDFCat().go(args); 
    }

//    Instance.

    public final Declaration VALUE = new Declaration(
		new Handler() {
			public void go(Argument argument) {
				parse(argument);
			}
		}
    );
    
    public final Declaration ADD_TEMPLATE = new Declaration(
    	Declaration.REQUIRE_VALUE, 
		new Handler() {
			public void go(Argument argument) {
				addTemplate(argument.getValue());
			}
		},
		Arrays.asList("addTemplate")
	);
   
    public final Declaration ADD_GROUP = new Declaration(
		Declaration.REQUIRE_VALUE, 
		new Handler() {
			public void go(Argument argument) {
				addGroup(argument.getValue());
			}
		},
		Arrays.asList("addGroup")
	);
    
    public final Declaration OUTPUT = new Declaration(
		Declaration.REQUIRE_VALUE,
		new Handler() {
			public void go(Argument argument) {
				setOutput(argument.getValue());
			}
		},
		Arrays.asList("o", "output")
	);
    
    public final Declaration MAX_ROWS = new Declaration(
    		Declaration.REQUIRE_VALUE,
    		new Handler() {
				public void go(Argument argument) {
					maxRows(Long.valueOf(argument.getValue()));
				}
			},
			Arrays.asList("maxRows")
	);
    
    private CLI cli = new CLI(new ArrayList<Declaration>(Arrays.asList(VALUE, MAX_ROWS, ADD_TEMPLATE, ADD_GROUP, OUTPUT)));
    
    private VelocityContext context;
    
    private Long maxRows = null;
    
    protected String configFile = null;
    protected String outputFile = null;

    private U4Output output;
    
    public static final String DEFAULT_TEMPLATE = "file:Default.ttl";
    
    protected U4AdJoin template = new U4AdJoin(ModelFactory.createDefaultModel().createResource(U4AdJoin.DEFAULT_TEMPLATE_URI));
    
//    protected LinkedList<U4Template> templates = new LinkedList<U4Template>();
    protected LinkedList<String> groups = new LinkedList<String>();
    
    public RDFCat() {
    	addTemplate(DEFAULT_TEMPLATE);
    	addGroup(U4AdJoin.DEFAULT_TEMPLATE_GROUP);
    }
    
//    public LinkedList<U4Template> templates() {
//    	return this.templates;
//    }
    
    public U4AdJoin getTemplate() {
    	return this.template;
    }
    
    protected void addTemplate(String uri) {
    	logger.debug("addTemplate(uri={})", uri);
    	getTemplate().readModel(uri);
    }

    protected LinkedList<String> groups() {
    	return this.groups;
    }
    
    /**
     * Add a group as the first element of the Group list i.e. LIFO.
     * @param name
     */
    protected void addGroup(String name) {
    	logger.debug("addGroup(name={})", name);
    	groups().addFirst(name);
    }
    
    protected void setOutput(String output) {
    	
    }
    
    protected void maxRows(Long maxRows) {
    	logger.debug("maxRows(maxRows={})", maxRows);
    	this.maxRows = maxRows;
    }
    
    protected Long maxRows() {
    	return this.maxRows;
    }
    
    protected void go(String[] args) {
       	cli.go(args);
    }

    protected void parse(Argument argument) {
    	U4Common common = new U4Common();
    	
    	U4Input input = U4InputFactory.getInstance().createInputByURI(argument.getValue());
    	
    	U4InputCallback inputCallback = new U4InputCallback() {
			
			@Override
			public void header(U4Common common) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeRow(U4Common common) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void row(U4Common common) {
				System.out.println(common.getColumns().toString());
				System.out.println(common.getRow().toString());
			}
			
			@Override
			public void afterRow(U4Common common) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void footer(U4Common common) {
				// TODO Auto-generated method stub
				
			}
		};
		input.setCommon(common);
		input.setCallback(inputCallback);
		
		U4Output output = new U4OutputRDF();
		
		U4Columns columns = new U4Columns();
		
		U4Row row = new U4Row();
		
		common.setInput(input);
		common.setOutput(output);
		common.setColumns(columns);
		common.setRow(row);
		
		input.parse(argument.getValue());
    }
    
    protected void parseold(Argument argument) {
    	logger.debug("parse(arguement={})", argument);

    	logger.debug("Get templates.");
    	List<U4AdJoin> columnsTemplates = null; // new ArrayList<U4Convert>();
    	List<U4AdJoin> columnTemplates = null; // new ArrayList<U4Convert>();
    	List<U4AdJoin> headerTemplates = null; // new ArrayList<U4Convert>();
    	List<U4AdJoin> beforeRowTemplates = null; // new ArrayList<U4Convert>();
    	List<U4AdJoin> rowTemplates = null; // new ArrayList<U4Convert>();
    	List<U4AdJoin> afterRowTemplates = null; // new ArrayList<U4Convert>();
    	List<U4AdJoin> footerTemplates = null; // new ArrayList<U4Convert>();
    	U4AdJoin template = getTemplate();
		for (String group : groups()) {
			logger.trace("group={}", group);
			if ((columnsTemplates == null) && template.hasColumns(group)) {
				columnsTemplates = template.getColumns(group);
			}
			if ((columnTemplates == null) && template.hasColumn(group)) {
				columnTemplates = template.getColumn(group);
			}
			if ((headerTemplates == null) && template.hasHeader(group)) {
				headerTemplates = template.getHeader(group);
			}
			if ((beforeRowTemplates == null) && template.hasBeforeRow(group)) {
				beforeRowTemplates = template.getBeforeRow(group);
			}
			if ((rowTemplates == null) && template.hasRow(group)) {
				rowTemplates = template.getRow(group);
			}
			if ((afterRowTemplates == null) && template.hasAfterRow(group)) {
				afterRowTemplates = template.getAfterRow(group);
			}
			if ((footerTemplates == null) && template.hasFooter(group)) {
				footerTemplates = template.getFooter(group);
			}
		}

		U4Input input = new U4Input();
		input.setURI(argument.getValue());
		
    	logger.debug("Open input {}", argument);
    	InputStream is = FileManager.get().open(argument.getValue());
    	if (is == null) {
    		throw new Exception(String.format("Unable to read [%s].", argument.getValue()));
    	}
    	CsvReader csvInput = new CsvReader(is, Charset.forName("UTF-8"));
    	
    	logger.debug("Read input headers.");
    	try {
			csvInput.readHeaders();
		} catch (IOException e) {
			logger.error("Unable to read input headers due to [{}].", e);
			return;
		}

		logger.debug("Create Columns.");
		U4Columns columns;
		try {
			columns = new U4Columns(csvInput.getHeaders());
		} catch (IOException e) {
			logger.error("Unable to get input headers due to [{}].", e);
			return;
		}

		logger.debug("Match columns to templates.");
		for (Integer index = 0; index < columns.getNames().size(); index++) {
			columns.setMatch(index, U4AdJoin.match(columnTemplates, columns.getName(index)));
		}
		logger.trace("{}", columns.toString());
		
		logger.debug("Create the output model.");
		output = new U4Output();
//		output.setNsPrefix(U4ORG.getPrefix(), U4ORG.getNS());
//		output.setNsPrefix("payment", "http://reference.data.gov.uk/def/payment#");

		logger.debug("Create Common.");
		U4Common common = new U4Common();

		logger.debug("Create Row.");
		U4Row row = new U4Row();
		row.setCommon(common);
		row.setColumns(columns);

		// Configure velocity.
		logger.debug("Create Velocity.");
        Velocity.init();
        context = new VelocityContext();
        context.put("Input", input);
		context.put("Output", output);
		context.put("Common", common);
        context.put("Columns", columns);
        context.put("Row", row);
        
        context.put("Math", Math.class);
        context.put("String", String.class);
        context.put("UUID", UUID.class);
        
        context.put("DCTerms", new FieldMethodizer("com.unit4.vocabulary.U4DCTerms"));
        context.put("FOAF", new FieldMethodizer("com.unit4.vocabulary.U4FOAF"));
        context.put("Org", new FieldMethodizer("com.unit4.vocabulary.U4Org"));
        context.put("RDF", new FieldMethodizer("com.unit4.vocabulary.U4RDF"));
        context.put("RDFS", new FieldMethodizer("com.unit4.vocabulary.U4RDFS"));
        context.put("XSD", new FieldMethodizer("com.unit4.vocabulary.U4XSD"));
        context.put("VoID", new FieldMethodizer("com.unit4.vocabulary.U4VoID"));
//        context.put("U4Payment", new FieldMethodizer("com.unit4.vocabulary.U4Payment"));
//        context.put("U4Interval", new FieldMethodizer("com.unit4.vocabulary.U4Payment"));
        
//        @prefix interval: <http://reference.data.gov.uk/def/intervals/> .

        logger.trace("Processing header templates.");
        if (headerTemplates != null) {
	        for (U4AdJoin headerTemplate : headerTemplates) {
        		output.getModel().add(headerTemplate.getStatements(context));
	        }
        }

        logger.trace("Processing columns templates.");
        row.setIndex(null);
        if (columnsTemplates != null) {
	        for (U4AdJoin columnsTemplate : columnsTemplates) {
				for (Integer index = 0; index < columns.getNames().size(); index++) {
					columns.setIndex(index); // Set the current column index.
					output.getModel().add(columnsTemplate.getStatements(context));
				}
	        }
        }

        
        //        Read the input.
        Long rowIndex;

        logger.debug("Read rows.");
		try {
			while (csvInput.readRecord()) {
				maxRows = maxRows();
				rowIndex = csvInput.getCurrentRecord();
				if (maxRows != null) {
					if (rowIndex >= maxRows) {
						logger.info("maxRows of {} reached.", maxRows);
						break;
					}
				}
				// Update the row details.
				row.setIndex(rowIndex);
				row.setValues(csvInput.getValues());
				
			    logger.trace("Processing beforeRow templates.");
			    if (beforeRowTemplates!= null) {
				    for (U4AdJoin beforeRowtemplate : beforeRowTemplates) {
			    		output.getModel().add(beforeRowtemplate.getStatements(context));
				    }
			    }
				    
			    logger.trace("Processing Row templates.");
				for (Integer index = 0; index < csvInput.getValues().length; index++) {
					columns.setIndex(index); // Set the current column index.
					if (row.getValue() == "") {
						logger.trace("Value is empty string.");
					} else {
						if (columns.hasMatch(index)) {
							output.getModel().add(columns.getMatch(index).getStatements(context));
						} else {
							columns.setMatch(index, U4AdJoin.match(rowTemplates, row.getValue()));
							if (columns.hasMatch(index)) {
								output.getModel().add(columns.getMatch(index).getStatements(context));
							}
						}
					}
				}

				logger.trace("Processing afterRow templates.");
			    if (afterRowTemplates!= null) {
				    for (U4AdJoin afterRowtemplate : afterRowTemplates) {
			    		output.getModel().add(afterRowtemplate.getStatements(context));
				    }
			    }
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

        logger.debug("Processing footer templates.");
        if (footerTemplates != null) {
	        for (U4AdJoin footerTemplate : footerTemplates) {
        		output.getModel().add(footerTemplate.getStatements(context));
	        }
        }
		
		//    	Close input.
    	csvInput.close();
    	//
//    	logger.trace("Common \n{}", common.toString());
//    	output.setLanguage("RDF/XML");
     	output.render(); //getModel().write(System.out, "RDF/XML");
    }
    
    public void processTemplate() {
    	
    }
    
    protected void help() {
    	logger.info("Usage: java adjoin.Adjoin" );
    }
}
