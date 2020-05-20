package com.statefarm.codingcomp.model;

import java.util.*;
import java.util.stream.Collectors;

public class AgeDistributionSummary {

    private static final String[] AGE_GROUPS = {"< 20", "20-29", "30-39", "40-49", "50-59", "60-69", "70-79", "80-89", "> 90"};

    private String state;
    private List<Policy> policies;

    public AgeDistributionSummary(String state, String policyType, List<Policy> policies) {
        this.state = state;
        this.policies = policies;

        if (!state.equals("All")) {
            this.policies = this.policies.stream().filter(x -> x.getState().equals(state)).collect(Collectors.toList());
        }

        if (!policyType.equals("All")) {
            this.policies = this.policies.stream().filter(x -> x.getPolicyType().equals(policyType)).collect(Collectors.toList());
        }
    }

    public static List<String> getAgeGroups() {
        return Arrays.asList(AGE_GROUPS);
    }

    public List<Map<String, Object>> getAgeDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        for (String group : AGE_GROUPS) {
            distribution.put(group, 0);
        }

        for (Policy policy : policies) {
            int currentAge = policy.getAge();

            if (currentAge < 20) {
                distribution.put("< 20", distribution.getOrDefault("< 20", 0) + 1);
            } else if (currentAge < 30) {
                distribution.put("20-29", distribution.getOrDefault("20-29", 0) + 1);
            } else if (currentAge < 40) {
                distribution.put("30-39", distribution.getOrDefault("30-39", 0) + 1);
            } else if (currentAge < 50) {
                distribution.put("40-49", distribution.getOrDefault("40-49", 0) + 1);
            } else if (currentAge < 60) {
                distribution.put("50-59", distribution.getOrDefault("50-59", 0) + 1);
            } else if (currentAge < 70) {
                distribution.put("60-69", distribution.getOrDefault("60-69", 0) + 1);
            } else if (currentAge < 80) {
                distribution.put("70-79", distribution.getOrDefault("70-79", 0) + 1);
            } else if (currentAge < 90) {
                distribution.put("80-89", distribution.getOrDefault("80-89", 0) + 1);
            } else {
                distribution.put("> 90", distribution.getOrDefault("> 90", 0) + 1);
            }
        }

        List<Map<String, Object>> summary = new LinkedList<>();
        for (String ageGroup : distribution.keySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("ageGroup", ageGroup);
            item.put("policyholderCount", distribution.get(ageGroup));
            summary.add(item);
        }

        return summary;
    }

    public Map<Integer, Double> getAveragePremiumByAge() {
        Map<Integer, List<Double>> allPremiums = new HashMap<>();

        for (Policy policy : policies) {
            int currentAge = policy.getAge();

            if (!allPremiums.containsKey(currentAge))
                allPremiums.put(currentAge, new LinkedList<>());

            allPremiums.get(currentAge).add(policy.getAnnualPremium());
        }

        Map<Integer, Double> averagePremiums = new HashMap<>();
        for (Integer age : allPremiums.keySet()) {
            List<Double> premiums = allPremiums.get(age);

            if (premiums.size() != 0) {
                double totalPremium = allPremiums.get(age).stream().reduce(0.0, Double::sum);
                double avgPremium = ((int) (totalPremium * 100 / premiums.size())) / 100.0;
                averagePremiums.put(age, avgPremium);
            }
        }

        return averagePremiums;
    }

    public Map<Integer, Double> getAverageIncidentsByAge() {
        Map<Integer, List<Integer>> allIncidents = new HashMap<>();

        for (Policy policy : policies) {
            int currentAge = policy.getAge();

            if (!allIncidents.containsKey(currentAge))
                allIncidents.put(currentAge, new LinkedList<>());

            allIncidents.get(currentAge).add(policy.getNumberOfAccidents());
        }

        Map<Integer, Double> averageIncidents = new HashMap<>();
        for (Integer age : allIncidents.keySet()) {
            List<Integer> incidents = allIncidents.get(age);

            if (incidents.size() != 0) {
                double totalPremium = allIncidents.get(age).stream().reduce(0, Integer::sum);
                double avgPremium = ((int) (totalPremium * 100 / incidents.size())) / 100.0;
                averageIncidents.put(age, avgPremium);
            }
        }

        return averageIncidents;
    }


}
