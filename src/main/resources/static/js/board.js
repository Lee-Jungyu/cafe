function loadPost() {
    var urlString = window.location.href;
    var url = new URL(urlString);
    var category = url.searchParams.get('category');

    if(category === '0' || category === null) {
        $.ajax({
            type: "GET",
            url: "/post",
            dataType: 'text',
            contentType: 'application/json'
        }).done(function(data) {
            var jsonData = JSON.parse(data);
            var table_post = document.getElementsByClassName('table-post')[0];
            var tbody = table_post.getElementsByTagName('tbody')[0];

            var btnNewPost = document.getElementsByClassName('btn-new-post')[0];
            btnNewPost.href += '?category=' + 1;

            for(i = jsonData.length - 1; i >= 0; i--) {
                var createdDate = jsonData[i].createdDate.split("T")[0];
                var td = `
                    <td>${jsonData[i].id}</td>
                    <td><a href="/read-post?postId=${jsonData[i].id}">${jsonData[i].title}</a></td>
                    <td><a href="/profile?email=${jsonData[i].author}">${jsonData[i].author}</a></td>
                    <td>${createdDate}</td>
                `;
                tbody.insertAdjacentHTML("beforeend",td);
            }
        }).fail(function(error) {
            console.log(error);
            alert(error.responseText);
        });
    }
    else {
        $.ajax({
            type: "GET",
            url: "/post/category/" + category,
            dataType: 'text',
            contentType: 'application/json'
        }).done(function(data) {
            var jsonData = JSON.parse(data);
            var table_post = document.getElementsByClassName('table-post')[0];
            var tbody = table_post.getElementsByTagName('tbody')[0];

            var btnNewPost = document.getElementsByClassName('btn-new-post')[0];
            btnNewPost.href += '?category=' + category;


            for(i = jsonData - 1; i >= 0; i--) {
                var createdDate = jsonData[i].createdDate.split("T")[0];
                var td = `
                    <td>${jsonData[i].id}</td>
                    <td><a href="/read-post?postId=${jsonData[i].id}">${jsonData[i].title}</a></td>
                    <td><a href="/profile?email=${jsonData[i].author}">${jsonData[i].author}</a></td>
                    <td>${createdDate}</td>
                `;
                tbody.insertAdjacentHTML("beforeend",td);
            }
        }).fail(function(error) {
            console.log(error);
            alert(error.responseText);
        });
    }
}

loadPost();