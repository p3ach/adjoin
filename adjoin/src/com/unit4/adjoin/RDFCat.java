package com.unit4.adjoin;

import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit4.cli.U4Argument;
import com.unit4.cli.U4CLI;
import com.unit4.cli.U4Declaration;
import com.unit4.cli.U4Handler;
import com.unit4.cli.U4Options;
import com.unit4.input.U4InputI;
import com.unit4.input.U4InputF;
import com.unit4.output.U4Output;
import com.unit4.output.U4OutputFactory;
import com.unit4.tabular.U4Columns;
import com.unit4.tabular.U4Common;
import com.unit4.tabular.U4Row;
import com.unit4.vocabulary.U4AdJoinTemplate;

public class RDFCat {

//	Class.
	
	private static Logger logger = LoggerFactory.getLogger(RDFCat.class);
	
	public static final String HEADER_URN = "Header";
	public static final String FOOTER_URN = "Footer";
	public static final String BEFORE_ROW_URN = "BeforeRow";
	public static final String AFTER_ROW_URN = "AfterRow";
	public static final String ROW_URN = "Row";
	public static final String COLUMNS_URN = "Columns";

    public static final String DEFAULT_TEMPLATE = "file:Default.ttl";
	
    public static void main(String[] args) {
		new RDFCat().go(args);
		logger.info("Goodbye.");
    }

//    Instance.

    public final U4Declaration VALUE = new U4Declaration(
		new U4Handler() {
			public void go(U4Argument argument) {
				parse(argument);
			}
		}
    );
    
    public final U4Declaration ADD_TEMPLATE = new U4Declaration(
    	U4Declaration.REQUIRE_VALUE, 
		new U4Handler() {
			public void go(U4Argument argument) {
				addTemplate(argument.getValue());
			}
		},
		Arrays.asList("addTemplate")
	);
   
    public final U4Declaration ADD_GROUP = new U4Declaration(
		U4Declaration.REQUIRE_VALUE, 
		new U4Handler() {
			public void go(U4Argument argument) {
				addGroup(argument.getValue());
			}
		},
		Arrays.asList("addGroup")
	);
    
    public final U4Declaration OUTPUT_URI = new U4Declaration(
		U4Declaration.NO_REQUIRE_VALUE,
		new U4Handler() {
			public void go(U4Argument argument) {
				setOption("outputURI", argument.getValue());
			}
		},
		Arrays.asList("o", "outputURI")
	);
    
    public final U4Declaration OUTPUT_LANGUAGE = new U4Declaration(
		U4Declaration.REQUIRE_VALUE,
		new U4Handler() {
			public void go(U4Argument argument) {
				setOption("outputLanguage", argument.getValue());
			}
		},
		Arrays.asList("outputLanguage")
	);
    
    public final U4Declaration INPUT_MAX_ROWS = new U4Declaration(
    		U4Declaration.NO_REQUIRE_VALUE,
    		new U4Handler() {
				public void go(U4Argument argument) {
					setOption("inputMaxRows", argument.getValue());
				}
			},
			Arrays.asList("inputMaxRows")
	);
    
    public final U4Declaration WIZARD = new U4Declaration(
    		U4Declaration.REQUIRE_VALUE,
    		Arrays.asList("Example", "RDFS", "IATI", "CSV2Template"),
    		new U4Handler() {
				public void go(U4Argument argument) {
					wizard(argument.getValue());
				}
			},
			Arrays.asList("wizard"),
			U4Declaration.NO_LOOK_FOR,
			U4Declaration.NO_END_CLI
	);
    
    public final U4Declaration DEBUG = new U4Declaration(
    		U4Declaration.REQUIRE_VALUE,
    		Arrays.asList("Common"),
    		new U4Handler() {
				public void go(U4Argument argument) {
					setOption(argument.getText(), true);
				}
			},
			Arrays.asList("debug"),
			U4Declaration.NO_LOOK_FOR,
			U4Declaration.NO_END_CLI
	);
    
    private U4CLI cli = new U4CLI(new ArrayList<U4Declaration>(Arrays.asList(VALUE, INPUT_MAX_ROWS, ADD_TEMPLATE, ADD_GROUP, OUTPUT_URI, OUTPUT_LANGUAGE, WIZARD, DEBUG)));
    
    private U4Options options = new U4Options();
    
    private final U4AdJoinTemplate template = new U4AdJoinTemplate();
    
    public RDFCat() {
    	logger.info("Initialising RDFCat.");
    	addTemplate(DEFAULT_TEMPLATE);
    	addGroup(U4AdJoinTemplate.DEFAULT_TEMPLATE_GROUP);
    }
    
    protected U4AdJoinTemplate getTemplate() {
    	return this.template;
    }
    
    protected void addTemplate(String uri) {
    	logger.info("Adding template [{}].", uri);
    	getTemplate().addTemplate(uri);
    }
    
    protected void addGroup(String name) {
    	logger.info("Adding group [{}].", name);
    	getTemplate().addGroup(name);
    }
    
    protected void setOption(String key, Object value) {
    	logger.info("Setting option [{}={}].", key, value);
    	getOptions().setOption(key, value);
    }
    
    protected U4Options getOptions() {
    	return this.options;
    }

    protected void wizard(String value) {
    	logger.info("Configuring wizard [{}].", value);
    	if (value.equals("Example")) {
    		addTemplate("file:Example.ttl");
    		addGroup("Example");
    	} else if (value.equals("RDFS")) {
    		addTemplate("file:RDFS.ttl");
    		addGroup("RDFS");
    	} else if (value.equals("IATI")) {
    		addTemplate("file:IATI.ttl");
    		addGroup("IATI");
    	} else if (value.equals("CSV2Template")) {
    		addTemplate("file:CSV2Template.ttl");
    		addGroup("CSV2Template");
    	}
    }
    
    protected void go(String[] args) {
       	cli.go(args);
    }
    
    protected void parse(U4Argument argument) {
    	logger.info("Parsing [{}].", argument.getValue());

    	final U4Common common = new U4Common();
    	final U4InputI input = U4InputF.getInstance().createInputByURI(argument.getValue());
    	final U4Output output = U4OutputFactory.getInstance().createOutputByType("RDF");
    	final U4Options options = getOptions();
    	
    	options.setOption("inputURI", argument.getValue());
    	
    	input.setOptions(options);
    	output.setOptions(options);
    	
    	common.setInput(input);
    	common.setOutput(output);
    	common.setOptions(options);
    	common.setValue("defaultTemplatePriority", getOptions().getOption("defaultTemplatePriority", 65000));
		common.setTemplate(getTemplate());
    	common.setColumns(new U4Columns());
		common.setRow(new U4Row());

    	output.processInput();
		output.render();
		
		if (options.hasOption("--debug:Common")) {
			logger.info("{}", common.toString());
		}
    }
}
