function login() {
    var data = {
        email: $('.login-email').val(),
        password: $('.login-password').val()
    };

    $.ajax({
        type: "POST",
        url: "/login",
        data: JSON.stringify(data),
        //dataType: 'json', // parse error 나옴 ajax가 생각한 값은 json이지만 실제로 오는 값은 단순 http header 값 (text)
        dataType: 'text',
        contentType: 'application/json',
    }).done(function() {
        window.location.href = '/';
    }).fail(function(error) {
        //alert(JSON.stringify(error));
        alert(error.responseText);
    });
}

function logout() {
    $.ajax({
        type: "POST",
        url: "/log-out",
        dataType: 'text',
        contentType: 'application/json',
    }).done(function() {
        window.location.href = '/';
    }).fail(function(error) {
        //alert(JSON.stringify(error));
        alert(error.responseText);
    });
}