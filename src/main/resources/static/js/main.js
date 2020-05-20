let states = [];
let policyTypes = [];

function populatePolicyTypes() {
    fetch('/api/policyTypes')
        .then(response => response.json())
        .then(jsonResponse => {
            addPolicyTypeToSelect(jsonResponse);
        })
        .catch(error => console.log(error));
}

function populateStates() {
    fetch('/api/states')
        .then(response => response.json())
        .then(jsonResponse => {
            states = jsonResponse.filter(x => x !== 'All');
            addStatesToSelect();
            drawMap();
        })
        .catch(error => console.log(error));
}

function updateSummary(state, policyType) {

    // Update title for summary
    updateSummaryTitle(state);

    const url = `/api/summary/${state}?policyType=${policyType}&summaryType=details`;
    fetch(url).then(response => response.json())
    .then(jsonResponse => updateSummaryDetailsForPolicyType(policyType, jsonResponse))    
    .catch(error => showErrorSummaryDetailsForPolicyType(policyType));
    
    
}

// Event Listeners
$('#state-select').on("change", function() {
    let selectedState = $(this).val();
    let selectedPolicyType = $("#policy-type-select").val();
    updateDetails(selectedState, selectedPolicyType);
});

$("#policy-type-select").on("change", function() {
    let selectedState = $("#state-select").val();
    let selectedPolicyType = $(this).val();
    updateDetails(selectedState, selectedPolicyType);
});

function updateDetails(state, policyType) {
    drawMap();
    updateSummary(state, policyType);
    updateAgeGraph(state, policyType);
    updateStatusGraph(state, policyType);
    updatePremiumGraph(state, policyType);
}


