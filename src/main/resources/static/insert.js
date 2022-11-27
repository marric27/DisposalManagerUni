
async function insertSample() {
    let tagid = document.getElementById('tid').value
    let time = document.getElementById('time').value
    let truck = document.getElementById('ttid').value
    let occ = document.getElementById('occ').value
    let lat = document.getElementById('lat').value
    let lon = document.getElementById('lon').value

    let we = JSON.stringify({
        tagID: tagid,
        timestamp: time,
        truckID: truck,
        occurency: occ,
        latitude: lat,
        longitude: lon
    })

    console.log(we)
    var credentials = btoa("admin:admin");

    await fetch('http://localhost:8080/api_v1/producer', {
        method: 'POST',
        headers: {
            'Authorization': 'Basic ${credentials}',
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: we
    })
        .then(response => response.json())
        .then(response => console.log(JSON.stringify(response)))
}