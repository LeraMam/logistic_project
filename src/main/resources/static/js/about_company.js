const extractSelectedItems = (selectId) => { //извлечение данных из селектов
    const selected = $('#' + selectId).val()
    return selected ? selected : [];
}

const extractSingleSelectedItem = (selectId) => {//извлечение данных из одного селекта
    const selected = $('#' + selectId).val()
    //возвращает пустую строку если ничего не выбрано, поэтому ставим null а не пустую строку
    return selected ? selected : null;
}

const companyBlock = (company) => {
    const name = $('<h4 class="font-weight-bold">' + company.name + '</h4>');
    const email = $('<h6 class="font-weight-bold">' + company.email + '</h6>');
    const description = $('<p>' + company.description + '</p>');
    $('#companyInfoBlock').append(name, email, description);
}

const openCompanyModal = (company = null, submitAction = (company) => {
}) => {
   /* console.log('С сервера приходит это: ');
    console.log(company);*/
    if (!company.name && !company.email && !company.description) {
        $('#companyModalLabel').text('Создать портфолио')
    } else {
        $('#companyModalLabel').text('Изменить портфолио: ' + company.name)
    }
    company = company ? company : {};

    /*ajaxGET('/api/meta', data => { //это для заполнения формы при редактировании
        $('#bookModalBookName').val(company.name);
        $('#bookModal').modal('show')
    })*/
    $('#companyModalName').val(company.name);
    $('#companyModalContact').val(company.email);
    $('#companyModalDescription').val(company.description);
    $('#companyModal').modal('show')

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
    if (!tariff.type && !tariff.price ) {
        $('#tariffModalLabel').text('Добавить тариф')
    } else {
        $('#tariffModalLabel').text('Изменить тариф')
    }
    tariff = tariff ? tariff : {};

    $('#tariffModalTypes').val(tariff.type);
    $('#tariffModalPrice').val(tariff.price);

    $('#tariffModal').modal('show')
    $('#tariffForm').submit((event) => {
        event.preventDefault();
        const newTariff = {};
        newTariff.type = extractSingleSelectedItem('tariffModalTypes');
        newTariff.price = Number.parseFloat($('#tariffModalPrice').val());
        submitAction(newTariff);
        console.log(newTariff);
        $('#tariffModal').modal('hide');
    })
}

$(document).ready(() => {
    function getCookie() {
        return document.cookie.split('; ').reduce((acc, item) => {
            const [name, value] = item.split('=')
            acc[name] = value
            return acc
        }, {})
    }
    const cookie = getCookie()
    /*console.log(cookie.userId)*/
    ajaxGET('/login/' + cookie.userId, user => {
        console.log('На ходе имеем');
        console.log(user.company);
        console.log(user.company.tariff);
        console.log('Получаем потом');

        companyBlock(user.company);

        $('#createCompanyBtn').click(() => {
            openCompanyModal(user.company, (company) => {
                ajaxPOSTWithoutResponse('/about/company', company, () => {
                    showMessage("Портфолио создано", 1000, () => {
                        //reloadBooks();
                    })
                })
            });
        })
        $('#createTariffBtn').click(() => {
            openTariffModal(user.company.tariff, (tariff) => {
                ajaxPOSTWithoutResponse('/about/tariff/' + user.id, tariff, () => {
                    showMessage("Тариф создан", 1000, () => {
                        //reloadBooks();
                    })
                })
            });
        })
    })
})


