

var map = L.map('map').setView([41.133118167, 14.783774], 14);

var tiles = L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

async function getSamplesByDate() {
    let from = document.getElementById('from').value
    let to = document.getElementById('to').value

    const response = await fetch('http://localhost:8080/api_v1/between?from=' + from + '&to=' + to)
    const responseDate = await response.json();
    console.log(responseDate)
    let output = ' '
    responseDate.forEach(function (r) {
        output +=
            `<tr class="user-element">
                <td class="first-name">${r.tagID}</td>
                <td class="first-name">${r.timestamp}</td>
                <td class="first-name">${r.occurency}</td>
                <td class="first-name">${r.truckID}</td>
            </tr>`
            ;

        var marker = L.marker([r['latitude'], r['longitude']]).addTo(map);
        marker.bindPopup("<b>TagID: </b>" + r['tagID'] +
            "<br><b>Coordinates:</b> " + r['latitude'] + ", " + r['longitude'] +
            "<br><b>TruckId:</b> " + r['truckID'] +
            "<br><b>Occurrencies:</b> " + r['occurency']
        )
    });
    document.getElementById('user-list').innerHTML = output;



}