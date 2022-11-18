

async function getSamplesByDistance() {

    let lat = document.getElementById('lat').value
    let lon = document.getElementById('lon').value
    let dis = document.getElementById('dis').value

    const response = await fetch('http://localhost:8080/api_v1/distance?lat=' + lat + '&lon=' + lon + '&dis=' + dis)
    const responseDistance = await response.json();
    //console.log(responseDistance)
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
            
        
    });


    document.getElementById('user-list').innerHTML = output;



}