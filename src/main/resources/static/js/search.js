const extractSingleSelectedItem = (selectId) => {
    const selected = $('#' + selectId).val()
    return selected ? selected : null;
}

const searchTable = tariffs => {
    document.getElementById("searchTableBlock").style.display = "block";
    const table = $('#userSearchTable');
    table.find('tr:not(:first)').remove();
    tariffs.forEach(tariff =>{
        const tr1 = $('<tr></tr>');
        const td1 = $('<td>' + tariff.startPoint + '</td>');
        const td2 = $('<td>' + tariff.endPoint + '</td>');
        const td3 = $('<td>' + tariff.distance + '</td>');
        const td4 = $('<td>' + tariff.time + '</td>');
        const td5 = $('<td>' + tariff.type + '</td>');
        const td6 = $('<td>' + tariff.price + '</td>');
        const td7 = $('<td>' + (tariff.price * tariff.distance).toFixed(2) + '</td>');
        const td8 = $('<td>' + tariff.maxWeight + '</td>');
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
        tr1.append(td1, td2,td3, td4, td5, td6, td7, td8, tdDOP3, tdDOP4);
        $('#userSearchTable').append(tr1);
    })
}

function clearForm() {
    document.getElementById("userSearchTypes").selectedIndex = 0;
    document.getElementById("price").value = "";
    document.getElementById("weight").value = "";
    document.getElementById("startPoint").value = "";
    document.getElementById("endPoint").value = "";
    document.getElementById("distance").value = "";
    document.getElementById("time").value = "";
}

$(document).ready(() => {
    $('#goodsCountForm').submit(event => {
        event.preventDefault();
        let body = {};
        body.startPoint = $('#startPoint').val();
        body.endPoint = $('#endPoint').val();
        body.distance = Number.parseFloat($('#distance').val());
        body.time = Number.parseFloat($('#time').val());
        body.type = extractSingleSelectedItem('userSearchTypes');
        body.price = Number.parseFloat($('#price').val());
        body.maxWeight = Number.parseFloat($('#weight').val());
        console.log(body);
        ajaxPOST('/about/tariffs', body, tariffs => {
            console.log("результат фильтрации:")
            console.log(tariffs)
            searchTable(tariffs)
        })
    });

});
