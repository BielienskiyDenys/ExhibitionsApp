<#import "parts/common.ftlh" as c>
<#import "parts/navbar.ftlh" as n>
<#import "/spring.ftl" as spring>
<@c.page>
    <@n.navbar>
        <li class="nav-item">
            <a class="nav-link" href="/main?lang=${springMacroRequestContext.locale}"><@spring.message "header.home"/></a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/login?lang=${springMacroRequestContext.locale}"><@spring.message "header.login"/></a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/registration?lang=uk_UA"><@spring.message "header.switchUk"/></a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/registration?lang=en_US"><@spring.message "header.swithEng"/></a>
        </li>
    </@n.navbar>
    <div class="container mt-5">
        <h5><@spring.message "pagename.registration"/></h5>
        <#if registrationMessage??>
            <h4>${registrationMessage}</h4>
        </#if>
        <form action="/registration" method="post">
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
            <div class="form-group row">
                <label class="col-sm-2 col-form-label"><@spring.message "form.nameNat"/>:</label>
                <div class="col-sm-10">
                    <input type="text" name="nameNative" class="form-control"/>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label"><@spring.message "form.email"/>:</label>
                <div class="col-sm-10">
                    <input type="email" name="email" class="form-control"/>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label"><@spring.message "form.register_as"/>:</label>
                <div class="col-sm-10">
                    <select id="selectRole" size="1" name="role" class="form-control">
                        <option value="USER"><@spring.message "form.value.user"/></option>
                        <option value="ADMIN"><@spring.message "form.value.admin"/></option>
                    </select>
                </div>
            </div>
            <input type="hidden" name="lang" value="${springMacroRequestContext.locale}"/>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-primary"><@spring.message "button.register"/></button>
        </form>
    </div>
</@c.page>