const extractSelectedItems = (selectId) => { //извлечение данных из селектов
    const selected = $('#' + selectId).val()
    return selected ? selected : [];
}

const extractSingleSelectedItem = (selectId) => {//извлечение данных из одного селекта
    const selected = $('#' + selectId).val()
    //возвращает пустую строку если ничего не выбрано, поэтому ставим null а не пустую строку
    return selected ? selected : null;
}

const openCompanyModal = (company = null, submitAction = (company) => {
}) => {
    $('#companyModal').modal('show')
    /*if (!company) {
        $('#bookModalBookImage').prop('required', true)
        $('#bookModalLabel').text('Добавить книгу')
    } else {
        $('#bookModalBookImage').prop('required', false)
        $('#bookModalLabel').text('Изменить книгу: ' + company.name)
    }*/
    company = company ? company : {};

    /*ajaxGET('/api/meta', data => { //это для заполнения формы при редактировании
        $('#bookModalBookName').val(company.name);
        $('#bookModal').modal('show')
    })*/

    $('#companyForm').submit((event) => {
        event.preventDefault();
        company.name = $('#companyModalName').val();
        company.email = $('#companyModalContact').val();
        //company.type = extractSelectedItems('companyModalTypes');
        company.description = $('#companyModalDescription').val();
        /*company.tariff = $('#companyModalPrice').val().replace(/\s/g, '')
            .split(',').map(function(tariff) {
            return parseFloat(tariff);
        });*/
        submitAction(company);
        console.log(company);
        $('#companyModal').modal('hide');
    })
}

const openTariffModal = (tariff = null, submitAction = (tariff) => {
}) => {
    $('#tariffModal').modal('show')
    /*if (!company) {
        $('#bookModalBookImage').prop('required', true)
        $('#bookModalLabel').text('Добавить книгу')
    } else {
        $('#bookModalBookImage').prop('required', false)
        $('#bookModalLabel').text('Изменить книгу: ' + company.name)
    }*/
    tariff = tariff ? tariff : {};

    /*ajaxGET('/api/meta', data => { //это для заполнения формы при редактировании
        $('#bookModalBookName').val(company.name);
        $('#bookModal').modal('show')
    })*/

    $('#tariffForm').submit((event) => {
        event.preventDefault();
        tariff.type = extractSingleSelectedItem('tariffModalTypes');
        tariff.price = Number.parseFloat($('#tariffModalPrice').val());
        submitAction(tariff);
        console.log(tariff);
        $('#tariffModal').modal('hide');
    })
}

$(document).ready(() => {
    $('#createCompanyBtn').click(() => {
        openCompanyModal(null, (company) => {
            ajaxPOSTWithoutResponse('/about/company', company, () => {
                 showMessage("Портфолио создано", 1000, () => {
                     //reloadBooks();
                 })
             })
        });
    })
    $('#createTariffBtn').click(() => {
        openTariffModal(null, (tariff) => {
            ajaxPOSTWithoutResponse('/about/tariff', tariff, () => {
                showMessage("Тариф создан", 1000, () => {
                    //reloadBooks();
                })
            })
        });
    })
})

/*$(document).ready(function(){
    var multipleCancelButton = new Choices('#tariffModalTypes', {
        removeItemButton: true,
        maxItemCount:5,
        searchResultLimit:5,
        renderChoiceLimit:5
    });
});*/

$(document).ready(() => {

});

