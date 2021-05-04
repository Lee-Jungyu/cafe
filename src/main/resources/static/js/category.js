function loadCategory() {
    $.ajax({
        type: "GET",
        url: "/category",
        dataType: 'text',
        contentType: 'application/json'
    }).done(function(data) {
        var jsonData = JSON.parse(data);
        var div_category = document.getElementsByClassName('category')[0];

        for(i = 0; i < jsonData.length; i++) {
            div_category.innerHTML += `<a href='/?category=${jsonData[i].id}'>${jsonData[i].name}</a>`
        }
    }).fail(function(error) {
        console.log(error);
        alert(error.responseText);
    });
}

loadCategory();