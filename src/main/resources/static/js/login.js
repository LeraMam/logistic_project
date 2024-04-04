/*
$(document).ready(() => {
    $('#userButton').click((event) => {
        event.preventDefault();
        const userBody = {
            login: $('#userLogin').val(),
            role: 'ROLE_USER',
            password: $('#userPassword').val()
        }
        console.log(userBody)
        ajaxPOSTWithoutResponse('/login/sign', userBody, () => {
            showMessage("Успешно ", 1000, ()=> {
                //reloadUsers();
            })
        })
    })
    $('#companyButton').click((event) => {
        event.preventDefault();
        const companyBody = {
            login: $('#companyLogin').val(),
            role: 'ROLE_COMPANY',
            password: $('#companyPassword').val()
        }

        ajaxPOST('/login/sign', companyBody, () => {
            showMessage("Успешно ", 1000, ()=> {
                //reloadUsers();
            })
        })
    })
})

*/
