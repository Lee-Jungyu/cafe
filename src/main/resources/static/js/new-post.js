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
function savePost() {
    var sel = document.getElementsByClassName('select-category-name')[0];
    var c_id = sel.options[sel.selectedIndex].value;
    var data = {
        title: $('.text-title').val(),
        content: $('.textarea-content').val(),
        categoryId: c_id
    }

    $.ajax({
        type: "POST",
        url: "/post",
        dataType: "text",
        contentType: "application/json",
        data: JSON.stringify(data)
    }).done(function() {
        alert('게시글이 추가되었습니다.');
        window.location.href = '/';
    }).fail(function(error) {
        alert(error.responseText);
    });
}
function btnCancel() {
    window.history.back();
}
loadCategory();