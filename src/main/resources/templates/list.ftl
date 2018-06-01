<#include "header.ftl">
<table id="articleTable" class="table table-striped">
    <thead>
    <tr>
        <th scope="col">id</th>
        <th scope="col">title</th>
        <th scope="col">Content</th>
        <th scope="col">Author</th>
    </tr>
    </thead>
    <tbody id="articleTableBody"></tbody>
</table>
<#include "footer.ftl">
<script type="text/javascript">
    function fillTable() {
        var data = ${context.articles};
        data.forEach(function (obj) {
            $("#articleTable > tbody:last").after("<tr><td>" + obj._id + "</td>"
                    + "<td>" + obj.title + "</td><td>" + obj.content + "</td><td>" + obj.author + "</td>");
        });
    }

    $(document).ready(function() {
        $(fillTable());
    });

</script>
