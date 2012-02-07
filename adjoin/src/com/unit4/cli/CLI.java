package com.unit4.cli;

import java.util.Arrays;

import java.util.HashSet;
import java.util.Set;

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
	
//	Instance.

	protected Set<ArgumentDeclaration> declarations = new HashSet<ArgumentDeclaration>();
	
	public CLI(ArgumentDeclaration[] declarations) {
		add(declarations);
	}
	
	protected Set<ArgumentDeclaration> getDeclarations() {
		return this.declarations;
	}
	
	protected void add(ArgumentDeclaration[] declarations) {
		for (ArgumentDeclaration argument : declarations) {
			add(argument);
		}
	}

	protected void add(ArgumentDeclaration declaration) {
		getDeclarations().add(declaration);
	}
	
	protected Boolean has(String name) {
		return (argument(name) != null);
	}
	
	protected ArgumentDeclaration argument(String name) {
		for (ArgumentDeclaration argument : getDeclarations()) {
			if (argument.hasName(name)) {
				return argument;
			}
		}
		return null;
	}
	
	public void process(String[] argv) {
		logger.debug("process({})", Arrays.toString(argv));
		Argument argument;
        for (String arg : argv) {
        	argument = new Argument(arg);
            if (endProcessing(argument)) {
                break ;
            }
            if (ignoreArgument(argument)) {
                continue ;
            }
	        ArgumentDeclaration declaration = match(argument);
	        if (declaration == null) {
	        	unknownArgument(argument);
	        } else {
        		declaration.getHandler().action(argument);
	        }
        }
	}

	protected ArgumentDeclaration match(Argument argument) {
		for (ArgumentDeclaration declaration : getDeclarations()) {
			if (declaration.hasName(argument.name)) {
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
}
