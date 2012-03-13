package com.unit4.vocabulary;

import java.util.Comparator;

import com.unit4.vocabulary.U4AdJoin;

public class U4ConvertComparator implements Comparator<U4AdJoin> {

	@Override
	public int compare(U4AdJoin o1, U4AdJoin o2) {
		System.out.println(String.format("%s %s", o1, o2));
		Integer p1 = o1.getPriority();
		Integer p2 = o2.getPriority();
		if (p1 == null && p2 == null) {
			return 0;
		}
		
		if (p1 == null) {
			return 1;
		}
		
		if (p2 == null) {
			return -1;
		}
		
		if (p1 > p2) {
			return 1;
		}
		
		if (p1 < p2) {
			return -1;
		}
		
		return 0;
	}
}
