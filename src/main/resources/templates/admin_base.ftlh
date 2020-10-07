<#import "parts/common.ftlh" as c>
<#import "parts/navbar.ftlh" as n>
<#import "parts/pager.ftlh" as p>
<#import "/spring.ftl" as spring>

<@c.page>
    <@n.navbar>
        <li class="nav-item" xmlns="http://www.w3.org/1999/html">
            <a class="nav-link"
               href="/main?lang=${springMacroRequestContext.locale}"><@spring.message "header.home"/></a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/admin_base?lang=uk_UA"><@spring.message "header.switchUk"/></a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/admin_base?lang=en_US"><@spring.message "header.swithEng"/></a>
        </li>
        <li class="nav-item">
            <@n.logout/>
        </li>
    </@n.navbar>
    <div class="container mt-5">
        <div class="mt-3 mb-3">
            <h2><@spring.message "pagename.admin_page"/></h2>
        </div>

        <div class="row">
            <div class="col">
                <h5><@spring.message "form.create_exhibit"/></h5>
            </div>
        </div>


        <form action="/add_exhibition" class="container" method="post">

            <div class="row mt-3 mb-3">
                <div class="col">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.ex_name_eng"/>:
                            </span>
                        </div>
                        <input type="text" name="exNameEng" id="exNameEng" class="form-control" required="required"/>
                    </div>
                </div>
                <div class="col">
                    <div class="input-group">
                        <div class="input-group-prepend">
                             <span class="input-group-text">
                             <@spring.message "form.ex_name_nat"/>:
                             </span>
                        </div>
                        <input type="text" name="exNameNative" id="exNameNative" class="form-control"
                               required="required"/>
                    </div>
                </div>
            </div>

            <div class="row mt-3 mb-3">
                <div class="col">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.open_time"/>:
                            </span>
                        </div>
                        <input type="time" name="openTime" class="form-control" required="required" id="openTime"/>
                    </div>
                </div>
                <div class="col">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.close_time"/>:
                            </span>
                        </div>
                        <input type="time" name="closeTime" class="form-control" required="required" id="closeTime"/>
                    </div>
                </div>
                <div class="col">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.ticket_price"/>:
                            </span>
                        </div>
                        <input type="text" name="ticketPrice" id="ticketPrice" class="form-control"
                               required="required"/>
                    </div>
                </div>
            </div>

            <div class="row mt-3 mb-3">
                <div class="col">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.desc_eng"/>:
                            </span>
                        </div>
                        <input type="text" name="descriptionEng" id="descriptionEng" class="form-control"
                               required="required"/>
                    </div>
                </div>
            </div>

            <div class="row mt-3 mb-3">
                <div class="col">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.desc_nat"/>:
                            </span>
                        </div>
                        <input type="text" name="descriptionNative" id="descriptionNative" class="form-control"
                               required="required"/>
                    </div>
                </div>
            </div>

            <div class="row mt-3 mb-3">
                <div class="col">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.themes_eng"/>:
                            </span>
                        </div>
                        <input type="text" name="themesEng" id="themesEng" class="form-control" required="required"/>
                    </div>
                </div>
                <div class="col">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.themes_nat"/>:
                            </span>
                        </div>
                        <input type="text" name="themesNative" id="themesNative" class="form-control"
                               required="required"/>
                    </div>
                </div>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-outline-secondary"><@spring.message "button.create_ex"/></button>


            <div class="row mt-5 mb-5">
                <#if exhibition_added??>
                    <table class="table table-hover">

                        <h5><@spring.message "message.ex_created"/>:</h5>
                        <thead>
                        <tr>
                            <th scope="col"><@spring.message "form.id"/></th>
                            <th scope="col"><@spring.message "form.ex_name_eng"/></th>
                            <th scope="col"><@spring.message "form.ex_name_nat"/></th>
                            <th scope="col"><@spring.message "form.ticket_price"/></th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr>
                            <th scope="row">${exhibition_added.id}</th>
                            <td>${exhibition_added.exNameEng}</td>
                            <td>${exhibition_added.exNameNative}</td>
                            <td>${exhibition_added.ticketPrice}</td>
                        </tr>
                        </tbody>
                    </table>
                </#if>
            </div>
        </form>

        <div class="row">
            <div class="col">
                <#if errormessage??>
                    <h4>${errormessage}</h4>
                </#if>
            </div>
        </div>

        <div class="row">
            <div class="col">
                <h5><@spring.message "form.cancel_exhibit"/>:</h5>
            </div>
        </div>


        <div class="row">
            <form action="/cancel_exhibition" method="post" class="container">
                <div class="col">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.enter_ex_id"/>:
                            </span>
                        </div>
                        <input type="text" name="exhibitionId" id="exhibitionId" class="form-control"
                               required="required">
                        <div class="input-group-append">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <button type="submit"
                                    class="btn btn-outline-secondary"><@spring.message "button.cancel"/></button>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <div class="row">
            <#if cancel_exhibition_message??>
                <h4>${cancel_exhibition_message}</h4>
            </#if>
        </div>


        <div class="container">
            <form action="/add_exhibition_to_hall" method="post">
                <div class="row mt-3 mb-3">
                    <h5><@spring.message "form.assign_ex_to_hall"/></h5>
                </div>

                <div class="row mt-3 mb-3">
                    <div class="col">
                        <div class="input-group">
                            <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.enter_ex_id"/>:
                            </span>
                            </div>
                            <input type="text" name="exhibitionId" id="exhibitionId" class="form-control"
                                   required="required"/>
                        </div>
                    </div>
                    <div class="col">
                        <div class="input-group">
                            <div class="input-group-prepend">
                            <span class="input-group-text">
                                <@spring.message "form.assign_a_hall"/>:
                            </span>
                            </div>
                            <select name="hallName" size="1" id="hallName" class="custom-select">
                                <option value="0"><@spring.message "hall.red"/></option>
                                <option value="1"><@spring.message "hall.yellow"/></option>
                                <option value="2"><@spring.message "hall.blue"/></option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row mt-3 mb-3">
                    <div class="col">
                        <div class="input-group">
                            <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.start_date"/>:
                            </span>
                            </div>
                            <input type="date" name="startDate" id="startDate" class="form-control" required="required">
                        </div>
                    </div>
                    <div class="col">
                        <div class="input-group">
                            <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.end_date"/>:
                            </span>
                            </div>
                            <input type="date" name="endDate" id="endDate" class="form-control" required="required">
                        </div>
                    </div>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-outline-secondary"><@spring.message "button.assign"/></button>


                <div class="row mt-3 mb-3">
                    <#if hall_error??>
                        <h4>${hall_error}</h4>
                    </#if>
                    <#if hall_added??>
                        <h4>${hall_added}</h4>
                    </#if>
                </div>
            </form>

        </div>


        <div class="row mt-3 mb-3">
            <div class="col">
                <h5><@spring.message "form.find_exhibition"/></h5>
            </div>
        </div>

        <div class="container">
            <div class="row mt-3 mb-3">
                <form class="col" id="searchByName" method="get" action="filter_ex_by_name_admin">
                    <div class="input-group mb-3">
                        <input type="text" name="filterByName" class="form-control"
                               placeholder="<@spring.message "form.name"/>:">
                        <div class="input-group-append">
                            <button type="submit"
                                    class="btn btn-outline-secondary"><@spring.message "button.find"/></button>
                        </div>
                    </div>
                </form>

                <form class="col" id="searchByTheme" method="get" action="filter_ex_by_theme_admin">
                    <div class="input-group mb-3">
                        <input type="text" name="filterByTheme" class="form-control"
                               placeholder="<@spring.message "form.theme"/>:">
                        <div class="input-group-append">
                            <button type="submit"
                                    class="btn btn-outline-secondary"><@spring.message "button.find"/></button>
                        </div>
                    </div>
                </form>

                <form class="col" id="searchByStatus" method="get" action="filter_ex_by_status_admin">
                    <div class="input-group">
                        <div class="input-group-prepend">
                        <span class="input-group-text"><@spring.message "form.status"/>
                        </span>
                        </div>
                        <select class="custom-select" size="1" name="filterByStatus" class="form-control">
                            <option value="ACTIVE"><@spring.message "value.active"/></option>
                            <option value="CANCELED"><@spring.message "value.canceled"/></option>
                            <option value="ENDED"><@spring.message "value.ended"/></option>
                        </select>
                        <div class="input-group-append">
                            <button type="submit"
                                    class="btn btn-outline-secondary"><@spring.message "button.find"/></button>
                        </div>
                    </div>
                </form>

            </div>

            <div class="row mt-3 mb-3">

                <form class="col" id="searchByPrice" method="get" action="filter_ex_by_price_admin">
                    <div class="input-group">
                        <div class="input-group-prepend">
                                   <span class="input-group-text"><@spring.message "form.ticket_price"/>
                                       :</span>
                        </div>
                        <input type="text" name="filterByPriceFrom" class="form-control"
                               required="required">
                        <input type="text" name="filterByPriceUpTo" class="form-control"
                               required="required">
                        <div class="input-group-append">
                            <button type="submit"
                                    class="btn btn-outline-secondary"><@spring.message "button.find"/></button>
                        </div>
                    </div>
                </form>

                <form class="col" id="searchByDate" method="get" action="/filter_ex_by_date_admin">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><@spring.message "form.date"/>:</span>
                        </div>
                        <input type="date" name="filterByDateStart" class="form-control" required="required">

                        <input type="date" name="filterByDateEnd" class="form-control" required="required">
                        <div class="input-group-append">
                            <button type="submit"
                                    class="btn btn-outline-secondary"><@spring.message "button.find"/></button>
                        </div>
                    </div>
                </form>

            </div>

            <div class="row mt-4 mb-4">
                <div class="col">
                    <#if searchResult??>
                        <h4>${searchResult}</h4>
                    </#if>
                </div>
            </div>

            <#if exhibitions??>
                <@p.pager url exhibitions />
                <#list exhibitions.content as exhibition>
                    <div class="card" style="width: 100%;">
                        <div class="card-body">

                            <h5 class="card-title">
                                # ${exhibition.id} &nbsp;&nbsp;&nbsp;
                                <#if .locale == "en_US">
                                    ${exhibition.exNameEng}&nbsp;&nbsp;&nbsp;
                                <#else>
                                    ${exhibition.exNameNative}&nbsp;&nbsp;&nbsp;
                                </#if>
                            </h5>

                            <#list exhibition.hallScheduleList as hallSchedule>
                                <h6 class="card-subtitle mb-2 text-muted">
                                    <span><@spring.message "form.hall"/>:&nbsp${hallSchedule.hallName}</span>&nbsp&nbsp&nbsp
                                    <span><@spring.message "form.start_date"/>: &nbsp${hallSchedule.startDateAsString}</span>&nbsp&nbsp&nbsp
                                    <span><@spring.message "form.end_date"/>:&nbsp${hallSchedule.endDateAsString}</span>
                                </h6>
                            </#list>

                            <p class="card-text">
                                <@spring.message "form.opening_hours"/>:
                                <span>${exhibition.openTime}</span>
                                -
                                <span>${exhibition.closeTime}</span>
                            </p>

                            <#if .locale == "en_US">
                                <p class="card-text">
                                    ${exhibition.descriptionEng}
                                </p>
                                <p class="card-text">
                                    ${exhibition.themesEng}
                                </p>
                            <#else>
                                <p class="card-text">
                                    ${exhibition.descriptionNative}
                                </p>
                                <p class="card-text">
                                    ${exhibition.themesNative}
                                </p>
                            </#if>

                            <p class="card-text">
                                <@spring.message "form.ticket_price"/>:
                                <span>${exhibition.ticketPrice}</span>
                            </p>
                            <form action="buy_ticket_admin" method="post" class="form-inline">
                                <select size="1" name="ticketQuantity" id="ticketQuantity" class="form-control">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                </select>
                                <input type="hidden" name="exhibitionId" value="${exhibition.id}"/>
                                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                <button type="submit"
                                        class="btn btn-outline-secondary"><@spring.message "button.buy_ticket"/></button>
                            </form>
                        </div>
                    </div>
                </#list>
                <@p.pager url exhibitions />
            <#else>
            </#if>


            <#if halls_with_exhibitions??>
                <#list halls_with_exhibitions as hall_with_exhibition>
                    <div class="card" style="width: 100%;">
                        <div class="card-body">
                            <h5 class="card-title">
                                # ${hall_with_exhibition.exhibition.getId()}
                                <#if .locale == "en_US">
                                    ${hall_with_exhibition.exhibition.getExNameEng()}
                                <#else>
                                    ${hall_with_exhibition.exhibition.getExNameNative()}
                                </#if>
                            </h5>
                            <h6 class="card-subtitle mb-2 text-muted">
                                <span><@spring.message "form.hall"/>:&nbsp${hall_with_exhibition.hallName}</span>&nbsp&nbsp&nbsp
                                <span><@spring.message "form.start_date"/>: &nbsp${hall_with_exhibition.startDateAsString}</span>&nbsp&nbsp&nbsp
                                <span><@spring.message "form.end_date"/>:&nbsp${hall_with_exhibition.endDateAsString}</span>
                            </h6>
                            <p class="card-text">
                                <@spring.message "form.opening_hours"/>:
                                <span>${hall_with_exhibition.exhibition.getOpenTime()}</span>
                                -
                                <span>${hall_with_exhibition.exhibition.getCloseTime()}</span>
                            </p>

                            <#if .locale == "en_US">
                                <p class="card-text">
                                    ${hall_with_exhibition.exhibition.getDescriptionEng()}
                                </p>
                                <p class="card-text">
                                    ${hall_with_exhibition.exhibition.getThemesEng()}
                                </p>
                            <#else>
                                <p class="card-text">
                                    ${hall_with_exhibition.exhibition.getDescriptionNative()}
                                </p>
                                <p class="card-text">
                                ${hall_with_exhibition.exhibition.getThemesNative()}
                                </p>
                            </#if>
                            <p class="card-text">
                                <@spring.message "form.ticket_price"/>:
                                <span>${hall_with_exhibition.exhibition.getTicketPrice()}</span>
                            </p>
                            <form action="buy_ticket_admin" method="post" class="form-inline">
                                <select size="1" name="ticketQuantity" id="ticketQuantity" class="form-control">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                </select>
                                <input type="hidden" name="exhibitionId"
                                       value="${hall_with_exhibition.exhibition.id}"/>
                                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                <button type="submit"
                                        class="btn btn-outline-secondary"><@spring.message "button.buy_ticket"/></button>
                            </form>
                        </div>
                    </div>
                </#list>
            <#else>
            </#if>
        </div>


        <div class="row">
            <div class="col">
                <h5><@spring.message "form.find_tickets"/></h5>
            </div>
        </div>

        <div class="container">
            <div class="row mt-3 mb-3">
                <div class="col">
                    <form id="searchMyTickets" method="get" action="find_my_tickets_admin">
                        <button type="submit"
                                class="btn btn-outline-secondary"><@spring.message "button.show_my_tickets"/></button>
                    </form>
                </div>
            </div>


            <div class="row">
                <div class="col">
                    <form id="searchTicketByUserId" method="get" action="search_tickets_by_user_id_admin">
                        <div class="input-group">
                            <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.search_by_user_id"/>:
                            </span>
                            </div>
                            <input type="text" name="userId" class="form-control"/>
                            <div class="input-group-append">
                                <button type="submit"
                                        class="btn btn-outline-secondary"><@spring.message "button.find"/></button>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col">
                    <form id="searchByExhibition" method="get" action="search_tickets_by_exhibition_id_admin">

                        <div class="input-group">
                            <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.search_by_exhibit_id"/>:
                            </span>
                            </div>
                            <input type="text" name="exhibitionId" class="form-control col-sm-2"/>
                            <div class="input-group-append">
                                <button type="submit"
                                        class="btn btn-outline-secondary"><@spring.message "button.find"/></button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <div class="row mt-3 mb-3">
                <form id="searchTicketsByStatus" method="get" action="search_tickets_by_status_admin">
                    <div class="col">
                        <div class="input-group">
                            <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.status"/>:
                            </span>
                            </div>
                            <select size="1" name="ticketStatus" class="custom-select">
                                <option value="TICKET_ACTIVE"><@spring.message "value.active"/></option>
                                <option value="TICKET_WAITING_REFUND"><@spring.message "value.waiting_refund"/></option>
                                <option value="TICKET_REFUNDED"><@spring.message "value.refunded"/></option>
                            </select>
                            <div class="input-group-append">
                                <button type="submit"
                                        class="btn btn-outline-secondary"><@spring.message "button.find"/></button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>

            <div class="row mt-3 mb-3">
                <#if search_ticket_message??>
                    <h4>${search_ticket_message}</h4>
                </#if>
            </div>

            <#if ticketList??>
                <#list ticketList as ticket>
                    <div class="card" style="width: 100%;">
                        <div class="card-body">
                            <h5 class="card-title">
                                <@spring.message "form.ticket_id"/> &nbsp; ${ticket.id}
                                <#if .locale == "en_US">
                                    ${ticket.exhibition.exNameEng}&nbsp;
                                <#else>
                                    ${ticket.exhibition.exNameNative}
                                </#if>
                            </h5>


                            <p class="card-text">
                                <@spring.message "form.opening_hours"/>:&nbsp
                                ${ticket.exhibition.openTime}&nbsp-&nbsp
                                ${ticket.exhibition.closeTime}
                            </p>

                            <#if ticket.exhibition.hallScheduleList??>
                                <#list ticket.exhibition.hallScheduleList as hall>
                                    <h6 class="card-subtitle mb-2 text-muted">
                                        <@spring.message "form.hall"/>:&nbsp${hall.hallName}
                                        <@spring.message "form.start_date"/>:&nbsp${hall.startDateAsString}
                                        &nbsp&nbsp&nbsp
                                        <@spring.message "form.end_date"/>:&nbsp${hall.endDateAsString}
                                    </h6>
                                </#list>
                            </#if>

                            <p class="card-text">
                                ${ticket.customer.email}
                            </p>
                            <p class="card-text">
                                <@spring.message "form.quantity"/>:&nbsp${ticket.quantity}
                            </p>
                            <p class="card-text">
                                <@spring.message "form.ticket_status"/>:&nbsp${ticket.ticketStatus}
                            </p>
                        </div>
                    </div>
                </#list>
            </#if>
        </div>

        <div class="row mt-3 mb-3">
            <div class="col">
                <h5><@spring.message "form.find_user"/></h5>
            </div>
        </div>

        <div class="container">
            <div class="row mt-3 mb-3">
                <div class="col">
                    <form id="searchAllUsers" method="get" action="find_all_users_admin">
                        <button type="submit"
                                class="btn btn-outline-secondary"><@spring.message "button.find_all"/></button>
                    </form>
                </div>
            </div>

            <div class="row mt-3 mb-3">
                <div class="col">
                    <form id="searchUserByEmail" method="get" action="find_user_by_email_admin">
                        <div class="input-group">
                            <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.find_by_email"/>:
                            </span>
                            </div>
                            <input type="text" name="userEmail" class="form-control col-sm-4"/>
                            <div class="input-group-append">
                                <button type="submit"
                                        class="btn btn-outline-secondary"><@spring.message "button.find"/></button>
                            </div>
                        </div>

                    </form>
                </div>
            </div>


            <#if userList??>
                <#list userList as user>
                    <div class="card" style="width: 100%;">
                        <div class="card-body">
                            <h5 class="card-title">
                                ${user.id}</b>
                            </h5>
                            <p class="card-text">
                                ${user.username} &nbsp;&nbsp;&nbsp;
                                ${user.nameNative}&nbsp;&nbsp;&nbsp;
                                ${user.email}
                            </p>
                        </div>
                    </div>
                </#list>
            </#if>

            <div class="row mt-4 mb-4">
                <div class="col">
                    <#if userSearchMessage??>
                        <h4>${userSearchMessage}</h4>
                    </#if>
                </div>
            </div>
        </div>


        <div class="row mt-3 mb-3">
            <div class="col">
                <h5><@spring.message "form.mark_ticket_refunded"/>:</h5>
            </div>
        </div>

        <div class="container">
            <div class="row mt-3 mb-3">
                <div class="col">
                    <form action="/cancel_ticket" method="post">
                        <div class="input-group">
                            <div class="input-group-prepend">
                            <span class="input-group-text">
                            <@spring.message "form.ticket_id"/>:
                            </span>
                            </div>
                            <input type="text" name="ticketId" id="ticketId" class="form-control"
                                   required="required">
                            <div class="input-group-append">
                                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                <button type="submit"
                                        class="btn btn-outline-secondary"><@spring.message "button.mark"/></button>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col">
                </div>
            </div>

            <div class="row mt-3 mb-3">
                <#if cancel_ticket_message??>
                    <h4>${cancel_ticket_message}</h4>
                </#if>
            </div>
        </div>
    </div>
    </div>
</@c.page>