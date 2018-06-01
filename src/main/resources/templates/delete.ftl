<#include "header.ftl">
<div style="width: 800px; margin: 30px auto auto auto; padding: 5px;">
    <form>
        <div class="form-group row">
            <label for="inputId" class="col-sm-2 col-form-label">Document ID</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="inputId" placeholder="ID">
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"></label>
            <div class="col-sm-10">
                <button id="delete" class="btn btn-danger">Delete</button>
            </div>
        </div>
    </form>
</div>
<#include "footer.ftl">
<script type="text/javascript">
    $(document).ready(function() {
        $("#delete").click(function(){
            event.preventDefault();
            var url = "/api/articles/remove";
            var article = {
                _id: $("#inputId").val()
            };

            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(article),
                contentType: "application/json; charset=utf-8",
                success: function(result) {
                    alert(result);
                    $("#inputId").val("");
                },
                error: function(err) {
                    alert(JSON.stringify(err, null, 2));
                }
            });
        });
    });
</script>