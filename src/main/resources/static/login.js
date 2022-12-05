$(document).ready(function () {
    $('#eye').on('click', function () {
        if($("#password").attr("type") == "password") {
            $("#password").attr("type", "text");
            $($(this)).attr('class', 'far fa-eye');
        } else {
            $("#password").attr("type", "password");
            $($(this)).attr('class', 'far fa-eye-slash');
        }
    });
});