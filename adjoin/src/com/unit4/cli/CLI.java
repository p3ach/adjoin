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

//	Instance.
	
	private Boolean autoHelp;
	
	private List<Declaration> declarations;

	private List<Argument> arguments;

	private final Declaration HELP = new Declaration(
			new Handler() {
				public void go(Argument argument) {
					autoHelp();
				}
			},
			Arrays.asList("h", "help"),
			Declaration.LOOK_FOR
		);
	
	
//	Constructors.
	
	public CLI() {
		this(AUTO_HELP, new ArrayList<Declaration>());
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
		if (getAutoHelp()) {
			this.declarations.add(HELP);
		}
		return this;
	}
	
	public List<Declaration> getDeclarations() {
		return this.declarations;
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
	
	public void go(String[] argv) {
		setArguments(argv);
		go();
	}
	
	public void go() {
		for (Declaration declaration : getDeclarations()) {
			if (declaration.getLookFor()) {
				for (Argument argument : getArguments()) {
					if (declaration.equals(argument)) {
						declaration.getHandler().go(argument);
					}
				}
			}
		}
		
        for (Argument argument : getArguments()) {
            if (endProcessing(argument)) {
                break ;
            }
            if (ignoreArgument(argument)) {
                continue ;
            }
            
	        Declaration declaration = match(argument);
	        if (declaration == null) {
        		unknownArgument(argument);
	        } else {
        		if (declaration.hasValidValues()) {
        			if (argument.hasValue()) {
        				if (declaration.getValidValues().contains(argument.getValue())) {
        					declaration.getHandler().go(argument);
        				} else {
        					invalidValue(argument);
        				}
        			} else {
        				missingValue(declaration);
        			}
        		} else {
               		declaration.getHandler().go(argument);
        		}
	        }
        }

        if (getArguments().size() == 0) {
			autoHelp();
		}
	}

	public Declaration match(Argument argument) {
		for (Declaration declaration : getDeclarations()) {
			if (declaration.hasName(argument.getName())) {
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

	public void autoHelp() {
		for (Declaration declaration : getDeclarations()) {
			System.out.println(String.format("%s", declaration.toString()));
		}
	}
	
	public Object render() {
		return renderAsText();
	}
	
	public String renderAsText() {
		StringBuilder sb = new StringBuilder();
		for (Declaration declaration : getDeclarations()) {
//			sb.append(ad.getNames() + ":" + ad.getValue() + "\n");
		}
		return sb.toString();
	}
	
	public String toString() {
		return String.format("CLI [%s]", getDeclarations());
	}
}
