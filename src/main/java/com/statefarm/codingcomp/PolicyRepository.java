package com.statefarm.codingcomp;

import com.statefarm.codingcomp.model.Policy;
import com.statefarm.codingcomp.reader.Reader;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class PolicyRepository {

    private List<Policy> policies;

    public PolicyRepository() {
        try {
            policies = new Reader().read();
        } catch (Exception ex) {
            policies = null;
        }
    }

    public List<Policy> getAllPoliciesByState(String state){
        List<Policy> statePolicies = new LinkedList<>();
        for (Policy policy : policies) {
            if (policy.getState().trim().toUpperCase().equals(state.trim().toUpperCase())) {
                statePolicies.add(policy);
            }
        }
        return statePolicies;
    }

    public List<Policy> getPoliciesByPolicyType(String policyType){
        List<Policy> rentersPolicy = new LinkedList<>();
        for (Policy policy : policies) {
            if (policy.getPolicyType().trim().toUpperCase().equals(policyType.trim().toUpperCase())) {
                rentersPolicy.add(policy);
            }
        }
        return rentersPolicy;
    }

    public List<Policy> getPolicyByStateAndType(String state, String policyType){
        List<Policy> returnPolicies = getPoliciesByPolicyType("Renters");
        for (Policy policy : policies) {
            if (policy.getState().trim().toUpperCase().equals(state.trim().toUpperCase())) {
                if (policy.getPolicyType().trim().toUpperCase().equals(policyType.trim().toUpperCase())){
                    returnPolicies.add(policy);
                }
            }
        }
        return returnPolicies;
    }

    // ------------------- DISTINCT DATA SET --------------------------
    public Set<String> getDistinctPolicyTypes(){
        Set<String> distinctPolicy = new HashSet<>();
        for(Policy policy: policies){
            distinctPolicy.add(policy.getPolicyType());
        }
        return distinctPolicy;
    }

    public Set<String> getDistinctStates(){
        Set<String> distinctStates = new HashSet<>();
        for(Policy policy: policies){
            distinctStates.add(policy.getState());
        }
        return distinctStates;
    }

    public Set<Integer> getDistinctAge(){
        Set<Integer> distinctAge = new HashSet<>();
        for(Policy policy: policies){
            distinctAge.add(policy.getAge());
        }
        return distinctAge;
    }


    public List<Policy> getAllPolicies(){
        return policies;
    }

}
