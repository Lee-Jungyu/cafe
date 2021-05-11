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
