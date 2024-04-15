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
        const td3 = $('<td>' + order.way.startPoint + '</td>');
        const td4 = $('<td>' + order.way.endPoint + '</td>');
        const td5 = $('<td></td>');
        const td6 = $('<td>' + order.intervalWay.sumTime + '</td>');
        const td7 = $('<td>' + order.intervalWay.sumPrice + '</td>');
        const td8 = $('<td>' + order.intervalWay.maxWeight + '</td>');
        const td9 = $('<td></td>');
        let textPoints = '';
        order.intervalWay.tariffs.forEach(tariff =>{
            textPoints += tariff.startPoint + '- ';
        })
        textPoints += order.intervalWay.tariffs[order.intervalWay.tariffs.length - 1].endPoint;
        td5.append(textPoints)

        if(order.state != 'RESOLVED'){
            const button = $('<input id="orderStateButton" type="submit" value="" class="btn btn-primary">');
            if(order.state == 'WAITING') button.val("Принять заказ")
            if(order.state == 'IN_PROCESSING') button.val("Заказ доставлен")
            button.click(() => {
                ajaxPUTWithoutResponse('order/state', order, () =>
                    showMessage("Статус заказа изменен", 1000, () => {
                        ajaxGET('/order/companies',orders => {
                            searchUserOrdersTable(orders)
                        })
                    })
                )
            });
            td9.append(button);
        }

        tr1.append(td1, td2, td3, td4, td5, td6, td7, td8, td9);
        $('#userOrdersSearchTable').append(tr1);
    })
};

$(document).ready(() => {
    ajaxGET('/order/companies',orders => {
        console.log(orders);
        searchUserOrdersTable(orders)
    })
});