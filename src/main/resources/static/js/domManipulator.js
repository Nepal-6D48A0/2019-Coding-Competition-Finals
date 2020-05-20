let $summary = $('#summary');
let $summaryTitle = $("#summary-title");
let $summaryDetails = $('#summary-details');

function addStatesToSelect() {
    let $dropdown = $("#state-select");
    states.sort();
    for (let i = 0; i < states.length; i++) {
        $dropdown.append($(`<option value="${states[i]}">${states[i]}</option>`));
    }
}

function addPolicyTypeToSelect(policyTypes) {
    let $dropdown = $("#policy-type-select");
    states.sort();
    for (let i = 0; i < policyTypes.length; i++) {
        $dropdown.append($(`<option value="${policyTypes[i]}">${policyTypes[i]}</option>`));
    }
}

function updateSummaryTitle(state) {
    if (statesAbbr.hasOwnProperty(state)) {
        state = statesAbbr[state];
    }
    $summaryTitle.text(`Summary for ${state}`);
}

function updateSummaryDetailsForPolicyType(policyType, data) {
    $("#policy-type").text(`${policyType}`);
    $("#total-policyholders").text(`${data.totalPolicyHolders}`);
    $("#average-age").text(`${data.averageAge}`);
    $("#average-premium").text(`\$ ${(data.totalPremium / data.totalPolicyHolders).toFixed(2)}`);
    $("#average-incidents").text(`${(data.totalIncidents / data.totalPolicyHolders).toFixed(2)}`);
}

function showErrorSummaryDetailsForPolicyType(policyType, data) {
    $("#policy-type").text(`${policyType}`);
    $("#total-policyholders").text(`-`);
    $("#average-age").text(`-`);
    $("#average-premium").text(`\$ 0.00`);
    $("#average-incidents").text(`-`);
}

function summaryTooltip(state, data) {
    return `<h4>${state}</h4>`;
}

function drawMap() {
    $("#statesvg").html("");
    let resultMap = {};
    let selectedState = $('#state-select').val();

    states.forEach(state => {
        let color = (state === selectedState) ? d3.rgb(255, 0, 0) : d3.rgb(255, 255, 255);
        resultMap[state] = {
            color: color
        }
    });

    uStates.draw("#statesvg", resultMap, summaryTooltip);
    d3.select(self.frameElement).style("height", "600px");
}