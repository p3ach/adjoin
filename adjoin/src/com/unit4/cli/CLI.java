package com.unit4.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit4.exception.Exception;

/**
 * 
 * @author dick
 *
 */
public class CLI {
	
//	Class.

	private static Logger logger = LoggerFactory.getLogger(CLI.class);

	public static final Boolean AUTO_HELP = true;
	public static final Boolean NO_AUTO_HELP = false;
	
	public static final List<Declaration> EMPTY_DECLARATIONS = new ArrayList<Declaration>();
	
//	Instance.
	
	private Boolean autoHelp;
	
	private List<Declaration> declarations;

	private List<Argument> arguments;
	
	private final Declaration HELP = new Declaration(
		new Handler() {
			public void action(Argument argument) {
			}
		},
		Arrays.asList("-h", "--help"),
		Declaration.LOOK_FOR
	);
	
//	Constructors.
	
	public CLI() {
		this(AUTO_HELP, EMPTY_DECLARATIONS);
	}
	
	public CLI(List<Declaration> declarations) {
		this(AUTO_HELP, declarations);
	}
	
	public CLI(Boolean autoHelp, List<Declaration> declarations) {
		setAutoHelp(autoHelp);
		setDeclarations(declarations);
	}
	
//	Set/Get (Has).
	
	public CLI setAutoHelp(Boolean autoHelp) {
		this.autoHelp = autoHelp;
		return this;
	}
	
	public Boolean getAutoHelp() {
		return this.autoHelp;
	}
	
	public CLI setDeclarations(List<Declaration> declarations) {
		this.declarations = declarations;
		return this;
	}
	
	public List<Declaration> getDeclarations() {
		List<Declaration> declarations = this.declarations;
		if (getAutoHelp()) {
			declarations.add(HELP);
		}
		return declarations;
	}
	
	public Declaration getDeclaration(Argument argument) {
		for (Declaration declaration : getDeclarations()) {
			if (declaration.hasName(argument.getName())) {
				return declaration;
			}
		}
		return null;
	}

	public CLI setArguments(String[] argv) {
		List<Argument> arguments = new ArrayList<Argument>();
		for (String text : argv) {
			arguments.add(new Argument(text));
		}
		this.arguments = arguments;
		return this;
	}
	
	public List<Argument> getArguments() {
		return this.arguments;
	}
	
	public void process(String[] argv) {
		setArguments(argv);
		process();
	}
	
	public void process() {
		List<Argument> arguments = getArguments();
		List<Declaration> declarations = getDeclarations();
		
		for (Declaration declaration : declarations) {
			if (declaration.getLookFor()) {
				for (Argument argument : arguments) {
					if (declaration.equals(argument)) {
						declaration.getHandler().action(argument);
					}
				}
			}
		}
		
		Argument argument;
        for (String arg : argv) {
        	argument = new Argument(arg);
            if (endProcessing(argument)) {
                break ;
            }
            if (ignoreArgument(argument)) {
                continue ;
            }
	        for (Declaration declaration : getDeclarations()) {
	        	if (declaration.hasName(argument.getName())) {
	        		if (declaration.hasValidValues()) {
	        			if (argument.hasValue()) {
	        				if (declaration.hasValidValues() && declaration.getValidValues().contains(argument.getValue())) {
	        					declaration.getHandler().action(argument);
	        				} else {
	        					invalidValue(argument);
	        				}
	        			} else {
	        				missingValue(declaration);
	        			}
	        		} else {
	               		declaration.getHandler().action(argument);
	        		}
	        	} else {
	        		unknownArgument(argument);
	        	}
	        }
        }
	}

	protected Declaration match(Argument argument) {
		for (Declaration declaration : getDeclarations()) {
			if (declaration.hasName(argument.name)) {
				if (declaration.hasValidValues()) {
					if (declaration.getValidValues().contains(argument.getValue())) {
						return declaration;
					} else {
					}
				}
				return declaration;
			}
		}
		return null;
	}
	
	protected Boolean endProcessing(Argument argument) {
		return false;
	}
	
	protected Boolean ignoreArgument(Argument argument) {
		return false;
	}
	
	protected void unknownArgument(Argument argument) {
		throw new Exception(String.format("Unknown argument [%s].", argument.toString()));
	}
	
	protected void missingValue(Declaration declaration) {
		throw new Exception(String.format("Missing value for [%s].", declaration));
	}

	protected void invalidValue(Argument argument) {
		throw new Exception(String.format("Invalid value for [%s].", argument));
	}
	
	public Object render() {
		return renderAsText();
	}
	
	public String renderAsText() {
		StringBuilder sb = new StringBuilder();
		for (Declaration ad : getDeclarations()) {
//			sb.append(ad.getNames() + ":" + ad.getValue() + "\n");
		}
		return sb.toString();
	}
	
	public String toString() {
		return String.format("CLI [%s]", getDeclarations());
	}
}
