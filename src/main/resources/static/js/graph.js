const ChartColors = {
    red: 'rgb(255, 0, 0)',
    orange: 'rgb(255, 165, 0)',
    yellow: 'rgb(255, 255, 100)',
    green: 'rgb(0, 255, 0)',
    blue: 'rgb(100, 180, 255)',
    purple: 'rgb(155, 50, 255)',
    grey: 'rgb(160, 160, 160)'
};

// Age Distribution Chart
let ageDistLabels;
let ageDistGraphData;

 async function initializeAgeDistributionGraph() {
    let ageGraphContainer = document.getElementById('ageDistributionChart').getContext('2d');

    ageDistLabels = await fetch("/api/ages").then(response => response.json());
    ageDistGraphData = {
        labels: ageDistLabels,
        datasets: [{
            label: 'Total policyholders',
            backgroundColor: Chart.helpers.color(ChartColors.red).alpha(0.5).rgbString(),
            borderColor: ChartColors.red,
            borderWidth: 1,
            data: ageDistLabels.map(label => 0)
        }]
    };

    window.ageBar = new Chart(ageGraphContainer, {
        type: 'bar',
        data: ageDistGraphData,
        options: {
            responsive: true,
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: 'Age Distribution for Policyholders'
            },
            scales: {
                xAxes: [{
                    display: true,
                    gridLines: {
                        display: false
                    },
                    scaleLabel: {
                        display: true,
                        labelString: 'Age groups'
                    }
                }],
                yAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: 'Policyholders'
                    },
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    });
}

function setAgeGraphToZero() {
    ageDistGraphData.datasets[0].data = ageDistGraphData.datasets[0].data.map(x => 0);
    window.ageBar.update();
}

function updateAgeGraph(state, policyType) {
    setAgeGraphToZero();

    const url = `/api/summary/${state}?summaryType=age&policyType=${policyType}`;
    fetch(url)
        .then(response => response.json())
        .then(jsonResponse => {
            let newData = [];
            ageDistLabels.forEach(label => {
                let group = jsonResponse.filter(x => x['ageGroup'] === label)[0];
                newData.push(group.policyholderCount);
            });

            ageDistGraphData.datasets[0].data = newData;
            window.ageBar.options.title.text = `${policyType} Policyholders by age in ${state}`;
            window.ageBar.update();
        })
        .catch(error => console.log(error));
}

// Policy Status Chart
let statusTypes;
let policyStatusData;

function getRandomColors(totalItems) {
    let possibleColors = Object.keys(ChartColors);
    for (let i = 0; i < possibleColors.length; i++) {
        let randomIndex = Math.floor(Math.random() * possibleColors.length);
        let temp = possibleColors[i];
        possibleColors[i] = possibleColors[randomIndex];
        possibleColors[randomIndex] = temp;
    }

    if (totalItems <= possibleColors.length) {
        return possibleColors.slice(0, totalItems);
    } else {
        console.log("Add more colors");
        return possibleColors;
    }
}

async function initializeStatusGraph() {
    let policyStatusContainer = document.getElementById('policy-status-chart').getContext('2d');

    statusTypes = await fetch('/api/status')
                            .then(response => response.json())
                            .then(jsonResponse => jsonResponse.map(x => x.replace(/_/g, " ")));

    let initialData = statusTypes.map(x => 0);
    let backgroundColor = getRandomColors(statusTypes.length).map(color => ChartColors[color]);

    policyStatusData = {
        datasets: [{
            data: initialData,
            backgroundColor: backgroundColor
        }],
        labels: statusTypes
    };

    window.statusDoughnut = new Chart(policyStatusContainer, {
        type: 'doughnut',
        data: policyStatusData,
        options: {
            responsive: true,
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: 'Policy Status for Policy Holders'
            },
            animation: {
                animateScale: true,
                animateRotate: true
            }
        }
    });
}

function setStatusGraphToZero() {
    policyStatusData.datasets[0].data = policyStatusData.datasets[0].data.map(x => 0);
    window.statusDoughnut.update();
}

function updateStatusGraph(state, policyType) {
    setStatusGraphToZero();

    const url = `/api/summary/${state}?summaryType=status&policyType=${policyType}`;
    fetch(url)
        .then(response => response.json())
        .then(jsonResponse => {
            let newData = [], newLabels = [];
            for (let key in jsonResponse) {
                newLabels.push(key.replace(/_/g, " "));
                newData.push(jsonResponse[key]);
            }
            let newBackgroundColor = getRandomColors(newData.length).map(color => ChartColors[color]);

            policyStatusData.datasets[0].data = newData;
            policyStatusData.datasets[0].backgroundColor = newBackgroundColor;
            policyStatusData.labels = newLabels;

            window.statusDoughnut.options.title.text = `Policy status of ${policyType} insurance in ${state}`;
            window.statusDoughnut.update();
        })
        .catch(error => console.log(error));
}


// Premium Graph
let premiumDistributionData;

function initializePremiumGraph() {    
    premiumDistributionData =  {
        datasets: [{
            hoverBorderColor: ChartColors.blue,
            borderColor: Chart.helpers.color(ChartColors.blue).alpha(0.75).rgbString(),
            backgroundColor: Chart.helpers.color(ChartColors.blue).alpha(0.5).rgbString(),
            data: [],
            xAxisID: 'x-axis-1',
            yAxisID: 'y-axis-1',
            label: 'Average Annual Premium',
            fill: false,
            showLine: true
        }, {
            hoverBorderColor: ChartColors.red,
            borderColor: Chart.helpers.color(ChartColors.red).alpha(0.75).rgbString(),
            backgroundColor: Chart.helpers.color(ChartColors.red).alpha(0.5).rgbString(),
            borderDash: [3, 3],
            data: [],
            xAxisID: 'x-axis-1',
            yAxisID: 'y-axis-2',
            label: 'Average Incidents',
            fill: false,
            showLine: true
        }]
    };

    let premiumDistributionContainer = document.getElementById('premium-chart').getContext('2d');
    window.premiumChart = Chart.Scatter(premiumDistributionContainer, {
        data: premiumDistributionData,
        options: {
            title: {
                display: true,
                text: 'Average Annual Premium vs Average Incidents for all Policyholders'
            },
            scales: {
                xAxes: [{
                    position: 'bottom',
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: 'Age'
                    }
                }],
                yAxes: [
                    {
                        type: 'linear',
                        display: true,
                        gridLines: {
                            display: false
                        },
                        position: 'left',
                        id: 'y-axis-1',
                        scaleLabel: {
                            display: true,
                            labelString: 'Average Annual Premium'
                        }
                    }, {
                        type: 'linear', // only linear but allow scale type registration. This allows extensions to exist solely for log scale for instance
                        display: true,
                        position: 'right',
                        reverse: true,
                        id: 'y-axis-2',
                        scaleLabel: {
                            display: true,
                            labelString: 'Average Incidents'
                        },
                        // grid line settings
                        gridLines: {
                            drawOnChartArea: false, // only want the grid lines for one axis to show up
                        },
                    }
                ]
            }
        }
    });
}

 async function updatePremiumGraph(state, policyType) {
    premiumDistributionData.datasets.forEach(dataset => {
        dataset.data = [];
    });
    window.premiumChart.update();

    const baseUrl = `/api/summary/${state}?policyType=${policyType}`;

    let newPremiumDistribution = await fetch(`${baseUrl}&summaryType=premiums`).then(response => response.json());
    let newIncidentDistribution = await fetch(`${baseUrl}&summaryType=incidents`).then(response => response.json());

    let newPremiumDataset = [];
     for (let age in newPremiumDistribution) {
         newPremiumDataset.push({ x: age, y: newPremiumDistribution[age] })
     }
     premiumDistributionData.datasets[0].data = newPremiumDataset;

    let newIncidentDataset = [];
    for (let age in newIncidentDistribution) {
        newIncidentDataset.push({x : age, y: newIncidentDistribution[age]});
    }
    premiumDistributionData.datasets[1].data = newIncidentDataset;

    window.premiumChart.options.title.text = `Comparison of ${policyType} insurance Policyholders in ${state}`;
    window.premiumChart.update();
}