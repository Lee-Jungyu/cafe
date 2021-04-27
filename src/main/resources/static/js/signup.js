function signup() {
    var data = {
        email: $('.signup-email').val(),
        password: $('.signup-password').val(),
        roles: $('input:radio[name="roles"]:checked').val()
    };

    $.ajax({
        type: "POST",
        url: "/signup",
        data: JSON.stringify(data),
        //dataType: 'json', // parse error 나옴 ajax가 생각한 값은 json이지만 실제로 오는 값은 단순 http header 값 (text)
        dataType: 'text',
        contentType: 'application/json',
    }).done(function() {
        alert('회원 가입 완료');
        window.location.href = '/';
    }).fail(function(error) {
        //alert(JSON.stringify(error));
        alert(error.responseText);
    });
}