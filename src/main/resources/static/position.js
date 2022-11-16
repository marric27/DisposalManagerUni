

var map = L.map('map').setView([41.133118167, 14.783774], 14);

var tiles = L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

async function getSamplesByDistance() {
    let lat = document.getElementById('lat').value
    let lon = document.getElementById('lon').value
    let dis = document.getElementById('dis').value

    const response = await fetch('http://localhost:8080/api_v1/distance?lat=' + lat + '&lon=' + lon + '&dis=' + dis)
    const responseDistance = await response.json();
    console.log(responseDistance)
    let output = ' '
    responseDistance.forEach(function (r) {
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