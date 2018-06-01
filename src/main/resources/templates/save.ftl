<#include "header.ftl">
<div style="width: 600px; margin: 30px auto auto auto; padding: 5px;">
    <form>
        <div class="form-group row">
            <label for="inputTitle" class="col-sm-2 col-form-label">Title</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="inputTitle" placeholder="Title">
            </div>
        </div>
        <div class="form-group row">
            <label for="inputContent" class="col-sm-2 col-form-label">Content</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="inputContent" placeholder="Content">
            </div>
        </div>
        <div class="form-group row">
            <label for="inputAuthor" class="col-sm-2 col-form-label">Author</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="inputAuthor" placeholder="Author">
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"></label>
            <div class="col-sm-10">
                <button id="save" class="btn btn-primary">Save</button>
            </div>
        </div>
    </form>
</div>
<#include "footer.ftl">
<script type="text/javascript">
    $(document).ready(function() {
        function clearFields(){
            $("#inputTitle").val("");
            $("#inputContent").val("");
            $("#inputAuthor").val("");
        }
        $("#save").click(function(){
            event.preventDefault();
            var url = "/api/articles/save";
            var article = {
                id: $("#inputTitle").val(),
                content: $("#inputContent").val(),
                author: $("#inputAuthor").val()
            };

            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(article),
                contentType: "application/json; charset=utf-8",
                success: function(result) {
                    alert(result);
                    $(clearFields());
                },
                error: function(err) {
                    alert(JSON.stringify(err, null, 2));
                }
            });
        });
    });
</script>