package com.statefarm.codingcomp.enums;

public enum PolicyStatus {
	ACTIVE, CANCELLED_BY_POLICYHOLDER, CANCELLED_BY_COMPANY;
	
	public static PolicyStatus from(int num) {
		if(num <= 2) {
			return ACTIVE;
		} else if(num <= 6) {
			return CANCELLED_BY_POLICYHOLDER;
		} else  {
			return CANCELLED_BY_COMPANY;
		}
	}

	public static Boolean isValidPolicyStatus(String policyStatus) {
		try {
			PolicyStatus.valueOf(policyStatus);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
