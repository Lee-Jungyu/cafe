function loadCategory() {
    $.ajax({
        type: "GET",
        url: "/category",
        dataType: 'text',
        contentType: 'application/json'
    }).done(function(data) {
        var jsonData = JSON.parse(data);
        var select_category_name = document.getElementsByClassName('select-category-name')[0];

        for(i = 0; i < jsonData.length; i++) {
            select_category_name.innerHTML += `<option value='${jsonData[i].id}'>${jsonData[i].name}</option>`;
        }
    }).fail(function(error) {
        console.log(error);
        alert(error);
    });
}

function btnClose() {
    location.reload();
}

loadCategory();