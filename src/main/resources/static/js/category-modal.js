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

function addCategory() {
    var data = {
        name: $('.input-category-name').val()
    }

    $.ajax({
        type: "POST",
        url: "/category",
        dataType: "text",
        contentType: "application/json",
        data: JSON.stringify(data)
    }).done(function() {
        alert('카테고리가 추가되었습니다.');
        window.location.href = '/';
    }).fail(function(error) {
        alert(error.responseText);
    });
}

function editCategory() {
    var sel = document.getElementsByClassName('select-category-name')[0];
    var c_id = sel.options[sel.selectedIndex].value;
    var c_name = document.getElementsByClassName('input-category-name')[1].value;
    var data = {
        name: c_name
    }

    $.ajax({
        type: "PUT",
        url: "/category/" + c_id,
        dataType: "text",
        contentType: "application/json",
        data: JSON.stringify(data)
    }).done(function() {
        alert('카테고리가 수정되었습니다.');
        window.location.href = '/';
    }).fail(function(error) {
        alert(error.responseText);
    });
}

function removeCategory() {
    var sel = document.getElementsByClassName('select-category-name')[1];
    var c_id = sel.options[sel.selectedIndex].value;

    $.ajax({
        type: "DELETE",
        url: "/category/" + c_id,
        dataType: "text",
        contentType: "application/json"
    }).done(function() {
        alert('카테고리가 삭제되었습니다.');
        window.location.href = '/';
    }).fail(function(error) {
        alert(error.responseText);
    });
}

function btnCancel() {
    location.reload();
}

loadCategory();
