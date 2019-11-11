package com.statefarm.codingcomp.model;

import com.statefarm.codingcomp.PolicyRepository;

import java.util.List;

public class PolicySummary {

    private int totalPolicyHolders;
    private int minAge = Integer.MAX_VALUE;
    private int maxAge = Integer.MIN_VALUE;
    private double minPremium= Integer.MAX_VALUE;
    private double maxPremium = Integer.MIN_VALUE;
    private double totalPremium;

    private int minIncidents= Integer.MAX_VALUE;
    private int maxIncidents= Integer.MIN_VALUE;
    private int totalIncidents = 0;

    private String state;
    private String policyType;
    private List<Policy> policies;

    public PolicySummary(String state, String policyType){
        this.setState(state);
        this.setPolicyType(policyType);
        this.policies = retrievePolicies();
        populateFields();
    }

    private List<Policy> retrievePolicies(){
        PolicyRepository repo = new PolicyRepository();
        return repo.getPolicyByStateAndType(getState(), getPolicyType());
    }

    private void populateFields(){
        for(Policy policy: policies){
            int currentAge = policy.getAge();
            double currentPremium = policy.getAnnualPremium();
            int currentIncidents = policy.getNumberOfAccidents();

            setTotalPolicyHolders(getTotalPolicyHolders() + 1);
            if (currentAge > getMaxAge()){
                setMaxAge(currentAge);
            }
            else if (currentAge < getMinAge()){
                setMinAge(currentAge);
            }

            if (currentPremium > getMaxPremium()){
                setMaxPremium(currentPremium);
            }
            else if  (currentPremium < getMinPremium()){
                setMinPremium(currentPremium);
            }

            if (currentIncidents < getMinIncidents()){
                setMinIncidents(currentIncidents);
            }
            else if (currentIncidents > getMaxIncidents()){
                setMaxIncidents(currentIncidents);
            }

            setTotalIncidents(getTotalIncidents() + currentIncidents);
            setTotalPremium(getTotalPremium() + currentPremium);
        }
    }

    public int getTotalPolicyHolders() {
        return totalPolicyHolders;
    }

    public void setTotalPolicyHolders(int totalPolicyHolders) {
        this.totalPolicyHolders = totalPolicyHolders;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public double getMinPremium() {
        return minPremium;
    }

    public void setMinPremium(double minPremium) {
        this.minPremium = minPremium;
    }

    public double getMaxPremium() {
        return maxPremium;
    }

    public void setMaxPremium(double maxPremium) {
        this.maxPremium = maxPremium;
    }

    public double getTotalPremium() {
        return totalPremium;
    }

    public void setTotalPremium(double totalPremium) {
        this.totalPremium = totalPremium;
    }

    public int getMaxIncidents() {
        return maxIncidents;
    }

    public void setMaxIncidents(int maxIncidents) {
        this.maxIncidents = maxIncidents;
    }

    public int getMinIncidents() {
        return minIncidents;
    }

    public void setMinIncidents(int minIncidents) {
        this.minIncidents = minIncidents;
    }

    public int getTotalIncidents() {
        return totalIncidents;
    }

    public void setTotalIncidents(int totalIncidents) {
        this.totalIncidents = totalIncidents;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }
}
