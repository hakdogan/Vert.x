<#include "header.ftl">
<table id="articleTable" class="table table-striped">
    <thead>
    <tr>
        <th scope="col">Title</th>
        <th scope="col">Content</th>
        <th scope="col">Author</th>
        <th scope="col">Action</th>
    </tr>
    </thead>
    <tbody id="articleTableBody"></tbody>
</table>
<#include "footer.ftl">
<script type="text/javascript">
    function fillTable() {
        var data = ${context.articles};
        data.forEach(function (obj) {
            $("#articleTable > tbody:last").after("<tr><td>" + obj.title + "</td><td>"
                    + obj.content + "</td><td>" + obj.author + "</td><td><button onclick=\"deleteDocument(\'" + obj._id + "\')\" class=\"btn btn-danger\">Delete</button></td>");
        });
    }

    function deleteDocument(id){
        var removeEndPoint = "/api/articles/remove";
        var articlesEndPoint = "/api/articles";
        var article = {
            _id: id
        };

        $.ajax({
            type: "POST",
            url: removeEndPoint,
            data: JSON.stringify(article),
            contentType: "application/json; charset=utf-8",
            success: function(result) {
                $("#articleTable").find("tr:gt(0)").remove();
                result.forEach(function (obj) {
                    $("#articleTable > tbody:last").after("<tr><td>" + obj._id + "</td>"
                            + "<td>" + obj.title + "</td><td>" + obj.content + "</td><td>" + obj.author + "</td><td><button onclick=\"deleteDocument(\'" + obj._id + "\')\" class=\"btn btn-danger\">Delete</button></td>");
                });
            },
            error: function(err) {
                alert(JSON.stringify(err, null, 2));
            }
        });
    }

    $(document).ready(function() {
        $(fillTable());
    });

</script>
