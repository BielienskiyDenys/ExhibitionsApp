<#import "parts/common.ftlh" as c>
<#import "parts/navbar.ftlh" as n>
<#import "parts/pager.ftlh" as p>
<#import "/spring.ftl" as spring>

<@c.page>
    <@n.navbar>
        <li class="nav-item">
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
        <h1><@spring.message "pagename.admin_page"/></h1>
        <br>
        <br>
        <form action="/add_exhibition" method="post">
            <h5><@spring.message "form.create_exhibit"/></h5>
            <br>
            <div class="form-group row">
                <label for="exNameEng" class="col-sm-2 col-form-label"><@spring.message "form.ex_name_eng"/>:</label>
                <div class="col-sm-10">
                    <input type="text" name="exNameEng" id="exNameEng" class="form-control" required="required"/>
                </div>
            </div>
            <div class="form-group row">
                <label for="exNameEng" class="col-sm-2 col-form-label"><@spring.message "form.ex_name_nat"/>:</label>
                <div class="col-sm-10">
                    <input type="text" name="exNameNative" id="exNameNative" class="form-control" required="required"/>
                </div>
            </div>
            <div class="form-group row">
                <label for="openTime" class="col-sm-2 col-form-label"><@spring.message "form.open_time"/>:</label>
                <div class="col-sm-2">
                    <input type="time" name="openTime" class="form-control" required="required" id="openTime"/>
                </div>
                <label for="closeTime" class="col-sm-2 col-form-label"><@spring.message "form.close_time"/>:</label>
                <div class="col-sm-2">
                    <input type="time" name="closeTime" class="form-control" required="required" id="closeTime"/>
                </div>

            </div>
            <div class="form-group row">
                <label for="descriptionEng" class="col-sm-2 col-form-label"><@spring.message "form.desc_eng"/>:</label>
                <div class="col-sm-10">
                    <input type="text" name="descriptionEng" id="descriptionEng" class="form-control"
                           required="required"/>
                </div>
            </div>
            <div class="form-group row">
                <label for="descriptionNative" class="col-sm-2 col-form-label"><@spring.message "form.desc_nat"/>
                    :</label>
                <div class="col-sm-10">
                    <input type="text" name="descriptionNative" id="descriptionNative" class="form-control"
                           required="required"/>
                </div>
            </div>
            <div class="form-group row">
                <label for="themesEng" class="col-sm-2 col-form-label"><@spring.message "form.themes_eng"/>:</label>
                <div class="col-sm-4">
                    <input type="text" name="themesEng" id="themesEng" class="form-control" required="required"/>
                </div>
                <label for="themesNative" class="col-sm-2 col-form-label"><@spring.message "form.themes_nat"/>:</label>
                <div class="col-sm-4">
                    <input type="text" name="themesNative" id="themesNative" class="form-control" required="required"/>
                </div>
            </div>
            <div class="form-group row">
                <label for="ticketPrice" class="col-sm-2 col-form-label"><@spring.message "form.ticket_price"/>:</label>
                <div class="col-sm-2">
                    <input type="text" name="ticketPrice" id="ticketPrice" class="form-control" required="required"/>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-primary"><@spring.message "button.create_ex"/></button>

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
        </form>

        <div>
            <#if errormessage??>
                <h4>${errormessage}</h4>
            </#if>
        </div>
        <br>
        <br>
        <br>
        <div>
            <form action="/cancel_exhibition" method="post" class="form-inline">
                <h5><@spring.message "form.cancel_exhibit"/>:&nbsp</h5>
                <br>
                <input type="text" name="exhibitionId" id="exhibitionId" class="form-control" required="required">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary"><@spring.message "button.cancel"/></button>
                <#if cancel_exhibition_message??>
                    <h4>${cancel_exhibition_message}</h4>
                </#if>
            </form>
        </div>
        <br>
        <br>
        <br>
        <div>
            <form action="/add_exhibition_to_hall" method="post">
                <h5><@spring.message "form.assign_ex_to_hall"/></h5>
                <br>

                <div class="form-group row">
                    <label for="exhibitionId" class="col-sm-2 col-form-label"><@spring.message "form.enter_ex_id"/>
                        :</label>
                    <div class="col-sm-4">
                        <input type="text" name="exhibitionId" id="exhibitionId" class="form-control"
                               required="required"/>
                    </div>
                    <label for="hallName" class="col-sm-2 col-form-label"><@spring.message "form.assign_a_hall"/>
                        :</label>
                    <div class="col-sm-4">
                        <select name="hallName" size="1" id="hallName" class="form-control">
                            <option value="0"><@spring.message "hall.red"/></option>
                            <option value="1"><@spring.message "hall.yellow"/></option>
                            <option value="2"><@spring.message "hall.blue"/></option>
                        </select>
                    </div>
                </div>

                <div class="form-group row">
                    <label for="exhibitionId" class="col-sm-2 col-form-label"><@spring.message "form.start_date"/>
                        :</label>
                    <div class="col-sm-4">
                        <input type="date" name="startDate" id="startDate" class="form-control" required="required">
                    </div>
                    <label for="exhibitionId" class="col-sm-2 col-form-label"><@spring.message "form.end_date"/>
                        :</label>
                    <div class="col-sm-4">
                        <input type="date" name="endDate" id="endDate" class="form-control" required="required">
                    </div>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary"><@spring.message "button.assign"/></button>
            </form>
            <br>
            <#if hall_error??>
                <h4>${hall_error}</h4>
            </#if>
            <#if hall_added??>
                <h4>${hall_added}</h4>
            </#if>
        </div>
        <br>
        <br>
        <br>
        <div>
            <h5><@spring.message "form.find_exhibition"/></h5>
            <div class="form-group row">
        <span>
        <form id="searchByName" method="get" action="filter_ex_by_name_admin" class="form-inline">
            <label for="searchByName" class="col-sm-2 col-form-label"><@spring.message "form.name"/>:</label>
            <input type="text" name="filterByName" class="form-control">
            <button type="submit" class="btn btn-primary"><@spring.message "button.find"/></button>
        </form>
        </span>
                <span>
        <form id="searchByTheme" method="get" action="filter_ex_by_theme_admin" class="form-inline">
            <label for="searchByTheme" class="col-sm-2 col-form-label"><@spring.message "form.theme"/>:</label>
            <input type="text" name="filterByTheme" class="form-control">
            <button type="submit" class="btn btn-primary"><@spring.message "button.find"/></button>
        </form>
        </span>
            </div>

            <div class="form-group row">
                <form id="searchByPrice" method="get" action="filter_ex_by_price_admin" class="form-inline">
                    <div class="col-sm-16">
                        <label for="searchByPrice"
                               class="col-sm-2 col-form-label"><@spring.message "form.ticket_price"/>:</label>
                        <input type="text" name="filterByPriceFrom" class="form-control" required="required">
                        <input type="text" name="filterByPriceUpTo" class="form-control" required="required">
                        <button type="submit" class="btn btn-primary"><@spring.message "button.find"/></button>
                    </div>
                </form>
            </div>

            <div class="form-group row">
                <form id="searchByDate" method="get" action="filter_ex_by_date_admin" class="form-inline">
                    <div class="col-sm-16">
                        <label for="searchByDate" class="col-sm-2 col-form-label"><@spring.message "form.date"/>
                            :</label>
                        <input type="date" name="filterByDateStart" class="form-control" required="required">
                        <input type="date" name="filterByDateEnd" class="form-control" required="required">
                        <button type="submit" class="btn btn-primary"><@spring.message "button.find"/></button>
                    </div>
                </form>
            </div>

            <div class="form-group row">
                <form id="searchByStatus" method="get" action="filter_ex_by_status_admin" class="form-inline">
                    <label for="searchByStatus" class="col-sm-2 col-form-label"><@spring.message "form.status"/>
                        :</label>
                    <div class="col-sm-1"></div>
                    <select size="1" name="filterByStatus" class="form-control">
                        <option value="ACTIVE"><@spring.message "value.active"/></option>
                        <option value="CANCELED"><@spring.message "value.canceled"/></option>
                        <option value="ENDED"><@spring.message "value.ended"/></option>
                    </select>
                    <button type="submit" class="btn btn-primary"><@spring.message "button.find"/></button>
                </form>
            </div>
        </div>
        <div>
            <#if searchResult??>
                <h4>${searchResult}</h4>
            </#if>
            <#if exhibitions??>
                <@p.pager url exhibitions />
                <#list exhibitions.content as exhibition>
                    <table class="table table-hover">
                        <tbody>
                        <tr>
                            <th scope="row">${exhibition.id}</th>
                            <td>
                                <p>
                                    <b>${exhibition.exNameEng}</b>
                                    <b>${exhibition.exNameNative}</b>
                                </p>
                                <p>
                                    <#list exhibition.hallScheduleList as hallSchedule>
                                        <span><@spring.message "form.hall"/>:&nbsp${hallSchedule.hallName}</span>&nbsp&nbsp&nbsp
                                        <span><@spring.message "form.start_date"/>: &nbsp${hallSchedule.startDateAsString}</span>&nbsp&nbsp&nbsp
                                        <span><@spring.message "form.end_date"/>:&nbsp${hallSchedule.endDateAsString}</span>
                                    </#list>
                                </p>
                                <p>
                                    <@spring.message "form.opening_hours"/>:

                                    <span>${exhibition.openTime}</span>
                                    -
                                    <span>${exhibition.closeTime}</span>
                                </p>
                                <p>
                                    <span>${exhibition.descriptionEng}</span>
                                </p>
                                <p>
                                    <span>${exhibition.descriptionNative}</span>
                                </p>
                                <p>
                                    <span>${exhibition.themesEng}</span>
                                    <span>${exhibition.themesNative}</span>
                                </p>
                                <p>
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
                                            class="btn btn-primary"><@spring.message "button.buy_ticket"/></button>
                                </form>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </#list>
                <@p.pager url exhibitions />
            <#else>
            </#if>

            <#if halls_with_exhibitions??>
                <#list halls_with_exhibitions as hall_with_exhibition>
                    <table class="table table-hover">
                        <tbody>
                        <tr>
                            <th scope="row">${hall_with_exhibition.exhibition.getId()}</th>
                            <td>
                                <p>
                                    <b>${hall_with_exhibition.exhibition.getExNameEng()}</b>
                                    <b>${hall_with_exhibition.exhibition.getExNameNative()}</b>
                                </p>
                                <p>
                                    <span><@spring.message "form.hall"/>:&nbsp${hall_with_exhibition.hallName}</span>&nbsp&nbsp&nbsp
                                    <span><@spring.message "form.start_date"/>: &nbsp${hall_with_exhibition.startDateAsString}</span>&nbsp&nbsp&nbsp
                                    <span><@spring.message "form.end_date"/>:&nbsp${hall_with_exhibition.endDateAsString}</span>
                                </p>
                                <p>
                                    <@spring.message "form.opening_hours"/>:
                                    <span>${hall_with_exhibition.exhibition.getOpenTime()}</span>
                                    -
                                    <span>${hall_with_exhibition.exhibition.getCloseTime()}</span>
                                </p>
                                <p>
                                    <span>${hall_with_exhibition.exhibition.getDescriptionEng()}</span>
                                </p>
                                <p>
                                    <span>${hall_with_exhibition.exhibition.getDescriptionNative()}</span>
                                </p>
                                <p>
                                    <span>${hall_with_exhibition.exhibition.getThemesEng()}</span>
                                    <span>${hall_with_exhibition.exhibition.getThemesNative()}</span>
                                </p>
                                <p>
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
                                            class="btn btn-primary"><@spring.message "button.buy_ticket"/></button>
                                </form>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </#list>
            <#else>
            </#if>
        </div>
        <br>
        <br>
        <br>
        <div>
            <h5><@spring.message "form.find_tickets"/></h5>
            <div class="form-group row">
                <form id="searchMyTickets" method="get" action="find_my_tickets_admin">
                    <button type="submit" class="btn btn-primary"><@spring.message "button.show_my_tickets"/></button>
                </form>
            </div>

            <div class="form-group row">
            <span>
                <form id="searchTicketByUserId" method="get" action="search_tickets_by_user_id_admin"
                      class="form-inline">
                    <label for="searchTicketByUserId"
                           class="col-sm-4 col-form-label"><@spring.message "form.search_by_user_id"/>:</label>
                    <input type="text" name="userId" class="form-control col-sm-2"/>
                    <button type="submit" class="btn btn-primary"><@spring.message "button.find"/></button>
                </form>
            </span>
            </div>
            <div class="form-group row">
            <span>
                <form id="searchByExhibition" method="get" action="search_tickets_by_exhibition_id_admin"
                      class="form-inline">
                     <label for="searchByExhibition"
                            class="col-sm-4 col-form-label"><@spring.message "form.search_by_exhibit_id"/>:</label>
                     <input type="text" name="exhibitionId" class="form-control col-sm-2"/>
                     <button type="submit" class="btn btn-primary"><@spring.message "button.find"/></button>
                 </form>
            </span>
            </div>
            <div class="form-group row">
            <span>
                 <form id="searchTicketsByStatus" method="get" action="search_tickets_by_status_admin"
                       class="form-inline">
                      <label for="searchTicketsByStatus"
                             class="col-sm-4 col-form-label"><@spring.message "form.status"/>:</label>
                      <select size="1" name="ticketStatus" class="form-control col-sm-4">
                          <option value="TICKET_ACTIVE"><@spring.message "value.active"/></option>
                          <option value="TICKET_WAITING_REFUND"><@spring.message "value.waiting_refund"/></option>
                          <option value="TICKET_REFUNDED"><@spring.message "value.refunded"/></option>
                      </select>
                      <button type="submit" class="btn btn-primary"><@spring.message "button.find"/></button>
                 </form>
            </span>
            </div>

            <#if search_ticket_message??>
                <h4>${search_ticket_message}</h4>
            </#if>

            <#if ticketList??>
                <#list ticketList as ticket>
                    <table class="table table-hover">
                        <tbody>
                        <tr>
                            <th scope="row">${ticket.id}</th>
                            <td>
                                <p>
                                    <b>
                                        ${ticket.exhibition.exNameEng}
                                        ${ticket.exhibition.exNameNative}
                                    </b>
                                </p>
                                <p>
                                    <@spring.message "form.opening_hours"/>:&nbsp
                                    ${ticket.exhibition.openTime}&nbsp-&nbsp
                                    ${ticket.exhibition.closeTime}
                                </p>
                                <p>
                                    <#if ticket.exhibition.hallScheduleList??>
                                        <#list ticket.exhibition.hallScheduleList as hall>
                                            <@spring.message "form.hall"/>:&nbsp${hall.hallName}
                                            <@spring.message "form.start_date"/>:&nbsp${hall.startDateAsString}&nbsp&nbsp&nbsp
                                            <@spring.message "form.end_date"/>:&nbsp${hall.endDateAsString}
                                        </#list>
                                    </#if>
                                </p>
                                <p>
                                    ${ticket.customer.email}
                                </p>
                                <p>
                                    <@spring.message "form.quantity"/>:&nbsp${ticket.quantity}
                                </p>
                                <p>
                                    <@spring.message "form.ticket_status"/>:&nbsp${ticket.ticketStatus}
                                </p>
                            </td>
                            <br>
                        </tr>
                        </tbody>
                    </table>
                </#list>
            </#if>

        </div>

        <br>
        <br>
        <div>
            <h5><@spring.message "form.find_user"/></h5>
            <div class="form-group row">
                <form id="searchAllUsers" method="get" action="find_all_users_admin">
                    <button type="submit" class="btn btn-primary"><@spring.message "button.find_all"/></button>
                </form>
            </div>

            <div class="form-group row">
            <span>
                  <form id="searchUserByEmail" method="get" action="find_user_by_email_admin" class="form-inline">
            <label for="searchUserByEmail"
                   class="col-sm-4 col-form-label"><@spring.message "form.find_by_email"/>:</label>
            <input type="text" name="userEmail" class="form-control col-sm-4"/>
            <button type="submit" class="btn btn-primary"><@spring.message "button.find"/></button>
        </form>
            </span>
            </div>

            <#if userList??>

                <#list userList as user>
                    <br>
                    <b>${user.id}</b>
                    <span>${user.username}</span>
                    <span>${user.nameNative}</span>
                    <span>${user.email}</span>
                    <br>
                </#list>

            </#if>
            <#if userSearchMessage??>
                ${userSearchMessage}
            </#if>
        </div>
        <br>
        <br>
        <br>
        <div>
            <form action="/cancel_ticket" method="post" class="form-inline">
                <h5><@spring.message "form.mark_ticket_refunded"/>:&nbsp</h5>
                <br>
                <input type="text" name="ticketId" id="ticketId" class="form-control" required="required">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary"><@spring.message "button.mark"/></button>
                <#if cancel_ticket_message??>
                    ${cancel_ticket_message}
                </#if>
            </form>
        </div>
        <br>
        <br>
        <br>
    </div>
</@c.page>