$(document).ready(() => {
    $('#userRegButton').click((event) => {
        event.preventDefault();
        let userBody = {
            login: $('#userRegLogin').val(),
            role: 'ROLE_USER',
            password: $('#userRegPassword').val()
        }
        const checkbox = document.getElementById('userRole');
        const isChecked = checkbox.checked;
        if (isChecked === true) {
            userBody.role = 'ROLE_COMPANY'
        }

        ajaxPOSTWithoutResponse('/login/register', userBody, () => {
            showMessage("Пользователь зарегистрирован ", 1000)
            document.getElementById("registerText").style.display = "block";
        })
    });
});

