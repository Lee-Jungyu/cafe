function addComment() {
    var urlString = window.location.href;
    var url = new URL(urlString);
    var p_id = url.searchParams.get('postId');

    var data = {
        postId: p_id,
        content: $('.textarea-comment').val(),
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        dataType: "text",
        contentType: "application/json",
        data: JSON.stringify(data)
    }).done(function() {
        window.location.reload();
    }).fail(function(error) {
        alert(error.responseText);
    });
}

function editComment() {

}

function removeComment() {

}
