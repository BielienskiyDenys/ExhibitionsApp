<#import "parts/common.ftlh" as c>
<#import "parts/navbar.ftlh" as n>
<#import "parts/pager.ftlh" as p>
<#import "/spring.ftl" as spring>

<@c.page>
    <@n.navbar>
        <li class="nav-item">
            <a class="nav-link" href="/main?lang=${springMacroRequestContext.locale}">Home</a>
        </li>
        <li class="nav-item">
            <@n.logout/>
        </li>
    </@n.navbar>
    <div class="container mt-5">

        <h1><@spring.message "pagename.guest_page"/></h1>
        <br>
        <div>
            <#if errormessage??>
                <h4>${errormessage}</h4>
            </#if>
        </div>
        <br>
        <div>
            <h5><@spring.message "form.find_exhibition"/></h5>
            <div class="form-group row">
        <span>
        <form id="searchByName" method="get" action="filter_ex_by_name" class="form-inline">
            <label for="searchByName" class="col-sm-2 col-form-label"><@spring.message "form.name"/>:</label>
            <input type="text" name="filterByName" class="form-control">
            <button type="submit" class="btn btn-primary"><@spring.message "button.find"/></button>
        </form>
        </span>
                <span>
        <form id="searchByTheme" method="get" action="filter_ex_by_theme" class="form-inline">
            <label for="searchByTheme" class="col-sm-2 col-form-label"><@spring.message "form.theme"/>:</label>
            <input type="text" name="filterByTheme" class="form-control">
            <button type="submit" class="btn btn-primary"><@spring.message "button.find"/></button>
        </form>
        </span>
            </div>

            <div class="form-group row">
                <form id="searchByPrice" method="get" action="filter_ex_by_price" class="form-inline">
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
                <form id="searchByDate" method="get" action="filter_ex_by_date" class="form-inline">
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
                <form id="searchByStatus" method="get" action="filter_ex_by_status" class="form-inline">
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
                                <form action="buy_ticket" method="post" class="form-inline">
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
                                <form action="buy_ticket" method="post" class="form-inline">
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
        <div>
            <h5><@spring.message "form.find_tickets"/></h5>
            <div class="form-group row">
                <form id="searchMyTickets" method="get" action="find_my_tickets">
                    <button type="submit" class="btn btn-primary"><@spring.message "button.show_my_tickets"/></button>
                </form>
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
    </div>
</@c.page>