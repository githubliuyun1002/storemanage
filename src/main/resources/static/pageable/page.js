//添加分页插件，根据总页数来添加按钮
function addButtons(startPage, pageLength) {
    // alert(pageLength);
    $("#page").html(" ");
    var _html = "<center><button class='btn btn-default prev' ><<上一页</button> ";
    for (var i = startPage; i < startPage + pageLength; i++) {
        _html += "<button class='btn btn-default page-btn' data-pageno=" + i + ">" + i + "</button> ";
    }
    _html += " <button class='btn btn-default next'>下一页>></button></center>";
    $("#page").html(_html);
}
//分页按钮迭代(最要是对"上一页"和”下一页“是否禁用的判断)
function isDisabled(pagenum, totalpage) {
    if (totalpage == 0 || totalpage == 1) {
        $("#page .prev").attr("disabled", true);
        $("#page .next").attr("disabled", true);
    } else if (pagenum == 1) {
        $("#page .prev").attr("disabled", true);
        $("#page .next").removeAttr("disabled");
    } else if (pagenum == totalpage) {
        $("#page .next").attr("disabled", true);
        $("#page .prev").removeAttr("disabled");
    } else {
        $("#page .next").removeAttr("disabled");
        $("#page .prev").removeAttr("disabled");
    }
}
//按钮迭代器
function pageIteration(currPage) {
    if (page.currPage != 1 && page.currPage == page.startPage) {
        page.startPage = page.currPage - page.pageLength / 2;
        page.endPage = page.currPage + page.pageLength / 2 - 1;
        addButtons(page.startPage, page.pageLength);
        $("#page .page-btn").eq(page.pageLength / 2).addClass("curr-page");
    } else if (page.currPage != page.totalPage && page.currPage == page.endPage) {
        page.startPage = page.currPage - page.pageLength / 2 + 1;
        page.endPage = (page.currPage + page.pageLength / 2) >= page.totalPage ? page.totalPage : (page.currPage + page.pageLength / 2);
        addButtons(page.startPage, page.pageLength / 2 + (page.endPage == page.totalPage ? (page.endPage - page.currPage) : page.pageLength / 2));
        $("#page .page-btn").eq(page.pageLength / 2 - 1).addClass("curr-page");
    }
    $("#page .page-btn").removeClass("curr-page").parent().find("[data-pageno=" + currPage + "]").addClass("curr-page");
}