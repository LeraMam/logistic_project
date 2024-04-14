const extractSingleSelectedItem = (selectId) => {
    const selected = $('#' + selectId).val()
    return selected ? selected : null;
}

const searchPointsTable = tariffs => {
    document.getElementById("searchTableBlock").style.display = "block";
    const table = $('#userSearchTable');
    table.find('tr:not(:first)').remove();
    tariffs.forEach(tariff =>{
        const tr1 = $('<tr></tr>');
        const td1 = $('<td>' + tariff.startPoint + '</td>');
        const td2 = $('<td>' + tariff.startPoint + '</td>');
        const td3 = $('<td>' + tariff.endPoint + '</td>');
        const td4 = $('<td>' + tariff.time + '</td>');
        const td5 = $('<td>' + tariff.type + '</td>');
        const td6 = $('<td>' + (tariff.price * tariff.distance).toFixed(2) + '</td>');
        const td7 = $('<td>' + tariff.maxWeight + '</td>');
        const tdDOP3 = $('<td></td>');
        const tdDOP4 = $('<td></td>');
        /*<input id="companyButton" type="submit" value="Войти" className="btn btn-primary">*/
        const button = $(' <input id="companyButton" type="submit" value="Бронь" class="btn btn-primary">')
        button.click(() => {
            console.log("BUTTON!");
        })
        const button2 = $(' <input id="profileButton" type="submit" value="Профиль" class="btn btn-primary">')
        button.click(() => {
            console.log("BUTTONforprofile!");
        })
        tdDOP3.append(button)
        tdDOP4.append(button2)
        tr1.append(td1, td2,td3, td4, td5, td6, td7, tdDOP3, tdDOP4);
        $('#userSearchTable').append(tr1);
    })
}

/*function handleSelectChange() {
    let object = {};
    let way = '';
    let time = 0;
    let sumPrice = 0;
    let maxPrice = 0;
    var selectedValue = this.value;
    let selectIntervalWaysElement = $('<select style="margin-top: 20px"></select>');
    ajaxGET('/about/tariff/way/' + selectedValue, tariffs => {
        console.log(tariffs)
        for(let i = 0; i < tariffs.length; i++){
            console.log(tariffs[i])
            way += tariffs[i].startPoint + '—';
            let text = '';
            if(( i + 1)=== tariffs.length) {
                text = tariffs[i].endPoint;
            }
            way += text;
        }
        object.way = way;
    })
    console.log(object)
}*/

const searchTable = (companies, body) => {
    document.getElementById("searchTableBlock").style.display = "block";
    const table = $('#userSearchTable');
    table.find('tr:not(:first)').remove();
    companies.forEach(company => {
        company.ways.forEach(way => {
            way.intervalWays.forEach(intervalWay => {
                const tr1 = $('<tr></tr>');
                const tdDOP3 = $('<td></td>');
                const tdDOP4 = $('<td></td>');
                const button = $('<input id="companyButton" type="submit" value="Бронь" class="btn btn-primary">');
                button.click(() => {
                    console.log("BUTTON!");
                });
                const button2 = $('<input id="profileButton" type="submit" value="Профиль" class="btn btn-primary">');
                button2.click(() => {
                    console.log("BUTTONforprofile!");
                });
                tdDOP3.append(button);
                tdDOP4.append(button2);

                let td1 = $('<td>' + company.name + '</td>');
                let td2 = $('<td>' + way.startPoint + '</td>');
                let td3 = $('<td>' + way.endPoint + '</td>');
                let td4 = $('<td></td>');
                let td5 = $('<td></td>');
                let td6 = $('<td>' + intervalWay.sumTime.toFixed(2) + '</td>');
                let td7 = $('<td>' + intervalWay.sumPrice.toFixed(2) + '</td>');
                let td8 = $('<td>' + intervalWay.maxWeight.toFixed(2) + '</td>');
                let textPoints = '';
                let textTransport = '';
                intervalWay.tariffs.forEach(tariff =>{
                    textPoints += tariff.startPoint + '  ';
                    textTransport += tariff.type + '  ';
                })
                textPoints += intervalWay.tariffs[intervalWay.tariffs.length - 1].endPoint;
                td4.append(textPoints)
                td5.append(textTransport)

                tr1.append(td1, td2, td3, td4, td5, td6, td7, td8, tdDOP3, tdDOP4);
                console.log(tr1)
                $('#userSearchTable').append(tr1);
            });
        });
    });
};

function clearForm() {
    document.getElementById("userSearchTypes").selectedIndex = 0;
    document.getElementById("startPoint").value = "";
    document.getElementById("endPoint").value = "";
    document.getElementById("intervalPoint").value = "";
    document.getElementById("weight").value = "";
    document.getElementById("price").value = "";
    document.getElementById("time").value = "";
    document.getElementById("sortByTime").checked = false;
    document.getElementById("sortByPrice").checked = false;
    let body = {};
    ajaxPOST('/about/company/all', body, companies => {
        searchTable(companies)
    })
}

$(document).ready(() => {
    $('#goodsCountForm').submit(event => {
        event.preventDefault();
        let body = {};
        body.type = extractSingleSelectedItem('userSearchTypes');
        body.startPoint = $('#startPoint').val();
        body.endPoint = $('#endPoint').val();
        body.intervalPoint = $('#intervalPoint').val();
        body.maxWeight = Number.parseFloat($('#weight').val());
        body.sumTime = Number.parseFloat($('#time').val());
        body.sumPrice = Number.parseFloat($('#price').val());
        body.sortByTime = $('#sortByTime').prop('checked');
        body.sortByPrice = $('#sortByPrice').prop('checked');
        console.log(body);
        ajaxPOST('/about/company/all', body, companies => {
            console.log(companies);
            searchTable(companies)
        })
    });

});
