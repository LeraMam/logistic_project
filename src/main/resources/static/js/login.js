$(document).ready(() => {
    $('#companyButton').click(() => {
        event.preventDefault();
        const body = {
            login: $('#companyLogin').val(),
            role: 'COMPANY',
            password: $('#companyPassword').val()
        }

        ajaxPOST('/login/register', body, () => {
            showMessage("Пользователь зарегистрирован ", 1000, ()=> {
                //reloadUsers();
                // updateList(filterItemsConfig.years)
            })
        })
    })
    }
)

