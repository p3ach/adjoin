package com.unit4.scratch;

import com.hp.hpl.jena.vocabulary.RDFS;
import com.unit4.vocabulary.U4RDFS;

public class Scratch1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(U4RDFS.Resource);
		System.out.println(RDFS.Resource);
		System.out.println(RDFS.Resource.equals(U4RDFS.Resource));
	}

}
