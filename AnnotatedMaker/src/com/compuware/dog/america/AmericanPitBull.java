package com.compuware.dog.america;

import com.compuware.dog.Dog;
import com.compuware.service.Service;

@Service
public class AmericanPitBull implements Dog {
	
	public String bark () {
		return "WOOF! WOOF!";
	}

}
