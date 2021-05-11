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

function editPost() {
    var sel = document.getElementsByClassName('select-category-name')[0];
    var c_id = sel.options[sel.selectedIndex].value;

    var urlString = window.location.href;
    var url = new URL(urlString);
    var p_id = url.searchParams.get('postId');

    var data = {
        title: $('.text-title').val(),
        content: $('.textarea-content').val(),
        categoryId: c_id
    }

    $.ajax({
        type: "PUT",
        url: "/post/" + p_id,
        dataType: "text",
        contentType: "application/json",
        data: JSON.stringify(data)
    }).done(function() {
        alert('게시글이 수정되었습니다.');
        window.location.reload();
    }).fail(function(error) {
        alert(error.responseText);
    });
}

function removePost() {
    if(confirm('정말로 포스트를 삭제하시겠습니까?')) {
        var urlString = window.location.href;
        var url = new URL(urlString);
        var p_id = url.searchParams.get('postId');

        $.ajax({
            type: "DELETE",
            url: "/post/" + p_id,
            dataType: "text",
            contentType: "application/json"
        }).done(function() {
            alert('포스트가 삭제되었습니다.');
            location.reload();
        }).fail(function(error) {
            alert(error.responseText);
        });
    }
    else {
    }
}
function btnCancel() {
    window.history.back();
}