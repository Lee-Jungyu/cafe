function setProfileLink() {
    var profileLinks = document.getElementsByClassName("link-profile");

    for(var i = 0; i < profileLinks.length; i++) {
        var link = profileLinks.item(i);
        var email = link.innerText;
        link.href = '/profile?email=' + email;
    }
}

setProfileLink();