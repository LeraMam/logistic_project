$(document).ready(() => {
    $('#goodsCountForm').submit(event => {
        event.preventDefault();
        const body = {};
        console.log($('#goodsCountForm').serializeArray())
        $.each($('#goodsCountForm').serializeArray(), (i, field) => {
            body[field.name] = field.value;
        })
        ajaxPOSTWithoutResponse('/count', body, () => {
            //window.location.href = '/catalog'
        })
    });
});