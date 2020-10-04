<#import "parts/common.ftlh" as c>
<#import "parts/navbar.ftlh" as n>
<#import "/spring.ftl" as spring>

<@c.page>
    <@n.navbar>
        <li class="nav-item">
            <a class="nav-link"
               href="/main?lang=${springMacroRequestContext.locale}"><@spring.message "header.home"/></a>
        </li>
        <li class="nav-item">
            <a class="nav-link"
               href="/registration?lang=${springMacroRequestContext.locale}"><@spring.message "header.register"/></a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/login?lang=uk_UA"><@spring.message "header.switchUk"/></a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/login?lang=en_US"><@spring.message "header.swithEng"/></a>
        </li>
    </@n.navbar>
    <div class="container mt-5">
        <h5><@spring.message "pagename.login"/></h5>


        <form action="/login" method="post">
            <div class="form-group row">
                <label class="col-sm-2 col-form-label"><@spring.message "form.username"/>:</label>
                <div class="col-sm-10">
                    <input type="text" name="username" class="form-control"/>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label"><@spring.message "form.pass"/>:</label>
                <div class="col-sm-10">
                    <input type="password" name="password" class="form-control"/>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-primary"><@spring.message "button.login"/></button>

        </form>
    </div>
</@c.page>