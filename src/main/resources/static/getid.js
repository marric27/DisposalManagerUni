

var map = L.map('map').setView([41.133118167, 14.783774], 14);

var tiles = L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

var theMarker = []
async function getSamplesById() {

    let tagid = document.getElementById('tagid').value

    const response = await fetch('http://localhost:8080/api_v1/sample?id=' + tagid)
    const r = await response.json();
    //console.log(responseDistance)
    let output = ' '
    r/*.forEach(function (r) {*/
        output +=
            `<tr class="user-element">
                <td class="first-name">${r.tagID}</td>
                <td class="first-name">${r.timestamp}</td>
                <td class="first-name">${r.occurency}</td>
                <td class="first-name">${r.truckID}</td>
            </tr>`
            ;
            
        if (theMarker != undefined) {
            map.removeLayer(theMarker);
        };
        theMarker = L.marker([r['latitude'], r['longitude']]).addTo(map)
    //});


    document.getElementById('user-list').innerHTML = output;



}