function loadCategory() {
    var urlString = window.location.href;
    var url = new URL(urlString);
    var category = url.searchParams.get('category');

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

        select_category_name.selectedIndex = Number.parseInt(category) - 1;
    }).fail(function(error) {
        console.log(error);
        alert(error);
    });
}
loadCategory();