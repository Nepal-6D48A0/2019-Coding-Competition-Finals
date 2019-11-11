package com.statefarm.codingcomp;

import java.util.List;

import com.statefarm.codingcomp.model.Policy;
import com.statefarm.codingcomp.reader.Reader;


public class Analyzer {

	public static void main(String[] args) throws Exception {
		List<Policy> policies = new Reader().read();
		for(Policy policy: policies){
			System.out.println(policy);
		}
	}
}
