

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


    });
    document.getElementById('user-list').innerHTML = output;



}