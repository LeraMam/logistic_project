const extractSelectedItems = (selectId) => { //извлечение данных из селектов
    const selected = $('#' + selectId).val()
    return selected ? selected : [];
}
const openBookModal = (company = null, submitAction = (book) => {
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
        company.contact = $('#companyModalContact').val();
        company.type = extractSelectedItems('companyModalTypes');
        company.description = $('#companyModalDescription').val();
        company.traffic = $('#companyModalPrice').val().replace(/\s/g, '')
            .split(',').map(function(tariff) {
            return parseFloat(tariff);
        });
        submitAction(company);
        console.log(company);
        console.log(company.traffic);
        $('#companyModal').modal('hide');
    })
}

$(document).ready(() => {
    $('#createBookBtn').click(() => {
        openBookModal(null, (book) => {
            /* ajaxPOST('/api/book', book, () => {
                 showMessage("Книга создана", 1000, () => {
                     reloadBooks();
                 })
             })*/
        });
    })
})

$(document).ready(function(){
    var multipleCancelButton = new Choices('#companyModalTypes', {
        removeItemButton: true,
        maxItemCount:5,
        searchResultLimit:5,
        renderChoiceLimit:5
    });
});