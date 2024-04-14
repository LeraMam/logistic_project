
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

const waysTable = (userId) => {
    ajaxGET('/way/' + userId, ways => {
        const table = $('#waysTable');
        table.find('tr:not(:first)').remove();
        ways.forEach(way =>{
            console.log("way")
            console.log(way.intervalWays)
            let selectElement = $('<select style="margin-top: 20px"></select>');
            way.intervalWays.forEach(intervalWay =>{
                let option = $('<option value=""> </option>');
                for(let i = 0; i < intervalWay.tariffs.length; i++){
                    let text = intervalWay.tariffs[i].startPoint + '—';
                    let text2 =''
                    if((i + 1)=== intervalWay.tariffs.length) {
                        text2 = intervalWay.tariffs[i].endPoint;
                    }
                    option.append(text);
                    option.append(text2)
                    selectElement.append(option);
                }
            })
            const tr1 = $('<tr></tr>');
            const td1 = $('<td>' + way.startPoint + '</td>');
            const td2 = $('<td>' + way.endPoint + '</td>');
            /*const td3 = $('<td>' + way.startPoint + '</td>');*/
            const tdDOP3 = $('<td></td>');
            const tdDOP4 = $('<td></td>');
            const addPoint = $('<i class="fa fa-plus-circle" id="createIntervalPointBtn" style="color:#44944A"></i>')
            addPoint.click(() => {
                openIntervalWayModal(way, way.pointNumber, (newWay) => {
                    ajaxPOSTWithoutResponse('/way/add/interval/' + way.id , newWay, () => {
                        showMessage("Промежуточный путь создан", 1000, () => {
                            waysTable(userId);
                        })
                    })
                });
            })
            tdDOP3.append(addPoint);
            tr1.append(td1, td2, selectElement, tdDOP3, tdDOP4);
            $('#waysTable').append(tr1);
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

const openWayModal = (way = null, submitAction = (way) => {
}) => {
    if (!way.startPoint && !way.endPoint && !way.pointNumber) {
        $('#wayModalLabel').text('Создать путь')
    } else {
        $('#wayModalLabel').text('Изменить путь')
    }
    way = way ? way : {};

    $('#wayModalStartPoint').val(way.startPoint);
    $('#wayModalEndPoint').val(way.endPoint);
    $('#wayModalPointNumber').val(way.pointNumber);
    $('#wayModal').modal('show')

    $('#wayForm').submit((event) => {
        event.preventDefault();
        let newWay = {}; //не удалять!
        newWay.startPoint = $('#wayModalStartPoint').val();
        newWay.endPoint = $('#wayModalEndPoint').val();
        newWay.pointNumber = Number.parseInt($('#wayModalPointNumber').val());
        submitAction(newWay);
        console.log(newWay);
        $('#wayModal').modal('hide');
    })
}

const openIntervalWayModal = (intervalPoint = null, pointNumber, submitAction = (intervalPoint) => {
}) => {
    const pathForm = document.getElementById('intervalWayForm');
    const pathInputsContainer = document.getElementById('pathInputsContainer');
    let searchTariff = {};
    while (pathInputsContainer.firstChild) {
        pathInputsContainer.removeChild(pathInputsContainer.firstChild);
    }

    ajaxPOST('/about/tariffs', searchTariff, tariffs => {
        /*console.log("тарифы:")
        console.log(tariffs)*/
        for(let i= 0; i < pointNumber + 1; i++){
            let select = document.createElement('select');
            let div = document.createElement('div');
            let label = document.createElement('label');
            label.textContent = 'Точка номер ' +  (i + 1);
            div.appendChild(label);
            div.appendChild(select);
            select.classList.add('form-control');
            select.setAttribute('id', 'selectPoint' + (i + 1));
            pathInputsContainer.appendChild(div);
            let number = 1;
            tariffs.forEach(tariff => {
                /*console.log("тариф:")
                console.log(tariff)*/
                let option = document.createElement('option');
                option.value = tariff.id;
                option.text = tariff.startPoint + ' - ' + tariff.endPoint;
                /*console.log(option);*/
                select.appendChild(option);
                /*console.log(select);*/
                number++;
            })
        }
    });

    $('#intervalWayModal').modal('show')

    $('#intervalWayForm').off('submit').submit((event) => {
        event.preventDefault();
        let selectedValues = [];
        for (let i = 0; i < pointNumber + 1; i++) {
            let select = document.getElementById('selectPoint' + (i + 1));
            selectedValues.push(select.value);
        }
        console.log(selectedValues);
        submitAction(selectedValues);
        $('#intervalWayModal').modal('hide');
    })
}

$(document).ready(() => {
    ajaxGET('/login/auth', user => {
        console.log(user);
        companyBlock(user.company);
        waysTable(user.company.id);

        $('#createCompanyBtn').click(() => {
            openCompanyModal(user.company, (company) => {
                ajaxPOSTWithoutResponse('/about/company', company, () => {
                    showMessage("Портфолио создано", 1000, () => {
                        companyBlock(company);
                    })
                })
            });
        })
        $('#createWayBtn').click(() => {
            console.log(user.company.ways);
            openWayModal(user.company.ways, (way) => {
                ajaxPOSTWithoutResponse('/way/add/' + user.company.id, way, () => {
                    showMessage("Путь создан", 1000, () => {
                        waysTable(user.company.id);
                    })
                })
            });
        })
    })
})


