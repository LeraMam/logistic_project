
const companyBlock = (company) => {
    $('#companyInfoBlock').empty();
    if(company.name && company.email && company.maxTC && company.description){
        const name = $('<h4 class="font-weight-bold">' + company.name + '</h4>');
        const email = $('<h6 class="font-weight-bold">' + company.email + '</h6>');
        const maxTC = $('<p>' + "Максимальный ТС: " + company.maxTC + '</p>');
        const description = $('<p>' + company.description + '</p>');
        $('#companyInfoBlock').append(name, email, description, maxTC,);
    }
}

const waysTable = (companyId) => {
    ajaxGET('/way/' + companyId, ways => {  //нужно читать тарифы только для компании, а не все
        const table = $('#waysTable');
        table.find('tr:not(:first)').remove();
        ways.forEach(way =>{
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
                }
                option.val(intervalWay.id)
                selectElement.append(option);
            })
            const tr1 = $('<tr></tr>');
            const td1 = $('<td>' + way.startPoint + '</td>');
            const td2 = $('<td>' + way.endPoint + '</td>');
            const td3 = $('<td></td>');
            const tdDOP1 = $('<td></td>');
            const tdDOP2 = $('<td></td>');
            const tdDOP3 = $('<td></td>');
            const editWay = $('<i class="fa fa-pencil-square" style="color:#00043c"></i>')
            const deleteWay = $('<i class="fa fa-trash-o" style="color: darkred"></i>')
            const addIntervalWay = $('<i class="fa fa-plus-circle" id="createIntervalPointBtn" style="color:#44944A"></i>')
            const editIntervalWay = $('<i class="fa fa-pencil-square" style="color:#00043c"></i>')
            const deleteIntervalWay = $('<i class="fa fa-trash-o" style="color: darkred"></i>')
            addIntervalWay.click(() => {
                openIntervalWayModal(way,companyId, way.pointNumber, (newWay) => {
                    ajaxPOSTWithoutResponse('/way/add/interval/' + way.id, newWay, () => {
                        showMessage("Промежуточный путь создан", 1000, () => {
                            waysTable(companyId);
                        })
                    })
                });
            })
            editIntervalWay.click(() => {
                const selectedOptionValue = selectElement.val();
                openIntervalWayModal(way,companyId, way.pointNumber, (newWay) => {
                    ajaxPUTWithoutResponse('/way/edit/interval/' + way.id +'/'+ selectedOptionValue, newWay, () => {
                        showMessage("Промежуточный путь изменен", 1000, () => {
                            waysTable(companyId);
                        })
                    })
                });
            })
            deleteIntervalWay.click(() => {
                const selectedOptionValue = selectElement.val();
                ajaxDELETE('/way/delete/interval/' + companyId + '/' + way.id +'/'+ selectedOptionValue, () => {
                    showMessage("Промежуточный путь удален", 1000, () => {
                        waysTable(companyId);
                    })
                })
            })
            editWay.click(() => {
                openWayModal(way, (editWay) => {
                    ajaxPUTWithoutResponse('/way/edit/' + companyId + '/' + way.id, editWay, () => {
                        showMessage("Путь изменен", 1000, () => {
                            waysTable(companyId);
                        })
                    })
                });
            })
            deleteWay.click(() => {
                ajaxDELETE('/way/delete/' + companyId + '/' + way.id, () => {
                    showMessage("Путь удален", 1000, () => {
                        waysTable(companyId);
                    })
                })
            })
            tdDOP1.append(addIntervalWay);
            tdDOP2.append(editIntervalWay);
            tdDOP3.append(deleteIntervalWay);
            td3.append(editWay);
            td3.append(deleteWay);
            tr1.append(td1, td2, td3, selectElement, tdDOP3, tdDOP1, tdDOP2, tdDOP3);
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
    $('#companyModalTC').val(company.maxTC);
    $('#companyModalDescription').val(company.description);
    $('#companyModal').modal('show')
    $('#companyForm').off('submit');

    $('#companyForm').submit((event) => {
        event.preventDefault();
        company.name = $('#companyModalName').val();
        company.email = $('#companyModalContact').val();
        company.maxTC = Number.parseFloat($('#companyModalTC').val());
        company.description = $('#companyModalDescription').val();
        submitAction(company);
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
    $('#wayForm').off('submit');

    $('#wayForm').submit((event) => {
        event.preventDefault();
        let newWay = {}; //не удалять!
        newWay.startPoint = $('#wayModalStartPoint').val();
        newWay.endPoint = $('#wayModalEndPoint').val();
        newWay.pointNumber = Number.parseInt($('#wayModalPointNumber').val());
        submitAction(newWay);
        $('#wayModal').modal('hide');
    })
}

const openIntervalWayModal = (intervalPoint = null, companyId, pointNumber, submitAction = (intervalPoint) => {
}) => {
    const pathForm = document.getElementById('intervalWayForm');
    const pathInputsContainer = document.getElementById('pathInputsContainer');
    while (pathInputsContainer.firstChild) {
        pathInputsContainer.removeChild(pathInputsContainer.firstChild);
    }
    ajaxGET('/about/tariff/' + companyId, tariffs => {
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
                let option = document.createElement('option');
                option.value = tariff.id;
                option.text = tariff.startPoint + ' - ' + tariff.endPoint;
                select.appendChild(option);
                number++;
            })
        }
    });

    $('#intervalWayModal').modal('show')
    $('#intervalWayForm').off('submit');

    $('#intervalWayForm').off('submit').submit((event) => {
        event.preventDefault();
        let selectedValues = [];
        for (let i = 0; i < pointNumber + 1; i++) {
            let select = document.getElementById('selectPoint' + (i + 1));
            selectedValues.push(select.value);
        }
        submitAction(selectedValues);
        $('#intervalWayModal').modal('hide');
    })
}

$(document).ready(() => {
    ajaxGET('/login/auth', user => {
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


