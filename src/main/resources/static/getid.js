


var theMarker = []
async function getSamplesById() {

    let tagid = document.getElementById('tagid').value
    const ids = tagid.split(" ")

    let url = 'http://localhost:8080/api_v1/disposal?'

    for(i=0; i<ids.length; i++) {
        url = url + 'id=' + ids[i] + '&'
    }

    const response = await fetch(url)
    const r = await response.text()
    console.log(r)

    let output = ' '
    output +=
        `<tr class="user-element">
                <td class="first-name">${r}</td>
            </tr>`
        ;

    document.getElementById('user-list').innerHTML = output;



}