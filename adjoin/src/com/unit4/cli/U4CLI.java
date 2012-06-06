package com.unit4.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit4.exception.U4Exception;

/**
 * 
 * @author dick
 *
 */
public class U4CLI {
	
//	Class.

	private static Logger logger = LoggerFactory.getLogger(U4CLI.class);

	public static final Boolean AUTO_HELP = true;
	public static final Boolean NO_AUTO_HELP = false;

//	Instance.
	
	private Boolean autoHelp;
	
	private List<U4Declaration> declarations;

	private List<U4Argument> arguments;

	private final U4Declaration HELP = new U4Declaration(
			U4Declaration.NO_REQUIRE_VALUE,
			null,
			new U4Handler() {
				public void go(U4Argument argument) {
					autoHelp();
				}
			},
			Arrays.asList("h", "help"),
			U4Declaration.LOOK_FOR,
			U4Declaration.END_CLI
		);
	
	
//	Constructors.
	
	public U4CLI() {
		this(AUTO_HELP, new ArrayList<U4Declaration>());
	}
	
	public U4CLI(List<U4Declaration> declarations) {
		this(AUTO_HELP, declarations);
	}
	
	public U4CLI(Boolean autoHelp, List<U4Declaration> declarations) {
		setAutoHelp(autoHelp);
		setDeclarations(declarations);
	}
	
//	Set/Get (Has).
	
	public U4CLI setAutoHelp(Boolean autoHelp) {
		this.autoHelp = autoHelp;
		return this;
	}
	
	public Boolean getAutoHelp() {
		return this.autoHelp;
	}
	
	public U4CLI setDeclarations(List<U4Declaration> declarations) {
		this.declarations = declarations;
		if (getAutoHelp()) {
			this.declarations.add(HELP);
		}
		return this;
	}
	
	public List<U4Declaration> getDeclarations() {
		return this.declarations;
	}
	
	public U4Declaration getDeclaration(U4Argument argument) {
		for (U4Declaration declaration : getDeclarations()) {
			if (declaration.hasName(argument.getName())) {
				return declaration;
			}
		}
		return null;
	}

	public U4CLI setArguments(String[] argv) {
		List<U4Argument> arguments = new ArrayList<U4Argument>();
		for (String text : argv) {
			arguments.add(new U4Argument(text));
		}
		this.arguments = arguments;
		return this;
	}
	
	public List<U4Argument> getArguments() {
		return this.arguments;
	}
	
	/**
	 * Convenience method to allow easy call from <code>public static void main(String[] args)</code>
	 * @param argv
	 */
	public void go(String[] argv) {
		setArguments(argv);
		go();
	}
	
	/**
	 * Run the CLI.
	 */
	public void go() {
		for (U4Declaration declaration : getDeclarations()) {
			if (declaration.getLookFor()) {
				for (U4Argument argument : getArguments()) {
					if (declaration.equals(argument)) {
						declaration.getHandler().go(argument);
						if (declaration.getEndCLI()) {
							return;
						}
					}
				}
			}
		}
		
        for (U4Argument argument : getArguments()) {
            if (endProcessing(argument)) {
                break ;
            }
            if (ignoreArgument(argument)) {
                continue ;
            }
            
	        U4Declaration declaration = match(argument);
	        if (declaration == null) {
        		unknownArgument(argument);
	        } else {
        		if (declaration.hasValidValues()) {
        			if (argument.hasValue()) {
        				if (declaration.getValidValues().contains(argument.getValue())) {
        					declaration.getHandler().go(argument);
        				} else {
        					invalidValue(declaration, argument);
        				}
        			} else {
        				missingValue(declaration);
        			}
        		} else {
               		declaration.getHandler().go(argument);
					if (declaration.getEndCLI()) {
						return;
					}
        		}
	        }
        }

        if (getArguments().size() == 0) {
			autoHelp();
		}
	}

	public U4Declaration match(U4Argument argument) {
		for (U4Declaration declaration : getDeclarations()) {
			if (declaration.hasName(argument.getName())) {
				return declaration;
			}
		}
		return null;
	}
	
	protected Boolean endProcessing(U4Argument argument) {
		return false;
	}
	
	protected Boolean ignoreArgument(U4Argument argument) {
		return false;
	}
	
	protected void unknownArgument(U4Argument argument) {
		autoHelp();
		throw new U4Exception(String.format("Unknown argument [%s].", argument.toString()));
	}
	
	protected void missingValue(U4Declaration declaration) {
		autoHelp();
		throw new U4Exception(String.format("Missing value for [%s].", declaration));
	}

	protected void invalidValue(U4Declaration declaration, U4Argument argument) {
		autoHelp();
		throw new U4Exception(String.format("Invalid value for [%s] with [%s].", declaration, argument));
	}

	public void autoHelp() {
		logger.info("autoHelp()");
		for (U4Declaration declaration : getDeclarations()) {
			logger.info("{}", declaration.toString());
		}
	}
	
	public Object render() {
		return renderAsText();
	}
	
	public String renderAsText() {
		StringBuilder sb = new StringBuilder();
		for (U4Declaration declaration : getDeclarations()) {
//			sb.append(ad.getNames() + ":" + ad.getValue() + "\n");
		}
		return sb.toString();
	}
	
	public String toString() {
		return String.format("CLI [%s]", getDeclarations());
	}
}
