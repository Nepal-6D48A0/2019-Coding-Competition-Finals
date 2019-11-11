package com.statefarm.codingcomp;

import com.statefarm.codingcomp.model.Policy;
import com.statefarm.codingcomp.model.PolicySummary;
import com.statefarm.codingcomp.reader.Reader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@RestController
public class PolicyController {

    private PolicyRepository repo = new PolicyRepository();

    @RequestMapping("/")
    public List<Policy> welcome() {
       return repo.getAllPolicies();
    }

    @RequestMapping("/policyType")
    public Set<String> getUniquePolicyTypes(){
        return repo.getDistinctPolicyTypes();
    }

    @RequestMapping("/state")
    public Set<String> getUniqueStates(){
        return repo.getDistinctStates();
    }



    @RequestMapping("/renters")
    public List<Policy> getRentersPolicy(){
        return repo.getPoliciesByPolicyType("Renters");
    }

    @RequestMapping("/renters/summary")
    public List<PolicySummary> getPolicySummaryForRenters(){
        List<PolicySummary> retVal = new LinkedList<>();
        for(String state: repo.getDistinctStates()){
            retVal.add(new PolicySummary(state, "Renters"));
        }
        return retVal;
    }

    @RequestMapping("/passengers/all")
    public List<Policy> getPassengersPolicy(){
        return repo.getPoliciesByPolicyType("Private Passenger");
    }

    @RequestMapping("/passengers")
    public List<Policy> getPassengersPolicyByState(@RequestParam("state") String state){
        return repo.getPolicyByStateAndType(state, "Private Passengers");
    }

}
