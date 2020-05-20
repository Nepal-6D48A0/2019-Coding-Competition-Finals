package com.statefarm.codingcomp;

import com.statefarm.codingcomp.enums.PolicyStatus;
import com.statefarm.codingcomp.model.Policy;
import com.statefarm.codingcomp.reader.Reader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PolicyRepository {

    private List<Policy> policies;
    private Set<String> distinctPolicies;
    private Set<String> distinctStates;

    public PolicyRepository() {
        try {
            policies = new Reader().read();
            initializeDistinctPolicies();
            initializeDistinctStates();
        } catch (Exception ex) {
            policies = null;
        }
    }

    public List<Policy> getAllPoliciesByState(String state) {
        return state.equals("All") ? policies :
                policies.stream().filter(x -> x.getState().equals(state)).collect(Collectors.toList());
    }

    public List<Policy> getPoliciesByPolicyType(String policyType) {
        return policyType.equals("All") ? policies :
                policies.stream().filter(x -> x.getPolicyType().equals(policyType)).collect(Collectors.toList());
    }

    public List<Policy> getPolicyByStateAndType(String state, String policyType) {
        List<Policy> returnPolicies = getAllPoliciesByState(state);

        return policyType.equals("All") ? returnPolicies :
                returnPolicies.stream().filter(x -> x.getPolicyType().equals(policyType)).collect(Collectors.toList());
    }

    // ------------------- DISTINCT DATA SET --------------------------
    private void initializeDistinctPolicies() {
        distinctPolicies = new HashSet<>();
        for (Policy policy : policies) {
            distinctPolicies.add(policy.getPolicyType());
        }
        distinctPolicies.add("All");
    }

    private void initializeDistinctStates() {
        distinctStates = new HashSet<>();
        for (Policy policy : policies) {
            distinctStates.add(policy.getState());
        }
        distinctStates.add("All");
    }

    public Set<String> getDistinctPolicyTypes() {
        return distinctPolicies;
    }

    public Set<String> getDistinctStates() {
        return distinctStates;
    }

    public PolicyStatus[] getDistinctPolicyStatus() {
        return PolicyStatus.values();
    }

    public Set<Integer> getDistinctAge() {
        Set<Integer> distinctAge = new HashSet<>();
        for (Policy policy : policies) {
            distinctAge.add(policy.getAge());
        }
        return distinctAge;
    }

    public List<Policy> getAllPolicies() {
        return policies;
    }

}
