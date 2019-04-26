$(document).ready(function () {
    $('#sidebarCollapse').on('click', function () {
        console.log("click the sidebarCollapse button");
        $('.side-navbar').toggleClass('shrinked');
    });
});