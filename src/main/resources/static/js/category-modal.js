function loadCategory() {
    $.ajax({
        type: "GET",
        url: "/category",
        dataType: 'text',
        contentType: 'application/json'
    }).done(function(data) {
        var jsonData = JSON.parse(data);
        var select_edit_category_name = document.getElementsByClassName('select-category-name')[0];
        var select_remove_category_name = document.getElementsByClassName('select-category-name')[1];

        for(i = 1; i < jsonData.length; i++) {
            select_edit_category_name.innerHTML += `<option value='${jsonData[i].id}'>${jsonData[i].name}</option>`;
            select_remove_category_name.innerHTML += `<option value='${jsonData[i].id}'>${jsonData[i].name}</option>`;
        }

        if(jsonData.length == 1) {
            select_edit_category_name.innerHTML += `<option value='-1'>수정 가능한 카테고리 목록이 없습니다</option>`;
            select_remove_category_name.innerHTML += `<option value='-1'>수정 가능한 카테고리 목록이 없습니다</option>`;
        }

    }).fail(function(error) {
        console.log(error);
        alert(error);
    });
}

loadCategory();
