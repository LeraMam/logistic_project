function showMessage(message, timeout = 2000, afterMessageAction= ()=>{}) {
    const x = document.getElementById("snackbar");
    x.innerHTML = message;
    x.className = "show";
    setTimeout(function () {
        x.className = x.className.replace("show", "");
        afterMessageAction()
    }, timeout);
}