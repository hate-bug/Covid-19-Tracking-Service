$(document).ready(function () {

    showUploadedFile();
    $("#fileuploadbutton").click(function () {
        var formData = new FormData();
        formData.append("file", $("#userinputfile").prop("files")[0]);
        $.ajax({
            url: "/uploadfile",
            type: "POST",
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            data: formData,
        }).done(function (data) {
            alert("File uploaded.");
            $("#useruploadedfile").html("<a href=\'"+ data.fileDownloadUri +"\'>" + data.fileName +"</a>");
            $("#userinputfile").val('');
        });
    });

    function showUploadedFile() {
        $.ajax({
            url: "/useruploadedfile",
            type: "GET"
        }).done(function (data) {
            if (data!=null && typeof page_name != 'undefined'){
                $("#useruploadedfile").html("<a href=\'"+ data.fileDownloadUri +"\'>" + data.fileName +"</a>");
            }
        });
    }
});