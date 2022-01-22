function setRole(id, username){
    let form = document.getElementById("form" + id);
    const role = document.getElementById("select" + id)

    form.setAttribute("action", '/admin/setRole/' + username + '/' + role.value);
    form.submit();
}

function sortOrder(order){
    const sortButton = document.getElementById("sortButton");
    if (order) {
        sortButton.innerHTML = '<i class=\"fas fa-sort-down\"></i>' +
            '<input type="text" name="order" value="false" style="display: none;">';

    } else {
        sortButton.innerHTML = '<i class=\"fas fa-sort-up\"></i>' +
            '<input type="text" name="order" value="true" style="display: none;">';
    }
    sortButton.setAttribute("onclick", 'sortOrder(' + !order + ')');
    sortButton.setAttribute("value", !order);
}