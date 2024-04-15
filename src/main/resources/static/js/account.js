
const searchUserOrdersTable = (orders) => {
    document.getElementById("userOrdersTableBlock").style.display = "block";
    const table = $('#userOrdersSearchTable');
    table.find('tr:not(:first)').remove();
    orders.forEach(order =>{
        const tr1 = $('<tr></tr>');
        const td1 = $('<td>' + order.id + '</td>');
        const td2 = $('<td></td>');
        let textState;
        if(order.state == 'WAITING') textState = "Ожидает"
        if(order.state == 'IN_PROCESSING') textState = "Доставляется"
        if(order.state == 'RESOLVED') textState = "Доставлен"
        td2.append(textState)
        const td3 = $('<td>' + order.company.name + '</td>');
        const td4 = $('<td>' + order.way.startPoint + '</td>');
        const td5 = $('<td>' + order.way.endPoint + '</td>');
        const td6 = $('<td></td>');
        const td7 = $('<td>' + order.intervalWay.sumTime + '</td>');
        const td8 = $('<td>' + order.intervalWay.sumPrice + '</td>');
        const td9 = $('<td>' + order.intervalWay.maxWeight + '</td>');
        const tdDOP = $('<td></td>');
        let textPoints = '';
        order.intervalWay.tariffs.forEach(tariff =>{
            textPoints += tariff.startPoint + '- ';
        })
        textPoints += order.intervalWay.tariffs[order.intervalWay.tariffs.length - 1].endPoint;
        td6.append(textPoints)

        const button = $('<input id="profileButton" type="button" value="Профиль" class="btn btn-primary">');
        button.click(() => {
            window.location.href = '/about?companyId=' + order.company.id;
        });
        tdDOP.append(button);

        tr1.append(td1, td2, td3, td4, td5, td6, td7, td8, td9, tdDOP);
        $('#userOrdersSearchTable').append(tr1);
    })
};

$(document).ready(() => {
    ajaxGET('/order/users',orders => {
        console.log(orders);
        searchUserOrdersTable(orders)
    })
});