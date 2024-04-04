
const extractSingleSelectedItem = (selectId) => {
    const selected = $('#' + selectId).val()
    //возвращает пустую строку если ничего не выбрано, поэтому ставим null а не пустую строку
    return selected ? selected : null;
}

const companyBlock = (company) => {
    $('#companyInfoBlock').empty();
    if(company.name && company.email && company.description){
        const name = $('<h4 class="font-weight-bold">' + company.name + '</h4>');
        const email = $('<h6 class="font-weight-bold">' + company.email + '</h6>');
        const description = $('<p>' + company.description + '</p>');
        $('#companyInfoBlock').append(name, email, description);
    }
}

const tariffsTable = (userId) => {
    ajaxGET('/about/tariff/' + userId, tariffs => {
        /*$('#tariffsTable').empty();*/
        /*console.log("tarrifs: ")
        console.log(tariffs)*/
        const table = $('#tariffsTable');
        table.find('tr:not(:first)').remove();
        tariffs.forEach(tariff =>{
            const tr1 = $('<tr></tr>');
            const td1 = $('<td>' + tariff.startPoint + '</td>');
            const td2 = $('<td>' + tariff.endPoint + '</td>');
            const td3 = $('<td>' + tariff.distance + '</td>');
            const td4 = $('<td>' + tariff.time + '</td>');
            const td5 = $('<td>' + tariff.type + '</td>');
            const td6 = $('<td>' + tariff.price + '</td>');
            const td7 = $('<td>' + tariff.maxWeight + '</td>');
            const tdDOP3 = $('<td></td>');
            const tdDOP4 = $('<td></td>');
            const edit = $('<i class="fa fa-pencil-square" style="color:#00043c"></i>')
            edit.click(() => {
                openTariffModal(tariff, (updatedTariff) => {
                    console.log(updatedTariff);
                    ajaxPUT('/about/tariff/' + userId + "/" + tariff.id, updatedTariff, () => {
                        showMessage("Тариф изменен", 1000, () => {
                            tariffsTable(userId);
                        })
                    })
                });
            })
            const del = $('<i class="fa fa-trash-o" style="color: darkred"></i>')
            del.click(() => {
                console.log('delete button');
                ajaxDELETE('/about/' + tariff.id, () => {
                    showMessage('Тариф удален', 1500)
                    //tariffsTable(userId);
                });
            })
            tdDOP3.append(edit);
            tdDOP4.append(del);
            tr1.append(td1, td2,td3, td4, td5, td6, td7, tdDOP3, tdDOP4);
            $('#tariffsTable').append(tr1);
        })
    })
}

const openCompanyModal = (company = null, submitAction = (company) => {
}) => {
    if (!company.name && !company.email && !company.description) {
        $('#companyModalLabel').text('Создать портфолио')
    } else {
        $('#companyModalLabel').text('Изменить портфолио: ' + company.name)
    }
    company = company ? company : {};

    $('#companyModalName').val(company.name);
    $('#companyModalContact').val(company.email);
    $('#companyModalDescription').val(company.description);
    $('#companyModal').modal('show')

    $('#companyForm').submit((event) => {
        event.preventDefault();
        company.name = $('#companyModalName').val();
        company.email = $('#companyModalContact').val();
        company.description = $('#companyModalDescription').val();
        submitAction(company);
        console.log(company);
        $('#companyModal').modal('hide');
    })
}

const openTariffModal = (tariff = null, submitAction = (tariff) => {
}) => {
    if (!tariff.type && !tariff.price ) {
        $('#tariffModalLabel').text('Добавить тариф')
    } else {
        $('#tariffModalLabel').text('Изменить тариф')
    }
    tariff = tariff ? tariff : {};

    $('#tariffModalStartPoint').val(tariff.startPoint);
    $('#tariffModalEndPoint').val(tariff.endPoint);
    $('#tariffModalDistance').val(tariff.distance);
    $('#tariffModalTime').val(tariff.time);
    $('#tariffModalTypes').val(tariff.type);
    $('#tariffModalPrice').val(tariff.price);
    $('#tariffModalMaxWeight').val(tariff.maxWeight);

    $('#tariffModal').modal('show')
    $('#tariffForm').off('submit');

    $('#tariffForm').submit((event) => {
        event.preventDefault();
        let newTariff = {}; //не удалять!
        newTariff.startPoint = $('#tariffModalStartPoint').val();
        newTariff.endPoint = $('#tariffModalEndPoint').val();
        newTariff.distance = Number.parseFloat($('#tariffModalDistance').val());
        newTariff.time = Number.parseFloat($('#tariffModalTime').val());
        newTariff.type = extractSingleSelectedItem('tariffModalTypes');
        newTariff.price = Number.parseFloat($('#tariffModalPrice').val());
        newTariff.maxWeight = Number.parseFloat($('#tariffModalMaxWeight').val());
        submitAction(newTariff);
        /*console.log(newTariff);*/
        $('#tariffModal').modal('hide');
    })
}

$(document).ready(() => {
    /*function getCookie() {
        return document.cookie.split('; ').reduce((acc, item) => {
            const [name, value] = item.split('=')
            acc[name] = value
            return acc
        }, {})
    }*/

    ajaxGET('/login/auth' /*+ cookie.userId*/, user => {
        console.log(user);
        tariffsTable(user.id);
        companyBlock(user.company);

        $('#createCompanyBtn').click(() => {
            openCompanyModal(user.company, (company) => {
                ajaxPOSTWithoutResponse('/about/company', company, () => {
                    showMessage("Портфолио создано", 1000, () => {
                        companyBlock(company);
                    })
                })
            });
        })
        $('#createTariffBtn').click(() => {
           /* console.log("company.tariff:  " + user.company.tariff)*/
            openTariffModal(user.company.tariff, (tariff) => {
                ajaxPOSTWithoutResponse('/about/tariff/' + user.id, tariff, () => {
                    showMessage("Тариф создан", 1000, () => {
                        tariffsTable(user.id);
                    })
                })
            });
        })
    })
})


