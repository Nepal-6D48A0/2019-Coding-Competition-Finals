package com.statefarm.codingcomp;

import com.statefarm.codingcomp.enums.PolicyStatus;
import com.statefarm.codingcomp.model.AgeDistributionSummary;
import com.statefarm.codingcomp.model.Policy;
import com.statefarm.codingcomp.model.PolicySummary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class PolicyController {

    private static final String[] SUMMARY_TYPES = {"status", "age", "premiums", "incidents", "details"};
    private PolicyRepository repo = new PolicyRepository();

    // This returns all the available (valid) policy types in the dataset.
    // It can be referenced for sending valid requests for other endpoints as needed
    @GetMapping("/api/policyTypes")
    public Set<String> getUniquePolicyTypes() {
        return repo.getDistinctPolicyTypes();
    }

    // This returns all the available (valid) states available in the dataset
    // It can be referenced for sending valid requests for other endpoints as needed
    @GetMapping("/api/states")
    public Set<String> getUniqueStates() {
        return repo.getDistinctStates();
    }

    // This returns all the available (valid) policy status in the dataset
    // It can be referenced for sending valid requests for other endpoints as needed
    @GetMapping("/api/status")
    public PolicyStatus[] getUniquePolicyStatus() {
        return repo.getDistinctPolicyStatus();
    }

    // This returns all the valid age groups in the dataset
    // It can be referenced for sending valid requests for other endpoints as needed
    @GetMapping("/api/ages")
    public List<String> getAgeGroups() {
        return AgeDistributionSummary.getAgeGroups();
    }

    // This returns all the valid summary type used in the application
    // It can be referenced for sending valid requests for other endpoints as needed
    @GetMapping("/api/summaryTypes")
    public String[] getSummaryTypes() {
        return SUMMARY_TYPES;
    }

    // This returns all policies info based on the params provided
    @GetMapping("/api/policies")
    public ResponseEntity<Object> getPolicy(@RequestParam(name = "type", required = false) final String policyType,
                                            @RequestParam(name = "state", required = false) String state,
                                            @RequestParam(name = "status", required = false) String policyStatus) {

        List<Policy> returnPolicy = repo.getAllPolicies();
        if (policyType != null) {
            if (!repo.getDistinctPolicyTypes().contains(policyType)) {
                return new ResponseEntity<>("Invalid policy type. Check /api/policyTypes for all valid values",
                        HttpStatus.BAD_REQUEST);
            }
            if (!policyType.equals("All")) {
                returnPolicy = returnPolicy.stream().filter(x -> x.getPolicyType().equals(policyType))
                        .collect(Collectors.toList());
            }
        }

        if (state != null) {
            if (!repo.getDistinctStates().contains(state)) {
                return new ResponseEntity<>("Invalid state. Check /api/state for all valid values",
                        HttpStatus.BAD_REQUEST);
            }
            if (!state.equals("All")) {
                returnPolicy = returnPolicy.stream().filter(x -> x.getState().equals(state))
                        .collect(Collectors.toList());
            }
        }

        if (policyStatus != null) {
            if (!PolicyStatus.isValidPolicyStatus(policyStatus)) {
                return new ResponseEntity<>("Invalid policy status. Check /api/status for all valid values",
                        HttpStatus.BAD_REQUEST);
            }
            returnPolicy = returnPolicy.stream().filter(x -> x.getPolicyStatus() == PolicyStatus.valueOf(policyStatus))
                    .collect(Collectors.toList());
        }

        return new ResponseEntity<>(returnPolicy, HttpStatus.OK);
    }

    // This returns a summary for state given the summary type and  policy type
    @GetMapping("/api/summary/{state}")
    public Object getSummary(@PathVariable(name = "state") String state,
                             @RequestParam(name = "policyType") String policyType,
                             @RequestParam(name = "summaryType") String summaryType) {

        // Validate if the params are valid
        if (!repo.getDistinctStates().contains(state)) {
            return new ResponseEntity<>(
                    "Invalid state. Check /api/states for all valid values for state",
                    HttpStatus.BAD_REQUEST);
        }

        if (!repo.getDistinctPolicyTypes().contains(policyType)) {
            return new ResponseEntity<>(
                    "Invalid policy type. Check /api/policyTypes for all valid values for policy type",
                    HttpStatus.BAD_REQUEST);
        }

        if (summaryType.equals("status")) {
            return PolicySummary.getStatusSummary(state, repo.getPolicyByStateAndType(state, policyType));
        } else if (summaryType.equals("details")) {
            return new PolicySummary(state, policyType);
        }

        AgeDistributionSummary summary = new AgeDistributionSummary(state, policyType,
                repo.getPolicyByStateAndType(state, policyType));
        switch (summaryType) {
            case "age":
                return summary.getAgeDistribution();
            case "premiums":
                return summary.getAveragePremiumByAge();
            case "incidents":
                return summary.getAverageIncidentsByAge();
            default:
                return new ResponseEntity<>("Invalid summary type. Check /api/summaryTypes for valid values", HttpStatus.BAD_REQUEST);
        }
    }

}
