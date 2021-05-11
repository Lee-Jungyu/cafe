function loadPost() {
    var urlString = window.location.href;
    var url = new URL(urlString);
    var postId = url.searchParams.get('postId');

    $.ajax({
        type: "GET",
        url: "/post/" + postId,
        dataType: 'text',
        contentType: 'application/json'
    }).done(function(data) {
        var jsonData = JSON.parse(data);

        var post_title = document.getElementsByClassName('post-title')[0];
        var post_category = document.getElementsByClassName('post-category')[0].getElementsByTagName('a')[0];
        var post_author = document.getElementsByClassName('post-author')[0].getElementsByTagName('a')[0];
        var post_createdDate = document.getElementsByClassName('post-createdDate')[0];
        var post_modifiedDate = document.getElementsByClassName('post-modifiedDate')[0];
        var post_content = document.getElementsByClassName('post-content')[0];

        var createdDate = jsonData.createdDate.split("T")[0];
        var modifiedDate = jsonData.modifiedDate.split("T")[0];

        post_title.innerHTML = jsonData.title;
        post_category.innerHTML = jsonData.categoryId;
        post_category.href = '/?category=' + jsonData.categoryId;
        post_author.innerHTML = jsonData.author;
        post_author.href = '/profile?email=' + jsonData.author;
        post_createdDate.innerHTML += createdDate;
        post_modifiedDate.innerHTML += modifiedDate;
        post_content.innerHTML = jsonData.content.replace(/\n/g,'<br>');

        $.ajax({
            type: "GET",
            url: "/category/" + jsonData.categoryId,
            dataType: 'text',
            contentType: 'application/json'
        }).done(function(data2) {
            var jsonData2 = JSON.parse(data2);
            post_category.innerHTML = jsonData2.name;
        }).fail(function(error){
            console.log(error);
            alert(error.responseText);
        })

    }).fail(function(error) {
        console.log(error);
        alert(error.responseText);
    });
}

function showModal(option) {
    switch (option) {
        case 'update':
            var modal = document.getElementsByClassName('modal-background')[0];
            var modalContent = document.getElementsByClassName('modal-edit-post')[0];

            var text_title = document.getElementsByClassName('text-title')[0];
            var textarea_content = document.getElementsByClassName('textarea-content')[0];

            text_title.value = document.getElementsByClassName('post-title')[0].innerText;
            textarea_content.innerHTML = document.getElementsByClassName('post-content')[0].innerText;

            modalContent.style.display = 'block';
            modal.style.display = 'block';

            break;
    }
}

loadPost();
