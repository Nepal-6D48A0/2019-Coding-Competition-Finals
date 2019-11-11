package com.statefarm.codingcomp.model;

import com.statefarm.codingcomp.enums.PolicyStatus;

public class Policy {

	private String[] policy;
	
	public Policy(String[] policy) {
		this.policy = policy;
	}

	public String getPolicyType() {
		return policy[27];
	}

	public PolicyStatus getPolicyStatus() {
		return PolicyStatus.from(Integer.parseInt(policy[33]));
	}

	public String getState() {
		return policy[34];
	}

	public double getAnnualPremium() {
		return Double.parseDouble(policy[38]);
	}

	public int getAge() {
		return Integer.parseInt(policy[39]);
	}

	public int getNumberOfAccidents() {
		return Integer.parseInt(policy[40]);
	}

	@Override
	public String toString() {
		StringBuilder retVal = new StringBuilder();
		retVal.append(String.format("Policy Type: %s\n", getPolicyType()));
		retVal.append(String.format("Policy Status: %s\n", getPolicyStatus()));
		retVal.append(String.format("State: %s\n", getState()));
		retVal.append(String.format("Annual Premium: %s\n", getAnnualPremium()));
		retVal.append(String.format("Age: %s\n", getAge()));
		retVal.append(String.format("Number of Accidents: %s\n\n", getNumberOfAccidents()));

		return retVal.toString();
	}
}
