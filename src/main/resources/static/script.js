var map = L.map('map').setView([41.133118167, 14.783774], 14);

var tiles = L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

async function getSamples() {
    const response = await fetch("http://localhost:8080/api_v1/samples")
    const obj = await response.json()
    return obj;
}

(async () => {
    let x = await getSamples();
    // Loop to access all rows 
    for (let r of x) {
        var marker = L.marker([r['latitude'], r['longitude']]).addTo(map);
        marker.bindPopup("<b>TagID: </b>" + r['tagID'] +
            "<br><b>Coordinates:</b> " + r['latitude'] + ", " + r['longitude'] +
            "<br><b>TruckId:</b> " + r['truckID'] +
            "<br><b>Occurrencies:</b> " + r['occurency']
        )
    }
})()

async function getSampleById() {
    if (document.getElementById('tagid').value) {
        const response = await fetch('http://localhost:8080/api_v1/sample?id=' + document.getElementById('tagid').value);
        const myJson = await response.json(); //extract JSON from the http response
        // do something with myJson
        console.log(myJson);
    } else {
        alert('Inserisci un TAGID');
    }
}

async function getAllSamples() {
    const response = await fetch('http://localhost:8080/api_v1/samples');
    const myJson = await response.json(); //extract JSON from the http response
    // do something with myJson
    console.log(myJson);

    let output = ' '
    myJson.forEach(function (disposal) {
        output +=
            `<tr class="user-element">
                <td class="first-name">${disposal.tagID}</td>
                <td class="first-name">${disposal.timestamp}</td>
                <td class="first-name">${disposal.occurency}</td>
                <td class="first-name">${disposal.truckID}</td>
            </tr>`
            ;
    });
    document.getElementById('user-list').innerHTML = output;

}

document.onload = getAllSamples()