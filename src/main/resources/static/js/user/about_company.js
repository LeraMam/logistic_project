
const usersCompanyBlock = (company) => {
    $('#companyInfoBlock').empty();
    if(company.name && company.email && company.description){
        const name = $('<h4 class="font-weight-bold">' + company.name + '</h4>');
        const email = $('<h6 class="font-weight-bold">' + company.email + '</h6>');
        const description = $('<p>' + company.description + '</p>');
        $('#companyInfoBlock').append(name, email, description);
    }
}

const usersWaysTable = (companyId) => {
    ajaxGET('/way/' + companyId, ways => {
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
                    selectElement.append(option);
                }
            })
            const tr1 = $('<tr></tr>');
            const td1 = $('<td>' + way.startPoint + '</td>');
            const td2 = $('<td>' + way.endPoint + '</td>');
            tr1.append(td1, td2, selectElement);
            $('#waysTable').append(tr1);
        })
    })
}

$(document).ready(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const companyId = urlParams.get('companyId');
    ajaxGET('/about/one/' + companyId, company => {
        usersCompanyBlock(company);
    })
    usersWaysTable(companyId);
})


